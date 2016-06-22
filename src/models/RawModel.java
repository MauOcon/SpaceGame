package models;

/**
 * Represents a 3D model stored in memory by a
 * vaoID and its number of vertices
 * 
 * @author mau
 *
 */
public class RawModel {
	
	private int vaoID;			// Its ID of the model
	private int vertexCount;	// How many vertices are in the model
	
	/**
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
