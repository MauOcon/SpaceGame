package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Class to use and access shaders from java
 * <p>
 * Represents a generic shader program containing all the attributes
 * and methods that every shader program will have
 * 
 * @author mau
 *
 */
public abstract class ShaderProgram {
	
	private int programID;			
	private int vertexShaderID;
	private int fragmentShaderID;
	
	// to load a matrix into a uniform variable
	private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
	
	public ShaderProgram(String vertexFile, String fragmentFile){
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttributes();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		
		getAllUniformLocations();
	}
	
	/**
	 * All shader program classes will have a method that gets all the
	 * uniform locations
	 */
	protected abstract void getAllUniformLocations();
	
	/**
	 * Gets the location of the uniform variable in the shader code
	 * 
	 * @param uniVarName	Name of the uniform variable which location  
	 * 						will be returned
	 * @return
	 */
	protected int getUniVarLocation(String uniVarName){
		return GL20.glGetUniformLocation(programID, uniVarName);
	}
	
	/**
	 * Whenever we want to use the program we must
	 * start it
	 */
	public void start(){
		GL20.glUseProgram(programID);					
	}		
	
	/**
	 * Stops the program
	 */
	public void stop(){
		GL20.glUseProgram(0);
	}
	
	/**
	 * Deletes both shaders
	 */
	public void cleanUp(){
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	/**
	 * Links up the inputs to the shader programs
	 */
	protected abstract void bindAttributes();
	
	/**
	 * Method to binds an attribute to a variable in the
	 * shader
	 * 
	 * @param attribute		Number of attribute list in the VAO
	 * @param variableName	Variable name in shader code
	 */
	protected void bindAttribute(int attribute, String variableName){
		GL20.glBindAttribLocation(programID, attribute, variableName);		
	}
	
	/**
	 * Loads up a float value to the uniform variable
	 * located in location
	 * 
	 * @param location	Location of the uniform variable
	 * @param value		Variable to load to the uniform variable
	 */
	protected void loadFloat(int location, float value){
		GL20.glUniform1f(location, value);
	}
	
	/**
	 * Loads a vector to a uniform variable located in
	 * location in the shader code
	 * 
	 * @param location
	 * @param vector
	 */
	protected void loadVector(int location, Vector3f vector){
		GL20.glUniform3f(location, vector.x, vector.y, vector.z);
	}
	
	/**
	 * Loads a boolean value to a uniform variable located in
	 * location in the shader code
	 * 
	 * @param location
	 * @param value
	 */
	protected void loadBoolean(int location, boolean value){
		float toLoad = 0;
		if(value){
			toLoad = 1;
		}
		
		GL20.glUniform1f(location, toLoad);
	}
	
	/**
	 * Loads a matrix into a uniform variable located in 
	 * location in the shader code
	 * 
	 * @param location
	 * @param matrix
	 */
	protected void loadMatrix(int location, Matrix4f matrix){
		matrix.store(matrixBuffer);
		matrixBuffer.flip();
		GL20.glUniformMatrix4(location, false, matrixBuffer); 
	}
	
	private static int loadShader(String file, int type) {
		StringBuilder shaderSource = new StringBuilder();
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine())!=null){
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        }catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;
    }
		}

	

