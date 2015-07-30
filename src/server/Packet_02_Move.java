package server;

import org.lwjgl.util.vector.Vector3f;

public class Packet_02_Move extends Packet{
	
	private String username;
	private int x,y,z;
	private int rotX,rotY,rotZ;
	
	public Packet_02_Move(byte[] data) {
		super(02);
		String[] dataArray = readData(data).split(",");
		this.username = dataArray[0];
		this.x = Integer.parseInt(dataArray[1]);
		this.y = Integer.parseInt(dataArray[2]);
		this.z = Integer.parseInt(dataArray[3]);
		this.rotX = Integer.parseInt(dataArray[4]);
		this.rotY = Integer.parseInt(dataArray[5]);
		this.rotZ = Integer.parseInt(dataArray[6]);
	}
	
	public Packet_02_Move(String _username, int _x, int _y, int _z, int _rotX, int _rotY, int _rotZ) {
		super(02);
		this.username = _username;
		this.x =_x;
		this.y = _y;
		this.z = _z;
		this.rotX = _rotX;
		this.rotY = _rotY;
		this.rotZ = _rotZ;
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
		//02name,x,y,z,rotx,roty,rotz
		return ("02" + username + "," + this.x + "," + this.y + "," + this.z + "," + this.rotX + "," + this.rotY + "," + this.rotZ).getBytes(); 
	}
	
	public Vector3f getPosition() {
		return new Vector3f(x,y,z);
	}
	
	public Vector3f getRotation() {
		return new Vector3f(rotX,rotY,rotZ);
	}
	
	public String getUsername()
	{
		return this.username;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
}
