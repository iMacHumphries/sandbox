package guis;

import java.util.ArrayList;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import server.MultiplayerManager;
import server.Packet_03_Message;
import toolbox.Debug;
import entities.LocalPlayer;
import fonts.Label;
import fonts.LabelRenderer;
import guis.TypeWriter.TypeWriterDelegate;

public class ChatBox implements TypeWriterDelegate{

	private static final int MAX_MESSAGES = 10;
	private static final Vector2f LABEL_SCALE = new Vector2f(0.07f,0.07f);
	
	private Vector2f size;
	private Vector2f position;
	private ArrayList<Label> messageLabels;
	private LabelRenderer labelRenderer;
	private TypeWriter typeWriter;
	
	// Yikes..
	private static ChatBox sharedTextView = null;
	public static ChatBox getInstance() {
		return sharedTextView;
	}
	
	public ChatBox(Vector2f position, Vector2f size, LabelRenderer labelRenderer, Label mainLabel){
		sharedTextView = this;
		this.position = position;
		this.size = size;
		this.messageLabels = new ArrayList<Label>();
		this.labelRenderer = labelRenderer;
		
		
			
		typeWriter = new TypeWriter(mainLabel);
		typeWriter.setDelegate(this);
	}
	
	
	public void addMessage(String message) {
		if (message == null) return;
		Debug.log("adding new label " + message);
		Label label = new Label(message, new Vector2f(-1f,-1f), LABEL_SCALE, new Vector4f(1.0f,1.0f,1.0f,1.0f));
		messageLabels.add(label);
		shiftAllMessageseUp();
		
	}
	
	public void addMessage(String message, String username) {
		addMessage(username + ">" +message);
		
	}
	
	public void shiftAllMessageseUp() {
		for (Label label : messageLabels){
			label.setPosition(new Vector2f(label.getPosition().x, label.getPosition().y + 0.05f));
		}
	}
	
	public void render() { 
		typeWriter.update();
		ArrayList<Label> toBeRendered = new ArrayList<Label>();
		for (int i=0; i < MAX_MESSAGES && i < messageLabels.size(); i++){
			toBeRendered.add(messageLabels.get(messageLabels.size()-i-1));
		}

		labelRenderer.render(toBeRendered);
	}

	@Override
	public void typeWriterFinishedEditing(String text) {
		//this.addMessage(text, LocalPlayer.sharedLocalPlayer.getUsername());
		MultiplayerManager.getInstance().sendMessage(text);
	
	}

	
	
}
