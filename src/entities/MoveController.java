package entities;

public class MoveController {

	Entity currentControlledEntity;
	
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

	/**
	 * @param currentControlledEntity the currentControlledEntity to set
	 */
	public void setCurrentControlledEntity(Entity currentControlledEntity) {
		this.currentControlledEntity = currentControlledEntity;
	}
	
	
}
