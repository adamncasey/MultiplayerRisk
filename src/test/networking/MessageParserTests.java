package test.networking;

import static org.junit.Assert.*;
import networking.Command;
import networking.message.payload.*;
import networking.message.Message;
import networking.parser.Parser;
import networking.parser.ParserException;

import org.json.simple.JSONObject;
import org.junit.Test;

public class MessageParserTests {

	/*@Test
	public void escapeTest() {
		String test = "\"";
		assertEquals("\\\"", Parser.escapeJson(test));

		test = "\\";
		assertEquals("\\\\", Parser.escapeJson(test));

		test = "\n";
		assertEquals("\\n", Parser.escapeJson(test));
	}*/

	@Test
	public void testWrapperFormat() throws ParserException {
		String innerMessage = "{\"command\": \"acknowledgement\", \"payload\": 32183921, \"player_id\": 5 }";

		String message = "{\"message\": \"" + JSONObject.escape(innerMessage) + "\"}";

		Message msg = Parser.parseOuterMesage(message);

		assertNotNull(msg);
		assertEquals(msg.command, Command.ACKNOWLEDGEMENT);

		assertTrue(msg.payload instanceof IntegerPayload);
	}

	@Test
	public void testAcknowledgeMessage() throws ParserException {
		/*
		{
			 "command": "acknowledge",
			 "payload":	{
			 				"ack_id": 32183921,
			 				"response": 0,
			 				"data": null
			 			}
			 "player_id": "5",
			 "signature": "TBD"
		}
		*/
		String message = "{\r\n" +
				"			 \"command\": \"acknowledgement\",\r\n" +
				"			 \"payload\": 32183921,\r\n" +
				"			 \"player_id\": 5\r\n" +
				"		}";

		Message msg = Parser.parseMessage(message);

		assertNotNull(msg);
		assertEquals(msg.command, Command.ACKNOWLEDGEMENT);

        assertTrue(msg.payload instanceof IntegerPayload);
	}


	@Test
	public void testReady() throws ParserException {
		/*
		{
			 "command": "ready",
			 "payload":	null,
			 "player_id": 1,
			 "ack_id": 541232121,
		}
		*/
		String message = "{" +
				"			 \"command\": \"ready\"," +
				"			 \"payload\":	null," +
				"			 \"player_id\": 1,\n" +
				"			 \"ack_id\": 541232121," +
				"		}";

		Message msg = Parser.parseMessage(message);

		assertNotNull(msg);
		assertEquals(msg.command, Command.READY);
        assertNull(msg.payload);
	}
	@Test
	public void testPing() throws ParserException {
		/*
		{
			 "command": "ping",
			 "payload":	5,
			 "player_id": 1,
			 "signature": "TBD"
		}
		*/
		String message = "{" +
				"			 \"command\": \"ping\"," +
				"			 \"payload\":	5," +
				"			 \"player_id\": 1," +
				"		}";

		Message msg = Parser.parseMessage(message);

		assertNotNull(msg);
		assertEquals(msg.command, Command.PING);
        assertTrue(msg.payload instanceof IntegerPayload);
	}
	@Test
	public void testJoinGame() throws ParserException {
		/*
		{
			 "command": "join_game",
			 "payload":	{
			        "supported_versions": [1],
			        "supported_features": ["custom_map"]
			    },
			 "player_id": 1,
			 "signature": "TBD"
		}
		*/
		String message = "{" +
				"			 \"command\": \"join_game\"," +
				"			 \"payload\":	{" +
				"			        \"supported_versions\": [1]," +
				"			        \"supported_features\": [\"custom_map\"]" +
				"					\"name\": \"Player 1\"" +
				"			    }," +
				"			 \"player_id\": 1," +
				"		}";

		Message msg = Parser.parseMessage(message);

		assertNotNull(msg);
		assertEquals(msg.command, Command.JOIN_GAME);
        assertTrue(msg.payload instanceof JoinGamePayload);

		JoinGamePayload payload = (JoinGamePayload)msg.payload;

		assertEquals("Player 1", payload.playerName);
	}
	@Test
	public void testJoinAccept() throws ParserException {
		/*
		{
			 "command": "accept_join_game",
			 "payload":	{
			        "player_id": 1,
			        "acknowledgement_timeout": 2,
			        "move_timeout": 30
			    },
			 "player_id": 1,
			 "signature": "TBD"
		}
		*/
		String message = "{" +
				"			 \"command\": \"accept_join_game\"," +
				"			 \"payload\":	{" +
				"			        \"player_id\": 1," +
				"			        \"acknowledgement_timeout\": 2," +
				"			        \"move_timeout\": 30" +
				"			    }," +
				"			 \"player_id\": 1," +
				"		}";

		Message msg = Parser.parseMessage(message);

		assertNotNull(msg);
		assertEquals(msg.command, Command.JOIN_ACCEPT);
        assertTrue(msg.payload instanceof AcceptJoinGamePayload);
	}
	@Test
	public void testJoinReject() throws ParserException {
		/*
		{
			 "command": "reject_join_game",
			 "payload":	"I hate you",
			 "player_id": 1,
			 "signature": "TBD"
		}
		*/
		String message = "{" +
				"			 \"command\": \"reject_join_game\"," +
				"			 \"payload\":	\"I hate you\"," +
				"			 \"player_id\": 1," +
				"		}";

		Message msg = Parser.parseMessage(message);

		assertNotNull(msg);
		assertEquals(msg.command, Command.JOIN_REJECT);
        assertTrue(msg.payload instanceof RejectJoinGamePayload);
	}


