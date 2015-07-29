package entities;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Vector3f;

import engineTester.MainGameLoop;
import renderEngine.DisplayManager;
import server.MultiplayerManager;
import server.ServerPlayer;
import toolbox.Debug;
import models.TexturedModel;

public class EntityManager {

	private List<Entity> entities;
	private List<ServerPlayer> playersToAdd;
	
	private static EntityManager sharedManager;
	public static EntityManager getInstance(){
		if (sharedManager == null){
			sharedManager = new EntityManager();
		}
		return sharedManager;
	}
	
	public EntityManager() {
		entities = new ArrayList<Entity>();
		playersToAdd = new ArrayList<ServerPlayer>();
	}
	
	public void addEntity(Entity entity){
		entities.add(entity);
	}
	
	public void removeEntity(Entity entity){
		entities.remove(entity);
	}
	
	public List<Entity> getEntities(){
		return this.entities;
	}
	
	public void update() {
		
		if (playersToAdd.size() > 0)
		{
			ArrayList<ServerPlayer> playersToRemove = new ArrayList<ServerPlayer>();
			for (ServerPlayer player : playersToAdd)
			{
				addEntity(new NetworkPlayer(new TexturedModel("person","playerTexture"),
						new Vector3f(752,7,-290), 0, 0, 0, 0.3f, player.getIpAddress(),
						player.getPort(), player.getUsername()));
				Debug.log("added " + player.getUsername() + " to the world!");
				playersToRemove.add(player);
			}
			
			for (ServerPlayer player : playersToRemove) {
				playersToAdd.remove(player);
			}
		}
	}
	
	public void addNewNetworkedPlayer(InetAddress ip, int port, String username) {
		playersToAdd.add(new ServerPlayer(ip,port,username));
	}

	public void movePlayer(String username, Vector3f position, Vector3f rotation) {
		// TODO(BEN): handle this better. client does not need to receive data that it sent!
		if (!username.equals(MultiplayerManager.getInstance().getClient().getCurrentPlayer().getUsername())) {
			NetworkPlayer player = getPlayerNamed(username);
			player.setPosition(position);
			player.setRotX(rotation.x);
			player.setRotY(rotation.y);
			player.setRotZ(rotation.z);
		}
			
	}
	
	public NetworkPlayer getPlayerNamed(String username){
		NetworkPlayer result = null;
		for (Entity entity : this.entities) {
			if (entity instanceof NetworkPlayer){
				NetworkPlayer player = (NetworkPlayer) entity;
				if (player.getUsername().equals(username))
				{
					result = player;
					break;
				}
			}
		}
		return result;
	}
	
	public boolean isPlayerInWorld(String username){
		boolean result = false;
		if (getPlayerNamed(username) != null) {
			result = true;
		}
		return result;
	}
}
