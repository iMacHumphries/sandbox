package textures;

public class ModelTexture {
	
	private int textureID;
	
	private float shineDamper = 1;
	private float reflectivity = 0;
	
	private boolean hasTransparency;
	private boolean useFakeLighting;
	
	private int numberOfRows = 1;
	
	public ModelTexture(int id) {
		this.textureID = id;
		hasTransparency= false;
		useFakeLighting = false;
	}
	
	/**
	 * @return the numberOfRows
	 */
	public int getNumberOfRows() {
		return numberOfRows;
	}

	/**
	 * @param numberOfRows the numberOfRows to set
	 */
	public void setNumberOfRows(int numberOfRows) {
		this.numberOfRows = numberOfRows;
	}

	public int getID() {
		return this.textureID;
	}

	/**
	 * @return the shineDamper
	 */
	public float getShineDamper() {
		return shineDamper;
	}

	/**
	 * @param shineDamper the shineDamper to set
	 */
	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	/**
	 * @return the reflectivity
	 */
	public float getReflectivity() {
		return reflectivity;
	}

	/**
	 * @param reflectivity the reflectivity to set
	 */
	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	/**
	 * @return the hasTransparency
	 */
	public boolean isHasTransparency() {
		return hasTransparency;
	}

	/**
	 * @param hasTransparency the hasTransparency to set
	 */
	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

	/**
	 * @return the useFakeLighting
	 */
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	/**
	 * @param useFakeLighting the useFakeLighting to set
	 */
	public void setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
	}
	
}
