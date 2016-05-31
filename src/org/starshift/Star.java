package org.starshift;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Star {

	private static int ID = 0;
	
	public final int id;
	private Vector2 pos;
	private Vector2 posReal;
	private Vector2 velocity;
	
	public Star(float x, float y) {
		id = ++ID;
		pos = new Vector2(x, y);
		posReal = new Vector2(x, y);
		velocity = new Vector2();
	}

	public void setVelocity(float x, float y) {
		velocity.set(x - pos.x, y - pos.y);
		
		if(velocity.len() > Const.STAR_MAX_SPEED){
			velocity.nor();
			velocity.scl(Const.STAR_MAX_SPEED);
		}
	}
	
	public void setPosition(float steps) {
		pos.set(posReal.x - velocity.x*steps, posReal.y - velocity.y*steps);
	}
	
	public void setRealPosition(float steps) {
		posReal.set(pos.x + velocity.x*steps, pos.y + velocity.y*steps);
	}
	
	public void draw(ShapeRenderer render, boolean flagDrawStarRealPos){
		if(flagDrawStarRealPos){
			if(!pos.epsilonEquals(posReal, Const.STAR_RADIUS)){
				render.setColor(1.0f, 0.0f, 0.0f, 1.0f);
				render.circle(posReal.x, posReal.y, Const.STAR_RADIUS);
				render.line(pos, posReal);
			}
		}
		
		render.setColor(0.0f, 1.0f, 0.0f, 1.0f);
		render.circle(pos.x, pos.y, Const.STAR_RADIUS);
		render.line(pos.x, pos.y, pos.x + velocity.x, pos.y + velocity.y);
	}

	public float x() {
		return pos.x;
	} 
	
	public float y(){
		return pos.y;
	}

	public float realX() {
		return posReal.x;
	}
	
	public float realY() {
		return posReal.y;
	}
	
	@Override
	public int hashCode() {
		return PositionHash.hashCode(pos.x, pos.y); 
	}
}