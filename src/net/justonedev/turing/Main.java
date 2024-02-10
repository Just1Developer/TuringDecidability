package net.justonedev.turing;

import java.io.IOException;
import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        // Lets do a test
        BigInteger[] myNums = {
                //*
                new BigInteger("1"),
                new BigInteger("1"),
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
        TuringTape tape = new TuringTape(15, 3, myNums);
        
        TuringMachine machine = new TuringMachine();
        machine.setTape(tape);
        
        TuringState state1 = new TuringState(0);
        TuringState state2 = new TuringState(1);
        StateTransition tr1 = new StateTransition(state1, state1, myNums[0], myNums[7], MoveAction.RIGHT);
        StateTransition tr2 = new StateTransition(state1, state2, myNums[7], BigInteger.ONE, MoveAction.RIGHT);
        
        machine.addTransition(tr1);
        machine.addTransition(tr2);
        
        System.out.println("Tape: " + tape);
        int s = 0;
        
        while(machine.isNotHalted()) {
            System.out.println("Step: " + ++s);
            machine.nextStep(true);
            System.out.println("Tape: " + tape);
        }
        
        System.out.println("Tape: " + tape);
        
            /*
        System.out.println(tape.getCurrentValue());
        for (int i = 0; i < 25; i++) {
            try {
                System.in.read();
            } catch (IOException ignored) { /* dont really care here * / }
            tape.moveRight();
            System.out.println(tape.getCurrentValue());
        }
        */
    }
}