package test.networking;

import static org.junit.Assert.*;
import networking.parser.Parser;
import networking.parser.ParserException;

import org.junit.Test;

public class MessageParserTests {

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
				"			 \"command\": \"acknowledge\",\r\n" + 
				"			 \"payload\":	{\r\n" + 
				"			 				\"ack_id\": 32183921,\r\n" + 
				"			 				\"response\": 0,\r\n" + 
				"			 				\"data\": null\r\n" + 
				"			 			}\r\n" + 
				"			 \"player_id\": \"5\",\r\n" + 
				"			 \"signature\": \"TBD\"\r\n" + 
				"		}";
		
		assertNotNull(Parser.parseMessage(message));
	}
	
	@Test
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
				"		 \"payload\":	null,\r\n" + 
				"		 \"player_id\": \"0\",\r\n" + 
				"		 \"signature\": \"TBD\"\r\n" + 
				"		 \"ack_id\": 67812687\r\n" + 
				"	}";
		
		assertNotNull(Parser.parseMessage(message));
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
			 			
			 "player_id": "1",
			 "signature": "TBD"
		}
		*/
		String message = "		{\r\n" + 
				"		 \"command\": \"acknowledge\",\r\n" + 
				"		 \"payload\":	{\r\n" + 
				"		 				\"ack_id\": 67812687,\r\n" + 
				"		 				\"response\": 0,\r\n" + 
				"		 				\"data\": null\r\n" + 
				"		 			}\r\n" + 
				"		 			\r\n" + 
				"		 \"player_id\": \"1\",\r\n" + 
				"		 \"signature\": \"TBD\"\r\n" + 
				"	}";
		
		assertNotNull(Parser.parseMessage(message));
	}
	
	@Test
	public void testDeploy() throws ParserException {
		/*
		{
		    "command": "deploy",
		    "payload": [[12, 5], [13, 10]],
		    "ack_id": 12382,
			"player_id": "0",
			"signature": "TBD"
		}
		*/
		String message = "	{\r\n" + 
				"	    \"command\": \"deploy\",\r\n" + 
				"	    \"payload\": [[12, 5], [13, 10]],\r\n" + 
				"	    \"ack_id\": 12382,\r\n" + 
				"		\"player_id\": \"0\",\r\n" + 
				"		\"signature\": \"TBD\"\r\n" + 
				"	}";
		
		assertNotNull(Parser.parseMessage(message));
	}

}
