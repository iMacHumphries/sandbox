package guis;

import org.lwjgl.util.vector.Vector2f;

import renderEngine.Loader;

public class GuiTexture {

	private int texture;
	private Vector2f position;
	private Vector2f scale;
	public GuiTexture(int texture, Vector2f position, Vector2f scale) {
		super();
		this.texture = texture;
		this.position = position;
		this.scale = scale;
	}
	
	public GuiTexture(String fileName,Vector2f position, Vector2f scale) {
		this(Loader.getInstance().loadTexture(fileName), position, scale);
	}
	
	/**
	 * @return the texture
	 */
	public int getTexture() {
		return texture;
	}
	/**
	 * @return the position
	 */
	public Vector2f getPosition() {
		return position;
	}
	/**
	 * @return the scale
	 */
	public Vector2f getScale() {
		return scale;
	}
	
	
}
