package net.justonedev.turing.supervisor;

import net.justonedev.turing.TuringMachine;
import net.justonedev.turing.collections.LimitlessCollection;

/**
 * A smarter supervisor that only remembers affected tiles.
 * Downside: Needs to calculate from x to y every time.
 *
 * @author justonedeveloper
 */
public class HistorySupervisor extends Supervisor {

	/**
	 * Notes the changes of each iteration.
	 */
	private final LimitlessCollection<String> iterationChanges;

	/**
	 * Creates a new basic supervisor and assigns the machine.
	 *
	 * @param machine The turing machine.
	 */
	HistorySupervisor(TuringMachine machine) {
		super(machine);
		iterationChanges = new LimitlessCollection<>();
	}
	
	/**
	 * Runs the turing machine until a result HALTED or LOOPS is achieved.
	 *
	 * @return Either HALTED or LOOPED as result.
	 */
	@Override
	public SupervisorResult runTuringMachine() {
		return null;
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

		boolean halted = machine.nextStep(this.print);
		if (halted) return SupervisorResult.HALTS;


		return null;
	}

}
