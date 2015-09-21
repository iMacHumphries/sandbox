package renderEngine;

import java.util.List;
import java.util.Map;

import models.LevelOfDetail;
import models.RawModel;
import models.TexturedModel;

import org.lwjgl.opengl.APPLEVertexArrayObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Debug;
import toolbox.Maths;
import entities.Entity;

/**
 * Renderer.java - render game
 * 
 * @author Ben
 * @version 14-JUL-2015
 */
public class EntityRenderer {

	// Looking for projection math? its in MAths Duh!
	
	private StaticShader shader;
	
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	
	/**
	 * New render method.
	 * @param entities
	 */
	public void render(Map<TexturedModel, List<Entity>> entities) {
		// Get all models
		for (TexturedModel model : entities.keySet()) {
			// bind shader values and Attribs
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity: batch) {
				prepareInstance(entity);
				
				// Render array in triangles. start at 0 render all
				LevelOfDetail lod = model.getRawModel().getLevelOfDetail();
				lod.determineCorrectLOD();
				//GL11.glDrawElements(GL11.GL_TRIANGLES, lod.getFloatArray().length,
					//	GL11.GL_UNSIGNED_INT, lod.getBufferOffset());
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
				//GL11.glDrawElements(mode, indices_count, type, indices_buffer_offset);
				//GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());
				
			}
			unbindTexturedModel();
		}
	}
	
	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		// Bind VAO of model
		try {
			GL30.glBindVertexArray(rawModel.getVaoID());
		} catch (Exception e) {
			APPLEVertexArrayObject.glBindVertexArrayAPPLE(rawModel.getVaoID());
		}
		
		// Activate the attribute lists
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		shader.loadNumberOfRows(texture.getNumberOfRows());
		if (texture.isHasTransparency()) {
			MasterRenderer.disableCulling();
		}
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		shader.loadFakeLightingVariable(texture.isUseFakeLighting());
		
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getID());
	}
	
	private void unbindTexturedModel() {
		MasterRenderer.enableCulling();
		// Disable attribute lists
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		
		// Un-bind
		try {
			GL30.glBindVertexArray(0);
		} catch (Exception e) {
			APPLEVertexArrayObject.glBindVertexArrayAPPLE(0);
		}
	}
	 
	private void prepareInstance(Entity entity) {
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(),
				entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
	
	}

}
