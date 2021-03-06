package entities;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import server.MultiplayerManager;
import terrains.Terrain;
import toolbox.Debug;
import water.WaterTile;

public class Player extends Entity{

	private static final float RUN_SPEED = 30;
	private static final float SWIM_SPEED = 10;
	private static final float TURN_SPEED = 160;
	private static final float GRAVITY = -50;
	private static final float JUMP_POWER = 20;
	
	private static final float TERRAIN_HEIGHT = 0;
	
	private float currentSpeed = 0;
	private float currentTurnSpeed = 0;
	private float upwardsSpeed;
	private boolean isInAir = false;
	private boolean isSwimming = false;
	
	
	public Player(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		super(model, position, rotX, rotY, rotZ, scale, true);

	}

	public void move(Terrain terrain, WaterTile water) {
	
		float terrainHeight = terrain.getHeightOfTerrain(super.getPosition().x, super.getPosition().z);
		if (super.getPosition().y < terrainHeight) {
			upwardsSpeed = 0;
			super.getPosition().y = terrainHeight;
			isInAir = false;
		} else if (super.getPosition().y != terrainHeight) {
			isInAir = true;
			upwardsSpeed += GRAVITY * DisplayManager.getDelta();
		}
		
		if (MoveController.getInstance().getCurrentControlledEntity() == this)
			checkInputs();
		
		increaseRotation(0, currentTurnSpeed * DisplayManager.getDelta(), 0);
		float distance = currentSpeed * DisplayManager.getDelta();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		increasePosition(dx, upwardsSpeed * DisplayManager.getDelta(), dz);
		
		if (getPosition().y + this.boundingBox.getMaxY()/4 < water.getHeight()) {
			Debug.log("underwater");
			isSwimming = true;
		} else isSwimming = false;
		
	}
	

	private void jump() {
		if (!isInAir) {
			this.upwardsSpeed = JUMP_POWER;
			isInAir = true;
		}
		
	}
	
	public void moveWithCamera() {
		this.currentSpeed = RUN_SPEED;
	}
	
	private void checkInputs() {
		
		if (Mouse.isButtonDown(0) && Mouse.isButtonDown(1)) {
			this.currentSpeed = getSpeed();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.currentSpeed = getSpeed();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.currentSpeed = -getSpeed();
		} else {
			this.currentSpeed = 0;
		}
		
		if (Mouse.isButtonDown(0) && Mouse.isButtonDown(1)) {
			
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			this.currentTurnSpeed = -TURN_SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			this.currentTurnSpeed = TURN_SPEED;
		} else {
			this.currentTurnSpeed = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			jump();
		}
	}

	private float getSpeed(){
		if (!isSwimming) {
			return this.RUN_SPEED;
		} else {
			return this.SWIM_SPEED;
		}
	}
	
	/**
	 * @return the currentSpeed
	 */
	public float getCurrentSpeed() {
		return currentSpeed;
	}

	/**
	 * @param currentSpeed the currentSpeed to set
	 */
	public void setCurrentSpeed(float currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	/**
	 * @return the currentTurnSpeed
	 */
	public float getCurrentTurnSpeed() {
		return currentTurnSpeed;
	}

	/**
	 * @param currentTurnSpeed the currentTurnSpeed to set
	 */
	public void setCurrentTurnSpeed(float currentTurnSpeed) {
		this.currentTurnSpeed = currentTurnSpeed;
	}
	
	
}
