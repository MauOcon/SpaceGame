package renderEngine;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

/**
 * 
 * This class is going to deal with loading 3D models
 * into memory by storing positional data about the model 
 * in a VAO
 * 
 * @author mau
 *
 */
public class Loader {
	
	/**
	 * Memmory Management. At the end  when we close down the game
	 * we are going to delete all the VBO and VAO created.
	 * With this we can keep track of them 
	 * 
	 */
	private List<Integer> vaos = new ArrayList<>();
	private List<Integer> vbos = new ArrayList<>();
	
	/**
	 * Take in positions of the model's vertices, loads this data into
	 * the VAO and then return information about the VAO as a Raw Model
	 * Object
	 * 
	 * @param Array of positions 
	 * @return Raw model object
	 */
	public RawModel loadToVAO(float[] positions){
		// Create an empty VAO and store it's ID
		int vaoID = createVAO();
		// Store the positional data into one of the attribute lists
		// of the VAO
		storeDataInAttributeList(0, positions);
		//  When we finish using it we VAO we unbind it
		unbindVAO();
		
		// positions.length/3 because there are 3 coors by position (x,y,z)
		return new RawModel(vaoID, positions.length/3);
	}
	
	/**
	 * When we close down the game we can call this method that deletes
	 * all of the VBOs and VAOs in the lists
	 *  
	 */
	public void cleanUP(){
		for(int vao:vaos){
			GL30.glDeleteVertexArrays(vao);
		}
		for(int vbo:vbos){
			GL15.glDeleteBuffers(vbo);
		}
	}
	
	/**
	 * Creates a new empty VAO
	 * 
	 * @return VAO ID
	 */
	private int createVAO(){
		// Create an empty VAO andr return it's id
		int vaoID = GL30.glGenVertexArrays();
		// Create track of the VAOs
		vaos.add(vaoID);
		// Activate the VAO by binding it
		GL30.glBindVertexArray(vaoID);
		
		return vaoID;
	}
	
	/**
	 * Stores the data into one of the attributes lists of the VAO
	 * as a VBO
	 * 
	 * @param attributeNumber in which we want to store the data
	 * @param Array of data
	 */
	private void storeDataInAttributeList(int attributeNumber, float[] data){
		// We create an empty VBO
		int vboID = GL15.glGenBuffers();
		// Create track of the VBOs
		vbos.add(vboID);
		// We bind the buffer in order to use it
		// GL_ARRAY_BUFFER is the type of vbo
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		// We convert the data into a float buffer
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		// Once we've got a float buffer with our data in it we can store it
		// into the VBO
		// Type of VBO, data, what the data is going to be used for static or is going to be edited
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		/* Put that VBO into the VAO */
		GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
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
