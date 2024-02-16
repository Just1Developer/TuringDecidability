package net.justonedev.turing;

import net.justonedev.turing.acceptors.Acceptor;
import net.justonedev.turing.acceptors.AcceptorState;
import net.justonedev.turing.collections.LimitlessBinaryMap;
import net.justonedev.turing.supervisor.PrimitiveSupervisor;
import net.justonedev.turing.supervisor.Supervisor;
import net.justonedev.turing.supervisor.SupervisorResult;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;

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
        //TuringMachine machine = getLoopingMachine();
        //TuringMachine machine = getSmallHaltingMachine();
        TuringMachine machine = getSimpleFiniteAcceptor("aaaabb", 30);
        TuringTape tape = machine.getTape();
        
        System.out.println("Tape: " + tape);
        System.out.println("Tape unique: " + tape.getUniqueFullString());
        System.out.println("Tape better: " + machine.getTapeString());

        System.out.println("Press any button to continue...");
        waitForCharInput();

        Supervisor supervisor = new PrimitiveSupervisor(machine);
        supervisor.setPrintEnabled(true);
        SupervisorResult result = runMachineSupervised(supervisor, true, 300);
        System.out.println("Result: " + result);
        
        // I don't always want to comment / uncomment this
        if (machine instanceof Acceptor) {
            System.out.printf("Input accepted: %s, state: %s%n", ((Acceptor) machine).isInputAccepted(), machine.getCurrentState());
        }
        System.out.println("Tape: " + machine.getTapeString());
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
            waitForCharInput();
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
            waitForCharInput();
        }
    }

    /**
     * Waits for a console input by requesting a read() from the default input stream.
     * Catches and ignores a possible IO exception.
     */
    private static void waitForCharInput() {
        try {
            System.in.read();
        } catch (IOException ignored) { /* dont really care here */ }
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
        BigInteger output = null;
        TuringState singleState = new TuringState(0);
        machine.addTransitions(
            new StateTransition(singleState, singleState, output, MoveAction.RIGHT),
            new StateTransition(singleState, singleState, BigInteger.ZERO, output, MoveAction.RIGHT),
            new StateTransition(singleState, singleState, BigInteger.ONE, output, MoveAction.RIGHT),
            new StateTransition(singleState, singleState, BigInteger.TWO, output, MoveAction.RIGHT),
            new StateTransition(singleState, singleState, new BigInteger("3"), output, MoveAction.RIGHT)
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
    
    /**
     * Gets a simple acceptor for input validation.
     * Examples are taken from simple tasks, current configuration:
     * <p></p>
     * {0,1}*#{0,1}*, first half has at least as many 1s as second half.
     *
     * @param input The input for the acceptor.
     * @param overhead The amount the tape is longer than the input. Must be >= 0.
     * @return The Acceptor
     */
    private static Acceptor getSimpleFiniteAcceptor(String input, long overhead) {
        return getSimpleFiniteAcceptor(input, BigInteger.valueOf(overhead));
    }

    /**
     * Gets a simple acceptor for input validation.
     * Examples are taken from simple tasks, current configuration:
     * <p></p>
     * {0,1}*#{0,1}*, first half has at least as many 1s as second half.
     *
     * @param input The input for the acceptor.
     * @param overhead The amount the tape is longer than the input. Must be >= 0.
     * @return The Acceptor
     */
    private static Acceptor getSimpleFiniteAcceptor(String input, BigInteger overhead) {
        
        LimitlessBinaryMap<BigInteger, String> inputMap = new LimitlessBinaryMap<>();
        autofillCharsFromString(inputMap, input);
        
        TuringTape tape = new TuringTape(overhead.add(BigInteger.valueOf(input.length())), BigInteger.ZERO, inputMap, input.split(""));
        
        Acceptor acceptor = new Acceptor();
        acceptor.setTape(tape);
        acceptor.setCharacterTranslationMap(inputMap);
        
        // Regex: a*b

        AcceptorState state1 = new AcceptorState(1, "allAs");
        AcceptorState state2 = new AcceptorState(2, "StopOnB");
        AcceptorState state3 = new AcceptorState(3, "Failure");
        state2.setIsAcceptingState(true);

        acceptor.addTransitions(
            new StateTransition(state1, state1, inputMap, inputMap.getKey("a"), MoveAction.RIGHT, "a"),
            new StateTransition(state1, state2, inputMap, inputMap.getKey("b"), MoveAction.RIGHT, "b"),
            new StateTransition(state2, state3, inputMap, inputMap.getKey("a"), MoveAction.RIGHT, "a", "b")
                // No transition for state 3, just halt in a non-accepting state
        );

        return acceptor;
    }
    
    
    /**
     * Adds all character from a String to the map and automatically assigns them a BigInteger.
     * This method is not a general feature from LimitlessBinaryMap because of the types K and V needing to match.
     * @param map The map to insert the chars into.
     * @param input The String input
     */
    private static void autofillCharsFromString(LimitlessBinaryMap<BigInteger, String> map, String input) {
        BigInteger counter = BigInteger.ZERO;
        for (String in : input.split("")) {
            if (map.containsValue(in)) continue;
            // Count to next free
            while (map.containsKey(counter)) counter = counter.add(BigInteger.ONE);
            
            map.add(counter, in);
            // So we don't needlessly loop over the map
            counter = counter.add(BigInteger.ONE);
        }
    }
}