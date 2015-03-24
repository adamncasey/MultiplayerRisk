package lobby;

public class Validator {
	
	public static boolean isValidPort(String value) {

		if (value != null && value.length() > 0) {
			try {
				int port = Integer.parseInt(value);

				if(port >=0 && port <= 65535) {
					return true;
				}

			} catch (NumberFormatException e) {
			}
		}

		return false;
	}
}
