package test.lobby;
import static org.junit.Assert.*;

import lobby.LobbyUtil;
import networking.GameRouter;
import networking.NetworkClient;
import networking.networkplayer.NetworkPlayer;
import org.junit.Test;
import player.IPlayer;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;


/**
 * Created by Adam on 16/04/2015.
 */
public class LobbyUtilTests {

    @Test
    public void playerOrderTests() {
        GameRouter router = new GameRouter();

        NetworkClient client2 = new NetworkClient(router, 1, true);
        NetworkClient client3 = new NetworkClient(router, 2, true);

        LinkedList<IPlayer> before = new LinkedList<>(),
                            after = new LinkedList<>();
        int firstID = 0;

        LobbyUtil.createIPlayersInOrder(Arrays.asList(client2, client3), firstID, 0, before, after);

        checkList(before, new LinkedList<Integer>());
        checkList(after, Arrays.asList(1, 2));
    }

    @Test
    public void playerOrderTests2() {
        GameRouter router = new GameRouter();

        NetworkClient client2 = new NetworkClient(router, 1, true);
        NetworkClient client3 = new NetworkClient(router, 2, true);

        LinkedList<IPlayer> before = new LinkedList<>(),
                after = new LinkedList<>();

        int firstID = 1;

        LobbyUtil.createIPlayersInOrder(Arrays.asList(client2, client3), firstID, 0, before, after);

        checkList(before, Arrays.asList(1, 2));
        checkList(after, new LinkedList<Integer>());
    }

    @Test
    public void playerOrderTests3() {
        GameRouter router = new GameRouter();

        NetworkClient client2 = new NetworkClient(router, 1, true);
        NetworkClient client3 = new NetworkClient(router, 2, true);

        LinkedList<IPlayer> before = new LinkedList<>(),
                after = new LinkedList<>();

        int firstID = 2;

        LobbyUtil.createIPlayersInOrder(Arrays.asList(client2, client3), firstID, 0, before, after);

        checkList(before, Arrays.asList(2));
        checkList(after, Arrays.asList(1));
    }

    @Test
    public void playerOrderTests4() {
        GameRouter router = new GameRouter();

        NetworkClient client2 = new NetworkClient(router, 0, true);
        NetworkClient client3 = new NetworkClient(router, 2, true);

        LinkedList<IPlayer> before = new LinkedList<>(),
                after = new LinkedList<>();

        int firstID = 0;
        int ourPlayerID = 1;

        LobbyUtil.createIPlayersInOrder(Arrays.asList(client2, client3), firstID, ourPlayerID, before, after);

        checkList(before, Arrays.asList(0));
        checkList(after, Arrays.asList(2));
    }
    @Test
    public void playerOrderTests5() {
        GameRouter router = new GameRouter();

        NetworkClient client2 = new NetworkClient(router, 0, true);
        NetworkClient client3 = new NetworkClient(router, 2, true);

        LinkedList<IPlayer> before = new LinkedList<>(),
                after = new LinkedList<>();

        int firstID = 1;
        int ourPlayerID = 1;

        LobbyUtil.createIPlayersInOrder(Arrays.asList(client2, client3), firstID, ourPlayerID, before, after);

        checkList(before, new LinkedList<>());
        checkList(after, Arrays.asList(2, 0));
    }
    @Test
    public void playerOrderTests6() {
        GameRouter router = new GameRouter();

        NetworkClient client2 = new NetworkClient(router, 0, true);
        NetworkClient client3 = new NetworkClient(router, 2, true);

        LinkedList<IPlayer> before = new LinkedList<>(),
                after = new LinkedList<>();

        int firstID = 2;
        int ourPlayerID = 1;

        LobbyUtil.createIPlayersInOrder(Arrays.asList(client2, client3), firstID, ourPlayerID, before, after);

        checkList(before, Arrays.asList(2, 0));
        checkList(after, new LinkedList<>());
    }

    @Test
    public void playerOrderTests7() {
        GameRouter router = new GameRouter();

        NetworkClient client2 = new NetworkClient(router, 0, true);
        NetworkClient client3 = new NetworkClient(router, 1, true);

        LinkedList<IPlayer> before = new LinkedList<>(),
                after = new LinkedList<>();

        int firstID = 0;
        int ourPlayerID = 2;

        LobbyUtil.createIPlayersInOrder(Arrays.asList(client2, client3), firstID, ourPlayerID, before, after);

        checkList(before, Arrays.asList(0, 1));
        checkList(after, new LinkedList<>());
    }
    @Test
    public void playerOrderTests8() {
        GameRouter router = new GameRouter();

        NetworkClient client2 = new NetworkClient(router, 0, true);
        NetworkClient client3 = new NetworkClient(router, 1, true);

        LinkedList<IPlayer> before = new LinkedList<>(),
                after = new LinkedList<>();

        int firstID = 1;
        int ourPlayerID = 2;

        LobbyUtil.createIPlayersInOrder(Arrays.asList(client2, client3), firstID, ourPlayerID, before, after);

        checkList(before, Arrays.asList(1));
        checkList(after, Arrays.asList(0));
    }
    @Test
    public void playerOrderTests9() {
        GameRouter router = new GameRouter();

        NetworkClient client2 = new NetworkClient(router, 0, true);
        NetworkClient client3 = new NetworkClient(router, 1, true);

        LinkedList<IPlayer> before = new LinkedList<>(),
                after = new LinkedList<>();

        int firstID = 2;
        int ourPlayerID = 2;

        LobbyUtil.createIPlayersInOrder(Arrays.asList(client2, client3), firstID, ourPlayerID, before, after);

        checkList(before, new LinkedList<>());
        checkList(after, Arrays.asList(0, 1));
    }

    private void checkList(List<IPlayer> list, List<Integer> expected) {
        int i=0;
        assertEquals(list.size(), expected.size());

        for(IPlayer player : list) {
            assertTrue(player instanceof NetworkPlayer);

            NetworkPlayer netplayer = (NetworkPlayer)player;

            assertEquals((int) expected.get(i++), netplayer.getPlayerID());
        }
    }
}
