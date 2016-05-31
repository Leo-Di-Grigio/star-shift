package org.starshift;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Ship {

	private Vector2 pos;
	private Vector2 velocity;
	private Vector2 moveTo;
	
	public Ship() {
		pos = new Vector2();
		velocity = new Vector2();
		moveTo = new Vector2();
	}
	
	public void setPos(float x, float y){
		pos.set(x, y);
		moveTo.set(x, y);
	}	

	public float x(){
		return pos.x;
	}
	
	public float y(){
		return pos.y;
	}

	public void setMovementTo(float x, float y) {
		moveTo.set(x, y);
		velocity.set(x - pos.x, y - pos.y);
		velocity.nor();
		velocity.scl(Const.SHIP_SPEED);
	}	

	public void movementUpdate() {
		if(!velocity.isZero()){
			pos.add(velocity);
			
			if(pos.epsilonEquals(moveTo, velocity.len())){
				pos.set(moveTo);
				velocity.setZero();
			}
		}
	}
	
	@Override
	public int hashCode(){
		return PositionHash.hashCode(pos.x, pos.y); 
	}
	
	public void draw(ShapeRenderer render){
		if(!pos.epsilonEquals(moveTo, Const.SHIP_RADIUS)){
			render.setColor(0.0f, 0.0f, 1.0f, 1.0f);
			render.circle(moveTo.x, moveTo.y, Const.SHIP_RADIUS);
			render.line(pos, moveTo);
		}
		
		render.setColor(1.0f, 1.0f, 0.0f, 1.0f);
		render.circle(pos.x, pos.y, Const.SHIP_RADIUS);
	}

	public boolean velocityNotZero() {
		return !velocity.isZero();
	}
}
