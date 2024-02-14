package net.justonedev.turing.supervisor;

import net.justonedev.turing.TuringMachine;
import net.justonedev.turing.collections.LimitlessCollection;

/**
 * A primitive supervisor. Remembers, in no particular order, all state-triples
 * the machine has been in. If a triple occurs twice, a loop is detected and
 * automatic execution stops.
 * A state-triple is a combination of the machine's state ID, the head position
 * and the entire tape as a String. A state triple is again formatted to a String.
 * <p></p>
 * Warning: On longer tapes or machines with large alphabets or many states, this approach
 * may take a while. This is the most basic approach to get loop tracking to work.
 *
 * @author justonedeveloper
 */
public class PrimitiveSupervisor extends Supervisor {

	private final LimitlessCollection<String> states;
	
	/**
	 * Creates a new primitive supervisor and connects it to a turing machine.
	 * Run the turing machine through this to get primitive loop protection.
	 *
	 * @param machine The connected turing machine.
	 */
	public PrimitiveSupervisor(TuringMachine machine) {
		super(machine);
		states = new LimitlessCollection<>(false);
		states.add(generateSupervisorTriple(machine));
	}
	
	/**
	 * Runs the turing machine until a result HALTED or LOOPS is achieved.
	 * @return Either HALTED or LOOPED as result.
	 */
	@Override
	public SupervisorResult runTuringMachine() {
		SupervisorResult result = SupervisorResult.RUNNING;
		while (result == SupervisorResult.RUNNING) {
			result = runSingleMachineIteration();
		}
		finished = true;
		return result;
	}
	
	/**
	 * Runs a single iteration of the turing machine.
	 * Will return one of the three results, HALTED or LOOPED if it ended,
	 * and RUNNING if it's still going.
	 *
	 * @return Result of this iteration.
	 */
	@Override
	public SupervisorResult runSingleMachineIteration() {
		machine.nextStep(print);
		if (getTuringMachine().isHalted()) {
			finished = true;
			return SupervisorResult.HALTS;
		}
		String nextTriple = generateSupervisorTriple(machine);
		boolean contained = !states.add(nextTriple);
		if (contained) {
			finished = true;
			return SupervisorResult.LOOPS;
		}
		return SupervisorResult.RUNNING;
	}
	
	/**
	 * Generates a supervisor string representative of a triple from
	 * a given machine's state.
	 * @param machine The Turing machine.
	 * @return State-Triple string.
	 */
	private static String generateSupervisorTriple(TuringMachine machine) {
		return "%s;%s".formatted(machine.getCurrentState().getStateID(), machine.getTape().getUniqueFullString());
	}
}
