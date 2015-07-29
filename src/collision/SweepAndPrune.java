package collision;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import toolbox.Debug;

public class SweepAndPrune {
	
	private static final int X = 0;
	private static final int Y = 1;
	private static final int Z = 2;
	
	private ArrayList<AABBox> boxes;
	private ArrayList<EndPoint> endPointsX;
	private ArrayList<EndPoint> endPointsY;
	private ArrayList<EndPoint> endPointsZ;
	
	private PairManager pairManager;
	
	public static SweepAndPrune sharedSWP;
	public static SweepAndPrune getInstance(){
		if (sharedSWP == null) {
			sharedSWP = new SweepAndPrune();
		}
		return sharedSWP;
	}
	
	public SweepAndPrune() {
		boxes = new ArrayList<AABBox>();
		endPointsX = new ArrayList<EndPoint>();
		endPointsY = new ArrayList<EndPoint>();
		endPointsZ = new ArrayList<EndPoint>();
		pairManager = new PairManager();
	}
	
	public void addBox(AABBox box) {
		boxes.add(box);	
		endPointsX.add(box.getMin().get(X));
		endPointsY.add(box.getMin().get(Y));
		endPointsZ.add(box.getMin().get(Z));
		endPointsX.add(box.getMax().get(X));
		endPointsY.add(box.getMax().get(Y));
		endPointsZ.add(box.getMax().get(Z));
		update(box, null);
	}
	
	/**
	 * update method only called when an object changes position.
	 * 
	 * @param box
	 * @param dVector
	 */
	public void update(AABBox box, Vector3f position) {
		if (position == null) return;
		//Debug.log(box);
		pairManager.clear();
		changeBoxForDimension(X, box, endPointsX, position);
		changeBoxForDimension(Y, box, endPointsY, position);
		changeBoxForDimension(Z, box, endPointsZ, position);
		sortAll();
		findAllPairs();
		pairManager.updateAllDelegates();
	}
	
	private void changeBoxForDimension(int dimension, AABBox box, ArrayList<EndPoint> endpoints, Vector3f center) {
		int id = box.getID();
		for (EndPoint p : endpoints) {
			if (p.getBoxID() == id) {
				switch(dimension){
				case X:
					if (p.isMin){
						p.setValue(center.x + box.getMinX());
					} else {
						p.setValue(center.x + box.getMaxX());
					}
					break;
				case Y:
					if (p.isMin){
						p.setValue(center.y + box.getMinY());
					} else {
						p.setValue(center.y + box.getMaxY());
					}
					break;
				case Z:
					if (p.isMin){
						p.setValue(center.z + box.getMinZ());
					} else {
						p.setValue(center.z + box.getMaxZ());
					}
					break;
					
				}
			}
		}
	}
	
	private void sortAll() {
		sort(endPointsX);
		sort(endPointsY);
		sort(endPointsZ);	
	}
	
	private void sort(ArrayList<EndPoint> endPoints) {
		int i;
		for (int j = 1; j < endPoints.size(); j++){
			EndPoint key = endPoints.get(j);
			for (i = j - 1; (i>=0)  && (endPoints.get(i).getValue() > key.getValue()); i--) {
				endPoints.set(i+1, endPoints.get(i));
			}
			endPoints.set(i + 1, key);
		}
	}
	
	private void findAllPairs(){	
		findPair(endPointsX, X);
		findPair(endPointsY, Y);
		findPair(endPointsZ, Z);
	}
	
	private void findPair(ArrayList<EndPoint> endPoints, int dimension) {
		ArrayList<Integer> prevBoxIDs = new ArrayList<Integer>(); 
		for (int i = 0; i < endPoints.size(); i++) {
			EndPoint currentPoint = endPoints.get(i);
			if (isNumInArray(currentPoint.getBoxID(), prevBoxIDs)) break;
			
			for (int j = i + 1; (j < endPoints.size()); j ++) {
				EndPoint nextPoint = endPoints.get(j);
				if (currentPoint.getBoxID() == nextPoint.getBoxID()){
					// we have found the end off this box.
					break;
				} else {
					// currentPoint is intersecting with this newEndPoint
					// add pairX
					pairManager.addPair(dimension, getBox(currentPoint.getBoxID()), getBox(nextPoint.getBoxID()));
				}
			}
			prevBoxIDs.add(currentPoint.getBoxID());
		}
		
	}
	
	private boolean isNumInArray(Integer num, ArrayList<Integer> array) {
		boolean result = false;
		if (array.size() >= 0) 
			for (Integer i : array)
				if (i == num) {
					result = true;
					break;
				}
		return result;
	}
	
	private AABBox getBox(int id){ 
		AABBox result = null;
		for (AABBox box:boxes) {
			if (box.getID() == id){
				result = box;
				break;
			}
		}
		return result;
	}
}
