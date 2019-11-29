package fr.gtm;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Util {
	
	public String chiffrageSHA256(String password) throws NoSuchAlgorithmException{
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] hash = md.digest(password.getBytes());
		BigInteger passwordBigInteger = new BigInteger(1, hash);
		StringBuilder sb = new StringBuilder(passwordBigInteger.toString(16));
		for(int i=64 ; i>sb.length() ; i--) {
			sb.insert(0, "0");
		}
		return sb.toString();
		
	}
	
}
