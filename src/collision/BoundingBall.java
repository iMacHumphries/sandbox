package collision;

import org.lwjgl.util.vector.Vector3f;

public class BoundingBall {

	private float radius;
	private Vector3f center;
	
	public BoundingBall(float radius, Vector3f center) {
		this.radius = radius;
		this.center = center;
	}

	public boolean isInside(Vector3f point){
		if (point == null) return false;
		return Math.pow((point.x - center.x),2) + Math.pow((point.y - center.y), 2)
				+ Math.pow((point.z - center.z), 2) <= Math.pow(radius, 2);
	}
	
	/**
	 * @return the radius
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * @return the center
	 */
	public Vector3f getCenter() {
		return center;
	}
	
	public String toString() {
		return "center: (" + center.x + ", " + center.y + ", " + center.z + ")\n" + 
			   "radius: " + radius;
		
	}
	
}
