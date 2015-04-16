package com.des.core;

/**
 *	It's used to extract bits from key64 according to permutation table PC-1 (ref. p5) 
 */
public class Key64Rewinder {
	
	/** 64bit key, derived from original key as it is in grouping() method (Key56GeneratorImpl) */
    private byte[] key64;
    /** start byte position in key64 */
    private byte currentByte;
    /** start bit position in start byte */
    private int currentBitPosition;

    /**
     * Sets start position. All bits are returned relative to this position.
     * 
     * @param key64 - 64bit key, derived from original key as it is in grouping() method (Key56GeneratorImpl).
     * @param startByte - start byte position in key64.
     * @param startBit - start bit position in start byte.
     */
    public Key64Rewinder(byte[] key64, int startByte, int startBit) {
        this.key64 = key64;
        this.currentByte = (byte) startByte;
        this.currentBitPosition = (byte) startBit;
    }
    
    /**
     * Extracts next bit corresponding to permutation table PC-1 (ref. p5). When bit from byte 1 is returned, then bit number is incremented (+1). 
     * 
     * Assuming byte 8, bit 7 were selected (implementation has indexes are less on 1). So bit number 7 from byte 8 is
     * first returned by the method. After that byte number is decreased by 1 (it becomes 7) with the same bit number (it remains 7). So
     * bit 7 from byte 7 is returned. In this manner all bytes from 8 till 1 are visited and from each byte bit number 7 is extracted.
     * After the method returns bit number 7 from byte 1, byte position is changed to start position (byte number 8), and bit position
     * is decreased by one (it becomes 6). All process with bytes number decreasing is repeated in the manner described above until byte 1
     * is reached. After that bit position is decreased by one once again and the process continue from byte number 8.  
     * 
     * Note: bit number 8 from each byte is unused, so it is skipped.
     * 
     * @return next bit from key64 (byte has only one value bit. Example: 0000000V, where V value bit)
     */
    public byte nextForwardBit() {
        int mask = 0b00000001 << currentBitPosition;
        byte currentBit = (byte) ((key64[currentByte] & mask) >> currentBitPosition);

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


    /**
     * Extracts next bit corresponding to permutation table PC-1 (ref. p5). When bit from byte 1 is returned, then bit number is decremented (-1).
     * 
     * Considering when condition bit extraction process repeats like in nextBackwardBit().
     * 
     * @return next bit from key64 (byte has only one value bit. Example: 0000000V, where V value bit)
     */
    public byte nextBackwardBit() {
        int mask = 0b01000000 >> (6 - currentBitPosition);
        byte currentBit = (byte) ((key64[currentByte] & mask) >> currentBitPosition);

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
