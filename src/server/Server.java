package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

import engineTester.MainGameLoop;
import entities.EntityManager;
import entities.NetworkPlayer;
import server.Packet.PacketType;
import toolbox.Debug;

public class Server extends Thread{

	private DatagramSocket socket;
	private List<ServerPlayer> connectedPlayers = new LinkedList<ServerPlayer>();
	private static final int PORT_NUMBER = 25565;
	private InetAddress currentClientIP;
	
	public Server() {
		try {
			this.socket = new DatagramSocket(PORT_NUMBER);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run()
	{
		Debug.log("Starting server...");
		while(true)
		{
			byte[] data = new byte[1024];
			DatagramPacket packet = new DatagramPacket(data, data.length);
			try {
				socket.receive(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
			parsePacket(packet);
		}	
	}
	
	private void parsePacket(DatagramPacket packet)
	{
		System.out.println("server got a packet from a client " + packet);
		byte[] data = packet.getData();
		InetAddress ip = packet.getAddress();
		currentClientIP = ip;
		int port = packet.getPort();
		String message = new String(packet.getData());
		String ID = message.substring(0, 2);
		
		PacketType type = Packet.lookUpPacket(ID);
		switch (type)
		{
			case INVALID:
				break;
			case MOVE:
				Packet_02_Move movePack = new Packet_02_Move(data);
				this.handleMoveFrom(movePack);
				break;
			case DISCONNECT:
				Packet_00_Disconnect discp = new Packet_00_Disconnect(data);
				System.out.println("["+ip.getHostAddress()+":"+ port+"] " + discp.getUsername() +" has disconnected...");
				//NetworkPlayer player = new NetworkPlayer(ip, port,  p.getUsername());
				this.removeConnection(discp);
				break;
			case LOGIN:
				Packet_01_Login p = new Packet_01_Login(data);
				System.out.println("["+ip.getHostAddress()+":"+ port+"] " + p.getUsername() +" connected...");
				ServerPlayer player = new ServerPlayer(ip, port, p.getUsername());
				this.addConnection(player, p);
				break;
		}
	}
	
	private void handleMoveFrom(Packet_02_Move movePack) {
		if (this.getPlayer(movePack.getUsername()) != null)
		{
			movePack.writeData(this);
		}
	}

	
//	private int getPlayerIndex(String _username)
//	{
//		int index = 0;
//		for (ServerPlayer player : this.connectedPlayers)
//		{
//			if (player.getUsername().equals(_username))
//			{
//				return index;
//			}
//			index++;
//		}
//		return -1;
//	}
	
	private void removeConnection(Packet_00_Disconnect packet) {
		//get player to remove
		ServerPlayer player = getPlayer(packet.getUsername());
		//remove from list
		this.connectedPlayers.remove(player);
		//Send remove to all clients
		packet.writeData(this);
	}
	
	public ServerPlayer getPlayer(String _username)
	{
		ServerPlayer result = null;
		for (ServerPlayer p : this.connectedPlayers)
		{
			if (p.getUsername().equals(_username))
			{
				result = p;
			}
		}
		return result;
	}

	public void addConnection(ServerPlayer _player, Packet_01_Login _packet)
	{
		boolean alreadyConnected = false;
		
		// Loop through connected players
		for (ServerPlayer p : this.connectedPlayers)
		{
			// if the player is already in the connected players
			if (_player.getUsername().equals(p.getUsername()))
			{
				if (p.getIpAddress() == null)
				{
					p.setIpAddress(_player.getIpAddress());
				}
				if (p.getPort() == -1)
				{
					p.setPort(_player.getPort());
				}
				alreadyConnected = true;
			}
			else {
				// Send the new player packet to every connected player
				sendData(_packet.getData(), p.getIpAddress(), p.getPort());
				// Log this player into every other player
				Packet_01_Login packetLogin = new Packet_01_Login(p.getUsername());
				sendData(packetLogin.getData(), _player.getIpAddress(), _player.getPort());
			}
		}
		
		if (!alreadyConnected)
		{
			this.connectedPlayers.add(_player);
		}
	}
	
	public void sendData(byte[] data, InetAddress ip, int port)
	{
		DatagramPacket packet = new DatagramPacket(data, data.length, ip, port);
		try {
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDataToAllClients(byte[] data) {
		for (ServerPlayer player : this.connectedPlayers)
			sendData(data, player.getIpAddress(), player.getPort());
	}

	public void sendDataToAllClientsExcludeLocal(byte[] data) {
		for (ServerPlayer player : this.connectedPlayers) {
			if (!currentClientIP.equals(player.getIpAddress()))
				sendData(data, player.getIpAddress(), player.getPort());
		}
	}
	
	/**
	 * @return the connectedPlayers
	 */
	public List<ServerPlayer> getConnectedPlayers() {
		return connectedPlayers;
	}
}