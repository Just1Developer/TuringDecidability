package net.justonedev.turing.supervisor;

import net.justonedev.turing.TuringMachine;
import net.justonedev.turing.TuringTape;

public class PrimitiveSupervisor extends Supervisor {
	
	PrimitiveSupervisor(TuringMachine machine) {
		super(machine);
	}
	
	private static String generateSupervisorTriple(TuringMachine machine) {
		return "%s;%s".formatted(machine.getCurrentState().getStateID(), machine.getTape().getUniqueFullString());
	}
}
