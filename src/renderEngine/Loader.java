package renderEngine;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import models.RawModel;
import objConverter.ModelData;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.APPLEVertexArrayObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import textures.TextureData;
import toolbox.Debug;

/**
 * Loader.java - load a models VBO to a VAO.
 * 
 * *Vertex Array Object - holds VBOs
 * *Vertex Buffer Object - holds data (float)
 * 
 * @author Ben
 * @version 14-JUL-2015
 */
public class Loader {
	
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();
	
	public static Loader sharedLoader;
	public static Loader getInstance(){
		if (sharedLoader == null) {
			sharedLoader = new Loader();
		}
		return sharedLoader;
	}
	
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals, int[] indices) {
		//1. Create new empty vao
		int vaoID = createVAO();
		// bind indices to a 
		bindIndicesBuffer(indices);
		//2. Store positional data the new vao at index 0
		storeDataInAttributeList(0,3, positions);
		storeDataInAttributeList(1,2, textureCoords);
		storeDataInAttributeList(2,3, normals);
		
		//3. unbind vao
		unbindVAO();
		
		return new RawModel(vaoID, indices.length, positions);
	}
	
	public RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		this.storeDataInAttributeList(0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length/dimensions, positions);
	}
	
	public RawModel loadToVAO(ModelData data) {
		return loadToVAO(data.getVertices(), data.getTextureCoords(), data.getNormals(), data.getIndices());
	}
	
	/**
	 * Loads a texture from file returning 
	 * int reference to texture
	 * textures always in res folder and of type PNG
	 * 
	 * @param fileName
	 * @return textureID
	 */
	public int loadTexture(String fileName) {
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName+".png"));
			GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		return textureID;
		
	}
	
	/**
	 * Delete all VAOs and VBOs on close of app for memory.
	 */
	public void cleanUp() {
		for (int vao:vaos) {
			try {
				GL30.glDeleteVertexArrays(vao);
			} catch (Exception e) {
				APPLEVertexArrayObject.glDeleteVertexArraysAPPLE(vao);
			}
		}
		for (int vbo:vbos)
			GL15.glDeleteBuffers(vbo);
		
		for (int texture:textures)
			GL11.glDeleteTextures(texture);
	}
	
	public int loadCubeMap(String[] textureFiles) {
		int texID = GL11.glGenTextures();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texID);
		
		for (int i=0;i<textureFiles.length;i++) {
			TextureData data = this.decodeTextureFile("res/" + textureFiles[i] +".png");
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.getWidth(), data.getHeight(),
					0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		textures.add(texID);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
		return texID;
	}	
	
	private TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			Debug.log("Tried to load box texture " + fileName + ", didn't work");
		}
		return new TextureData(buffer, width, height);
	}
	
	/**
	 * Create a new VAO with a unique ID.
	 * 
	 * @return the new VAO's ID (int)
	 */
	private int createVAO() {
		// Create vao
		int vaoID;
		try {
			vaoID = GL30.glGenVertexArrays();
		} catch (Exception e) {
			//e.printStackTrace();
			vaoID = APPLEVertexArrayObject.glGenVertexArraysAPPLE();
		}
		// save vao
		vaos.add(vaoID);
		// bind the vao
		try {
			GL30.glBindVertexArray(vaoID);
		} catch (Exception e) {
			//e.printStackTrace();
			APPLEVertexArrayObject.glBindVertexArrayAPPLE(vaoID);
		}
		return vaoID;
	}
	
	/**
	 * Store data into a VBO so that it can be placed in a VAO
	 * @param attributeNumber
	 * @param data
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}
	
	private void unbindVAO() {
		try {
			GL30.glBindVertexArray(0);
		} catch (Exception e) {
			APPLEVertexArrayObject.glBindVertexArrayAPPLE(0);
		}
	}
	
	/**
	 * Loads indices into a vbo 
	 * 
	 * @param indices
	 */
	private void bindIndicesBuffer(int[] indices) {
		// Create vbo
		int vboID = GL15.glGenBuffers();
		// Save vbo
		vbos.add(vboID);
		// Bind buffer to vbo
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		// Store indices in buffer
		IntBuffer buffer = storeDataInIntBuffer(indices);
		// Store indices data in vbo.
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}
	
	/**
	 * Helper method to store data in an int
	 * buffer
	 * 
	 * @param data int[]
	 * @return IntBuffer containing data
	 */
	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}
	
	/**
	 * Helper method to store data in a FloatBuffer.
	 * 
	 * @param data
	 * @return FloatBuffer containing data.
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip(); // Finish writing
		return buffer;
	}
}
