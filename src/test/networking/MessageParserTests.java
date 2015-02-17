package test.networking;

import static org.junit.Assert.*;
import networking.Command;
import networking.message.AcceptJoinGamePayload;
import networking.message.AcknowledgementPayload;
import networking.message.JoinGamePayload;
import networking.message.Message;
import networking.message.PingPayload;
import networking.message.RejectJoinGamePayload;
import networking.parser.Parser;
import networking.parser.ParserException;

import org.junit.Test;

public class MessageParserTests {

    // TODO: Reenable tests as parsing progresses
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
				"			 \"payload\":	{\r\n" + 
				"			 				\"ack_id\": 32183921,\r\n" + 
				"			 				\"response\": 0,\r\n" + 
				"			 				\"data\": null\r\n" + 
				"			 			}\r\n" + 
				"			 \"player_id\": 5,\r\n" + 
				"			 \"signature\": \"TBD\"\r\n" + 
				"		}";
		
		Message msg = Parser.parseMessage(message);
		
		assertNotNull(msg);
		assertEquals(msg.command, Command.ACKNOWLEDGEMENT);

        assertTrue(msg.payload instanceof AcknowledgementPayload);
	}
	
	@Test
	public void testAcknowledge2() throws ParserException {
		/*
		{
			 "command": "acknowledge",
			 "payload":	{
			 				"ack_id": 67812687,
			 				"response": 0,
			 				"data": null
			 			}
			 			
			 "player_id": 1,
			 "signature": "TBD"
		}
		*/
		String message = "		{" + 
				"		 \"command\": \"acknowledgement\"," + 
				"		 \"payload\":	{\r\n" + 
				"		 				\"ack_id\": 67812687," + 
				"		 				\"response\": 0," + 
				"		 				\"data\": null" + 
				"		 			}," + 
				"		 \"player_id\": 1," + 
				"		 \"signature\": \"TBD\"" + 
				"	}";
		
		Message msg = Parser.parseMessage(message);
		
		assertNotNull(msg);
		assertEquals(msg.command, Command.ACKNOWLEDGEMENT);
        assertTrue(msg.payload instanceof AcknowledgementPayload);
	}
	
	@Test
	public void testReady() throws ParserException {
		/*
		{
			 "command": "ready",
			 "payload":	null,			 			
			 "player_id": 1,
			 "ack_id": 541232121,
			 "signature": "TBD"
		}
		*/
		String message = "{" + 
				"			 \"command\": \"ready\"," + 
				"			 \"payload\":	null," + 
				"			 \"player_id\": 1,\n" + 
				"			 \"ack_id\": 541232121," + 
				"			 \"signature\": \"TBD\"" + 
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
				"			 \"signature\": \"TBD\"" + 
				"		}";
		
		Message msg = Parser.parseMessage(message);
		
		assertNotNull(msg);
		assertEquals(msg.command, Command.PING);
        assertTrue(msg.payload instanceof PingPayload);
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
				"			    }," + 
				"			 \"player_id\": 1," + 
				"			 \"signature\": \"TBD\"" + 
				"		}";
		
		Message msg = Parser.parseMessage(message);
		
		assertNotNull(msg);
		assertEquals(msg.command, Command.JOIN_GAME);
        assertTrue(msg.payload instanceof JoinGamePayload);
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
				"			 \"signature\": \"TBD\"" + 
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
				"			 \"signature\": \"TBD\"" + 
				"		}";
		
		Message msg = Parser.parseMessage(message);
		
		assertNotNull(msg);
		assertEquals(msg.command, Command.JOIN_REJECT);
        assertTrue(msg.payload instanceof RejectJoinGamePayload);
	}
	

    //@Test
    public void testTradeInCards() throws ParserException {
		/*
		{
			 "command": "trade_in_cards",
			 "payload":	null,
			 "player_id": "0",
			 "signature": "TBD"
			 "ack_id": 67812687
		}
		*/
        String message = "	{\r\n" +
                "		 \"command\": \"trade_in_cards\",\r\n" +
                "		 \"payload\": null,\r\n" +
                "		 \"player_id\": 0,\r\n" +
                "		 \"signature\": \"TBD\"\r\n" +
                "		 \"ack_id\": 67812687\r\n" +
                "	}";

        Message msg = Parser.parseMessage(message);

        assertNotNull(msg);
        assertEquals(msg.command, Command.PLAY_CARDS);
    }
	
	//@Test
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
				"		\"signature\": \"TBD\"\r\n" + 
				"	}";
		
		Message msg = Parser.parseMessage(message);
		
		assertNotNull(msg);
		assertEquals(msg.command, Command.DEPLOY);
	}	
	//@Test
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
				"	 \"signature\": \"TBD\"\r\n" + 
				"}";
		
		Message msg = Parser.parseMessage(message);
		
		assertNotNull(msg);
		assertEquals(msg.command, Command.ATTACK);
	}
	//@Test
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
				"    \"payload\": 5,\r\n" + 
				"    \"ack_id\": 9879876,\r\n" + 
				"    \"player_id\" : 2\r\n" + 
				"	 \"signature\": \"TBD\"\r\n" + 
				"}";
		
		Message msg = Parser.parseMessage(message);
		
		assertNotNull(msg);
		assertEquals(msg.command, Command.ATTACK_CAPTURE);
	}
	//@Test
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
				"	 \"signature\": \"TBD\"\r\n" + 
				"}\r\n" + 
				"";
		
		Message msg = Parser.parseMessage(message);
		
		assertNotNull(msg);
		assertEquals(msg.command, Command.FORTIFY);
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
				"    \"command\": \"roll_number\"," +
				"    \"ack_id\": 4556554,\r\n" +
				"    \"player_id\": 2\r\n" +
				"    \"payload\": [19, 18, 4],\r\n" +
				"	 \"signature\": \"TBD\"\r\n" +
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
