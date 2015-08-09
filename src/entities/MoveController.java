package entities;

public class MoveController {

	private Entity currentControlledEntity;
	private Entity placeHolder;
	private boolean isLocked = false;
	
	public static MoveController sharedMoveController;
	public static MoveController getInstance(){
		if (sharedMoveController == null) {
			sharedMoveController = new MoveController();
		}
		return sharedMoveController;
	}
	
	public MoveController() {

	}

	/**
	 * @return the currentControlledEntity
	 */
	public Entity getCurrentControlledEntity() {
		return currentControlledEntity;
	}
	
	public Entity getPlaceHolder() {
		return this.placeHolder;
	}

	/**
	 * @param currentControlledEntity the currentControlledEntity to set
	 */
	public void setCurrentControlledEntity(Entity currentControlledEntity) {
		this.currentControlledEntity = currentControlledEntity;
	}
	
	public void lock(){
		isLocked = true;
		placeHolder = this.currentControlledEntity;
		this.currentControlledEntity = null;
	}
	
	public void unlock(){
		if (isLocked) {
			this.currentControlledEntity = placeHolder;
			isLocked = false;
		}
	}
}
