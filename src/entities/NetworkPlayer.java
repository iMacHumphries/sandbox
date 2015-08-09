package entities;

import java.net.InetAddress;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

import collision.SweepAndPrune;
import server.MultiplayerManager;
import terrains.Terrain;
import toolbox.Debug;
import water.WaterTile;

public class NetworkPlayer extends Player{

	private InetAddress ipAddress;
	private int port;
	String username;
	
	public NetworkPlayer(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, InetAddress ipAddress, int port, String username) {
		super(model, position, rotX, rotY, rotZ, scale);
		Debug.log("creating new player");
		this.ipAddress = ipAddress;
		this.port = port;
		this.username = username;

	}
	
	@Override
	public void move(Terrain terrain, WaterTile water) {
		super.move(terrain,water);
		
	}
	
	@Override
	public void increasePosition(float dx, float dy, float dz) {
		super.increasePosition(dx, dy, dz);
		if ((dx != 0 || dy != 0 || dz != 0)) {
			MultiplayerManager.getInstance().sendPosition(getUsername(), getPosition(), getRotation());
		}
	}
	
	@Override
	public void increaseRotation(float dx, float dy, float dz) {
		super.increaseRotation(dx, dy, dz);
		if ((dx != 0 || dy != 0 || dz != 0)) {
			MultiplayerManager.getInstance().sendPosition(getUsername(), getPosition(), getRotation());
		}
	}

	/**
	 * @return the ipAddress
	 */
	public InetAddress getIpAddress() {
		return ipAddress;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(InetAddress ipAddress) {
		this.ipAddress = ipAddress;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	
	
	
}
