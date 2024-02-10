package net.justonedev.turing.supervisor;

import net.justonedev.turing.TuringMachine;

public abstract class Supervisor {
	
	TuringMachine machine;
	
	Supervisor(TuringMachine machine) {
		this.machine = machine;
	}
	
	public TuringMachine getTuringMachine() {
		return machine;
	}
	
}
