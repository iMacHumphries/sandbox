package engineTester;

import entities.Camera;
import entities.EntityManager;
import entities.Light;
import entities.LocalPlayer;
import entities.MoveController;
import entities.NetworkPlayer;
import entities.Ship;
import fonts.Label;
import fonts.LabelRenderer;
import guis.GuiRenderer;
import guis.GuiTexture;
import guis.ChatBox;
import guis.TypeWriter;

import java.util.ArrayList;
import java.util.List;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import server.MultiplayerManager;
import terrains.Terrain;
import textures.TerrainTexture;
import textures.TerrainTexturePack;
import toolbox.MousePicker;
import water.WaterFrameBuffers;
import water.WaterRenderer;
import water.WaterShader;
import water.WaterTile;

public class MainGameLoop{

	//private static boolean isHosting = true;
	private static boolean isHosting = false;
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		Loader loader = Loader.getInstance();
		MasterRenderer renderer = new MasterRenderer();
		GuiRenderer guiRenderer = new GuiRenderer(loader);
		LabelRenderer labelRenderer = new LabelRenderer(loader);
		
		EntityManager entityManager = EntityManager.getInstance();
		List<GuiTexture> guis = new ArrayList<GuiTexture>();
		List<Terrain> terrains = new ArrayList<Terrain>();
		List<Light> lights = new ArrayList<Light>();
		List<Label> labels = new ArrayList<Label>();
		
		//guis.add(new GuiTexture("fontMap", new Vector2f(0,0), new Vector2f(0.25f,0.25f)));
		
		Label mainLabel = new Label("", new Vector2f(-1f,-1f), new Vector2f(0.1f,0.1f), new Vector4f(1.0f,0.0f,0.0f,1.0f));
		labels.add(mainLabel); // always render;
		ChatBox chatBox = new ChatBox(null, null, labelRenderer, mainLabel);
		
		
		String name = "Marz";
		if (!isHosting) name = "ben";
		
		LocalPlayer player = new LocalPlayer( new TexturedModel("person", "playerTexture"),new Vector3f(752,7,-290), 0, 0, 0, 0.3f,null,-1,name);
		MoveController.getInstance().setCurrentControlledEntity(player);
		entityManager.addEntity(player);
		
		MultiplayerManager multiplayerManager = MultiplayerManager.getInstance();
		multiplayerManager.startClient(player);	
		if (isHosting) {
			multiplayerManager.hostServer();	
		}
		multiplayerManager.login();
		
		
		/* Texture Pack */
		TerrainTexture backgroundTexture = new TerrainTexture("sand");
		TerrainTexture rTexture = new TerrainTexture("mud");
		TerrainTexture gTexture = new TerrainTexture("sand");
		TerrainTexture bTexture = new TerrainTexture("path");
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture,
				gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture("blendMap");
		
				
		//Light red = new Light(new Vector3f(185, 16,-293), new Vector3f(2,0,0), new Vector3f(1, 0.01f, 0.002f));
		lights.add(new Light(new Vector3f(0,10000,-7000), new Vector3f(0.7f,0.7f,0.7f)));
		
		
		Terrain terrain = new Terrain(0,-1,loader,texturePack, blendMap, "seaHeightmap");
		terrains.add(terrain);
		//Terrain terrain2 = new Terrain(-1,-1,loader,texturePack, blendMap, "heightmap");
		
			
		Camera camera = Camera.getInstance();
		
		
		MousePicker picker = new MousePicker(camera, renderer.getProjectionMatrix(), terrain);
				
			
		Light light = (new Light(new Vector3f(293, 7, -305), new Vector3f(0,2,2), new Vector3f(1, 0.01f, 0.002f)));
		lights.add(light);
		
		WaterFrameBuffers fbos = new WaterFrameBuffers();
		
		WaterShader waterShader = new WaterShader();
		WaterRenderer waterRenderer = new WaterRenderer(loader, waterShader, renderer.getProjectionMatrix(), fbos);
		List<WaterTile> waters = new ArrayList<WaterTile>();
		WaterTile water = new WaterTile(75,-75,4);
		waters.add(water);
			
		Ship ship = new Ship(new TexturedModel("ship", "boatTexture"), new Vector3f(752,7, -150), 0, 180, 0, 1, waters);
		entityManager.addEntity(ship);

