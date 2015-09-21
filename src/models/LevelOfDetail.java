package models;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

import toolbox.Debug;
import toolbox.Maths;

public class LevelOfDetail {

	private static final int MAX_LEVEL_OF_DETAIL = 0; // all vertices visible
	private static final int MIN_LEVEL_OF_DETAIL = 6; // not visible at all
	
	private int currentLevelOfDetail;
	private ArrayList<float[]> listOfDetails;
	
	public LevelOfDetail(float[] originalIndices) {		
		Debug.log("creating LOD");
		listOfDetails = new ArrayList<float[]>();
		currentLevelOfDetail = MAX_LEVEL_OF_DETAIL;
		for (int lod = MAX_LEVEL_OF_DETAIL; lod <= MIN_LEVEL_OF_DETAIL; lod++)
			listOfDetails.add(lod, generateEdgeReductionWithLevelOfDetail(originalIndices, lod));
	}
	
	public float[] getCorrectIndices() {
		return this.listOfDetails.get(currentLevelOfDetail);
	}
	
	public int getBufferOffset() {
		int offset = 0;
		for (int i = 0; i < currentLevelOfDetail; i++)
			offset += listOfDetails.get(i).length;
		return offset;
	}
	
	/**
	 * Generates a new mesh that is simpler (less vertices) based
	 * on the specified level of detail (LOD).
	 * 
	 * @param lod
	 */
	private float[] generateEdgeReductionWithLevelOfDetail(float[] indices, int lod) {
		return indices;
//		//Debug.log("generating LOD lvl->"+lod);
//		if (lod == MAX_LEVEL_OF_DETAIL) return indices;
//		else if(lod == MIN_LEVEL_OF_DETAIL) return new float[0];
//				
//		float[] indicesCopy = indices.clone();
//		float[] result =  indices.clone();
//				
//		int indexToRemove = 0;
//		
//		float value = indices.length - (indices.length * ((float)lod/(float)MIN_LEVEL_OF_DETAIL));
//		int MAX_VETICES = (int)value;
//		Debug.log("max Vertices-> " +MAX_VETICES + " original LEN->"+result.length);
//		int count = 0;
//		while (result.length > MAX_VETICES) {
//			count++;
//			// Find the min edge
//			float min = Float.MAX_VALUE;
//			for (int i = 0; i < result.length; i+=3) {
//				Vector3f point = new Vector3f(i,i+1,i+2);
//				Vector3f nextPoint = new Vector3f(i+3,i+4,i+5);
//				float distance = Maths.distance(point, nextPoint);
//				if (distance < min) {
//					min = distance;
//					indexToRemove = i;
//				}
//			}
//
//			// remove the vertices to combine the points
//			result = new float[result.length - 3];
//			int newI = 0;
//			for(int i = 0; i < result.length; i++){
//				if (i!=indexToRemove && i!=indexToRemove+1 && i!=indexToRemove+2) {
//					result[newI] = indicesCopy[i];
//					newI++;
//				} else {
//					//not increasing newI just increasing i (removing...)
//				}	
//			}	
//		}
//		Debug.log("removal count->"+count);
//		return result;
	}
	
	public void determineCorrectLOD() {
		this.setCurrentLOD(1);
//		while(Keyboard.next()) {
//			if (Keyboard.getEventKey() == Keyboard.KEY_0) {
//			    if (!Keyboard.getEventKeyState()) {
//			    	setCurrentLOD(0);
//			    }
//			}
//			else if (Keyboard.getEventKey() == Keyboard.KEY_1) {
//			    if (!Keyboard.getEventKeyState()) {
//			    	setCurrentLOD(1);
//			    }
//			}
//			else if (Keyboard.getEventKey() == Keyboard.KEY_2) {
//			    if (!Keyboard.getEventKeyState()) {
//			    	setCurrentLOD(2);
//			    }
//			}
//			else if (Keyboard.getEventKey() == Keyboard.KEY_3) {
//			    if (!Keyboard.getEventKeyState()) {
//			    	setCurrentLOD(3);
//			    }
//			}
//			else if (Keyboard.getEventKey() == Keyboard.KEY_4) {
//			    if (!Keyboard.getEventKeyState()) {
//			    	setCurrentLOD(4);
//			    }
//			}
//			else if (Keyboard.getEventKey() == Keyboard.KEY_5) {
//			    if (!Keyboard.getEventKeyState()) {
//			    	setCurrentLOD(5);
//			    }
//			}
//			else if (Keyboard.getEventKey() == Keyboard.KEY_6) {
//			    if (!Keyboard.getEventKeyState()) {
//			    	setCurrentLOD(6);
//			    }
//			}
//			
//		}
	}
	
	public void setCurrentLOD(int i) {
		currentLevelOfDetail = i;
//		Debug.log("buffer off->"+getBufferOffset());
//		Debug.log(this.getCorrectIndices());
	}
	
	public int getCurrentLOD() {
		return currentLevelOfDetail;
	}
	
	private int floatSizeOfList() {
		int count = 0;
		for(float[] array : this.listOfDetails)
			for (int i = 0; i < array.length; i++)
				count++;
		return count;
	}
	
	public float[] getFloatArray() {
		float[] combinedArray = new float[floatSizeOfList()];
		int index = 0;
		for(float[] array : this.listOfDetails) {
			for (int i = 0; i < array.length; i++) {
				combinedArray[index + i] = array[i];
			}
			index += array.length;
		}
		return combinedArray;
	}
}
