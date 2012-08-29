package br.com.redumobile.auxiliar;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class AuxiliarHash {
	public static String gerarHashMD5(String texto)
			throws NoSuchAlgorithmException {
		String hash = "";

		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(texto.getBytes(), 0, texto.length());

		hash = new BigInteger(1, md5.digest()).toString(16);

		return hash;
	}
}
