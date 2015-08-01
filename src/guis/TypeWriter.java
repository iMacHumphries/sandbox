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

import fonts.Label;
import toolbox.Debug;

public class TypeWriter{

	private String text;
	private boolean isListening;
	private Label label;
	private TypeWriterDelegate delegate;
	
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
			    	isListening = !isListening;
			    	Debug.log(isListening);
			    	if (isListening == false) {
			    		if (delegate != null) {
			    			delegate.typeWriterFinishedEditing(getText());
			    			label.setText("");
			    		}
			    	}
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
		
		
	}
	
	private static final int DELETE_KEY = 127;
	private static final int SHIFT_KEY = 0;
	private static final int ENTER_KEY = 13;
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