    @Test
    public void testPlayCards() throws ParserException {
        String message = "	{\r\n" +
                "		 \"command\": \"play_cards\",\r\n" +
                "		 \"payload\": null,\r\n" +
                "		 \"player_id\": 0,\r\n" +
                "		 \"ack_id\": 67812687\r\n" +
                "	}";

        Message msg = Parser.parseMessage(message);

        assertNotNull(msg);
        assertEquals(msg.command, Command.PLAY_CARDS);
    }
    @Test
    public void testPlayCards2() throws ParserException {
        String message = "	{\r\n" +
                "		 \"command\": \"play_cards\",\r\n" +
                "		 \"payload\": {" +
                "           \"cards\": [\n" +
                "           [1, 2, 3],\n" +
                "           [4, 5, 6]\n" +
                "           ],\n" +
                "           \"armies\": 3" +
                "       },\r\n" +
                "		 \"player_id\": 0,\r\n" +
                "		 \"ack_id\": 67812687\r\n" +
                "	}";

        Message msg = Parser.parseMessage(message);

        assertNotNull(msg);
        assertEquals(msg.command, Command.PLAY_CARDS);
    }
    @Test
    public void testSetup() throws ParserException {
        String message = "	{\r\n" +
                "		 \"command\": \"setup\",\r\n" +
                "		 \"payload\": 35,\r\n" +
                "		 \"player_id\": 0,\r\n" +
                "		 \"ack_id\": 67812687\r\n" +
                "	}";

        Message msg = Parser.parseMessage(message);

        assertNotNull(msg);
        assertEquals(msg.command, Command.SETUP);
    }
	@Test
	public void testDeploy() throws ParserException {
		/*
		{
		    "command": "deploy",
		    "payload": [[12, 5], [13, 10]],
		    "ack_id": 12382,
			"player_id": 0,
			"signature": "TBD"
		}
		*/
		String message = "	{\r\n" +
				"	    \"command\": \"deploy\",\r\n" +
				"	    \"payload\": [[12, 5], [13, 10]],\r\n" +
				"	    \"ack_id\": 12382,\r\n" +
				"		\"player_id\": 0,\r\n" +
				"	}";

		Message msg = Parser.parseMessage(message);

		assertNotNull(msg);
		assertEquals(msg.command, Command.DEPLOY);
	}
	@Test
	public void testAttack() throws ParserException {
		/*
		{
		    "command": "attack",
		    "payload": [12, 22, 3],
		    "ack_id": 23342334,
			"signature": "TBD",
		    "player_id" : 2
		}
		*/
		String message = "{\r\n" +
				"    \"command\": \"attack\",\r\n" +
				"    \"payload\": [12, 22, 3],\r\n" +
				"    \"ack_id\": 23342334,\r\n" +
				"    \"player_id\": 2\r\n" +
				"}";

		Message msg = Parser.parseMessage(message);

		assertNotNull(msg);
		assertEquals(msg.command, Command.ATTACK);
	}
    @Test
    public void testDefend() throws ParserException {
		/*
		{
		    "command": "defend",
		    "payload": 2,
		    "ack_id": 9879876,
			"signature": "TBD",
		    "player_id" : 2
		}
		*/
        String message = "{\r\n" +
                "    \"command\": \"defend\",\r\n" +
                "    \"payload\": 2,\r\n" +
                "    \"ack_id\": 9879876,\r\n" +
                "    \"player_id\" : 2\r\n" +
                "}";

        Message msg = Parser.parseMessage(message);

        assertNotNull(msg);
        assertEquals(msg.command, Command.DEFEND);
    }
    @Test
    public void testAttackCapture() throws ParserException {
		/*
		{
		    "command": "attack_capture",
		    "payload": 5,
		    "ack_id": 9879876,
			"signature": "TBD",
		    "player_id" : 2
		}
		*/
        String message = "{\r\n" +
                "    \"command\": \"attack_capture\",\r\n" +
                "    \"payload\": [1, 2, 2],\r\n" +
                "    \"ack_id\": 9879876,\r\n" +
                "    \"player_id\" : 2\r\n" +
                "}";

        Message msg = Parser.parseMessage(message);

        assertNotNull(msg);
        assertEquals(msg.command, Command.ATTACK_CAPTURE);
    }
	@Test
	public void testFortify() throws ParserException {
		/*
		{
		    "command": "fortify",
		    "payload": [19, 18, 4],
		    "ack_id": 4556554,
				"			 \"signature\": \"TBD\"\r\n" +
		    "player_id": 2
		}

		*/
		String message = "{\r\n" +
				"    \"command\": \"fortify\",\r\n" +
				"    \"payload\": [19, 18, 4],\r\n" +
				"    \"ack_id\": 4556554,\r\n" +
				"    \"player_id\": 2\r\n" +
				"}\r\n" +
				"";

		Message msg = Parser.parseMessage(message);

		assertNotNull(msg);
		assertEquals(msg.command, Command.FORTIFY);
	}
	// TODO Non player host tests...

