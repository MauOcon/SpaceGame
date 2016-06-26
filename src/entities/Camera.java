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
	
	private Vector3f position;
	
	private float pitch;
	private float yaw;
	private float roll;
	
	public Camera(){
		this.position = new Vector3f(0,0,0);
	}
	
	public Camera(Vector3f position){
		this.position = position;
	}
	
	/**
	 * Moves the camera
	 */
	public void move(){
		
		float paso = 0.2f;
		
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
		
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			yaw -= 2*paso;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			yaw += 2*paso;
		}
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
