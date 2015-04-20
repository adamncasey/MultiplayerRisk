package settings;

/**
 * Stores settings for the game.
 *
 * TODO: We will probably want to load these from a file so they can be easily changed.
 */
public class Settings {
    public static int port = 4444;
    public static String listenIPAddress = "0.0.0.0";
    public static int socketTimeout = 5000;
    public static String boardFilename = "resources/risk_map.json";
    public static final int MaxNumberOfPlayers = 6;
    public static final int MinNumberOfPlayers = 3;

    /**
     * Enables support for the outer wrapper message format.
     */
    public static boolean SUPPORT_WRAPPER_MESSAGES = true;

    public static final int ACKNOWLEDGEMENT_TIMEOUT = 100;
    public static final int MOVE_TIMEOUT = 200;


    public static final boolean PRINT_NETWORK_TRAFFIC = true;


    public static final boolean ExtraArmiesTogether = true;
}