	@Test
	public void testRollHash() throws ParserException {

		String message = "{\n" +
				"    \"command\": \"roll_hash\",\n" +
				"    \"payload\": \"7b3d979ca8330a94fa7e9e1b466d8b99e0bcdea1ec90596c0dcc8d7ef6b4300c\",\n" +
				"    \"player_id\": 0\n" +
				"}";

		Message msg = Parser.parseMessage(message);

		assertNotNull(msg);
		assertEquals(msg.command, Command.DICE_HASH);
	}
	@Test
	public void testRollNumber() throws ParserException {

		String message = "{\n" +
				"    \"command\": \"roll_number\",\n" +
				"    \"payload\": \"9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08\",\n" +
				"    \"player_id\": 0\n" +
				"}";

		Message msg = Parser.parseMessage(message);

		assertNotNull(msg);
		assertEquals(msg.command, Command.DICE_ROLL_NUM);
	}
	@Test
	public void testInitialiseGame() throws ParserException {

		String message = "{\n" +
				"\"command\": \"initialise_game\",\n" +
				"\"payload\": {\n" +
				"\"version\": 1,\n" +
				"\"supported_features\": [\"custom_map\"]\n" +
				"}\n" +
				"}";

		Message msg = Parser.parseMessage(message);

		assertNotNull(msg);
		assertEquals(msg.command, Command.INITIALISE_GAME);
	}
	@Test
	public void testPlayersJoined() throws ParserException {

		String message = "{\n" +
				"\"command\": \"players_joined\",\n" +
				"\"payload\": [\n" +
				"[0, \"Player A\"],\n" +
				"[1, \"Player B\"]\n" +
				"]\n" +
				"\t}";

		Message msg = Parser.parseMessage(message);

		assertNotNull(msg);
		assertEquals(msg.command, Command.PLAYERS_JOINED);
	}
	@Test
	public void testLeaveGame() throws ParserException {

		String message = "{\n" +
				"\"command\": \"leave_game\",\n" +
				"\"payload\": {\n" +
				"\"response\": 401,\n" +
				"\"message\": \"Defeated by player 2\",\n" +
				"\"receive_updates\": true\n" +
				"},\n" +
				"\"player_id\": 0\n" +
				"}";

		Message msg = Parser.parseMessage(message);

		assertNotNull(msg);
		assertEquals(msg.command, Command.LEAVE_GAME);
	}
	@Test
	public void testTimeout() throws ParserException {

		String message = "{\n" +
				"\"command\": \"timeout\",\n" +
				"\"payload\": 2,\n" +
				"\"player_id\": 0,\n" +
				"\"ack_id\": 1\n" +
				"}";

		Message msg = Parser.parseMessage(message);

		assertNotNull(msg);
		assertEquals(msg.command, Command.TIMEOUT);
	}

