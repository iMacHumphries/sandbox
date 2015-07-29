package server;

public class Packet_01_Login extends Packet{

	private String username;
	
	public Packet_01_Login(String _username) {
		super(01);
		this.username = _username;
	}
	
	public Packet_01_Login(byte[] data) {
		super(01);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
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
		return ("01" + this.username).getBytes(); 
	}
	
	public String getUsername()
	{
		return this.username;
	}
}
