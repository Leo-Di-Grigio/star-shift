package org.starshift;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.owlengine.OwlCycle;
import com.owlengine.input.UserInput;
import com.owlengine.scenes.SceneMng;

public class Cycle extends OwlCycle {
	
	private SceneMng scenes;
	
    private OrthographicCamera camera;
	
    private SpriteBatch batchScenes;
    private SpriteBatch batchUi;
    
    @Override
    public void setup() {
        scenes = engine.getSceneMng();
        
        setupEngine();
        setupGL();
        setupGDX();
        setupGame();
    }

	private void setupEngine() {
        Gdx.input.setInputProcessor(new UserInput(scenes));
    }
    
    private void setupGDX() {
        // batches
        batchScenes = new SpriteBatch();
        batchUi = new SpriteBatch();

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        
        camera = new OrthographicCamera(width, height);
    }
    
    private void setupGL() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }
    
    private void setupGame() {
        new GameAPI(scenes, camera);
        GameAPI.sceneDemo();
    }
    
    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    	Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        
        // update
        scenes.update(camera);
        camera.update();
        
        batchScenes.setProjectionMatrix(camera.combined);
        
        // render
        scenes.draw(batchScenes);
        scenes.postUpdate();
        scenes.drawUI(batchUi);
        scenes.drawHUD(batchUi);
    }
    
    @Override
    public void dispose() {
    	engine.dispose();
    }
}