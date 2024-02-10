package net.justonedev.turing;

import net.justonedev.turing.supervisor.PrimitiveSupervisor;
import net.justonedev.turing.supervisor.Supervisor;
import net.justonedev.turing.supervisor.SupervisorResult;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.math.BigInteger;

/**
 * The main class, used for debugging, testing and running programs.
 *
 * @author justonedeveloper
 */
public class Main {
    
    /**
     * The main entry point of the application.
     * @param args The command line arguments.
     */
    public static void main(String[] args) {
        System.out.println("Hello world!");
        // Lets do a test
        TuringMachine machine = getLoopingMachine();
        TuringTape tape = machine.getTape();
        
        System.out.println("Tape: " + tape);
        System.out.println("Tape unique: " + tape.getUniqueFullString());
        
        Supervisor supervisor = new PrimitiveSupervisor(machine);
        supervisor.setPrintEnabled(true);
        SupervisorResult result = runMachineSupervised(supervisor, true, 1500);
        System.out.println("Result: " + result);
    }
    
    /**
     * Runs a supervised turing machine with a given amount of delay between steps.
     * @param supervisor The supervisor of the turing machine.
     * @param printTape If the tape should be printed after each step.
     * @param delayMS The amount of seconds to wait between each step.
     * @return The result of the supervisor: HALTS / LOOPS.
     */
    private static SupervisorResult runMachineSupervised(final Supervisor supervisor, final boolean printTape, final int delayMS) {
        SupervisorResult result = SupervisorResult.RUNNING;
        while(!supervisor.isFinished()) {
            result = supervisor.runSingleMachineIteration();
            if (printTape) System.out.println("Tape: " + supervisor.getTuringMachine().getTape());
            if (delayMS < 1) continue;
            try {
                Thread.sleep(delayMS);
            } catch (InterruptedException ignored) { /* dont really care here */ }
        }
        return result;
    }
    
    /**
     * Runs a supervised turing machine and waits for a command line input between steps.
     * @param supervisor The supervisor of the turing machine.
     * @param printTape If the tape should be printed after each step.
     * @return The result of the supervisor: HALTS / LOOPS.
     */
    private static SupervisorResult runMachineSupervised(final Supervisor supervisor, final boolean printTape) {
        SupervisorResult result = SupervisorResult.RUNNING;
        while(!supervisor.isFinished()) {
            result = supervisor.runSingleMachineIteration();
            if (printTape) System.out.println("Tape: " + supervisor.getTuringMachine().getTape());
            try {
                System.in.read();
            } catch (IOException ignored) { /* dont really care here */ }
        }
        return result;
    }
    
    /**
     * Runs a turing machine with a given amount of delay between steps.
     * @param machine The turing machine.
     * @param printTape If the tape should be printed after each step.
     * @param printDetails If the machine should print each step in detail.
     * @param delayMS The amount of seconds to wait between each step.
     */
    private static void runMachine(final TuringMachine machine, final boolean printTape, final boolean printDetails, final int delayMS) {
        while(machine.isNotHalted()) {
            machine.nextStep(printDetails);
            if (printTape) System.out.println("Tape: " + machine.getTape());
            if (delayMS < 1) continue;
            try {
                Thread.sleep(delayMS);
            } catch (InterruptedException ignored) { /* dont really care here */ }
        }
    }
    
    /**
     * Runs a turing machine and waits for a command line input between steps.
     * @param machine The turing machine.
     * @param printTape If the tape should be printed after each step.
     * @param printDetails If the machine should print each step in detail.
     */
    private static void runMachine(final TuringMachine machine, final boolean printTape, final boolean printDetails) {
        while(machine.isNotHalted()) {
            machine.nextStep(printDetails);
            if (printTape) System.out.println("Tape: " + machine.getTape());
            try {
                System.in.read();
            } catch (IOException ignored) { /* dont really care here */ }
        }
    }
    
    /**
     * Gets a basic small turing machine that will loop by setting the whole tape to 0 and loop.
     * @return The configured looping turing machine.
     */
    private static TuringMachine getLoopingMachine() {
        BigInteger[] myNums = {
                null,
                null,
                new BigInteger("1"),
                new BigInteger("2"),
                new BigInteger("1"),
                new BigInteger("3"),
                new BigInteger("1"),
                new BigInteger("0"),
        };
        TuringTape tape = new TuringTape(17, 0, myNums);
        
        TuringMachine machine = new TuringMachine();
        machine.setTape(tape);
        
        // Generate a machine with a singular state that overrides everything and loops back to itself.
        
        TuringState singleState = new TuringState(0);
        machine.addTransitions(
            new StateTransition(singleState, singleState, BigInteger.ZERO, MoveAction.RIGHT),
            new StateTransition(singleState, singleState, BigInteger.ZERO, BigInteger.ZERO, MoveAction.RIGHT),
            new StateTransition(singleState, singleState, BigInteger.ONE, BigInteger.ZERO, MoveAction.RIGHT),
            new StateTransition(singleState, singleState, BigInteger.TWO, BigInteger.ZERO, MoveAction.RIGHT),
            new StateTransition(singleState, singleState, new BigInteger("3"), BigInteger.ZERO, MoveAction.RIGHT)
        );
        return machine;
    }
    
    /**
     * Gets a basic small turing machine that will definitely halt.
     * @return The configured halting turing machine.
     */
    private static TuringMachine getSmallHaltingMachine() {
        BigInteger[] myNums = {
                //*
                //new BigInteger("1"),
                //new BigInteger("1"),
                null,
                null,
                new BigInteger("1"),
                new BigInteger("1"),
                new BigInteger("1"),
                new BigInteger("1"),
                new BigInteger("1"),
                new BigInteger("0"),
                //*/
                /*
                new BigInteger("0"),
                new BigInteger("1"),
                new BigInteger("2"),
                new BigInteger("3"),
                new BigInteger("4"),
                new BigInteger("5"),
                new BigInteger("6"),
                new BigInteger("7"),
                new BigInteger("8"),
                new BigInteger("9"),
                //*/
        };
        TuringTape tape = new TuringTape(15, 7, myNums);
        
        TuringMachine machine = new TuringMachine();
        machine.setTape(tape);
        
        TuringState state1 = new TuringState(0);
        TuringState state2 = new TuringState(1);
        StateTransition tr1 = new StateTransition(state1, state1, BigInteger.ONE, BigInteger.ZERO, MoveAction.RIGHT);
        StateTransition tr2 = new StateTransition(state1, state2, BigInteger.ZERO, BigInteger.ONE, MoveAction.RIGHT);
        StateTransition tr3 = new StateTransition(state2, state2, (BigInteger) null, BigInteger.TWO, MoveAction.RIGHT);
        
        machine.addTransitions(tr1, tr2, tr3);
        return machine;
    }
}