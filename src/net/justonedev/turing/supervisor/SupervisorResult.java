package net.justonedev.turing.supervisor;

/**
 * A result for Turing machines via supervised execution.
 * Running: Still runs, only used for single step execution
 * Halts: The machine halts
 * Loops: The machine loops, will never halt.
 *
 * @author justonedeveloper
 */
public enum SupervisorResult {
	RUNNING, HALTS, LOOPS
}
