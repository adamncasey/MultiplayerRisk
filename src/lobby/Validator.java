package lobby;

public class Validator {
	
	public static boolean isValidPort(String value) {
		boolean isValid = false;

		if (value != null && value.length() > 0) {
			try {
				Integer.parseInt(value);
				isValid = true;
			} catch (NumberFormatException e) {
			}
		}

		return isValid;
	}
}
