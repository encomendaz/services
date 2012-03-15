package net.encomendaz.rest.server.util;

public class Strings {

	public static boolean isEmpty(String string) {
		return string == null || string.trim().length() == 0;
	}
	
	public static String firstToUpper(String string) {
		StringBuffer buffer = new StringBuffer();

		for (String part : string.split(" ")) {
			if (part.length() > 2) {
				buffer.append(part.substring(0, 1).toUpperCase() + part.substring(1).toLowerCase());
			} else {
				buffer.append(part.toLowerCase());
			}

			buffer.append(" ");
		}

		return buffer.toString().trim();
	}
}
