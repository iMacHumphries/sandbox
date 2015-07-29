package server;

public class Packet_00_Disconnect extends Packet{
	
	private String username;
	
	public Packet_00_Disconnect(byte[] data) {
		super(00);
		this.username = readData(data);
	}
	
	public Packet_00_Disconnect(String _username) {
		super(00);
		this.username = _username;
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
		return ("00" +username).getBytes(); 
	}
	
	public String getUsername()
	{
		return this.username;
	}

}
