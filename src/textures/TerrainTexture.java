package textures;

import renderEngine.Loader;

public class TerrainTexture {

	private int textureID;

	public TerrainTexture(int textureID) {
		super();
		this.textureID = textureID;
	}
	
	public TerrainTexture(String textureName) {
		this(Loader.getInstance().loadTexture(textureName));
	}

	/**
	 * @return the textureID
	 */
	public int getTextureID() {
		return textureID;
	}
	
	
}
