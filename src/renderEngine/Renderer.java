package renderEngine;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import models.RawModel;
import models.TexturedModel;

/**
 * Renders the TexturedModel from the VAO
 * 
 * @author mau
 *
 */
public class Renderer {
	
	/**
	 * We'll call this method once every frame
	 */
	public void prepare(){
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glClearColor(1, 0, 0, 1);
		
	}
	
	/**
	 * This method will render the RawModel
	 * 	
	 * @param model
	 */
	public void render(TexturedModel texturedModel){
		RawModel model = texturedModel.getRawModel();
		// Binds the VAO
		GL30.glBindVertexArray(model.getVaoID());
		// Activate the attribute list in which our data is stored
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		// Tell OpenGL which texture we want to render
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getTexture().getID());
		// Render the model
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}

}
