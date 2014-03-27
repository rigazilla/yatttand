package rigazilla.yatttand;

import java.util.Map;

import rigazilla.yatttand.GameManager.Player;


public class RunHumanGameState extends RunGameState implements GameState {

	@Override
	public void action(MainLoop context) {
		Map<String, Object> conf = context.theGM.conf;
		Object move = conf.get("move");
		if (move!=null)
		{
			Player turn = (Player)conf.get("turn");
			context.theRM.place((Integer)move, turn);
			context.theRM.deactivateAllFreeButtons();
			conf.remove("move");
			turn = (turn==Player.CIRCLE) ? Player.CROSS : Player.CIRCLE;
			conf.put("turn", turn);
			onExit(context, conf, turn);
		}
	}


}
