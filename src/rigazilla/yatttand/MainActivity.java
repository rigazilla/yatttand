package rigazilla.yatttand;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import javax.crypto.SealedObject;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.WakeLockOptions;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.util.debug.Debug;

import rigazilla.yatttand.GameManager.Player;

public class MainActivity extends BaseGameActivity implements
		IOnSceneTouchListener, ButtonSprite.OnClickListener {
	@Override
	public synchronized void onPauseGame() {
		Debug.e("onPauseGame");
		super.onPauseGame();

	}

	public MainActivity() {
		super();
		Debug.e("creating MainActivity: " + this);

	}

	static final int WIDTH = 800;
	static final int HEIGHT = 480;
	// Declare a Camera object for our activity
	private Camera mCamera;
	public ResourceManager theRM;
	public GameManager theGM;
	public static ExecutorService executor = Executors.newFixedThreadPool(1);

	@Override
	public EngineOptions onCreateEngineOptions() {
		Debug.e("onCreateEngineOptions");
		mCamera = new Camera(0, 0, WIDTH, HEIGHT);

		// Declare & Define our engine options to be applied to our Engine
		// object
		EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE_FIXED, new FillResolutionPolicy(),
				mCamera);

		// It is necessary in a lot of applications to define the following
		// wake lock options in order to disable the device's display
		// from turning off during gameplay due to inactivity
		engineOptions.setWakeLockOptions(WakeLockOptions.SCREEN_ON);

		// Return the engineOptions object, passing it to the engine

		return engineOptions;
	}

	@Override
	public Engine onCreateEngine(EngineOptions pEngineOptions) {
		// TODO Auto-generated method stub
		return super.onCreateEngine(pEngineOptions);
	}

	@Override
	public void onDestroyResources() throws Exception {
		Debug.e("onDestroyResources");
		theGM.cleanup();
		theRM.cleanup();
		super.onDestroyResources();
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		Debug.e("onCreateResources");
		theRM = ResourceManager.getInstance(this);
		theGM = GameManager.getInstance();
		theRM.loadTexture(this);
		theRM.loadSprites(this);
		pOnCreateResourcesCallback.onCreateResourcesFinished();

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		Debug.e("onCreateScene");
		theRM.gameScene = new Scene();
		theRM.menuScene = new Scene();
		pOnCreateSceneCallback.onCreateSceneFinished(theRM.menuScene);
		// mScene.setOnSceneTouchListener(this);
		MainLoop mainLoop = new MainLoop(this);

		// Setting initial state
		theRM.activateTouchMenu();
		this.getEngine().setScene(theRM.menuScene);
		theGM.conf = new HashMap<String, Object>();
		mainLoop.setState(new StartGameState());

		theRM.gameScene.registerUpdateHandler(mainLoop);
		theRM.menuScene.registerUpdateHandler(mainLoop);
		theRM.gameScene.setTouchAreaBindingOnActionDownEnabled(true);
		theRM.menuScene.setTouchAreaBindingOnActionMoveEnabled(true);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		Debug.e("onPopulateScene");

		theRM.buildMenu(this);

		pOnPopulateSceneCallback.onPopulateSceneFinished();

	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		return true;
	}

	@Override
	public void onClick(ButtonSprite pButtonSprite, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		Debug.e("onClick: mengine is " + mEngine + " activity is " + this
				+ "tag: " + (Integer) pButtonSprite.getTag());
		if ((Integer) pButtonSprite.getTag() == 32) {
			Debug.e("putting replay");
			theGM.conf.put("replay", "yes");
		} else if ((Integer) pButtonSprite.getTag() < 10) {
			onClickOnBoard(pButtonSprite);

		} else {
//			this.mEngine.setScene(theRM.gameScene);
//			theGM.setGameOver(false);
//			theRM.buildBoard(this);
//			theGM.cleanup();

			HashSet<Player> tSet = new HashSet<Player>();
			Debug.e("cross: "+theRM.menuCrossSprite.state+"     circle: "+theRM.menuCircleSprite.state);
			switch (pButtonSprite.getTag()) {
			case 16: // human pressed
				if (theRM.menuCircleSprite.state == 1) {
					theGM.conf.put("turn", Player.CIRCLE);
				}
				else
				if (theRM.menuCrossSprite.state == 1) {
					theGM.conf.put("turn", Player.CROSS);
				}
				else break;
				if (theRM.menuCircleSprite.state < 1) {
					tSet.add(Player.CIRCLE);
				}
				if (theRM.menuCrossSprite.state < 1) {
					tSet.add(Player.CROSS);
				}
				theGM.conf.put("ai", tSet);
				break;

			case 17: // computer pressed
				if (theRM.menuCircleSprite.state == -1) {
					theGM.conf.put("turn", Player.CIRCLE);
				}
				else
				if (theRM.menuCrossSprite.state == -1) {
					theGM.conf.put("turn", Player.CROSS);
				}
				else break;
				if (theRM.menuCircleSprite.state == -1) {
					tSet.add(Player.CIRCLE);
				}
				if (theRM.menuCrossSprite.state == -1) {
					tSet.add(Player.CROSS);
				}
				theGM.conf.put("ai", tSet);
				break;
			default: 
				break;
			}
			// if (theGM.isAi.get(theGM.currentTurn)) {
			// theRM.aiWork = new FutureTask<Integer>(new BackgroundLife(this));
			// executor.execute(theRM.aiWork);
			// }

		}
	}

	private void onClickOnBoard(final ButtonSprite pButtonSprite) {
		if (theRM.aiWork != null) {
			if (!theRM.aiWork.isDone())
				return;

		}
		Integer userData = pButtonSprite.getTag();
		theGM.conf.put("move", userData);
		// theGM.clickOnCell(userData);
		// theRM.placeSign(userData);
		// Debug.e("Engine is: "+this.getEngine());
		// this.runOnUpdateThread(new Runnable() {
		// public void run() {
		// theRM.gameScene.unregisterTouchArea(pButtonSprite);
		// theRM.boardLayer.detachChild(pButtonSprite);
		// }
		// });
		// GameManager.getInstance().goOn();
		// theGM.checkForWin(this);
		// if (theGM.isGameOver()) {
		// return;
		// }
		// if
		// (((Set<Player>)theGM.conf.get("ai")).contains(theGM.getCurrentTurn()))
		// {
		// theRM.aiWork = new FutureTask<Integer>(new BackgroundLife(this));
		// executor.execute(theRM.aiWork);
		// }
	}

	public void detachSprite(Sprite s) {

	}

	public void detachButton(ButtonSprite s) {

	}
}
