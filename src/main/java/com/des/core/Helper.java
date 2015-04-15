package com.des.core;

public class Helper {

    public static void printBinaryFromLowToHigh(byte b) {
        for (int i = 0, j = 0; i < 8; i++, j++) {
            int mask = (int) (0b00000001 << j);
            byte bit = (byte) ((b & mask) >>> i);
            System.out.print(bit);
        }
    }

}
