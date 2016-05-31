package org.starshift;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

public class Grid {

	private ShapeRenderer render;
	private Vector3 cameraResoultion;
	
	public Grid() {
		render = new ShapeRenderer();
		cameraResoultion = new Vector3();
	}
	
	public void draw(OrthographicCamera camera){
		render.setProjectionMatrix(camera.combined);
		render.begin(ShapeType.Line);
		
		Gdx.gl.glLineWidth(1.0f);
		render.setColor(0.5f, 0.5f, 0.5f, 1.0f);
		
		cameraResoultion.set(0.0f, 0.0f, 0.0f);
		camera.unproject(cameraResoultion);
		final int minx = ((int)(cameraResoultion.x)/Const.GRID_CELL_SIZE * Const.GRID_CELL_SIZE) - Const.GRID_CELL_SIZE;
		final int miny = ((int)(cameraResoultion.y)/Const.GRID_CELL_SIZE * Const.GRID_CELL_SIZE) + Const.GRID_CELL_SIZE;

		cameraResoultion.set(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), 0.0f);
		camera.unproject(cameraResoultion);
		final int maxx = ((int)(cameraResoultion.x)/Const.GRID_CELL_SIZE * Const.GRID_CELL_SIZE) + Const.GRID_CELL_SIZE;
		final int maxy = ((int)(cameraResoultion.y)/Const.GRID_CELL_SIZE * Const.GRID_CELL_SIZE) - Const.GRID_CELL_SIZE;
		
		for(int i = minx, j = miny; i < maxx; i += Const.GRID_CELL_SIZE, j -= Const.GRID_CELL_SIZE){
			render.line(i, miny, i, maxy);
			render.line(minx, j, maxx, j);
		}
		
		render.end();
	}

	public void dispose() {
		render.dispose();
	}
}