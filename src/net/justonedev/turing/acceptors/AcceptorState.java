package net.justonedev.turing.acceptors;

import net.justonedev.turing.TuringState;

/**
 * A turing state for an acceptor, adds accepting boolean property.
 * Compatible with turing machines and acceptors, though the behavior will
 * not be different from Turing states in Turing machines.
 *
 * @author justonedeveloper
 */
public class AcceptorState extends TuringState {

    private boolean isAcceptingState;

    //region Constructors

    /**
     * Creates a new acceptor state.
     * Uses default ID 0 and state name "q0".
     */
    public AcceptorState() {
        super(0, "q0");
    }

    /**
     * Creates a new acceptor state.
     * Uses the default name "q(id)", which can be changed
     * later using setStateName(String).
     *
     * @param id The state id.
     */
    public AcceptorState(int id) {
        super(id, "q%d".formatted(id));
    }

    /**
     * Creates a new acceptor state.
     * Automatically assigns the state the id 0, which cannot be changed later.
     *
     * @param stateName The state name.
     */
    public AcceptorState(String stateName) {
        super(0, stateName);
    }

    /**
     * Creates a new acceptor state.
     * Uses the given id and state name, both can be changed later.
     *
     * @param id The state id.
     * @param stateName The state name.
     */
    public AcceptorState(int id, String stateName) {
        super(id, stateName);
    }

    //endregion

    /**
     * If the state is an accepting state, meaning if the input is accepted
     * when the machine ends here.
     *
     * @return If state is accepting.
     */
    public boolean isAcceptingState() {
        return isAcceptingState;
    }

    /**
     * Sets the state to be accepting or not.
     * This status matters for input validation using a halting Acceptor.
     * <p></p>
     * If the machine halts on this state, the input is 'accepted' if this state
     * is marked as accepting state.
     *
     * @param acceptingState If the state should be accepting.
     */
    public void setIsAcceptingState(boolean acceptingState) {
        this.isAcceptingState = acceptingState;
    }

}
