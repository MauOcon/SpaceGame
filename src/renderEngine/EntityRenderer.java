package renderEngine;

import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import shaders.StaticShader;
import textures.ModelTexture;
import toolbox.Maths;

/**
 * Renders an entity
 * 
 * @author mau
 *
 */
public class EntityRenderer {

	private StaticShader shader;

	/**
	 * Constructor for the renderer class that creates the projection matrix and
	 * loads the projection matrix to the shader
	 * 
	 * @param shader
	 */
	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}


	/**
	 * Takes a hash map of textured models and list of entities
	 * 
	 * @param entities
	 */
	public void render(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			// get all the entities that use this texture model
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				// Render the model
				GL11.glDrawElements(GL11.GL_TRIANGLES,
						model.getRawModel().getVertexCount(),
						GL11.GL_UNSIGNED_INT, 0);

			}
			unbindTexturedModel();

		}
	}

	/**
	 * 
	 * 
	 * @param model
	 */
	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		// Binds the VAO
		GL30.glBindVertexArray(rawModel.getVaoID());
		// Activate the attribute list in which our data is stored
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		ModelTexture texture = model.getTexture();
		shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
		// Tell OpenGL which texture we want to render
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getID());

	}

	private void unbindTexturedModel() {
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		// We create a transformation matrix from the entity
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
				entity.getRotY(), entity.getRotZ(), entity.getScale());
		// Load the transformation matrix to the sahder
		shader.loadTransformationMatrix(transformationMatrix);
	}

	

	

}
