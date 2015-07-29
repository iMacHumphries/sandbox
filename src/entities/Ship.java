package entities;

import java.util.List;

import models.TexturedModel;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.DisplayManager;
import terrains.Terrain;
import water.WaterTile;

public class Ship extends Entity{

	private static final float SPEED = 21.0f;
	private static final float TURN_SPEED = 20.0f;
	
	private float currentTurnSpeed;
	private float currentSpeed;
	private List<WaterTile> waters;
	
	public Ship(TexturedModel model, Vector3f position, float rotX, float rotY,
			float rotZ, float scale, List<WaterTile> waters) {
		super(model, position, rotX, rotY, rotZ, scale, true);
		this.waters = waters;
	}
	

	public void move() {
		float waterHeight = waters.get(0).getHeight();
		if (super.getPosition().y < waterHeight) {
			super.getPosition().y = waterHeight;
		}
		if (MoveController.getInstance().getCurrentControlledEntity() != this) return;
		checkInputs();
		super.increaseRotation(0, currentTurnSpeed * DisplayManager.getDelta(), 0);
		float distance = currentSpeed * DisplayManager.getDelta();
		float dx = (float) (distance * Math.sin(Math.toRadians(super.getRotY())));
		float dz = (float) (distance * Math.cos(Math.toRadians(super.getRotY())));
		super.increasePosition(dx, 0, dz);

	}
	
	
	public void moveWithCamera() {
		this.currentSpeed = SPEED;
	}
	
	private void checkInputs() {
		
		 if (Keyboard.isKeyDown(Keyboard.KEY_W)){
			this.currentSpeed = SPEED;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)){
			this.currentSpeed = -SPEED;
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
