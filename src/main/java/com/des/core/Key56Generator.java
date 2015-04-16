package com.des.core;

public interface Key56Generator {

	/**
	 * Generate 56 bits key from original key.
	 * 
	 * @param originalKey - original key consists of 16 bytes. Each byte is one hexadecimal number (16 elements with actual size 4 bits). 
	 * 						To store hexadecimal number is used byte, and this mean, that only 4 lower bits contains value, 4 higher bits are
	 *            			empty(equal to zero). So each byte has the structure 0000VVVV, where V value bits.
	 * @return key56 - 56 bits key is derived from key64. It is stored in 8 bytes array. Each byte contains 7 value bits. The highest one is unused
	 * 				   and is filled with 0. So each byte has the structure 0VVVVVVV, where V value bits.
	 */
	byte[] generate(byte[] originalKey);

}
