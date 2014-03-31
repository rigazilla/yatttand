package rigazilla.yatttand;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.FutureTask;

import org.andengine.util.debug.Debug;

import rigazilla.yatttand.GameManager.Player;

public class MenuGameState implements GameState {


	@Override
	public void action(MainLoop context) {
		Map<String, Object> conf = context.theGM.conf;
		if (!conf.isEmpty()) {
			context.theRM.deactivateTouchMenu();
			context.theRM.buildBoard(context.theRM.mainActivity);
			context.theRM.mainActivity.getEngine().setScene(
					context.theRM.gameScene);
			Player turn = (Player) conf.get("turn");
			Debug.e("turn:"+turn+"     set:"+conf.get("ai")+"      conf:"+conf);
			if (((Set<Player>) conf.get("ai")).contains(turn)) {
				context.setState(new RunAiGameState());
				context.theRM.aiWork = new FutureTask<Integer>(new BackgroundLife(context));
				MainActivity.executor.execute(context.theRM.aiWork);
			} else {
//				context.theRM.activateAllFreeButtons();
				context.setState(new RunHumanGameState());
			}
		}
	}

}
