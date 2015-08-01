package fonts;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import models.RawModel;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

import renderEngine.Loader;
import toolbox.Debug;

public class Label {

	private static final String META_FILE = "src/fonts/fontMeta.txt";
	
	private String text;
	private Vector2f size;
	private Vector2f position;
	private Vector4f color;
	
	private RawModel labelMesh;
	private int texture;
	
	public Label(String text, Vector2f position, Vector2f scale, Vector4f color) {
		this.text = text;
		this.size = scale;
		this.position = position;
		this.color = color;
	
		try {
			labelMesh = createLabelMesh(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		texture = Loader.getInstance().loadTexture("fontMap2");
	}
	

	private RawModel createLabelMesh(String text) throws IOException {
		float[] positions = new float[text.length() * 8];
		float[] textureCoords = new float[text.length() *8];
		float currentOffsetX = 0.0f;
		int currentPosition = 0;
		
		for (int i = 0; i < text.length(); i++) {
			char character = text.charAt(i);
			//Debug.log("character value of "+character+": " + (int)character);
			int value = (int) character;
			int lineNumber = value - 32; // 32 is the value of 'space'
			FileInputStream fs = new FileInputStream(META_FILE);
			BufferedReader br = new BufferedReader(new InputStreamReader(fs));
			for(int j = 0; j < lineNumber; ++j) {
					br.readLine();
			}
			String line = br.readLine();
			//Debug.log(line);
			String[] array = line.split(",");
			float yOffset = Float.parseFloat(array[0]);
			float xSize = Float.parseFloat(array[1]);
			float ySize = Float.parseFloat(array[2]);
 
			br.close();
			
			float[] someTexCoords = getTextureCoordsFor(lineNumber);
			for (int j=0; j < someTexCoords.length; j++) {
				textureCoords[currentPosition + j] = someTexCoords[j]; 
			}
			
			positions[currentPosition  ] = currentOffsetX; //top left corner x
			positions[currentPosition+1] = ySize + yOffset; //top left corner y
			
			positions[currentPosition+2] = currentOffsetX; //bottom left corner x
			positions[currentPosition+3] = 0 + yOffset; //bottom left corner y
			
			positions[currentPosition+4] = currentOffsetX + xSize; //top right corner x
			positions[currentPosition+5] = ySize + yOffset; //top right corner y
			
			positions[currentPosition+6] = currentOffsetX + xSize; //bottom right corner x
			positions[currentPosition+7] = 0 + yOffset; //bottom right corner y
			
			currentPosition +=8;
			currentOffsetX += xSize - 0.1;
		}
		
		//Debug.log(positions);
		//Debug.log(textureCoords);
		
		return Loader.getInstance().loadToVAO(positions, textureCoords, 2);
	}


	private float[] getTextureCoordsFor(float myValue) {
		float[] array = new float[8];
		final float ROW_LENGTH = 19;
		final float SQUARE_SIZE = 25;
		
		final float WIDTH = 475;
		final float HEIGHT = 125;
		
		float x = 0, y = 0;
		
		float convertingX = myValue;
		
		while(convertingX > ROW_LENGTH - 1) {
			convertingX -= ROW_LENGTH;
		}
		x = convertingX * 22.8f;
		
		float convertingY = myValue;
		while(convertingY > ROW_LENGTH -1) {
			y++;
			convertingY -= ROW_LENGTH;
		}
		y *= SQUARE_SIZE;
		
			
		
		//top left
		array[0] = x/WIDTH;  
		array[1] = y/HEIGHT;
		//bottom left
		array[2] = x/WIDTH;  
		array[3] = (y + SQUARE_SIZE)/HEIGHT;
		//top right 
		array[4] = (x + SQUARE_SIZE)/WIDTH; 
		array[5] = y/HEIGHT;
		//bottom right
		array[6] = (x + SQUARE_SIZE)/WIDTH; 
		array[7] = (y + SQUARE_SIZE)/HEIGHT;
		
		return array;
	}
	
	/**
	 * @return the position
	 */
	public Vector2f getPosition() {
		return position;
	}

	public void setPosition(Vector2f pos) {
		this.position = pos;
	}

	/**
	 * @return the labelMesh
	 */
	public RawModel getLabelMesh() {
		return labelMesh;
	}


	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}


	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
		try {
			this.labelMesh = this.createLabelMesh(this.text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * @return the color
	 */
	public Vector4f getColor() {
		return color;
	}


	/**
	 * @param color the color to set
	 */
	public void setColor(Vector4f color) {
		this.color = color;
	}


	/**
	 * @return the size
	 */
	public Vector2f getSize() {
		return size;
	}


	/**
	 * @return the texture
	 */
	public int getTexture() {
		return texture;
	}


	public void deleteLastChar() {
		Debug.log("called");
		if (text.length() > 0){
			text = text.substring(0, text.length()-1);
			this.setText(text);
		}
	}

	
	
}

