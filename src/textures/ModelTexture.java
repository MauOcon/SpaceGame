package textures;

/**
 * Represents a texture that we can use to 
 * texture our models
 * 
 * @author mau
 *
 */
public class ModelTexture {
	
	private int textureID;
	
	public ModelTexture(int id){
		this.textureID = id;
	}
	
	public int getID() {
		return this.textureID;
	}

}