	@Test(expected=ParserException.class)
	public void testNoCommand() throws ParserException {
		/*
		{
		    "command": "fortify",
		    "ack_id": 4556554,
			"signature": "TBD",
		    "player_id": 2
		}

		*/
		String message = "{\r\n" +
				"    \"ack_id\": 4556554,\r\n" +
				"    \"player_id\": 2\r\n" +
				"    \"payload\": [19, 18, 4],\r\n" +
				"	 \"signature\": \"TBD\"\r\n" +
				"}\r\n" +
				"";

		Parser.parseMessage(message);
	}
	@Test(expected=ParserException.class)
	public void testInvalidCommand() throws ParserException {
		String message = "{\r\n" +
				"    \"command\": \"Thisisinvalid\"," +
				"    \"ack_id\": 4556554,\r\n" +
				"    \"player_id\": 2\r\n" +
				"    \"payload\": [19, 18, 4],\r\n" +
				"	 \"signature\": \"TBD\"\r\n" +
				"}\r\n" +
				"";

		Parser.parseMessage(message);
	}

	@Test(expected=ParserException.class)
	public void testInvalidJSON() throws ParserException {
		String message = "{\r\n" +
				"    \"command\": \"Thisisinvalid\"," +
				"    \"ack_id\": 4556554,\r\n" +
				"    \"player\r\n" +
				"";

		Parser.parseMessage(message);
	}

	@Test(expected=ParserException.class)
	public void testNotYetSupportedCommand() throws ParserException {
		String message = "{\r\n" +
				"    \"command\": \"unsupportedcommand\"," +
				"    \"ack_id\": 4556554,\r\n" +
				"    \"player_id\": 2\r\n" +
				"    \"payload\": [19, 18, 4],\r\n" +
				"}\r\n" +
				"";

		Parser.parseMessage(message);
	}

	@Test(expected=ParserException.class)
	public void testJsonMustBeObject() throws ParserException {
		String message = "[]";

		Parser.parseMessage(message);
	}

	@Test
	public void test100PercentCoverage() throws ParserException {
		new Parser();
	}

}
