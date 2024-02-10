package net.justonedev.turing;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * The main simulator for a turing machine, consists of connected Machine states and a Tape to read /
 * write on.
 */
public class TuringMachine {

    // Todo maybe add callback for output function

    private static final String HALT_STRING = "HALT";
    private static final String EMPTY_SQUARE = "[]";
    private static final String ADVANCED_PRINT = "%s --%s--> %s | >> %s-%s%n";

    private TuringTape turingTape;
    private List<TuringState> states;
    private TuringState currentState;
    private boolean halted;

    /**
     * Creates a new turing machine
     */
    public TuringMachine() {
        states = new ArrayList<>();
        halted = false;
    }

    /**
     * Creates a new turing machine
     * @param tape The tape of the machine. Can be updated later
     */
    public TuringMachine(TuringTape tape) {
        this();
        turingTape = tape;
    }

    /**
     * Gets the machine's current tape.
     * @return The current tape.
     */
    public TuringTape getTape() {
        return this.turingTape;
    }

    /**
     * Gets the current state of the turing machine.
     * @return The current state
     */
    public TuringState getCurrentState() {
        return this.currentState;
    }

    /**
     * Sets the turing tape. If invoked while running,
     * may cause bugs.
     * @param tape The new tape.
     */
    public void setTape(TuringTape tape) {
        this.turingTape = tape;
    }

    /**
     * Adds a given state to the turing machine.
     * If the state is already in the machine, it will not be added again. States are identified by
     * their unique ID, so cloning it would be a viable option.
     * <p></p>
     * States will automatically become the current state if no state is specified.
     *
     * @param state The turing state to add.
     */
    public void addTuringState(TuringState state) {
        addTuringState(state, false);
    }

    /**
     * Adds a given state to the turing machine.
     * If the state is already in the machine, it will not be added again. States are identified by
     * their unique ID, so cloning it would be a viable option.
     * <p></p>
     * If {@param setToCurrentState} is set to true, it will still set the given state to the current state.
     * States will automatically become the current state if no state is specified.
     *
     * @param state The turing state to add.
     * @param setToCurrentState If the state should become the current state.
     */
    public void addTuringState(TuringState state, boolean setToCurrentState) {
        if(!states.contains(state)) states.add(state);
        if (setToCurrentState || states.size() == 1 || currentState == null)
            this.currentState = state;
    }

    /**
     * Creates a new state and adds it to the turing machine.
     * If no turing state is currently set or this is the first state,
     * the machine's current state will automatically be set to the
     * newly created one.
     *
     * @return The new turing state.
     */
    public TuringState createTuringState() {
        TuringState state = new TuringState();
        addTuringState(state);
        return state;
    }

    /**
     * Removes a turing state from the machine.
     * Also removed all connection from any state to this state.
     * @param state The state to remove.
     * @return True if the state was removed, false if not.
     */
    public boolean removeTuringState(TuringState state) {
        boolean removal = states.remove(state);
        if (!removal) return false;
        for (TuringState _state : states) {
            _state.removeTransitionsTo(state);
        }
        return removal;
    }

    /**
     * Adds a transition. Doesn't really add the transition since they are entirely
     * stored in the states and added there through the transition's constructor,
     * but rather adds the two states to the turing machine if they are not already
     * included.
     * @param transition The transition to add.
     * @param transitions Additional transitions (optional).
     */
    public void addTransitions(StateTransition transition, StateTransition... transitions) {
        if (transition == null) return;
        addTuringState(transition.getOriginState());
        addTuringState(transition.getDestinationState());
        for (StateTransition tr2 : transitions) {
            addTransitions(tr2);
        }
    }

    /**
     * Gets the state at the specified index.
     *
     * @param index The set index
     * @return The turing state at the given index.
     */
    public TuringState getState(int index) {
        return states.get(index);
    }

    /**
     * If the machine has halted.
     */
    public boolean isHalted() {
        return halted;
    }

    /**
     * If the machine has not halted yet.
     */
    public boolean isNotHalted() {
        return !halted;
    }

    /**
     * Resets the halted parameter to false.
     */
    public void overrideHalted() {
        halted = false;
    }

    /**
     * Goes one step.
     * Move is not printed by default. Normal output is always printed.
     *
     * @return If the machine halted.
     */
    public boolean nextStep() {
        return nextStep(false);
    }

    /**
     * Goes one step. Normal output is always printed.
     * Returns true if the turing machine halts.
     *
     * @param print If the move should be printed in detail.
     * @return If the machine halted.
     */
    public boolean nextStep(boolean print) {
        if (turingTape == null) {
            System.err.println("Error: Tape is null.");
        }
        if (states.isEmpty() || currentState == null) {
            System.err.println("Error: Machine is currently not in any state.");
        }
        if (halted) {
            System.err.println("The machine has already halted. If you want to continue anyway," +
                    "first use the overrideHalted() function to reset the halted status");
        }

        // Okay lets go then, read input
        BigInteger input = turingTape.getCurrentValue();
        StateTransition transition = getCurrentState().getTransition(input);
        if (transition == null) {
            halted = true;
            System.out.println(HALT_STRING);
            return true;
        }

        BigInteger output = transition.getOutputChar();
        MoveAction moveAction = transition.getMoveAction();

        if (print) System.out.printf(ADVANCED_PRINT, transition.getOriginState(), getBigIntAsString(input),
                transition.getDestinationState(), getBigIntAsString(output), getMoveAction(moveAction));
        else System.out.print(output);
        this.turingTape.write(output);
        this.turingTape.move(moveAction);
        this.currentState = transition.getDestinationState();
        return false;
    }
    
    /**
     * Converts an input/output BigInteger to a number.
     * Mainly implemented for converting null to it's appropriate string.
     *
     * @param integer the BigInt value
     * @return BigInt as String
     */
    private static String getBigIntAsString(BigInteger integer) {
        if (integer == null) return EMPTY_SQUARE;
        return integer.toString();
    }
    
    /**
     * Converts a move action to its character for String representation.
     * @return Single character string
     */
    private static String getMoveAction(MoveAction action) {
        return action.toString().substring(0, 1);
    }

}
