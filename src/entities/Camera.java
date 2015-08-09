package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

import renderEngine.MasterRenderer;
import toolbox.Debug;


public class Camera{

	private static final float MAX_ZOOM = 80.0f;
	private static final float MIN_ZOOM = 10.0f;
	private static final float MAX_PITCH = 90.0f;
	private static final float MIN_PITCH = -90.0f;
	
	private float distanceFromPlayer = 50;
	private float angleAroundPlayer = 0;
	
	private Vector3f position = new Vector3f(100,35,50);
	private float pitch = 20;
	private float yaw;
	private float roll;
	
	
	public static Camera sharedCamera;
	public static Camera getInstance(){
		if (sharedCamera == null) {
			sharedCamera = new Camera();
		}
		return sharedCamera;
	}
	
	public Camera() {

	}
	
	private Entity getCurrentEntity(){
		Entity e = MoveController.getInstance().getCurrentControlledEntity();
		if (e == null)
			e =  MoveController.getInstance().getPlaceHolder();
		return e;
	}

	public void move() {
		this.calculateZoom();
		this.calculatePitchAndAngleAroundPlayer();
	
		float verticalDistance = this.calculateVerticalDistance();
		float horizontalDistance = this.calculateHorizontalDistance();

		this.calculateCameraPosition(horizontalDistance, verticalDistance);
		this.yaw = 180 - (getCurrentEntity().getRotY() + this.angleAroundPlayer);
		
		MasterRenderer master = MasterRenderer.getInstance();
		if (position.y < 4) {
			Debug.log("cam underwater");
			master.setFogDensity(0.05f);
			master.setFogGradient(2f);
		} else {
			master.setFogDensity(MasterRenderer.DEFAULT_FOG_DENSITY);
			master.setFogGradient(MasterRenderer.DEFAULT_FOG_GRADIENT);
		}

	}
	
	private float calculateHorizontalDistance() {
		return distanceFromPlayer * (float) Math.cos(Math.toRadians(pitch));
	}
	
	private float calculateVerticalDistance() {
		return distanceFromPlayer * (float) Math.sin(Math.toRadians(pitch));
	}
	
	private void calculateCameraPosition(float horz, float vert) {
		float theta = getCurrentEntity().getRotY() + this.angleAroundPlayer;
		float offsetX = (float) (horz * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horz * Math.cos(Math.toRadians(theta)));
		position.x = getCurrentEntity().getPosition().x - offsetX;
		position.z = getCurrentEntity().getPosition().z - offsetZ;
		position.y = getCurrentEntity().getPosition().y + vert;	
	}
	
	private void calculateZoom() {
		float zoomLevel = Mouse.getDWheel() * 0.1f;
		distanceFromPlayer -= zoomLevel;
		distanceFromPlayer = Math.min(distanceFromPlayer, MAX_ZOOM);
		distanceFromPlayer = Math.max(distanceFromPlayer, MIN_ZOOM);
	}

	private void calculatePitchAndAngleAroundPlayer() {
		 if (Mouse.isButtonDown(0) && Mouse.isButtonDown(1)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		 } else if (Mouse.isButtonDown(0) || Mouse.isButtonDown(1)) {
			float angleChange = Mouse.getDX() * 0.3f;
			angleAroundPlayer -= angleChange;
			float pitchChange = Mouse.getDY() * 0.1f;
			pitch -= pitchChange;
		 } else {
	
		 }
		 
		 angleAroundPlayer = this.limitAngle(angleAroundPlayer);
		 
		 pitch = Math.min(pitch, MAX_PITCH);
		 pitch = Math.max(pitch, MIN_PITCH);
	}
	
	private float limitAngle(float angle) {
		float newAngle = angle;
		if (angle > 360) {
			newAngle = 360 - angle;
		} else if (angle < 0) {
			newAngle = 360 + angle;
		}
		return newAngle;
	}
	
	public void invertPitch(){
		this.pitch = -pitch;
	}
	
	/**
	 * @return the position
	 */
	public Vector3f getPosition() {
		return position;
	}

	/**
	 * @return the pitch
	 */
	public float getPitch() {
		return pitch;
	}

	/**
	 * @return the yaw
	 */
	public float getYaw() {
		return yaw;
	}

	/**
	 * @return the roll
	 */
	public float getRoll() {
		return roll;
	}

	/**
	 * @return the distanceFromPlayer
	 */
	public float getDistanceFromPlayer() {
		return distanceFromPlayer;
	}

	/**
	 * @param distanceFromPlayer the distanceFromPlayer to set
	 */
	public void setDistanceFromPlayer(float distanceFromPlayer) {
		this.distanceFromPlayer = distanceFromPlayer;
	}
	
	
	
}
