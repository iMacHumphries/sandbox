package toolbox;

import java.util.ArrayList;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import collision.AABBox;
import collision.BoundingBall;
import collision.BoundingEllipsoid;
import entities.Camera;

public class Maths {

	
	
	public static float barryCentric(Vector3f p1, Vector3f p2, Vector3f p3, Vector2f pos) {
		float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;
		float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;
	
	}
	
	public static Matrix4f createTransformationMatrix(Vector2f translation, Vector2f scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.scale(new Vector3f(scale.x, scale.y, 1f), matrix, matrix);
		return matrix;
	}
	
	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
			float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
		return matrix;
	}

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000.0f;
	
	public static Matrix4f createProjectionMatrix() {
		float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = FAR_PLANE - NEAR_PLANE;
		
		Matrix4f matrix = new Matrix4f();
		matrix.m00 = x_scale;
		matrix.m11 = y_scale;
		matrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
		matrix.m23 = -1;
		matrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
		matrix.m33 = 0;
		return matrix;	
	}
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), matrix, matrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos= new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, matrix, matrix);
		return matrix;
		
	}
	
	public static BoundingBall createBoundingBall(Vector3f[] modelPositions, Vector3f center) {
		if (modelPositions.length <= 0) return null;
		
		float radius = 0.0f;
		
		float minX, minY, minZ;
		float maxX, maxY, maxZ;
		
		minX = maxX = modelPositions[0].x;
		minY = maxY = modelPositions[0].y;
		minZ = maxZ = modelPositions[0].z;
		
		
		for (int i = 0; i < modelPositions.length; i++) {
			Vector3f vec = modelPositions[i];
			// x
			if (vec.x < minX){
				minX = vec.x;
			} else if (vec.x > maxX) {
				maxX = vec.x;
			}
			// y
			if (vec.y < minY){
				minY = vec.y;
			} else if (vec.y > maxY) {
				maxY = vec.y;
			}
			// z
			if (vec.z < minZ){
				minZ = vec.z;
			} else if (vec.z > maxZ) {
				maxZ = vec.z;
			}
			
			float dxDistance = maxX - minX;
			float dyDistance = maxY - minY;
			float dzDistance = maxZ - minZ;

			radius = Math.max(dzDistance, Math.max(dxDistance, dyDistance));

		}
		
		return new BoundingBall(radius, center);
	}
	
	public static BoundingEllipsoid createBoundingEllipsoid(Vector3f[] modelPositions, Vector3f center) {
		if (modelPositions.length <= 0) return null;
		
		float radius = 0.0f;
		
		float minX, minY, minZ;
		float maxX, maxY, maxZ;
		
		minX = maxX = modelPositions[0].x;
		minY = maxY = modelPositions[0].y;
		minZ = maxZ = modelPositions[0].z;
		
		float dxDistance = 1;
		float dyDistance = 1;
		float dzDistance = 1;
		
		for (int i = 0; i < modelPositions.length; i++) {
			Vector3f vec = modelPositions[i];
			// x
			if (vec.x < minX){
				minX = vec.x;
			} else if (vec.x > maxX) {
				maxX = vec.x;
			}
			// y
			if (vec.y < minY){
				minY = vec.y;
			} else if (vec.y > maxY) {
				maxY = vec.y;
			}
			// z
			if (vec.z < minZ){
				minZ = vec.z;
			} else if (vec.z > maxZ) {
				maxZ = vec.z;
			}
			
			dxDistance = maxX - minX;
			dyDistance = maxY - minY;
			dzDistance = maxZ - minZ;

			radius = Math.min(dzDistance, Math.min(dxDistance, dyDistance));

		}
		
		return new BoundingEllipsoid(radius, center, dxDistance, dyDistance, dzDistance);
	}
	
	public static float distance(Vector3f v1, Vector3f v2) {
		return (float) (Math.sqrt(Math.pow(v2.x - v1.x, 2) + Math.pow(v2.y - v1.z, 2) + Math.pow(v2.z - v1.z, 2)));
	}

}
