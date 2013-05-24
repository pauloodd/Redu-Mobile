package br.com.redumobile.util;

public final class Utils {
	public static double clamp(double value, double min, double max) {
		return Math.max(min, Math.min(max, value));
	}

	private Utils() {
	}
}
