package org.starshift;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.owlengine.scenes.SceneMng;

public class GameAPI {
	
	private static SceneMng scenes;
	private static OrthographicCamera camera;

	public GameAPI(SceneMng scenes, OrthographicCamera camera) {
		GameAPI.scenes = scenes;
		GameAPI.camera = camera;
	}

	public static OrthographicCamera camera(){
		return camera;
	}
	public static void sceneDemo() {
		scenes.loadScene(new MainScene(camera));
	}
}