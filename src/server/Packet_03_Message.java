package server;

public class Packet_03_Message extends Packet{

	private String username;
	private String message;
	private static final int PACKET_ID = 03;
	
	public Packet_03_Message(String _username, String _message) {
		super(PACKET_ID);
		this.username = _username;
		this.message = _message;
	}
	
	public Packet_03_Message(byte[] data) {
		super(PACKET_ID);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.message = dataArray[1];
	}

	@Override
	public void writeData(Client client) {
		client.sendData(getData());
	}

	@Override
	public void writeData(Server server) {
		server.sendDataToAllClients(getData());
	}

	@Override
	public byte[] getData() {
		return ("03" + this.username + "," + this.message).getBytes(); 
	}
	
	public String getUsername() { return this.username; }
	public String getMessage() { return this.message; }
}
