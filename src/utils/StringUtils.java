package utils;

public class StringUtils {

	public static String formatNumber(int number) {
		int positiveNumber = Math.abs(number);
		String numberString = "" + number;
		// add zeroes and/or minuses if needed to get three character string
		if (number / 10 == 0) {
			numberString = ((number >= 0) ? "00" : "-0") + positiveNumber;
		} else if (number / 100 == 0) {
			numberString = ((number >= 0) ? "0" : "-") + positiveNumber;
		}
		return numberString;
	}

}
