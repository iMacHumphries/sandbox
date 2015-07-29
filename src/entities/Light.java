package entities;

import org.lwjgl.util.vector.Vector3f;

public class Light {

	private Vector3f position;
	private Vector3f color;
	private Vector3f attenuation = new Vector3f(1,0,0);
	
	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}
	
	public Light(Vector3f position, Vector3f color, Vector3f attenuation) {
		this(position, color);
		this.attenuation = attenuation;
	}

	public Vector3f getAttenuation() {
		return attenuation;
	}
	
	/**
	 * @return the position
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	/**
	 * @return the color
	 */
	public Vector3f getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	
}
