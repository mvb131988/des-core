package com.des.core;

//TODO: Change definition for key56 from byte to block, cause each block consist only of 7 bits.
public class Key {

    // each bytes actually consist 7 bits, the highest one is unused (leave with zero value)  
    private byte[] key56;

    public Key(byte[] key64) {
        key56 = new byte[8];

        buildBytes1234V2(key64);
        buildBytes5678V2(key64);
    }

    /**
     * Use 3 low bits from each byte of key64 to build low (3 bytes) of key56.
     * 
     * @param key64
     */
    @Deprecated
    public void buildBytes1234(byte[] key64) {
        // index(i) of current byte, that belongs to key56 (current byte is being built in the moment of execution) 
        int i56Byte = 0;
        // index(i) of current bit, that belongs to current byte of key56 (current byte is being selected from key64 in the moment of execution) 
        int i56Bit = 0;
        // indicates number(n) of bits, that belongs to current byte and have already been processed
        int n56Bits = 0;

        // mask, that is used to retrieve necessary bit
        byte mask = 0b00000001;
        // byte, that is being built at the moment of execution 
        byte byte56 = 0;

        // i64Bit - index(i) of current bit, that belongs to key64 (current byte is being processed in the moment of execution). Here we hold 
        // one position and get corresponding to this position bit from each byte of key64.
        for (int i64Bit = 0; i64Bit < 3; i64Bit++) {
            if (i64Bit > 0) {
                mask = (byte) (mask << 1);
            }

            for (int i64Byte = 7; i64Byte > -1; i64Byte--) {
                // Note, that selected from key64 bit, first of all should be moved to zero position. Only after, it can be moved to the necessary position
                // of key56. 
                byte56 = (byte) (byte56 | (((key64[i64Byte] & mask) >> i64Bit) << i56Bit++));

                n56Bits++;
                // if the last bit of (current byte of key56) has been processed, save this byte and prepare variables to process next byte 
                if (n56Bits == 7) {
                    key56[i56Byte++] = byte56;
                    i56Bit = 0;
                    n56Bits = 0;
                    byte56 = 0;
                }
            }

            Helper.printBinaryFromLowToHigh(key56[i56Byte - 1]);
            System.out.println();
        }
    }

    private void buildBytes1234V2(byte[] key64) {
        // index(i) of current byte, that belongs to key56 (current byte is being built in the moment of execution) 
        int i56Byte = 0;
        // index(i) of current bit, that belongs to current byte of key56 (current byte is being selected from key64 in the moment of execution) 
        int i56Bit = 0;
        // current byte, that is being built at the moment of execution 
        byte byte56 = 0;

        Key64Rewinder rewinder = new Key64Rewinder(key64, 7, 0);
        for (int i = 0; i < 4; i++) {

            for (int j = 0; j < 7; j++) {
                byte56 = (byte) (byte56 | (rewinder.nextBackwardBit() << i56Bit++));
            }

            key56[i56Byte++] = byte56;
            i56Bit = 0;
            byte56 = 0;

            Helper.printBinaryFromLowToHigh(key56[i56Byte - 1]);
            System.out.println();
        }
    }

    private void buildBytes5678V2(byte[] key64) {
        // index(i) of current byte, that belongs to key56 (current byte is being built in the moment of execution) 
        int i56Byte = 0;
        // index(i) of current bit, that belongs to current byte of key56 (current byte is being selected from key64 in the moment of execution) 
        int i56Bit = 0;
        // current byte, that is being built at the moment of execution 
        byte byte56 = 0;

        Key64Rewinder rewinder = new Key64Rewinder(key64, 7, 6);
        for (int i = 0; i < 3; i++) {

            for (int j = 0; j < 7; j++) {
                byte56 = (byte) (byte56 | (rewinder.nextForwardBit() << i56Bit++));
            }

            key56[i56Byte++] = byte56;
            i56Bit = 0;
            byte56 = 0;

            Helper.printBinaryFromLowToHigh(key56[i56Byte - 1]);
            System.out.println();
        }

        for (int i = 0; i < 3; i++) {
            byte56 = (byte) (byte56 | (rewinder.nextForwardBit() << i56Bit++));
        }
        rewinder = new Key64Rewinder(key64, 3, 3);
        for (int i = 0; i < 4; i++) {
            byte56 = (byte) (byte56 | (rewinder.nextBackwardBit() << i56Bit++));
        }
        key56[i56Byte++] = byte56;
        Helper.printBinaryFromLowToHigh(key56[i56Byte - 1]);
        System.out.println();
    }

}
