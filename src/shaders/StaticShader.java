package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import toolbox.Maths;

public class StaticShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "src/shaders/vertexShader";
	private static final String FRAGMENT_FILE = "src/shaders/fragmentShader";
	
	/**
	 * To save the locations of the uniform variables
	 */
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		
	}

	/**
	 * 
	 */
	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniVarLocation("transformationMatrix");
		location_projectionMatrix = super.getUniVarLocation("projectionMatrix");
		location_viewMatrix = super.getUniVarLocation("viewMatrix");
		
	}

	/**
	 * Loads up a transformation matrix to the uniform variable
	 * 
	 * @param matrix
	 */
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}

}
