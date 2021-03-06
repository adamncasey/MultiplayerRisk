package test.networking;

import junit.framework.TestCase;
import networking.LobbyClient;
import networking.Networking;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import settings.Settings;

@RunWith(JUnit4.class)
public class GetLobbyClientTest extends TestCase {
    private DummyConnection conn;

    @Before
    public void setup() {
        this.conn = new DummyConnection();
    }

    // Test when another message is there

    // Test when no message is there (short timeout advisable)

    // Test when payload in message is not an object
    // Test when payload in message does not contain supported versions (of array) and supported Features (of array)

    // Test when socket is closed?
    @Test
    public void testGetLobbyClient() throws Exception {
        setup(); // Teamcity build server doesn't want to run using JUnit4, our setup function is being ignored.
        String message = "{\n" +
                "    \"command\":\"join_game\",\n" +
                "    \"payload\":{\n" +
                "        \"supported_versions\":[1.0,2.1],\n" +
                "        \"supported_features\":[\"secure\",\"defaultmap\"]\n" +
                "        \"name\" : \"Player 1\"" +
                "    },\n" +
                "    \"player_id\": 1\n" +
                "}";
        conn.lineToReceive = message;
        Settings.SUPPORT_WRAPPER_MESSAGES = false;
        LobbyClient client = Networking.getLobbyClient(conn, 0);

        Settings.SUPPORT_WRAPPER_MESSAGES = true;
        assertNotNull(client);

        Assert.assertArrayEquals("Supported Features Equality", client.supportedFeatures, new String[]{"secure", "defaultmap"});
    }
}