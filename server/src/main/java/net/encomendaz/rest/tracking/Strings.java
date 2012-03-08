package net.encomendaz.rest.tracking;

public class Strings {

	public static String parseToFirstUpper(String texto) {
		StringBuffer buffer = new StringBuffer();

		for (String palavra : texto.split(" ")) {
			if (palavra.length() > 2) {
				buffer.append(palavra.substring(0, 1).toUpperCase() + palavra.substring(1).toLowerCase());
			} else {
				buffer.append(palavra.toLowerCase());
			}

			buffer.append(" ");
		}

		return buffer.toString().trim();
	}
}
