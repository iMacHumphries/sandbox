package server;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import entities.LocalPlayer;
import entities.NetworkPlayer;
import guis.ChatBox;
import toolbox.Debug;

public class MultiplayerManager {
	
	private Server server;
	private Client client;
	private ArrayList<String> messagesToAdd = new ArrayList<String>();
	
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
		client = new Client("192.168.0.4", player);
		client.start();
	}
	
	public void hostServer(){
		server = new Server();
		server.start();
	}
	
	public void login() {
		Packet_01_Login loginPack = new Packet_01_Login(client.getCurrentPlayer().getUsername());
		loginPack.writeData(client);
		sendMessage("Logged in :D");
	}
	
	public void sendPosition(String username, Vector3f position, Vector3f rotation) {
		Debug.log("sending pos");
		Packet_02_Move movePacket = new Packet_02_Move(username, (int)position.x, (int)position.y, (int)position.z, (int)rotation.x, (int)rotation.y,(int)rotation.z);
		movePacket.writeData(client);
	}

	public void sendMessage(String message){
		Debug.log("sending message " +message);
		Packet_03_Message mp = new Packet_03_Message(LocalPlayer.sharedLocalPlayer.getUsername(), message);
		mp.writeData(client);
	}
	
	public void recieveMessagePacket(Packet_03_Message mpack){
		messagesToAdd.add(mpack.getUsername() + ">" + mpack.getMessage());
	}
	
	public void update() {
		ArrayList<String> mToRemove = new ArrayList<String>();
		for (String message : messagesToAdd) {
			ChatBox.getInstance().addMessage(message);
			mToRemove.add(message);
		}
		for (String message : mToRemove){
			messagesToAdd.remove(message);
		}
		
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
