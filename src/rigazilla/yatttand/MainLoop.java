package rigazilla.yatttand;

import org.andengine.engine.handler.IUpdateHandler;

public class MainLoop implements IUpdateHandler {
	boolean visible = false;
	float elapsed;
	float over = 0;
	GameManager theGM = GameManager.getInstance();
	ResourceManager theRM;
	private GameState state;

	public MainLoop(MainActivity ma) {
		theRM = ma.theRM;
	}

	@Override
	public void onUpdate(float pSecondsElapsed) {
		elapsed=pSecondsElapsed;
		state.action(this);
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	public void setState(GameState pState) {
		this.state=pState;
		
	}

}
