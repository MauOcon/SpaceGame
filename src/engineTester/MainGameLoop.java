package engineTester;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.Loader;
import renderEngine.MasterRenderer;
import renderEngine.OBJLoader;
import renderEngine.EntityRenderer;
import shaders.StaticShader;
import terrain.Terrain;
import textures.ModelTexture;

public class MainGameLoop {
	
	public static void main(String[] args) {
		
		DisplayManager.createDisplay();		
		
		/********************* Defines the model ************************/		
		Loader loader = new Loader();			
		
		RawModel model = OBJLoader.loadObjModel("tree", loader);		
		TexturedModel staticModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("tree"))); 
		
		TexturedModel grass = new TexturedModel(OBJLoader.loadObjModel("grassModel", loader),
				new ModelTexture(loader.loadTexture("grassTexture")));
		grass.getTexture().setHasTransparency(true);
		grass.getTexture().setUseFakeLighting(true);
		TexturedModel fern = new TexturedModel(OBJLoader.loadObjModel("fern", loader),
				new ModelTexture(loader.loadTexture("fern")));
		fern.getTexture().setHasTransparency(true);
		
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
		
		Entity entity = new Entity(staticModel, new Vector3f(0,0,-25), 0,0,0,1);
		Light light = new Light(new Vector3f(3000,2000, 2000), new Vector3f(1,1,1));
		
		Terrain terrain = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("grass"))); 
		Terrain terrain2 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("grass"))); 
		
		Camera camera = new Camera( new Vector3f(0, 1, 0));
		
		
		/*************************************************************************/
		
		MasterRenderer renderer = new MasterRenderer();
		while(!Display.isCloseRequested()){
			//entity.increaseRotation(0, 1, 0);
			camera.move();
			//entity.increaseRotation(0, 1, 0 );
			// Game Logic
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
