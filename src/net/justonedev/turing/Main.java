package net.justonedev.turing;

import java.io.IOException;
import java.math.BigInteger;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        // Lets do a test
        BigInteger[] myNums = {
                new BigInteger("5"),
                new BigInteger("6"),
                new BigInteger("7"),
                new BigInteger("8"),
                new BigInteger("9"),
                new BigInteger("10"),
                new BigInteger("11"),
        };
        TuringTape tape = new TuringTape(10, 3, myNums);
        System.out.println(tape.getCurrentValue());
        for (int i = 0; i < 25; i++) {
            try {
                System.in.read();
            } catch (IOException ignored) { /* dont really care here */ }
            tape.moveRight();
            System.out.println(tape.getCurrentValue());
        }
    }
}