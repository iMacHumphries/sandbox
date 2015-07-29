package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import shaders.StaticShader;
import shaders.TerrainShader;
import skybox.SkyboxRenderer;
import terrains.Terrain;
import toolbox.Maths;

public class MasterRenderer {

	private static final float RED =  0.1f;
	private static final float GREEN =  0.4f;
	private static final float BLUE =  0.5f;
	
			
	private StaticShader shader = new StaticShader();
	private EntityRenderer renderer;
	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader = new TerrainShader();
	
	private Matrix4f projectionMatrix;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<TexturedModel, List<Entity>>();
	private List<Terrain> terrains = new ArrayList<Terrain>();
	
	private SkyboxRenderer skyboxRenderer;
	
	public MasterRenderer() {
		enableCulling();
		this.projectionMatrix = Maths.createProjectionMatrix();
		renderer = new EntityRenderer(shader, this.projectionMatrix);
		terrainRenderer = new TerrainRenderer(terrainShader, this.projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(Loader.getInstance(), this.projectionMatrix);
	}
	
	public static void enableCulling() {
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}
	
	public static void disableCulling() {
		GL11.glDisable(GL11.GL_CULL_FACE);
	}
	
	public void renderScene(List<Entity> entities, List<Terrain> terrains,
			List<Light> lights, Camera camera, Vector4f clipPlane) {
		for (Terrain terrain : terrains) {
			processTerrain(terrain);
		}
		for (Entity entity : entities) {
			processEntity(entity);
		}
		render(lights, camera, clipPlane);
	}
	
	public void render(List<Light> lights, Camera camera, Vector4f clipPlane) {
		this.prepare();
		
		shader.start();
		shader.loadClipPlane(clipPlane);
		shader.loadSkyColor(RED, GREEN, BLUE);
		shader.loadLights(lights);
		shader.loadViewMatrix(camera);
		renderer.render(entities);
		shader.stop();
		
		terrainShader.start();
		terrainShader.loadClipPlane(clipPlane);
		terrainShader.loadSkyColorVariable(RED, GREEN, BLUE);
		terrainShader.loadLights(lights);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		skyboxRenderer.render(camera, RED, GREEN, BLUE);
		
		terrains.clear();
		entities.clear();
	}
	
	/**
	 * Prepare OpenGL to render game.
	 */
	public void prepare() {
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(RED, GREEN, BLUE, 1);
	}
	
	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = this.entities.get(entityModel);
		if (batch!=null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<Entity>();
			newBatch.add(entity);
			this.entities.put(entityModel, newBatch);
		}
		
	}
	
	public void processTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void cleanUp() {
		terrainShader.cleanUp();
		shader.cleanUp();
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
}
