package com.des.core;

//TODO: Change definition for key56 from byte to block, cause each block consist only of 7 bits.
public class Key56GeneratorImpl implements Key56Generator{

    // each bytes actually consist of 7 bits, the highest one is unused (leave with zero value)  
    private byte[] key56;
    // index(i) of current byte, that belongs to key56 (current byte is being built in the moment of execution) 
    int i56Byte = 0;

    public Key56GeneratorImpl() {
        key56 = new byte[8];
    }

    @Override
    public byte[] generate(byte[] originalKey) {
    	byte[] key64 = grouping(originalKey);
    	
    	buildBytes1234(key64);
        buildBytes5678(key64);
    	return key56;
    }
    
    /**
     * Original key contains of 16 bytes (each byte represents hexadecimal number). 
     * Example: 133457799BBCDFF1 {0x1, 0x3, 0x3, 0x4, 0x5, 0x7, 0x7, 0x9, 0x9, 0xB, 0xB, 0xC, 0xD, 0xF, 0xF, 0x1}
     * 
     * This 16 bytes array is transformed in 8 bytes array. Bytes i and (i+1), where i = {0,2,4,6,8,10,12,14} are consolidated in one byte (consolidated byte).
     * However in consolidated byte bits numbers go from left to right, opposed to original byte where bits numbers go from right to left.
     * Example:
     *               original bytes        consolidated byte        
     * hex         |   0x1   |   0x3   |  hex         |   0x1   |   0x3   |
     * bit number: | 4 3 2 1 | 4 3 2 1 |  bit number: | 1 2 3 4 | 5 6 7 8 |
     * binary      | 0 0 0 1 | 0 0 1 1 |  binary      | 0 0 0 1 | 0 0 1 1 |
     * 
     * As a result before consolidation each original byte must be mirrored (bits positions have to be changed corresponding to scheme above - reverse ordering).  
     * 
     * Original key(16 bytes array) is transformed in 64bit key(8 bytes array) and is used in all later steps of this class.
     * Note: bits numbers start from left to right. Most left is 1 byte, most right is 64 byte.   
     * Example:
     *  1    3    3    4    5    7    7    9    9    B    B    C    D    F    F    1
     * 0001|0011 0011|0100 0101|0111 0111|1001 1001|1011 1011|1100 1101|1111 1111|0001
     * 
     * @param originalKey - 16 bytes of original key as it is described above.
     * @return - 64bit key, derived from original key as it is described above.
     */
    private byte[] grouping(byte[] originalKey){
    	byte[] key64 = new byte[originalKey.length / 2];

        for (int i = 0, j = 0; i < originalKey.length; i += 2, j++) {
            byte high = Helper.mirror4bits(originalKey[i + 1]);
            byte low = Helper.mirror4bits(originalKey[i]);
            byte b = (byte) ((high << 4) | low);
            key64[j] = b;
        }
        return key64;
    }

    private void buildBytes1234(byte[] key64) {
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
        }
    }

    private void buildBytes5678(byte[] key64) {
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
        }

        for (int i = 0; i < 3; i++) {
            byte56 = (byte) (byte56 | (rewinder.nextForwardBit() << i56Bit++));
        }
        rewinder = new Key64Rewinder(key64, 3, 3);
        for (int i = 0; i < 4; i++) {
            byte56 = (byte) (byte56 | (rewinder.nextBackwardBit() << i56Bit++));
        }
        key56[i56Byte++] = byte56;
    }

}
