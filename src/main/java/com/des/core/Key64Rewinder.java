package com.des.core;

public class Key64Rewinder {

    private byte[] key64;
    // Possible values 0 - 6
    private int startByte;
    // Possible values 0 - 6
    private int startBit;
    private int mask;
    
    /*------------------*/
    private byte currentBit = 0;
    private int currentBitPosition = 0;
    private byte currentByte = 0;

    public Key64Rewinder(byte[] key64, int startByte, int startBit) {
        this.key64 = key64;
        this.startByte = startByte;
        this.startBit = startBit;
        this.mask = 0b00000001 << startBit;

        this.currentBitPosition = (byte) startBit;
        this.currentByte = (byte) startByte;
    }
    
    //TODO: Check method names
    public byte nextBackwardBit() {
        mask = 0b00000001 << currentBitPosition;
        currentBit = (byte) ((key64[currentByte] & mask) >> currentBitPosition);

        if (currentByte > -1) {
            currentByte--;
        }
        if (currentByte == -1) {
            currentByte = 7;
            if (currentBitPosition < 6) {
                currentBitPosition++;
            } else {
                currentBitPosition = 0;
            }
        }

        return currentBit;
    }

    //TODO: Check method names
    public byte nextForwardBit() {
        mask = 0b01000000 >> (6 - currentBitPosition);
        currentBit = (byte) ((key64[currentByte] & mask) >> currentBitPosition);

        if (currentByte > -1) {
            currentByte--;
        }
        if (currentByte == -1) {
            currentByte = 7;
            if (currentBitPosition > -1) {
                currentBitPosition--;
            }
            if (currentBitPosition == -1) {
                currentBitPosition = 0;
            }
        }

        return currentBit;
    }

}
