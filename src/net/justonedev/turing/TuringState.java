package net.justonedev.turing;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * A state for a turing machine.
 *
 * @author justonedeveloper
 */
public class TuringState {
    private final List<StateTransition> transitions;
    private int stateID;
    private String stateName;

    /**
     * Creates a new turing state.
     * Uses default ID 0 and state name "q0".
     */
    public TuringState() {
        this(0, "q0");
    }

    /**
     * Creates a new turing state.
     * Uses the default name "q(id)", which can be changed
     * later using setStateName(String).
     *
     * @param id The state id.
     */
    public TuringState(int id) {
        this(id, "q%d".formatted(id));
    }

    /**
     * Creates a new turing state.
     * Automatically assigns the state the id 0, which cannot be changed later.
     *
     * @param stateName The state name.
     */
    public TuringState(String stateName) {
        this(0, stateName);
    }

    /**
     * Creates a new turing state.
     * Uses the given id and state name, both can be changed later.
     *
     * @param id The state id.
     * @param stateName The state name.
     */
    public TuringState(int id, String stateName) {
        this.transitions = new ArrayList<>();
        this.stateID = id;
        this.stateName = stateName;
    }

    /**
     * Adds a transition to the state.
     * Does not add transitions that are already added. Transition is not added
     * when the state already contains that transition.
     *
     * @param transition The transition to add.
     * @return If the transition was added or not.
     */
    public boolean addTransition(StateTransition transition) {
        if(!transitions.contains(transition)) {
            this.transitions.add(transition);
            return true;
        }
        return false;
    }

    /**
     * Removes a given transition from the state.
     * @param transition The transition to remove.
     * @return If the transition was removed.
     */
    public boolean removeTransition(StateTransition transition) {
        return this.transitions.remove(transition);
    }

    /**
     * Removes all transitions from this state to a given other state.
     * @param state The destination state.
     */
    public void removeTransitionsTo(TuringState state) {
        for (int i = 0; i < transitions.size(); i++) {
            StateTransition tr = transitions.get(i);
            if (!tr.getDestinationState().equals(state)) {
                continue;
            }
            i--;
            transitions.remove(tr);
        }
    }

    /**
     * Gets the transition for the given character.
     * If no transition exists, returns null, indicating a halt.
     *
     * @return The transition or null.
     */
    public StateTransition getTransition(BigInteger character) {
        for (StateTransition transition : transitions) {
            if (transition.hasInputChar(character)) {
                return transition;
            }
        }
        return null;
    }

    /**
     * Gets the states name.
     * @return The states name.
     */
    public String getStateName() {
        return stateName;
    }

    /**
     * Sets the states name to a new value.
     * State is not equal to ID and not used for comparison.
     * @param stateName New state name.
     */
    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    /**
     * Gets the ID of a state.
     * Should be unique.
     * @return The state ID.
     */
    public int getStateID() {
        return stateID;
    }

    /**
     * Sets the ID of a state. Should be unique.
     * Updating is not recommended.
     *
     * @param stateID The new state ID.
     */
    public void setStateID(int stateID) {
        this.stateID = stateID;
    }
    
    @Override
    public String toString() {
        return "(%s::%d)".formatted(getStateName(), getStateID());
    }
}
