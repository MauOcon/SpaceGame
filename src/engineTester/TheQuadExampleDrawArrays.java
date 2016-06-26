package engineTester;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.util.glu.GLU;
 
/**
 * Simple example of how to render a square using
 * just vertex
 * 
 * @author mau
 *
 */
public class TheQuadExampleDrawArrays {
    // Entry point for the application
    public static void main(String[] args) {
        new TheQuadExampleDrawArrays();
    }
     
    // Setup variables
    private final String WINDOW_TITLE = "The Quad: glDrawArrays";
    private final int WIDTH = 720;
    private final int HEIGHT = 1280;
    // Quad variables
    private int vaoId = 0;
    private int vboId = 0;
    private int vertexCount = 0;
     
    // Shader variables
    private int programID;			
	private int vertexShaderID;
	private int fragmentShaderID;
    
    public TheQuadExampleDrawArrays() {
        // Initialize OpenGL (Display)
        this.setupOpenGL();
         
        this.setupQuad();
        
        this.setupShader();
         
        while (!Display.isCloseRequested()) {
            // Do a single loop (logic/render)
        	GL20.glUseProgram(programID);	
            this.loopCycle();
            GL20.glUseProgram(0);
             
            // Force a maximum FPS of about 60
            Display.sync(60);
            // Let the CPU synchronize with the GPU if GPU is tagging behind
            Display.update();
        }
         
        GL20.glUseProgram(0);
        GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
        // Destroy OpenGL (Display)
        this.destroyOpenGL();
    }
     
    private void setupShader() {
    	
    	String vertexFile = "#version 330 core\n "
    			+ "in vec3 position;"
    			+ "out vec3 colour;"
    			+ "void main(void){"
    			+ "	gl_Position =  vec4(position, 1.0);"
    			+ "	colour = vec3(position.x+0.5,1.0,position.y+0.5);"
    			+ "}";
    	String fragmentFile = "#version 330 core\n"
    			+ "in vec3 colour;"
    			+ "out vec4 out_Color;"
    			+ "uniform sampler2D textureSampler;"    			
    			+ "void main(void){"
    			+ "	out_Color = vec4(colour,1.0);"
    			+ "}";
    	
    	vertexShaderID = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
    	fragmentShaderID = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
    	GL20.glShaderSource(vertexShaderID, vertexFile);
    	GL20.glCompileShader(vertexShaderID);
        if(GL20.glGetShaderi(vertexShaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(vertexShaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        
        GL20.glShaderSource(fragmentShaderID, fragmentFile);
    	GL20.glCompileShader(fragmentShaderID);
        if(GL20.glGetShaderi(fragmentShaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(fragmentShaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
		
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		GL20.glBindAttribLocation(programID, 0, "position");	// Attribute list 0 go to position in vertexShader
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		
	}

	public void setupOpenGL() {
        // Setup an OpenGL context with API version 3.2
        try {
            PixelFormat pixelFormat = new PixelFormat();
            ContextAttribs contextAtrributes = new ContextAttribs(3, 2)
                .withForwardCompatible(true)
                .withProfileCore(true);
             
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setTitle(WINDOW_TITLE);
            Display.create(pixelFormat, contextAtrributes);
             
            GL11.glViewport(0, 0, WIDTH, HEIGHT);
        } catch (LWJGLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
         
        // Setup an XNA like background color
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
         
        // Map the internal OpenGL coordinate system to the entire screen
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
         
        this.exitOnGLError("Error in setupOpenGL");
    }
     
    public void setupQuad() {       
        // OpenGL expects vertices to be defined counter clockwise by default
        float[] vertices = {
                // Left bottom triangle
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                // Right top triangle
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f,
                -0.5f, 0.5f, 0f
        };
        // Sending data to OpenGL requires the usage of (flipped) byte buffers
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices);
        verticesBuffer.flip();
         
        vertexCount = 6;
         
        // Create a new Vertex Array Object in memory and select it (bind)
        // A VAO can have up to 16 attributes (VBO's) assigned to it by default
        vaoId = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vaoId);
         
        // Create a new Vertex Buffer Object in memory and select it (bind)
        // A VBO is a collection of Vectors which in this case resemble the location of each vertex.
        vboId = GL15.glGenBuffers();
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, verticesBuffer, GL15.GL_STATIC_DRAW);
        // Put the VBO in the attributes list at index 0
        GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, 0, 0);
        // Deselect (bind to 0) the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
         
        // Deselect (bind to 0) the VAO
        GL30.glBindVertexArray(0);
         
        this.exitOnGLError("Error in setupQuad");
    }
     
    public void loopCycle() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
         
        // Bind to the VAO that has all the information about the quad vertices
        GL30.glBindVertexArray(vaoId);
        GL20.glEnableVertexAttribArray(0);
         
        // Draw the vertices
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);
         
        // Put everything back to default (deselect)
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
         
        this.exitOnGLError("Error in loopCycle");
    }
     
    public void destroyOpenGL() {       
        // Disable the VBO index from the VAO attributes list
        GL20.glDisableVertexAttribArray(0);
         
        // Delete the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
        GL15.glDeleteBuffers(vboId);
         
        // Delete the VAO
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoId);
         
        Display.destroy();
    }
     
    public void exitOnGLError(String errorMessage) {
        int errorValue = GL11.glGetError();
         
        if (errorValue != GL11.GL_NO_ERROR) {
            String errorString = GLU.gluErrorString(errorValue);
            System.err.println("ERROR - " + errorMessage + ": " + errorString);
             
            if (Display.isCreated()) Display.destroy();
            System.exit(-1);
        }
    }
}
