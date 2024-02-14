package net.justonedev.turing.acceptors;

import net.justonedev.turing.TuringMachine;

/**
 * A turing machine with input acceptance, states can be set to accepting.
 * If the machine halts in an accepting state at the end, the input is marked
 * as accepted. If the machine loops, the input is generally not accepted.
 * <p></p>
 * To implement functionality, add AcceptorStates instead of turing states.
 * While turing states are valid to implement, they will not provide halting functionality.
 *
 * @author justonedeveloper
 */
public class Acceptor extends TuringMachine {

    private boolean inputAccepted;

    /**
     * If the input is accepted, this value can only be true after halting,
     * and returns if the machine halted in an accepting state.
     *
     * @return If the input is accepted.
     */
    public boolean isInputAccepted() {
        return inputAccepted;
    }

    /**
     * Resets the halted parameter to false.
     * Also resets the accepted parameter
     */
    @Override
    public void overrideHalted() {
        super.overrideHalted();
        this.inputAccepted = false;
    }

    /**
     * Goes one step. Normal output is always printed.
     * Returns true if the turing machine halts.
     * <p></p>
     * If the machine halts, updates the accepted property.
     *
     * @param print If the move should be printed in detail.
     * @return If the machine halted.
     */
    @Override
    public boolean nextStep(boolean print) {
        boolean halted = super.nextStep(print);
        if (!halted) return false;  // Not yet halted, irrelevant

        if (getCurrentState() instanceof AcceptorState) {
            this.inputAccepted = ((AcceptorState) getCurrentState()).isAcceptingState();
        }

        return true;
    }
}
