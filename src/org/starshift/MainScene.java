package org.starshift;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.owlengine.input.UserInput;
import com.owlengine.interfaces.Event;
import com.owlengine.interfaces.Script;
import com.owlengine.resources.Assets;
import com.owlengine.scenes.Scene;
import com.owlengine.tools.Tools;

public class MainScene extends Scene {

	private static final String UI_JSON_DATA = "ui/ui.json";
	private static final String UI_BUTTON_ADD_STAR = "button_add_star";
	private static final String UI_BUTTON_MOVEMENT = "button_movement";
	private static final String UI_BUTTON_GRID_ON = "button_grid_on";
	private static final String UI_BUTTON_GRID_OFF = "button_grid_off";
	private static final String UI_BUTTON_REAL_POS_ON = "button_real_pos_on";
	private static final String UI_BUTTON_REAL_POS_OFF = "button_real_pos_off";
	
	private static final String FONT_PATH = "ui/font.ttf";

	private static final int INPUT_TYPE_ADD_STAR = 0;
	private static final int INPUT_TYPE_MOVEMENT = 1;
	
	private static final int STAR_POS_HASH_NULL = Integer.MIN_VALUE;
	
	private static final float CAMERA_SPEED = 3.0f;
	private static final float ZOOM_SPEED = 0.05f;
	private static final float ZOOM_MAX = 2.0f;
	
	// misc
	private Cursor cursor;
	private Grid grid;
	private ShapeRenderer render;
	private BitmapFont font;
	
	// data
	private Ship ship;
	private HashMap<Integer, Star> stars;
	private HashMap<Integer, Integer> starsPos;
	
	// flags
	private int inputType = INPUT_TYPE_ADD_STAR;
	private int addStarId = STAR_POS_HASH_NULL;
	private boolean flagSetupStar;
	private boolean flagMovementMode;
	private boolean flagDrawGrid;
	private boolean flagDrawStarRealPos;

	public MainScene(OrthographicCamera camera) {
		setUI(UI_JSON_DATA);
		setupUi();
		
        cursor = new Cursor(camera);
        grid = new Grid();
        render = new ShapeRenderer();
        
        ship = new Ship();
        stars = new HashMap<Integer, Star>();
        starsPos = new HashMap<Integer, Integer>();
        
        flagDrawGrid = true;
        flagDrawStarRealPos = false;
        
        font = Assets.getFont(FONT_PATH);
	}
	
	private void setupUi() {
		getUI().getWidget(UI_BUTTON_MOVEMENT).setScriptOnAction(new Script() {
			@Override
			public void execute() {
				inputType = INPUT_TYPE_MOVEMENT;
				getUI().getWidget(UI_BUTTON_MOVEMENT).setVisibible(false);
				getUI().getWidget(UI_BUTTON_ADD_STAR).setVisibible(true);
			}
		});
		
		getUI().getWidget(UI_BUTTON_ADD_STAR).setScriptOnAction(new Script() {
			@Override
			public void execute() {
				flagMovementMode = false;
				inputType = INPUT_TYPE_ADD_STAR;
				getUI().getWidget(UI_BUTTON_ADD_STAR).setVisibible(false);
				getUI().getWidget(UI_BUTTON_MOVEMENT).setVisibible(true);
			}
		});
		
		getUI().getWidget(UI_BUTTON_GRID_ON).setScriptOnAction(new Script() {
			@Override
			public void execute() {
				flagDrawGrid = false;
				getUI().getWidget(UI_BUTTON_GRID_ON).setVisibible(false);
				getUI().getWidget(UI_BUTTON_GRID_OFF).setVisibible(true);
			}
		});
		
		getUI().getWidget(UI_BUTTON_GRID_OFF).setScriptOnAction(new Script() {
			@Override
			public void execute() {
				flagDrawGrid = true;
				getUI().getWidget(UI_BUTTON_GRID_ON).setVisibible(true);
				getUI().getWidget(UI_BUTTON_GRID_OFF).setVisibible(false);
			}
		});
		
		getUI().getWidget(UI_BUTTON_REAL_POS_ON).setScriptOnAction(new Script() {
			@Override
			public void execute() {
				flagDrawStarRealPos = false;
				getUI().getWidget(UI_BUTTON_REAL_POS_ON).setVisibible(false);
				getUI().getWidget(UI_BUTTON_REAL_POS_OFF).setVisibible(true);
			}
		});
		
		getUI().getWidget(UI_BUTTON_REAL_POS_OFF).setScriptOnAction(new Script() {
			@Override
			public void execute() {
				flagDrawStarRealPos = true;
				getUI().getWidget(UI_BUTTON_REAL_POS_ON).setVisibible(true);
				getUI().getWidget(UI_BUTTON_REAL_POS_OFF).setVisibible(false);
			}
		});
	}

