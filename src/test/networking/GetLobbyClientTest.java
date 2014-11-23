package test.networking;

import junit.framework.TestCase;
import networking.LobbyClient;
import networking.Network;
import org.junit.Before;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.prefs.Preferences;

@RunWith(JUnit4.class)
public class GetLobbyClientTest extends TestCase {
    private DummyConnection conn;

    @Before
    public void setup() {
        this.conn = new DummyConnection();
        System.out.println("@Before annotation success");
    }

    // Test when another message is there

    // Test when no message is there (short timeout advisable)

    // Test when payload in message is not an object
    // Test when payload in message does not contain supported versions (of array) and supported Features (of array)

    // Test when socket is closed?
    @Test
    public void testGetLobbyClient() throws Exception {
        String message = "{\n" +
                "    \"command\":\"join_game\",\n" +
                "    \"payload\":{\n" +
                "        \"supported_versions\":[1.0,2.1],\n" +
                "        \"supported_features\":[\"secure\",\"defaultmap\"]\n" +
                "    },\n" +
                "    \"signature\": \"TBD\",\n" +
                "    \"player_id\": 1\n" +
                "}";
        conn.lineToReceive = message;
        LobbyClient client = Network.getLobbyClient(conn);
        assertNotNull(client);

        Assert.assertArrayEquals("Supported Features Equality", client.supportedFeatures, new String[]{"secure", "defaultmap"});
    }
}