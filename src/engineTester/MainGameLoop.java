package engineTester;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import entities.Player;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.ModelData;
import renderEngine.OBJFileLoader;
import terrain.Terrain;
import textures.ModelTexture;
import textures.TerrainTexture;
import textures.TerrainTexturePack;

public class MainGameLoop {
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();		
		
		/********************* Defines the model ************************/		
		Loader loader = new Loader();	
		
		ModelData treeData = OBJFileLoader.loadOBJ("tree"); 	
		ModelData grassModelData = OBJFileLoader.loadOBJ("grassModel"); 
		ModelData fernData = OBJFileLoader.loadOBJ("fern"); 
		ModelData bunnyData = OBJFileLoader.loadOBJ("stanfordBunny"); 
		//ModelData bunnyData = OBJFileLoader.loadOBJ("bunny"); 
		RawModel tree = loader.loadToVAO(treeData.getVertices(),
				treeData.getTextureCoords(),
				treeData.getNormals(), 
				treeData.getIndices());		
		
		TexturedModel staticModel = new TexturedModel(tree, new ModelTexture(loader.loadTexture("tree")));
		
		TexturedModel grass = new TexturedModel(loader.loadToVAO(
				grassModelData.getVertices(), 
				grassModelData.getTextureCoords(), 
				grassModelData.getNormals(), 
				grassModelData.getIndices()),
				new ModelTexture(loader.loadTexture("grassTexture"))); 
		
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		
		
		TexturedModel fern = new TexturedModel(loader.loadToVAO(
				fernData.getVertices(), 
				fernData.getTextureCoords(), 
				fernData.getNormals(), 
				fernData.getIndices()),
				new ModelTexture(loader.loadTexture("fern"))); 
		
		TexturedModel bunny = new TexturedModel(loader.loadToVAO(
				bunnyData.getVertices(), 
				bunnyData.getTextureCoords(), 
				bunnyData.getNormals(), 
				bunnyData.getIndices()),
				new ModelTexture(loader.loadTexture("white"))); 
		
		/*TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
				new ModelTexture(loader.loadTexture("fern")));*/
		fern.getTexture().setHasTransparency(true);
		fern.getTexture().setUseFakeLighting(true);
		
		List<Entity> entities = new ArrayList<>();
		Random random = new Random();
		
		for(int i = 0; i<500; i++){
			entities.add(new Entity(staticModel, new Vector3f(random.nextFloat() * 800 - 400, 0,
					random.nextFloat() * -600), 0, 0, 0, 3));
			entities.add(new Entity(grass, new Vector3f(random.nextFloat() * 800 - 400, 0,
					random.nextFloat() * -600), 0, 0, 0, 1));
			entities.add(new Entity(fern, new Vector3f(random.nextFloat() * 800 - 400, 0,
					random.nextFloat() * -600), 0, 0, 0, 0.6f));
		}
		
		//Entity entity = new Entity(staticModel, new Vector3f(0,0,-25), 0,0,0,1);
		Light light = new Light(new Vector3f(3000,2000, 2000), new Vector3f(1,1,1));
		
		TerrainTexture backgroundTexture = new TerrainTexture(loader.loadTexture("grassy"));
		TerrainTexture rTexture = new TerrainTexture(loader.loadTexture("dirt"));
		TerrainTexture gTexture = new TerrainTexture(loader.loadTexture("pinkFlowers"));
		TerrainTexture bTexture = new TerrainTexture(loader.loadTexture("path"));
		
		TerrainTexturePack texturePack = new TerrainTexturePack(backgroundTexture, rTexture, 
				gTexture, bTexture);
		TerrainTexture blendMap = new TerrainTexture(loader.loadTexture("blendMap"));
		
		Terrain terrain = new Terrain(0, -1, loader, texturePack, blendMap);
		Terrain terrain2 = new Terrain(-1, -1, loader, texturePack, blendMap);
		
		
		
		Player player  = new Player(bunny, new Vector3f(0, 0, 0), 0, 0, 0, 1);
		player.increaseRotation(0, 180, 0);
		Camera camera = new Camera(player);
		
		
		/*************************************************************************/
		
		
		MasterRenderer renderer = new MasterRenderer();
		while(!Display.isCloseRequested()){
			//entity.increaseRotation(0, 1, 0);
			camera.move();
			player.move();
			//entity.increaseRotation(0, 1, 0 );
			// Game Logic
			renderer.processEntity(player);
			renderer.processTerrain(terrain);	
			renderer.processTerrain(terrain2);
			for(Entity entitty:entities){
                renderer.processEntity(entitty);
            }
			
			// render	
			renderer.render(light, camera);
			// Update the display
			DisplayManager.updateDisplay();
			
		}
		
		renderer.cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
	}

}
