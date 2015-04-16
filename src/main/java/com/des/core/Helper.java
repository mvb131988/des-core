package com.des.core;

public class Helper {

	public static void printBinaryFromLowToHigh(byte[] bytes) {
		for(byte b: bytes){
        	Helper.printBinaryFromLowToHigh(b);
        	System.out.println();
        }
    }
	
    public static void printBinaryFromLowToHigh(byte b) {
        for (int i = 0, j = 0; i < 8; i++, j++) {
            int mask = (int) (0b00000001 << j);
            byte bit = (byte) ((b & mask) >>> i);
            System.out.print(bit);
        }
    }
    
    public static byte mirror4bits(byte b) {
        byte b0 = (byte) ((b & 0b00000001) << 3);
        byte b1 = (byte) ((b & 0b00000010) << 1);
        byte b2 = (byte) ((b & 0b00000100) >> 1);
        byte b3 = (byte) ((b & 0b00001000) >> 3);

        return (byte) (b0 | b1 | b2 | b3);
    }

}
