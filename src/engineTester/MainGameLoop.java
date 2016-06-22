package engineTester;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.RawModel;
import renderEngine.Renderer;
import shaders.StaticShader;

public class MainGameLoop {
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		
		/********************* Defines the model ************************/		
		Loader loader = new Loader();		
		Renderer renderer = new Renderer();	
		
		StaticShader shader = new StaticShader();
		
		// Vertices of the model
		float[] vertices = {
				-0.5f, 0.5f, 0f,	// V0
				-0.5f, -0.5f, 0f,	// V1
				0.5f, -0.5f, 0f,	// V2
				0.5f, 0.5f, 0f		// V3
		};
		
		// Indices of the 2 triangles
		int[] indices = {
				0,1,3,	// Top left triangle (V0, V1, V3)
				3,1,2 // Bottom right triangle (V3, V1, V2)
		};
		
		RawModel model = loader.loadToVAO(vertices,indices);		
		/*************************************************************************/
		
		
		while(!Display.isCloseRequested()){
			renderer.prepare();
			// Game Logic
						
			
			// render	
			shader.start();
			renderer.render(model);
			shader.stop();
			
			// Update the display
			DisplayManager.updateDisplay();
			
		}
		
		shader.cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
	}

}
