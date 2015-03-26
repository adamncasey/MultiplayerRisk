package lobby;

import networking.NetworkClient;
import networking.NetworkPlayer;
import player.IPlayer;

import java.util.List;

public class LobbyUtil {

    public static void createIPlayersInOrder(List<NetworkClient> clients, int firstID, int ourPlayerID, List<IPlayer> playersBefore, List<IPlayer> playersAfter) {

        boolean after = false;
        int lastID = firstID;
        int delegatedPlayerID = ourPlayerID;

        for(NetworkClient client : clients) {
            // if we've skipped over our playerid, then after is now true.
            if(lastID <= ourPlayerID && client.playerid > ourPlayerID) {
                after = true;
            }

            NetworkPlayer player = new NetworkPlayer(client, delegatedPlayerID);
            delegatedPlayerID = -1;

            // If we've passed our playerid, add to playersAfter
            if(!after) {
                playersBefore.add(player);
            }
            else {
                playersAfter.add(player);
            }
        }
    }
}
