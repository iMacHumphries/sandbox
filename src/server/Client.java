package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import engineTester.MainGameLoop;
import entities.EntityManager;
import entities.NetworkPlayer;
import guis.ChatBox;
import server.Packet.PacketType;
import toolbox.Debug;

public class Client extends Thread {

		private InetAddress hostIPAddress;
		private DatagramSocket socket;
		private NetworkPlayer currentPlayer;
		
		private static final int PORT_NUMBER = 25565;
		
		public Client(String _ipAddress, NetworkPlayer currentPlayer) {
			this.currentPlayer = currentPlayer;
			try {
				this.hostIPAddress = InetAddress.getByName(_ipAddress);
				this.socket = new DatagramSocket();
			} catch (UnknownHostException e){
				e.printStackTrace();
			} catch (SocketException e) {
				e.printStackTrace();
			}
			
		}

		public void run()
		{
			Debug.log("Starting client...");
			while (true)
			{
				byte[] data = new byte[1024];
				DatagramPacket packet = new DatagramPacket(data, data.length);
				try {
					socket.receive(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.parsePacket(packet.getData(), packet.getAddress(), packet.getPort());
			}
		}
		
		private void parsePacket(byte[] _data, InetAddress _ip, int _port)
		{
			String messageFromServer = new String(_data).trim();
			PacketType type = Packet.lookUpPacket(messageFromServer.substring(0, 2));
			switch(type)
			{
				case MOVE:
					Packet_02_Move packetMove = new Packet_02_Move(_data);
					this.handleMove(packetMove);
					break;
				case LOGIN:
					Packet_01_Login packet = new Packet_01_Login(_data);
					System.out.println("Got a login message from the server saying " + packet.getUsername() + " is suppose to join the game.");
					EntityManager.getInstance().addNewNetworkedPlayer(_ip, _port, packet.getUsername());
						
					break;
				case DISCONNECT:
					Packet_00_Disconnect packetDisc = new Packet_00_Disconnect(_data);
					System.out.println(packetDisc.getUsername() + " has left the game...");
					break;
					
				case MESSAGE:
					Packet_03_Message mPack = new Packet_03_Message(_data);
					Debug.log("got a message from " + mPack.getUsername());
					MultiplayerManager.getInstance().recieveMessagePacket(mPack);
					break;
				default:
				case INVALID:
					break;
			}
		}
		
		private void handleMove(Packet_02_Move packetMove) {
			EntityManager.getInstance().movePlayer(packetMove.getUsername(), packetMove.getPosition(), packetMove.getRotation());
		}

		public void sendData(byte[] data)
		{
			DatagramPacket packet = new DatagramPacket(data, data.length, hostIPAddress, PORT_NUMBER);
			try {
				socket.send(packet);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/**
		 * @return the currentPlayer
		 */
		public NetworkPlayer getCurrentPlayer() {
			return currentPlayer;
		}		
}
