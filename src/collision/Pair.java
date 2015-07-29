package collision;

public class Pair {

	private AABBox box1;
	private AABBox box2;
	
	public Pair(AABBox box1, AABBox box2) {
		this.box1 = box1;
		this.box2 = box2;
	}
	
	public boolean equals(Pair p) {
		
		int id1 = this.box1.getID();
		int id2 = this.box2.getID();
		
		int otherId1 = p.getBox1().getID();
		int otherId2 = p.getBox2().getID();
		
		if ((id1 == otherId1) && (id2 == otherId2)){
			return true;
		}
		
		if ((id1 == otherId2) && (id2 == otherId1)){
			return true;
		}
	
		return false;
	}
	
	/**
	 * @return the box1
	 */
	public AABBox getBox1() {
		return box1;
	}

	/**
	 * @return the box2
	 */
	public AABBox getBox2() {
		return box2;
	}
	
}