	@Override
	protected void update(OrthographicCamera camera) {
		if(flagMovementMode){
			movementUpdate();
		}
		
		drawShapes(camera);
		
		if(UserInput.key(Keys.W)){
			camera.translate(0.0f, CAMERA_SPEED*camera.zoom);
		}
		else if(UserInput.key(Keys.S)){
			camera.translate(0.0f, -CAMERA_SPEED*camera.zoom);
		}
		
		if(UserInput.key(Keys.A)){
			camera.translate(-CAMERA_SPEED*camera.zoom, 0.0f);
		}
		else if(UserInput.key(Keys.D)){
			camera.translate(CAMERA_SPEED*camera.zoom, 0.0f);
		}
		camera.update();		
	}
	
	@Override
	protected void drawHUD(SpriteBatch batch) {
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, 20);
		
		if(inputType == INPUT_TYPE_ADD_STAR){
			font.draw(batch, "Input Mode: star edit", 5, 35);
			font.draw(batch, "Mouse left on space: add star", 5, 60);
			font.draw(batch, "Mouse right on star: remove star", 5, 75);
		}
		else{
			font.draw(batch, "Input Mode: movement", 5, 35);
			font.draw(batch, "Mouse left on star: select end point", 5, 60);
			font.draw(batch, "Mouse right on star: select start point", 5, 75);
			font.draw(batch, "Space: start\\pause movement", 5, 90);
		}
		
