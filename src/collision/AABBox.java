package collision;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import toolbox.Debug;

/**
 * AABBox.java
 * Axis-Aligned Bounding Box
 * 
 * @author Ben
 * @version 21-JUL-2015
 */
public class AABBox {
	
	private static final int X = 0;
	private static final int Y = 1;
	private static final int Z = 2;
	
	private ArrayList<EndPoint> min; // holds the current world position of min endpoints
	private ArrayList<EndPoint> max; // holds the current world position of max endpoints
	private int ID;
	private SWPDelegate delegate;
	private float minX, minY, minZ = 0.0f; // holds the original local coords of mins
	private float maxX, maxY, maxZ = 0.0f; // holds the original local coords of maxs
	
	private static int ID_COUNTER = 0;
	
	public AABBox(ArrayList<EndPoint> min, ArrayList<EndPoint> max, SWPDelegate delegate) {
		this.min = min;
		this.max = max;
		this.ID = generateID();
		this.delegate = delegate;
	}
	
	private int generateID() {
		return ID_COUNTER++;
	}
	
	public void updateDelegate(AABBox box){
		delegate.collisionUpdate(this, box);
	}
	
	public boolean isIntersectXAxis(AABBox b) {
		return this.min.get(X).getValue() < b.max.get(X).getValue() && this.max.get(X).getValue() > b.min.get(X).getValue();
	}
	
	public boolean isIntersectYAxis(AABBox b) {
		return this.min.get(Y).getValue() < b.max.get(Y).getValue() && this.max.get(Y).getValue() > b.min.get(Y).getValue();
	}
	
	public boolean isIntersectZAxis(AABBox b) {
		return this.min.get(Z).getValue() < b.max.get(Z).getValue() && this.max.get(Z).getValue() > b.min.get(Z).getValue();
	}
	
	public boolean containsPoint(Vector3f point) {
		return (point.x > min.get(X).getValue() && point.x < max.get(X).getValue() 
			 && point.y > min.get(Y).getValue() && point.y < max.get(Y).getValue()
			 && point.z > min.get(Z).getValue() && point.z < max.get(Z).getValue() ); 
	}
	
	public float getMinX() {
		return minX;
	}
	public float getMinY() {
		return minY;
	}
	public float getMinZ() {
		return minZ;
	}
	public float getMaxX() {
		return maxX;
	}
	public float getMaxY() {
		return maxY;
	}
	public float getMaxZ() {
		return maxZ;
	}
	
	/**
	 * @param iD the iD to set
	 */
	public void setID(int iD) {
		ID = iD;
	}

	/**
	 * @param minX the minX to set
	 */
	public void setMinX(float minX) {
		this.minX = minX;
	}

	/**
	 * @param minY the minY to set
	 */
	public void setMinY(float minY) {
		this.minY = minY;
	}

	/**
	 * @param minZ the minZ to set
	 */
	public void setMinZ(float minZ) {
		this.minZ = minZ;
	}

	/**
	 * @param maxX the maxX to set
	 */
	public void setMaxX(float maxX) {
		this.maxX = maxX;
	}

	/**
	 * @param maxY the maxY to set
	 */
	public void setMaxY(float maxY) {
		this.maxY = maxY;
	}

	/**
	 * @param maxZ the maxZ to set
	 */
	public void setMaxZ(float maxZ) {
		this.maxZ = maxZ;
	}

	/**
	 * @return the min
	 */
	public ArrayList<EndPoint> getMin() {
		return min;
	}

	/**
	 * @param min the min to set
	 */
	public void setMin(ArrayList<EndPoint> min) {
		this.min = min;
	}

	/**
	 * @return the max
	 */
	public ArrayList<EndPoint> getMax() {
		return max;
	}

	/**
	 * @param max the max to set
	 */
	public void setMax(ArrayList<EndPoint> max) {
		this.max = max;
	}

	/**
	 * @return the ID
	 */
	public int getID() {
		return ID;
	}
	
	public String toString() {
		String result = "AABBox["+ID+"]:\n";
		result += "mins[" + min.get(X) + "," + min.get(Y) + "," + min.get(Z) + "]\n"; 
		result += "maxs[" + max.get(X) + "," + max.get(Y) + "," + max.get(Z) + "]\n";  
		result += "minX->" + minX + " " + "minY->" + minY + " " + "minZ->" + minZ + "\n";
		result += "maxX->" + maxX + " " + "maxY->" + maxY + " " + "maxZ->" + maxZ + "\n";
		return result;
	}
	
}
