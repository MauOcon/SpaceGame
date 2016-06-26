package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector3f;

/**
 * Represents a virtual camera's position and orientation
 * 
 * @author mau
 *
 */
public class Camera {
	
	private Vector3f position = new Vector3f(0,0,0);
	
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera(){}
	
	/**
	 * Moves the camera
	 */
	public void move(){
		
		float paso = 0.5f;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			position.z -= paso;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			position.z += paso;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			position.x += paso;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			position.x -= paso;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)){
			position.y += paso;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){
			position.y -= paso;
		}
		/*if(Keyboard.isKeyDown(Keyboard.KEY_E)){
			roll -= paso;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
			roll += paso;
		}*/
	}
	
	
	public Vector3f getPosition() {
		return position;
	}
	public float getPitch() {
		return pitch;
	}
	public float getYaw() {
		return yaw;
	}
	public float getRoll() {
		return roll;
	}
	
	

}
