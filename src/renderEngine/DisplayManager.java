package renderEngine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;

/**
 * All about managing the display: create, update and close
 * 
 * @author mau
 *
 */
public class DisplayManager {
	
	/**
	 * Variables that determine the size and fps of the display to create
	 */
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	private static final int FPS_CAP = 120;
	
	/**
	 * 
	 */
	public static void createDisplay(){
		
		ContextAttribs attribs = new ContextAttribs(3,2);
		attribs.withForwardCompatible(true);
		attribs.withProfileCore(true);
		
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.create(new PixelFormat(), attribs);
			Display.setTitle("Game Engine");
		} catch (LWJGLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		GL11.glViewport(0, 0, WIDTH, HEIGHT);
		
	}
	
	public static void updateDisplay(){
		
		/**
		 * Sinchronizes the games run at steady fps
		 */
		Display.sync(FPS_CAP);
		Display.update();
		
	}
	
	
	public static void closeDisplay(){
		
		Display.destroy();
		
	}

}