package rigazilla.yatttand;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.FutureTask;

import org.andengine.util.debug.Debug;

import rigazilla.yatttand.GameManager.Player;

public class RunGameState {

	public RunGameState() {
		super();
	}

	protected void onExit(MainLoop context, Map<String, Object> conf, Player turn) {
		// Check for gameover
		if (GameManager.gameStatus()!=null)
		{
			context.theRM.deactivateAllFreeButtons();
			context.theGM.checkForWin(context.theRM.mainActivity);
			context.theRM.addReplayButton();
			Debug.e("added button");
			conf.remove("replay");
			context.setState(new EndGameState());
			return;
		}
		if (((Set<Player>)conf.get("ai")).contains(turn))
		{
			context.setState(new RunAiGameState());
			context.theRM.aiWork = new FutureTask<Integer>(new BackgroundLife(context));
			MainActivity.executor.execute(context.theRM.aiWork);
		}
		else
		{
			context.theRM.activateAllFreeButtons();
			context.setState(new RunHumanGameState());
		}
	}

}