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
        Integer i11 = (Integer) null;
        //testFile();

        // each byte is one hexadecimal number (16 elements with actual size 4 bits). key1[0] is zero byte, byte[key1.length] is n byte
        byte[] key1 = new byte[] {0x1, 0x3, 0x3, 0x4, 0x5, 0x7, 0x7, 0x9, 0x9, 0xB, 0xB, 0xC, 0xD, 0xF, 0xF, 0x1};

        // Each byte consist of two hexadecimal numbers from key1. Two hexadecimal numbers are joined in the following manner:
        // bit number:         | 1 2 3 4 | 5 6 7 8 |
        // hexadecimal number: |   0x1   |   0x3   |
        // binary number:      | 0 0 0 1 | 0 0 1 1 |
        // Conclusion: each hexadecimal number from key1 has to be transformed(all bits have to be reverse ordered) 
        byte[] key2 = new byte[key1.length / 2];

        for (int i = 0, j = 0; i < key1.length; i += 2, j++) {
            byte high = mirror4bits(key1[i + 1]);
            byte low = mirror4bits(key1[i]);
            byte b = (byte) ((high << 4) | low);
            key2[j] = b;
            //printBinary(key2[j]);
            Helper.printBinaryFromLowToHigh(key2[j]);
            System.out.println();
        }
        
        System.out.println();
        new Key(key2);

        String unicodeString = "\u0013\u0034\u0057\u0079\u009b\u00bc\u00df\u00f1";

        printStringAsBinary(unicodeString);
        printHex(unicodeString);

        //        String s = "Random text 0 \r\n";
        //        char[] sChars = s.toCharArray();
        //        for (char c : sChars) {
        //            int i = c;
        //            System.out.printf("%s(%x)", c, i);
        //        }
        //
        //        char[] ch = {0x0E, 0x32, 0x92, 0x32, 0xEA, 0x6D, 0x0D, 0x73};
        //        String str = new String(ch);
        //        System.out.println();
        //        System.out.println(str);
        //
        //        byte b = -0b1111000;
        //        char c = (char) b;
        //        int i = c;
        //        System.out.println(b);
        //
        //        Console console = System.console();
        //
        //        Set<String> words = new TreeSet<>();
        //        StringReader sr = new StringReader("word1 word2.  word3,,  word4");
        //        try (Scanner scanner = new Scanner(sr)) {
        //            scanner.useDelimiter("word3");
        //            while (scanner.hasNext()) {
        //                String word = scanner.next();
        //                if (!word.equals("")) { //
        //                    words.add(word.toLowerCase());
        //                    System.out.print(word.toLowerCase() + '\t');
        //                }
        //            }
        //            for (String word : words) {
        //                System.out.print(word + '\t');
        //            }
        //        }

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

        printHex("pasпас");
        printStringAsBinary("pasпас");
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

    public static byte mirror4bits(byte b) {
        byte b0 = (byte) ((b & 0b00000001) << 3);
        byte b1 = (byte) ((b & 0b00000010) << 1);
        byte b2 = (byte) ((b & 0b00000100) >> 1);
        byte b3 = (byte) ((b & 0b00001000) >> 3);

        return (byte) (b0 | b1 | b2 | b3);
    }

}
