package renderEngine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import models.RawModel;

/**
 * 
 * This class is going to deal with loading 3D models
 * into memory by storing positional data about the model 
 * and the texture coords into the  VAO 
 * 
 * @author mau
 *
 */
	
public class Loader {
	
	/**
	 * Memmory Management. At the end  when we close down the game
	 * we are going to delete all the VBO, VAO and textures loaded.
	 * With this we can keep track of them 
	 * 
	 */
	private List<Integer> vaos = new ArrayList<>();
	private List<Integer> vbos = new ArrayList<>();
	private List<Integer> textures = new ArrayList<>();
	
	/**
	 * Take in positions of the model's vertices and the indices and loads 
	 * this data into the VAO and then return information about the 
	 * VAO as a Raw Model Object
	 * <p>
	 * To do it we first must create an empty VAO, bind it in order to use it,
	 * store the vertices data into it, unbind it and create a raw model with
	 * the ID of the VAO and the number of vertices and return the Raw Model
	 * 
	 * @param positions	Float array of positions of the vertex of the model
	 * @param textureCoords Float array of positions of the texture in the model
	 * @param indices	Index buffer array
	 * @return Raw model object
	 */
	public RawModel loadToVAO(float[] positions, float[] textureCoords, float[] normals,  int[] indices){
		// Create an empty VAO, bind it and store it's ID
		int vaoID = createVAO();
		// Bind the Indices Buffer to the VAO
		bindIndicesBuffer(indices);
		// Store the positional data into one of the attribute lists
		// of the VAO
		storeDataInAttributeList(0, 3, positions);
		// Store the texture coordinates data into one of the 
		//attribute lists of the VAO
		storeDataInAttributeList(1, 2, textureCoords);
		storeDataInAttributeList(2, 3, normals);
		//  When we finish using it we VAO we unbind it
		unbindVAO();		
		// We create a new RawModel
		// positions.length/3 because there are 3 coors by position (x,y,z)
		return new RawModel(vaoID, indices.length);
	}
	
	/**
	 * Loads up a texture into OpenGL
	 * 
	 * @param filename	Filename of the texture
	 * @return	ID of the texture
	 */
	public int loadTexture(String fileName){
		Texture texture = null;
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream("res/" + fileName + ".png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		int textureID = texture.getTextureID();
		textures.add(textureID);
		
		return textureID;
	}
	
	/**
	 * When we close down the game we can call this method that deletes
	 * all of the VBOs, VAOs ant textures in the lists
	 *  
	 */
	public void cleanUP(){
		for(int vao:vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo:vbos){
			GL15.glDeleteBuffers(vbo);
		}
		for(int texture:textures){
			GL11.glDeleteTextures(texture);
		}
	}
	
	/**
	 * Creates a new empty VAO and binds it 
	 * 
	 * @return VAO ID
	 */
	private int createVAO(){
		// Create an empty VAO andr return it's id
		int vaoID = GL30.glGenVertexArrays();
		// Create track of the VAOs to delete them later
		vaos.add(vaoID);
		// Activate the VAO by binding it
		GL30.glBindVertexArray(vaoID);
		
		return vaoID;
	}
	
	/**
	 * Stores the vertex data into one of the attributes lists of the VAO
	 * as a VBO
	 * 
	 * @param attributeNumber in which we want to store the data
	 * @param coordinateSize How big each coordinate is
	 * @param Array of data
	 */
	private void storeDataInAttributeList(int attributeNumber, int coordinateSize, float[] data){
		// We create an empty VBO
		int vboID = GL15.glGenBuffers();
		// Create track of the VBO to delete it later
		vbos.add(vboID);
		// We bind the vbo (GL_ARRAY_BUFFER) in order to use it later
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		// We convert the vertex data into a float buffer
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		// Once we've got a float buffer with our data in it we can store it
		// into the VBO
		// Type of VBO, data, what the data is going to be used for static or is going to be edited
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		/* Put that VBO into the VAO */
		GL20.glVertexAttribPointer(attributeNumber, coordinateSize, GL11.GL_FLOAT, false, 0, 0);
		/* 0: we unBind the current VBO*/
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		
	}
	
	/**
	 * When we finish using a VAO you have to unBind it
	 */
	private void unbindVAO(){
		
		GL30.glBindVertexArray(0);
		
	}
	
	
	/**
	 * 
	 * Load up the indices buffer and bind it
	 * 
	 * @param indices
	 */
	private void bindIndicesBuffer(int[] indices){
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		// Bind the buffer  to use as the indices buffer
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);		
		IntBuffer buffer = storeDataInIntBuffer(indices);
		// Store indices into the VBO
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		// Unbind indices buffer
		//GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
	
	/**
	 * Stores the indices into an int buffer and returns it
	 * 
	 * @param data
	 * @return
	 */
	private IntBuffer storeDataInIntBuffer(int[] data){
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		
		return buffer;
	}
	
	
	
	/**
	 * Stores thata into a VBO as a float buffer
	 * 
	 * @param data
	 * @return
	 */
	private FloatBuffer storeDataInFloatBuffer(float[] data){
		// We create an empty float buffer
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		// We need to prepare the buffer to be read from because it's 
		// expecting to be written to
		buffer.flip();
		
		return buffer;
		
	}

}
