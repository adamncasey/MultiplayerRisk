package lobby;

import java.util.ArrayList;

import networking.LobbyServer;
import networking.PortInUseException;

public class Lobby implements ILobby {
	
	LobbyFinder f = new LobbyFinder();
	
	@Override
	public void startSearchingForLobbies() {
		f.start();
	}
	
	@Override
	public void stopSearchingForLobbies() {
		f.close();
	}
	
	@Override
	public ArrayList<LobbyServer> findLobbies() {
		return f.getLobbies();
	}

	@Override
	public boolean joinGameLobby(LobbyServer lobby) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LocalGameLobby createGameLobby(String friendlyName, int noOfPlayers)
			throws PortInUseException {
		
		LocalGameLobby lobby = new LocalGameLobby(friendlyName);
		lobby.start();
		
		return lobby;
	}

}
