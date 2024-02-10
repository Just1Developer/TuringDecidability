package net.justonedev.turing.supervisor;

import net.justonedev.turing.TuringMachine;

/**
 * Abstract class for any supervisor for a turing machine.
 *
 * @author justonedeveloper
 */
public abstract class Supervisor {
	
	protected static final String PRINT_LOOP = "LOOPED. Does not halt.";
	
	/**
	 * The turing machine connected to the supervisor.
	 */
	protected TuringMachine machine;
	/**
	 * If the machine is finished. Stronger than machine's halted parameter as this takes loops into account.
	 */
	protected boolean finished;
	/**
	 * If moves of the machine should be printed in detail.
	 */
	protected boolean print;
	
	/**
	 * Creates a new basic supervisor and assigns the machine.
	 * @param machine The turing machine.
	 */
	Supervisor(TuringMachine machine) {
		this.machine = machine;
		this.finished = false;
	}
	
	/**
	 * Gets the turing machine connected to this supervisor.
	 * @return The supervisor's turing machine.
	 */
	public TuringMachine getTuringMachine() {
		return machine;
	}
	
	/**
	 * Gets if the machine has either halted or the supervisor has detected a loop and ended.
	 * @return True if machine has halted or loops, false if it's still running.
	 */
	public boolean isFinished() {
		return finished;
	}
	
	/**
	 * If moves of the machine should be printed in detail.
	 * @return If detailed printing is on.
	 */
	public boolean isPrintEnabled() {
		return print;
	}
	
	/**
	 * Enables / disables detailed printing of machine iterations.
	 */
	public void setPrintEnabled(boolean enabled) {
		print = enabled;
	}
	
	/**
	 * Runs the turing machine until a result HALTED or LOOPS is achieved.
	 * @return Either HALTED or LOOPED as result.
	 */
	public abstract SupervisorResult runTuringMachine();
	
	/**
	 * Runs a single iteration of the turing machine.
	 * Will return one of the three results, HALTED or LOOPED if it ended,
	 * and RUNNING if it's still going.
	 *
	 * @return Result of this iteration.
	 */
	public abstract SupervisorResult runSingleMachineIteration();
	
}
