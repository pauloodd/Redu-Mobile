package br.com.redumobile.auxiliar;

public class AuxiliarMatematica {
	public static double clamp(double valor, double min, double max)
			throws RuntimeException {
		double resultado = valor;

		if (min < max) {
			if (valor > max) {
				resultado = max;
			} else if (valor < min) {
				resultado = min;
			}
		} else {
			throw new RuntimeException(
					"O valor mínimo não deve ser maior do que o máximo!");
		}

		return resultado;
	}
}
