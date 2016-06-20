package engineTester;

import org.lwjgl.opengl.Display;

import renderEngine.DisplayManager;

public class MainGameLoop {
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();
		
		while(!Display.isCloseRequested()){
			
			// Game Logic
			
			// render	
			
			// Update the display
			DisplayManager.updateDisplay();
			
		}
		
		DisplayManager.closeDisplay();
	}

}
