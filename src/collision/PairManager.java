package collision;

import java.util.ArrayList;

import toolbox.Debug;

public class PairManager {

	private static final int X = 0;
	private static final int Y = 1;
	private static final int Z = 2;
	
	private ArrayList<Pair> pairsX;
	private ArrayList<Pair> pairsY;
	private ArrayList<Pair> pairsZ;
	
	public PairManager() {
		clear();
	}
	
	public void clear() {
		pairsX = new ArrayList<Pair>();
		pairsY = new ArrayList<Pair>();
		pairsZ = new ArrayList<Pair>();
	}
	
	public void updateAllDelegates() {
		
		for (Pair pairX : pairsX){
			for (Pair pairY : pairsY) {
				for (Pair pairZ : pairsZ) {
					if (isTrue3dPair(pairX, pairY, pairZ)) {
						pairZ.getBox1().updateDelegate(pairZ.getBox2());
					}
				}
			}	
		}
		
//		for (int i = 0; i< pairsX.size(); i++) {
//			Pair pairX = pairsX.get(i);
//			for (int j = 0; j< pairsY.size(); j++) {
//				Pair pairY = pairsY.get(j);
//				for (int k = 0; k< pairsZ.size(); k++) {
//					Pair pairZ = pairsZ.get(k);
//					if (isTrue3dPair(pairZ, pairY, pairX)) {
//						pair
//					}
//				}
//				
//			}
//			
//		}
		
		
	}
	
	private boolean isTrue3dPair(Pair x, Pair y, Pair z) {
		if (isTrue2dPair(z, y)){
			return isTrue2dPair(z, x);
		}
		return false;
	}
	
	private boolean isTrue2dPair(Pair x, Pair y) {
		return x.equals(y);
	}
	
	public void addPair(int dimension, AABBox box1, AABBox box2) {
		Pair newPair = new Pair(box1, box2);
		if (isPairInDimension(dimension, newPair)) return;
		
		switch(dimension) {
			case X:
				pairsX.add(newPair);
				break;
			case Y:
				pairsY.add(newPair);
				break;
			case Z:
				pairsZ.add(newPair);
				break;
		}
	}
		
	private boolean isPairInDimension(int dimension, Pair newPair) {
		boolean result = false;
		
		ArrayList<Pair> checkingArray = null;
 		switch(dimension) {
			case X:
				checkingArray = pairsX;
				break;
			case Y:
				checkingArray = pairsY;
				break;
			case Z:
				checkingArray = pairsZ;
				break;
		}
 		
 		if (checkingArray == null || checkingArray.size() <= 0) return false;
 		
 		for (Pair p : checkingArray) {
 			if (p.equals(newPair)) {
 				result = true;
 				break;
 			}
 		}
		
		return result;
	}
}
