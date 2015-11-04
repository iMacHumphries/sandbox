package guis;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JTextField;

import org.lwjgl.input.Keyboard;

import entities.MoveController;
import fonts.Label;
import renderEngine.DisplayManager;
import toolbox.Debug;

public class TypeWriter{

	private static final int DELETE_KEY = 127;
	private static final int SHIFT_KEY = 0;
	private static final int ENTER_KEY = 13;
	
	private String text;
	private boolean isListening;
	private Label label;
	private TypeWriterDelegate delegate;
	private float curTime;
	private boolean isBlinking;
	
	public TypeWriter(Label label){ 
		this.label = label;
		isListening = false;	
	}
	
	public interface TypeWriterDelegate{
		void typeWriterFinishedEditing(String text);
	}
	public void setDelegate(TypeWriterDelegate delegate){
		this.delegate = delegate;
	}
	
	public void update() {
		
		
		while(Keyboard.next()) {
			if (Keyboard.getEventKey() == Keyboard.KEY_RETURN) {
			    if (Keyboard.getEventKeyState()) {
			  
			    }
			    else {
			    	toggleIsListening();
			    }
			    
			}
			
			if (isListening) {
				
				if (Keyboard.getEventKey() != -1) {
					 if (Keyboard.getEventKeyState()) {
				    	updateText(Keyboard.getEventCharacter());
				    }
				}
			}
		}
		if (isListening)
			blink();
		
	}
	
	private void blink() {
		curTime += DisplayManager.getDelta();
		
		if (curTime > 1){
			Debug.log("second?");
			//toggleLine();
			curTime =0;
		}
	}
	
	private void toggleLine(){
		isBlinking = !isBlinking;
		if (isBlinking) {
			label.addText("-");
		}
		else {
			label.removeChar();
		}
		
	}

	private void toggleIsListening() {
		isListening = !isListening;
    	if (isListening) {
    		MoveController.getInstance().lock();
    	} else {
    		MoveController.getInstance().unlock();
    		if (delegate != null) {
    			delegate.typeWriterFinishedEditing(getText());
    			label.setText("");
    		}
    	}
	}
	

	private void updateText(char c){
		//Debug.log(c);
		if(c == DELETE_KEY){
			delete();
		} else if(c == SHIFT_KEY || c == ENTER_KEY) {
			// do nothing
		}
		else {
			label.setText(label.getText() + c);
			text = label.getText();
		}
	}
	
	private void delete() {
		label.deleteLastChar();
	}
	
	public String getText() {
		return text;
	}

	public boolean isListening() {
		return isListening;
	}

	public void setListening(boolean isListening) {
		this.isListening = isListening;
	}
	
}


