package textures;

public class TerrainTexturePack {

	private TerrainTexture backgroundTexture;
	private TerrainTexture rTexture;
	private TerrainTexture gTexture;
	private TerrainTexture bTexture;
	
	public TerrainTexturePack(TerrainTexture backgroundTexture,
			TerrainTexture rTexture, TerrainTexture gTexture,
			TerrainTexture bTexture) {
		this.backgroundTexture = backgroundTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}

	/**
	 * @return the backgroundTexture
	 */
	public TerrainTexture getBackgroundTexture() {
		return backgroundTexture;
	}

	/**
	 * @return the rTexture
	 */
	public TerrainTexture getrTexture() {
		return rTexture;
	}

	/**
	 * @return the gTexture
	 */
	public TerrainTexture getgTexture() {
		return gTexture;
	}

	/**
	 * @return the bTexture
	 */
	public TerrainTexture getbTexture() {
		return bTexture;
	}
	
	
}
