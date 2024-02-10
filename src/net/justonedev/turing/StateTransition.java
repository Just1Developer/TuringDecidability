package net.justonedev.turing;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

/**
 * A transition to transition from one state to another.
 *
 * @author justonedeveloper
 */
public class StateTransition {

    private final TuringState fromState;
    private final TuringState toState;
    private Set<BigInteger> inputChars;
    private final BigInteger outputChar;
    private MoveAction moveAction;

    public StateTransition(
            TuringState fromState,
            TuringState toState,
            Set<BigInteger> inputChars,
            BigInteger outputChar,
            MoveAction moveAction
    ) {
        this.fromState = fromState;
        this.toState = toState;
        this.inputChars = inputChars;
        this.outputChar = outputChar;
        this.moveAction = moveAction;

        this.fromState.addTransition(this);
    }

    public StateTransition(
            TuringState fromState,
            TuringState toState,
            BigInteger inputChar,
            BigInteger outputChar,
            MoveAction moveAction
    ) {
        this(fromState, toState, new HashSet<>(Collections.singletonList(inputChar)), outputChar, moveAction);
    }
    
    /**
     * Creates a new State Transition.
     * This constructor does not take an input char,
     * used for making input char null.
     *
     * @param fromState The origin state.
     * @param toState The destination state.
     * @param outputChar The output character. May be null.
     * @param moveAction The move action after.
     */
    public StateTransition(
            TuringState fromState,
            TuringState toState,
            BigInteger outputChar,
            MoveAction moveAction
    ) {
        this(fromState, toState, new HashSet<>(Collections.singletonList(null)), outputChar, moveAction);
    }

    public StateTransition(
            TuringState fromState,
            TuringState toState
    ) {
        this(fromState, toState, new HashSet<>(), BigInteger.ZERO, MoveAction.NONE);
    }

    public TuringState getOriginState() {
        return this.fromState;
    }

    public TuringState getDestinationState() {
        return this.toState;
    }

    public Set<BigInteger> getInputChars() {
        return this.inputChars;
    }

    public boolean hasInputChar(BigInteger input) {
        return getInputChars().contains(input);
    }

    public BigInteger getOutputChar() {
        return this.outputChar;
    }

    public MoveAction getMoveAction() {
        return this.moveAction;
    }

    public void setMoveAction(MoveAction action) {
        this.moveAction = action;
    }

    public void setInputChars(Set<BigInteger> inputChars) {
        this.inputChars = inputChars;
    }

}
