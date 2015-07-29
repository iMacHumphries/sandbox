package collision;

public class EndPoint {
	
	private int boxID;
	private float value;
	boolean isMin;
	
	public EndPoint(int boxID, float value, boolean isMin) {
		this.boxID = boxID;
		this.value = value;
		this.isMin = isMin;
	}

	/**
	 * @return the boxID
	 */
	public int getBoxID() {
		return boxID;
	}

	/**
	 * @param boxID the boxID to set
	 */
	public void setBoxID(int boxID) {
		this.boxID = boxID;
	}

	/**
	 * @return the value
	 */
	public float getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(float value) {
		this.value = value;
	}

	/**
	 * @return the isMin
	 */
	public boolean isMin() {
		return isMin;
	}

	/**
	 * @param isMin the isMin to set
	 */
	public void setMin(boolean isMin) {
		this.isMin = isMin;
	}
	
	public String toString() {
		return value + " ";
	}
}
