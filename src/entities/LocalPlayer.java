package entities;

import java.net.InetAddress;

import models.TexturedModel;

import org.lwjgl.util.vector.Vector3f;

public class LocalPlayer extends NetworkPlayer{

	public static LocalPlayer sharedLocalPlayer = null;
	
	public LocalPlayer(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, InetAddress ipAddress,
			int port, String username) {
		super(model, position, rotX, rotY, rotZ, scale, ipAddress, port, username);
		sharedLocalPlayer = this;
	}

}
