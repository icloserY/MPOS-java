package model;

import java.util.Random;

public class Converter {
	public static String getRandomByte() {
		Random randomGenerator = new Random();
		byte[] randomBytes = new byte[32];
		randomGenerator.nextBytes(randomBytes);
		return Converter.bin2hex(randomBytes);
	}
	public static byte[] hex2bin(String hex) throws NumberFormatException {
	    if (hex.length() % 2 > 0) {
	        throw new NumberFormatException("Hexadecimal input string must have an even length.");
	    }
	    byte[] r = new byte[hex.length() / 2];
	    for (int i = hex.length(); i > 0;) {
	        r[i / 2 - 1] = (byte) (digit(hex.charAt(--i)) | (digit(hex.charAt(--i)) << 4));
	    }
	    return r;
	}
	private static int digit(char ch) {
	    //TODO Optimize this
	    int r = Character.digit(ch, 16);
	    if (r < 0) {
	        throw new NumberFormatException("Invalid hexadecimal string: " + ch);
	    }
	    return r;
	}

	public static String bin2hex(byte[] in) {
	    StringBuilder sb = new StringBuilder(in.length * 2);
	    for (byte b : in) {
	        sb.append(
	                forDigit((b & 0xF0) >> 4)
	        ).append(
	                forDigit(b & 0xF)
	        );
	    }
	    return sb.toString();
	}

	private static char forDigit(int digit) {
	    if (digit < 10) {
	        return (char) ('0' + digit);
	    }
	    return (char) ('A' - 10 + digit);
	}
}
