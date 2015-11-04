package fonts;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import shaders.ShaderProgram;

public class FontShader extends ShaderProgram{

	private static final String VERTEX_FILE = "src/fonts/fontVertexShader.txt";
	private static final String FRAGMENT_FILE = "src/fonts/fontFragmentShader.txt";
	
	private int location_transformationMatrix;
	private int location_fontColor;

	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public void loadTransformation(Matrix4f matrix){
		super.loadMatrix(location_transformationMatrix, matrix);
	}

	public void loadFontColor(Vector4f color){
		super.loadVector(location_fontColor, color);
	}
	
	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_fontColor = super.getUniformLocation("fontColor");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
	}
	

}
