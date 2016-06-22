package shaders;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

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
	
	public ShaderProgram(String vertexFile, String fragmentFile){
		vertexShaderID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
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

	

