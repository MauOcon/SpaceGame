package shaders;

import org.lwjgl.util.vector.Matrix4f;

import entities.Camera;
import entities.Light;
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
	private int location_lightPosition;
	private int location_lightColor;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_useFakeLighting;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "textureCoords");
		super.bindAttribute(2, "normal");
		
	}

	/**
	 * Gets the locations of the uniform variables in the shader
	 */
	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniVarLocation("transformationMatrix");
		location_projectionMatrix = super.getUniVarLocation("projectionMatrix");
		location_viewMatrix = super.getUniVarLocation("viewMatrix");
		location_lightPosition = super.getUniVarLocation("lightPosition");
		location_lightColor = super.getUniVarLocation("lightColor");
		location_shineDamper = super.getUniVarLocation("shineDamper");
		location_reflectivity = super.getUniVarLocation("reflectivity");
		location_useFakeLighting = super.getUniVarLocation("useFakeLighting");
	}
	
	public void loadFakeLighting(boolean useFake){
		super.loadBoolean(location_useFakeLighting, useFake);
	}
	
	/**
	 * Loads up shine values to the shader
	 * 
	 * @param damper
	 * @param reflectivity
	 */
	public void loadShineVariables(float damper, float reflectivity){
		super.loadFloat(location_shineDamper, damper);
		super.loadFloat(location_reflectivity, reflectivity);
	}

	/**
	 * Loads up a transformation matrix to the uniform variable
	 * 
	 * @param matrix
	 */
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadLight(Light light){
		super.loadVector(location_lightPosition, light.getPosition());
		super.loadVector(location_lightColor, light.getColor());
	}
	
	/**
	 * Loads up the view matrix to the uniform variable
	 * 
	 * @param camera
	 */
	public void loadViewMatrix(Camera camera){
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		super.loadMatrix(location_viewMatrix, viewMatrix);
	}
	
	/**
	 * Loads up a projection matrix to the uniform variable
	 * 
	 * @param projection
	 */
	public void loadProjectionMatrix(Matrix4f projection){
		super.loadMatrix(location_projectionMatrix, projection);
	}

}
