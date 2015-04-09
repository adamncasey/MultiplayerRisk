package lobby;

import java.util.ArrayList;

import lobby.handler.HostLobbyEventHandler;
import networking.LobbyServer;
import networking.PortInUseException;
import settings.Settings;

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
	public LocalGameLobby createGameLobby(String friendlyName, int noOfPlayers, HostLobbyEventHandler handler)
			throws PortInUseException {
		
		// TODO is friendly name the right string to pass in as playername?
		LocalGameLobby lobby = new LocalGameLobby(handler, Settings.port, friendlyName);
		lobby.start();
		
		return lobby;
	}

}
