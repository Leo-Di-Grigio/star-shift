package org.starshift;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.owlengine.input.UserInput;

public class Cursor {

	private OrthographicCamera camera;
    private Vector3 pointVector;
    private Vector3 unprojectVector;

    private float x;
    private float y;
	private float dragX;
	private float dragY;
    
    public Cursor(OrthographicCamera camera) {
    	this.camera = camera;
    	pointVector = new Vector3();
        unprojectVector = new Vector3();
    }

    public void update() {    	
        pointVector.set(UserInput.mouseX(), Gdx.graphics.getHeight() - UserInput.mouseY(), 0.0f);
        unprojectVector = camera.unproject(pointVector);
        dragX = unprojectVector.x;
        dragY = unprojectVector.y;
        x = unprojectVector.x;
        y = unprojectVector.y;
    }
    
    public void updateDrag(){
        pointVector.set(UserInput.dragX(), UserInput.dragY(), 0.0f);
        unprojectVector = camera.unproject(pointVector);
        dragX = unprojectVector.x;
        dragY = unprojectVector.y;
        x = unprojectVector.x;
        y = unprojectVector.y;
    }
    
    public float x(){
    	return x;
    }
    
    public float y(){
    	return y;
    }
    
    public float dragX(){
    	return dragX;
    }
    
    public float dragY(){
    	return dragY;
    }

	public void draw(ShapeRenderer render) {
		render.setColor(1.0f, 0.0f, 0.0f, 1.0f);
		render.circle(x, y, 5.0f);
	}
}