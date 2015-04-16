package com.des.core;

import java.io.IOException;

public class Main {

    /**
     * @param args
     * @throws InterruptedException
     * @throws IOException
     */
    @SuppressWarnings("unused")
    public static void main(String[] args) throws InterruptedException, IOException {
        // Format of input data. In this example K = 133457799BBCDFF1.
    	// Each byte is one hexadecimal number (16 elements with actual size 4 bits). To store hexadecimal number is used byte, and this 
    	// mean, that only 4 lower bits contains value, 4 higher bits are empty(equal to zero). So each byte has the structure 0000VVVV, 
    	// where V value bits.
        byte[] key1 = new byte[] {0x1, 0x3, 0x3, 0x4, 0x5, 0x7, 0x7, 0x9, 0x9, 0xB, 0xB, 0xC, 0xD, 0xF, 0xF, 0x1};
        
        Helper.printBinaryFromLowToHigh(new Key56GeneratorImpl().generate(key1));
     
        //---------------------------------
        
        String unicodeString = "\u0013\u0034\u0057\u0079\u009b\u00bc\u00df\u00f1";

        printStringAsBinary(unicodeString);
        printHex(unicodeString);
        
        //---------------------------------
        
        byte fromInt = (byte) 202;
        int fromByte = fromInt & 0b11111111;

        byte[] magicNumber = {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE};
        for (byte b : magicNumber) {
            byte low = (byte) (b & 0b00001111);
            byte high = (byte) ((b & 0b11110000) >>> 4);
            System.out.printf("%x%x", high, low);
            System.out.println();
        }

        for (int i = 7, j = 0; i > -1; i--, j++) {
            int mask = (int) (0b10000000 >>> j);
            byte bit = (byte) ((magicNumber[0] & mask) >>> i);
            System.out.print(bit);
        }
        System.out.println();

        char c1 = 'p';
        int i1 = c1;

        printHex("pasÐ¿Ð°Ñ�");
        printStringAsBinary("pasÐ¿Ð°Ñ�");
    }

    public static void printStringAsBinary(String str) {
        for (char ch : str.toCharArray()) {
            byte low = (byte) (ch & 0b0000000011111111);
            byte high = (byte) ((ch & 0b1111111100000000) >>> 8);
            printBinary(high);
            printBinary(low);
            System.out.println();
        }
    }

    public static void printBinary(byte b) {
        for (int i = 7, j = 0; i > -1; i--, j++) {
            int mask = (int) (0b10000000 >>> j);
            byte bit = (byte) ((b & mask) >>> i);
            System.out.print(bit);
        }
    }



    public static void printHex(String str) {
        for (char ch : str.toCharArray()) {
            byte low = (byte) (ch & 0b0000000011111111);
            byte high = (byte) ((ch & 0b1111111100000000) >>> 8);
            System.out.printf("%x%x", high, low);
            System.out.println();
        }
    }

    public static byte rotate4bits(byte b, int rotationNumber) {
        byte result = 0;
        for (int i = 0; i < rotationNumber; i++) {
            byte highBit = (byte) (b & 0b00001000);
            result = (byte) (((b << 1) & 0b00001111) | highBit);
        }
        return result;
    }

   

}
