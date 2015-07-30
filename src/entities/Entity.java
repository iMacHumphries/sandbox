package entities;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import toolbox.Debug;
import toolbox.Maths;
import collision.AABBox;
import collision.BoundingBall;
import collision.BoundingEllipsoid;
import collision.EndPoint;
import collision.SWPDelegate;
import collision.SweepAndPrune;
import models.TexturedModel;

public class Entity implements SWPDelegate{

	private TexturedModel model;
	private Vector3f position;
	private float rotX,rotY,rotZ;
	private float scale;
	private int textureIndex = 0;
	
	private AABBox boundingBox;
		
	// MAIN CONSTRUCTOR
	public Entity(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotX = rotX;
		this.rotY = rotY;
		this.rotZ = rotZ;
		this.scale = scale;
	}
	
	public Entity(TexturedModel model, int textureIndex, Vector3f position, float rotX,
			float rotY, float rotZ, float scale) {
		this(model, position, rotX, rotY, rotZ, scale);
		this.textureIndex = textureIndex;
	}
	
	public Entity(TexturedModel model, Vector3f position, float rotX,
			float rotY, float rotZ, float scale, boolean addBoundingBox) {
		this(model, position, rotX, rotY, rotZ, scale);
		if (addBoundingBox) {
			this.boundingBox = this.createAABBox(this.getModel().getRawModel().getPositions());
			SweepAndPrune.getInstance().addBox(boundingBox);
		}
	}
	
	public float getTextureXOffset() {
		int col = textureIndex % model.getTexture().getNumberOfRows();
		return (float) col / (float) model.getTexture().getNumberOfRows();
	}
	
	public float getTextureYOffset() {
		int row = textureIndex / model.getTexture().getNumberOfRows();
		return (float) row / (float) model.getTexture().getNumberOfRows();
	}
	

	public void increasePosition(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
		if (dx != 0 || dy != 0 || dz != 0) {
			//Debug.log("dx: " + dx + " dy: " + dy + " dz:" + dz);
			SweepAndPrune.getInstance().update(this.getBoundngBox(), this.position);
		}
	}
	
	public void increaseRotation(float dx, float dy, float dz) {
		this.rotX += dx;
		this.rotY += dy;
		this.rotZ += dz;
	}
	
	/**
	 * @return the model
	 */
	public TexturedModel getModel() {
		return model;
	}

	/**
	 * @param model the model to set
	 */
	public void setModel(TexturedModel model) {
		this.model = model;
	}

	/**
	 * @return the position
	 */
	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getRotation() {
		return new Vector3f(rotX, rotY, rotZ);
	}
	
	/**
	 * @param position the position to set
	 */
	public void setPosition(Vector3f position) {
		this.position = position;
	}

	/**
	 * @return the rotX
	 */
	public float getRotX() {
		return rotX;
	}

	/**
	 * @param rotX the rotX to set
	 */
	public void setRotX(float rotX) {
		this.rotX = rotX;
	}

	/**
	 * @return the rotY
	 */
	public float getRotY() {
		return rotY;
	}

	/**
	 * @param rotY the rotY to set
	 */
	public void setRotY(float rotY) {
		this.rotY = rotY;
	}

	/**
	 * @return the rotZ
	 */
	public float getRotZ() {
		return rotZ;
	}

	/**
	 * @param rotZ the rotZ to set
	 */
	public void setRotZ(float rotZ) {
		this.rotZ = rotZ;
	}

	/**
	 * @return the scale
	 */
	public float getScale() {
		return scale;
	}

	/**
	 * @param scale the scale to set
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	public AABBox createAABBox(Vector3f[] modelPositions) {
		if (modelPositions.length <= 0) return null;
		
		boundingBox = new AABBox(null, null, this);
		
		float minX, minY, minZ;
		float maxX, maxY, maxZ;
		
		minX = maxX = modelPositions[0].x;
		minY = maxY = modelPositions[0].y;
		minZ = maxZ = modelPositions[0].z;
		
		for (int i = 0; i < modelPositions.length; i++) {
			Vector3f vec = modelPositions[i];
			// x
			if (vec.x < minX)
				minX = vec.x;
			else if (vec.x > maxX)
				maxX = vec.x;
			// y
			if (vec.y < minY)
				minY = vec.y;
			else if (vec.y > maxY)
				maxY = vec.y;
			// z
			if (vec.z < minZ)
				minZ = vec.z;
			else if (vec.z > maxZ)
				maxZ = vec.z;
		}
		ArrayList<EndPoint> maxs = new ArrayList<EndPoint>();
		ArrayList<EndPoint> mins = new ArrayList<EndPoint>();
		
		maxs.add(new EndPoint(boundingBox.getID(), position.x + maxX, false));
		maxs.add(new EndPoint(boundingBox.getID(), position.y + maxY, false));
		maxs.add(new EndPoint(boundingBox.getID(), position.z + maxZ, false));
		
		mins.add(new EndPoint(boundingBox.getID(), position.x + minX, true));
		mins.add(new EndPoint(boundingBox.getID(), position.y + minY, true));
		mins.add(new EndPoint(boundingBox.getID(), position.z + minZ, true));
		
		boundingBox.setMax(maxs);
		boundingBox.setMin(mins);
		
		boundingBox.setMaxX(maxX);
		boundingBox.setMaxY(maxY);
		boundingBox.setMaxZ(maxZ);
		
		boundingBox.setMinX(minX);
		boundingBox.setMinY(minY);
		boundingBox.setMinZ(minZ);
		
		return boundingBox;
	}
	
	public AABBox getBoundngBox(){
		return boundingBox;
	}

	@Override
	public void collisionUpdate(AABBox box1, AABBox box2) {
		Debug.log("["+box1.getID()+"] collided with ["+box2.getID()+"]");
		
	}
}