		boolean isDevModeSet = false;

		while(!Display.isCloseRequested()) {
			
//			if (Keyboard.isKeyDown(Keyboard.KEY_P)){
//				if(!isDevModeSet) {
//					isDevModeSet = true;
//					GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE );
//				}
//				else{ 
//					GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL );
//					isDevModeSet = false;
//				}
//			}
			
			player.move(terrain);
			ship.move();
			camera.move();
			picker.update();
						
			GL11.glEnable(GL30.GL_CLIP_DISTANCE0);
			//render reflection texture
			fbos.bindReflectionFrameBuffer();
			float distance = 2 * (camera.getPosition().y - water.getHeight());
			camera.getPosition().y -=distance;
			camera.invertPitch();
			renderer.renderScene(entityManager.getEntities(), terrains, lights, camera, new Vector4f(0,1,0,-water.getHeight()));
			camera.getPosition().y += distance;
			camera.invertPitch();
			
			//render refraction texture
			fbos.bindRefractionFrameBuffer();
			renderer.renderScene(entityManager.getEntities(), terrains, lights, camera, new Vector4f(0,-1,0,water.getHeight()));
			fbos.unbindCurrentFrameBuffer();
					
			// render to screen
			GL11.glDisable(GL30.GL_CLIP_DISTANCE0);
			renderer.renderScene(entityManager.getEntities(), terrains, lights, camera, new Vector4f(0,-1,0,1500));
			waterRenderer.render(waters, camera);
			guiRenderer.render(guis);
			labelRenderer.render(labels);
			
			multiplayerManager.update();
			entityManager.update();
			chatBox.render();
			
			DisplayManager.updateDisplay();
		}

		fbos.cleanUp();
		waterShader.cleanUp();
		guiRenderer.cleanUp();
		labelRenderer.cleanUp();
		renderer.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();	
	}

	
}

// ray casting moving object / terrain collision
//Vector3f terrainPoint = picker.getCurrentTerrainPoint();
//if (terrainPoint !=null) {
//	lampEntity.setPosition(terrainPoint);
//	light.setPosition(new Vector3f (terrainPoint.x, terrainPoint.y+15, terrainPoint.z));
//}

// Using textureAtlas
//TexturedModel fernAtlas = new TexturedModel("fern", "fern");
//fernAtlas.getTexture().setNumberOfRows(2);
//fernAtlas.getTexture().setHasTransparency(true);


// Terrain entity placement
//Random random = new Random();
//for (int i = 0; i < 500; i++) {
//	float x = random.nextFloat() * 800 - 400;
//	float z = random.nextFloat() * -600;
//	float y = terrain.getHeightOfTerrain(x, z);
//	entities.add(new Entity(grass, new Vector3f(x, y,
//			z), 0,0,0,1));
//	x = random.nextFloat() * 800 - 400;
//	z = random.nextFloat() * -600;
//	y = terrain.getHeightOfTerrain(x, z);
//	entities.add(new Entity(fernAtlas,random.nextInt(4), new Vector3f(x, y,
//			z), 0,0,0,1));	
//}

// Algorithm test
//SweepAndPrune swp = new SweepAndPrune();
//ArrayList<EndPoint> mins = new ArrayList<EndPoint>();
//ArrayList<EndPoint> maxs = new ArrayList<EndPoint>();
//
//// box 0
//mins.add(new EndPoint(0, 1, true));
//maxs.add(new EndPoint(0, 2, false));
//mins.add(new EndPoint(0, 3, true));
//maxs.add(new EndPoint(0, 4, false));
//mins.add(new EndPoint(0, 4, true));
//maxs.add(new EndPoint(0, 5, false));
//
//swp.addBox(new AABBox(mins, maxs, this));
// mins = new ArrayList<EndPoint>();
// maxs = new ArrayList<EndPoint>();
////box 1
//mins.add(new EndPoint(1, -5, true));
//maxs.add(new EndPoint(1, 33, false));
//mins.add(new EndPoint(1, -3, true));
//maxs.add(new EndPoint(1, 40, false));
//mins.add(new EndPoint(1, 0, true));
//maxs.add(new EndPoint(1, 10, false));
//
//swp.addBox(new AABBox(mins, maxs, this));
