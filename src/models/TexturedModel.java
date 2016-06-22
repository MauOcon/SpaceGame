package models;

import textures.ModelTexture;

/**
 * Contains both the RawModel and the Texture
 * 
 * @author mau
 *
 */
public class TexturedModel {
	
	private RawModel rawModel;
	private ModelTexture texture;
	
	
	public TexturedModel(RawModel rawModel, ModelTexture texture) {
		this.rawModel = rawModel;
		this.texture = texture;
	}


	public RawModel getRawModel() {
		return rawModel;
	}


	public ModelTexture getTexture() {
		return texture;
	}
	
	

}
