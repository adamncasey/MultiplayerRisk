package lobby;

import networking.PortInUseException;

public class Lobby implements ILobby {

	@Override
	public RemoteGameLobby[] searchForLobbies() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean joinGameLobby(RemoteGameLobby lobby) {
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
