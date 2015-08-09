package models;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import toolbox.Debug;
import toolbox.Maths;

public class LevelOfDetail {

	private static final int MAX_LEVEL_OF_DETAIL = 0; // all vertices visible
	private static final int MIN_LEVEL_OF_DETAIL = 6; // not visible at all
	
	private int currentLevelOfDetail;
	private ArrayList<float[]> listOfDetails;
	
	public LevelOfDetail(float[] originalIndices) {		
		listOfDetails = new ArrayList<float[]>();
		currentLevelOfDetail = MAX_LEVEL_OF_DETAIL;
		for (int lod = MAX_LEVEL_OF_DETAIL; lod < MIN_LEVEL_OF_DETAIL; lod++)
			listOfDetails.add(lod, generateEdgeReductionWithLevelOfDetail(originalIndices, lod));
	}
	
	public float[] getCorrectIndices() {
		return this.listOfDetails.get(currentLevelOfDetail);
	}
	
	public int getBufferOffset() {
		int offset = 0;
		for (int i = 0; i < currentLevelOfDetail; i++) {
			offset += listOfDetails.get(i).length;
		}
		return offset;
	}
	
	/**
	 * Generates a new mesh that is simpler (less vertices) based
	 * on the specified level of detail (LOD).
	 * 
	 * @param lod
	 */
	private float[] generateEdgeReductionWithLevelOfDetail(float[] indices, int lod) {
		if (lod == MAX_LEVEL_OF_DETAIL) return indices;
		else if(lod == MIN_LEVEL_OF_DETAIL) return new float[0];
		
		if (indices == null) return null;
		
		float[] indicesCopy = indices.clone();
		float[] result = indicesCopy;
		int verticesRemoved = 0;
		float min = Float.MAX_VALUE;
		int indexToRemove = 0;
		
		int MAX_VETICES = indices.length - (indices.length * (lod/MIN_LEVEL_OF_DETAIL));
		
		while (result.length > MAX_VETICES) {
			// Find the min edge
			for (int i = 0; i < indicesCopy.length -5; i+=3) {
				Vector3f point = new Vector3f(i,i+1,i+2);
				Vector3f nextPoint = new Vector3f(i+3,i+4,i+5);
				float distance = Maths.distance(point, nextPoint);
				if (distance < min) {
					min = distance;
					indexToRemove = i;
				}
			}

			verticesRemoved += 3;
			// remove the vertices to combine the points
			result = new float[indicesCopy.length - verticesRemoved];
			Debug.log(result.length);
			int newI = 0;
			for(int i = 0; i < indicesCopy.length; i++){
				if (i<indexToRemove && i>indexToRemove+3) {
					result[newI] = indicesCopy[i];
					newI+=3;
				} else {
					//Debug.log("removed vertex");
				}	
			}
			
		}
		
		return result;
	}
	
	public void setCurrentLOD(int i) {
		currentLevelOfDetail = i;
	}
	
	public int getCurrentLOD() {
		return currentLevelOfDetail;
	}
	
	
}
