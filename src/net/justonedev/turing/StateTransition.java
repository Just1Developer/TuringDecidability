package net.justonedev.turing;

import net.justonedev.turing.collections.LimitlessBinaryMap;
import net.justonedev.turing.collections.MapResult;

import java.math.BigInteger;
import java.util.Collection;
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

    /**
     * Sets the input characters for this transition using Strings and translating using
     * the given map.
     * If {@code addMissingMappings} is true, the characters that are not mapped yet are
     * added, if possible, to the map. Otherwise, the missing input chars will not be added
     * to the transition characters.
     * <p></p>
     * Returns the new map. If {@code addMissingMappings} is false, will always return the map.
     * Otherwise, might return map with new entries.
     *
     * @param map The translation map.
     * @param addMissingMappings If the Strings that are not mapped should be added to the map.
     * @param inputChars The inputs.
     * @return The new map of translations.
     */
    public LimitlessBinaryMap<BigInteger, String> setInputChars(LimitlessBinaryMap<BigInteger, String> map, boolean addMissingMappings, String... inputChars) {
        this.inputChars = new HashSet<>();
        return addInputChars(map, addMissingMappings, inputChars);
    }

    /**
     * Adds the input characters for this transition using Strings and translating using
     * the given map. Current characters will not be erased, though watch out for map collisions
     * when using multiple maps.
     * If {@code addMissingMappings} is true, the characters that are not mapped yet are
     * added, if possible, to the map. Otherwise, the missing input chars will not be added
     * to the transition characters.
     * <p></p>
     * Returns the new map. If {@code addMissingMappings} is false, will always return the map.
     * Otherwise, might return map with new entries.
     * <p></p>
     * Note that null is always mapped to empty square so null will not translate but still be added.
     *
     * @param map The translation map.
     * @param addMissingMappings If the Strings that are not mapped should be added to the map.
     * @param inputChars The inputs.
     * @return The new map of translations.
     */
    public LimitlessBinaryMap<BigInteger, String> addInputChars(LimitlessBinaryMap<BigInteger, String> map, boolean addMissingMappings, String... inputChars) {
        for (String input : inputChars) {
            MapResult<BigInteger, String> result = map.getPairByValue(input);
            if (result.isFound()) {
                this.inputChars.add(result.getKey());
                continue;
            }

            if (!addMissingMappings) continue;

            BigInteger i = BigInteger.ZERO;
            while (map.containsKey(i)) {
                i = i.add(BigInteger.ONE);
            }
            map.add(i, input);
            this.inputChars.add(i);
        }
        return map;
    }

}
