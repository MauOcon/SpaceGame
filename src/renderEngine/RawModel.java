package renderEngine;

/**
 * Represents a 3D model stored in memory
 * 
 * @author mau
 *
 */
public class RawModel {
	
	/**
	 * There are two things we need to know about a model when
	 * it's stored in memmory
	 */
	private int vaoID;			// Its ID of the model
	private int vertexCount;	// How many vertices are in the model
	
	/**
	 * 
	 * @param vaoID
	 * @param vertexCount
	 */
	public RawModel(int vaoID, int vertexCount){
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}

	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	

}
