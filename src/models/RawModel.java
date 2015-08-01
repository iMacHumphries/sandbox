package models;

import org.lwjgl.util.vector.Vector3f;


public class RawModel {
	
	private int vaoID;
	private int vertexCount;
	private Vector3f[] positions;
	
	public RawModel(int _vaoID, int _vertexCount, float[] points) {
		this.vaoID = _vaoID;
		this.vertexCount = _vertexCount;
		if (points != null)
			fillPositionsArray(points);
	}
	
	private void fillPositionsArray(float[] points) {
		if (points.length % 3 == 0)
		{
			positions = new Vector3f[points.length/3];
			int index = 0;
			for (int i = 0; i < points.length; i+=3)
			{
				this.positions[index] = new Vector3f(points[i], points[i + 1], points[i + 2]);
				index++;
			}
		}
	}
			
	/**
	 * @return the positions
	 */
	public Vector3f[] getPositions() {
		return positions;
	}

	/**
	 * @return the vaoID
	 */
	public int getVaoID() {
		return vaoID;
	}

	/**
	 * @return the vertexCount
	 */
	public int getVertexCount() {
		return vertexCount;
	}
		
}
