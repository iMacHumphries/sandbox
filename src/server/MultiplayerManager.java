package server;

import org.lwjgl.util.vector.Vector3f;

import entities.NetworkPlayer;
import toolbox.Debug;

public class MultiplayerManager {
	
	private Server server;
	private Client client;
	
	private static MultiplayerManager sharedManager;
	public static MultiplayerManager getInstance() {
		if (sharedManager == null) {
			sharedManager = new MultiplayerManager();
		}
		return sharedManager;
	}
	
	public MultiplayerManager() {

	}
	
	public void startClient(NetworkPlayer player) {
		client = new Client("192.168.0.3", player);
		client.start();
	}
	
	public void hostServer(){
		server = new Server();
		server.start();
	}
	
	public void login() {
		Packet_01_Login loginPack = new Packet_01_Login(client.getCurrentPlayer().getUsername());
		loginPack.writeData(client);
	}
	
	public void sendPosition(String username, Vector3f position, Vector3f rotation) {
		Debug.log("sending pos");
		Packet_02_Move movePacket = new Packet_02_Move(username, (int)position.x, (int)position.y, (int)position.z, (int)rotation.x, (int)rotation.y,(int)rotation.z);
		movePacket.writeData(client);
	}

	/**
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * @param server the server to set
	 */
	public void setServer(Server server) {
		this.server = server;
	}

	/**
	 * @return the client
	 */
	public Client getClient() {
		return client;
	}

	/**
	 * @param client the client to set
	 */
	public void setClient(Client client) {
		this.client = client;
	}	
}
