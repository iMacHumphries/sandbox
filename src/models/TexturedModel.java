package models;

import objConverter.OBJFileLoader;
import renderEngine.Loader;
import textures.ModelTexture;

public class TexturedModel {

	private RawModel rawModel;
	private ModelTexture texture;
	
	public TexturedModel(RawModel model, ModelTexture texture) {
		this.rawModel = model;
		this.texture = texture;
	}

	public TexturedModel(String modelName, String textureName) {
		this(Loader.getInstance().loadToVAO(OBJFileLoader.loadOBJ(modelName)), new ModelTexture(Loader.getInstance().loadTexture(textureName)));
	}
	
	/**
	 * @return the rawModel
	 */
	public RawModel getRawModel() {
		return rawModel;
	}

	/**
	 * @return the texture
	 */
	public ModelTexture getTexture() {
		return texture;
	}
	
}
