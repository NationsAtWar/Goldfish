package org.nationsatwar.goldfish.util;

public class Constants {
	
	public static final String PROTOTYPE_NAME_REGEX = "[A-Za-z0-9_\\-\\s]+";
	public static final String NUMBERS_ONLY_REGEX = "[0-9]+";
	
	public static boolean allowedUnicodes(char key) {
		
		if (key == 8 || key == 27 || key == 0)
			return true;
		
		return false;
	}
}