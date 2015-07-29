package collision;

import org.lwjgl.util.vector.Vector3f;

public class BoundingEllipsoid {

	private float radius;
	private Vector3f center;
	private float dxDistance;
	private float dyDistance;
	private float dzDistance;
	
	public BoundingEllipsoid(float radius, Vector3f center,
			float dxDistance, float dyDistance, float dzDistance ) {
		this.radius = radius;
		this.center = center;
		this.dxDistance = dxDistance;
		this.dyDistance = dyDistance;
		this.dzDistance = dzDistance;
	}

	public boolean isInside(Vector3f point){
		if (point == null) return false;
		return (Math.pow((point.x - center.x),2))/dxDistance
			  +(Math.pow((point.y - center.y), 2))/dyDistance
			  +(Math.pow((point.z - center.z), 2))/dzDistance <= Math.pow(radius, 1);
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