		font.draw(batch, "Zoom: Mouse Wheel", 5, 120);
		font.draw(batch, "Camera: W,A,S,D", 5, 135);
	}
	
	private void movementUpdate() {
		if(ship.velocityNotZero()){
			ship.movementUpdate();
			moveStars();
			updateStarsPosHash();
		}
		else{
			flagMovementMode = false;
		}
	}

	private void moveStars() {
		if(stars.size() > 0){
			for(Star star: stars.values()){
				final float steps = Tools.getRange(star.realX(), star.realY(), ship.x(), ship.y())/Const.GRID_CELL_SIZE;
				star.setPosition(steps);
			}
		}
	}

	private void updateStarsPosHash() {
		starsPos.clear();
		for(Star star: stars.values()){
			starsPos.put(star.hashCode(), star.id);
		}
	}
	
	private void rebuildRealPosition() {
		if(stars.size() > 0){
			for(Star star: stars.values()){
				final float steps = Tools.getRange(star.x(), star.y(), ship.x(), ship.y())/Const.GRID_CELL_SIZE;
				star.setRealPosition(steps);
			}
		}
	}
	
	private void drawShapes(OrthographicCamera camera) {
		if(flagDrawGrid){
			grid.draw(camera);
		}
		
		render.setProjectionMatrix(camera.combined);
		{
			render.begin(ShapeType.Line);
			if(!getUI().selected()){
				cursor.draw(render);
			}
			
			if(stars.size() > 0){
				for(Star star: stars.values()){
					star.draw(render, flagDrawStarRealPos);
				}
			}
			render.end();
		}
		
		{
			render.begin(ShapeType.Filled);
			if(stars.size() > 0){
				ship.draw(render);
			}
			render.end();
		}
	}

	@Override
	public void event(int code) {
		if(code == Event.MOUSE_MOVE){
			cursor.update();
		}
		else if(code == Event.MOUSE_KEY_UP){
			if(inputType == INPUT_TYPE_ADD_STAR){
				flagSetupStar = false;
				rebuildRealPosition();
			}
		}
		else if(code == Event.MOUSE_KEY_LEFT){
			mouseKeyLeft();
		}
		else if(code == Event.MOUSE_DRAG){
			mouseDrag();
		}
		else if(code == Event.MOUSE_KEY_RIGHT){
			mouseKeyRight();
		}
	}
	
	@Override
	public void event(int code, int data) {
		if(code == Event.MOUSE_SCROLL){
			zoom(data);
		}
		else if(code == Event.KEY_UP){
			if(data == Keys.SPACE){
				movementMode();
			}
		}
	}

	private void movementMode() {
		if(inputType == INPUT_TYPE_MOVEMENT){
			if(flagMovementMode){
				flagMovementMode = false;	
			}
			else{
				if(stars.size() > 1){
					flagMovementMode = true;
				}
			}
		}
	}

	private void zoom(int data) {
		final float zoom = GameAPI.camera().zoom + ZOOM_SPEED*data;
		
		if(zoom > 0.01f && zoom < ZOOM_MAX){
			GameAPI.camera().zoom = zoom;
		}
	}
	
	private void mouseKeyLeft() {
		if(!getUI().selected() && !flagMovementMode){
			if(inputType == INPUT_TYPE_ADD_STAR){
				flagSetupStar = true;
				addStar();
			}
			else if(inputType == INPUT_TYPE_MOVEMENT){
				moveTo();
			}
		}
	}

	private void mouseDrag() {
		if(flagSetupStar){
			cursor.updateDrag();
			setStarVelocity();
		}
	}

	private void mouseKeyRight() {
		if(!getUI().selected() && !flagMovementMode){
			if(inputType == INPUT_TYPE_ADD_STAR){
				removeStar();
			}
			else if(inputType == INPUT_TYPE_MOVEMENT){
				selectActiveStar();
			}
		}
	}
	
	private void selectActiveStar() {
		if(stars.size() > 1){
			final int starId = selectStar();
			
			if(starId != STAR_POS_HASH_NULL){
				Star star = stars.get(starId);
				ship.setPos(star.x(), star.y());
				rebuildRealPosition();
			}
		}
	}

	private void addStar() {
		Star star = new Star(cursor.x(), cursor.y());
		addStarId = star.id;
		
		if(stars.size() == 0){
			ship.setPos(cursor.x(), cursor.y());
		}
		
		stars.put(star.id, star);
		starsPos.put(star.hashCode(), star.id);
	}
	
	private void setStarVelocity() {		
		Star star = stars.get(addStarId);
		
		if(star != null){
			star.setVelocity(cursor.dragX(), cursor.dragY());	
		}
	}

	private void removeStar() {
		if(stars.size() > 0){
			final int starId = selectStar();
			
			if(starId != STAR_POS_HASH_NULL){
				Star star = stars.remove(starId);
				starsPos.remove(star.hashCode());
			}
		}
	}
	
	private void moveTo() {
		if(stars.size() > 1){
			final int starId = selectStar();
			
			if(starId != STAR_POS_HASH_NULL){
				Star star = stars.get(starId);
				
				if(star != null){
					ship.setMovementTo(star.realX(), star.realY());
				}
			}
		}
	}

	private int selectStar(){
		int addStarHash = PositionHash.hashCode(cursor.x(), cursor.y());
		
		if(starsPos.containsKey(addStarHash)){
			return starsPos.get(addStarHash);
		}
		else{
			return STAR_POS_HASH_NULL;
		}
	}
	
	@Override
	public void dispose() {
		grid.dispose();
		render.dispose();
	}
}