package fonts;

import java.util.List;

import org.lwjgl.opengl.APPLEVertexArrayObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import renderEngine.Loader;
import toolbox.Debug;
import toolbox.Maths;

public class LabelRenderer {

	private FontShader shader;
	
	public LabelRenderer(Loader loader) {
//		float[] positions = {-1, 1, -1, -1, 1, 1, 1, -1};
//		mesh2d = loader.loadToVAO(positions, 2);
		shader = new FontShader();
	}
	
	public void render(List<Label> labels) {
		shader.start();
		for (Label label : labels) {
			
			try {
				GL30.glBindVertexArray(label.getLabelMesh().getVaoID());
			} catch (Exception e) {
				APPLEVertexArrayObject.glBindVertexArrayAPPLE(label.getLabelMesh().getVaoID());
			}
			
			GL20.glEnableVertexAttribArray(0);
			GL20.glEnableVertexAttribArray(1);
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glDisable(GL11.GL_DEPTH_TEST);

			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, label.getTexture());
			Matrix4f matrix = Maths.createTransformationMatrix(label.getPosition(), label.getSize());
			shader.loadTransformation(matrix);
			shader.loadFontColor(label.getColor());
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, label.getLabelMesh().getVertexCount());
			
		}
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		try {
			GL30.glBindVertexArray(0);
		} catch (Exception e) {
			APPLEVertexArrayObject.glBindVertexArrayAPPLE(0);
		}
		shader.stop();
	}

	public void cleanUp() {
		shader.cleanUp();
	}
}
