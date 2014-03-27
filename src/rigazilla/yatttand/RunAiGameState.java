package rigazilla.yatttand;

import java.util.Map;

import rigazilla.yatttand.GameManager.Player;


public class RunAiGameState extends RunGameState implements GameState {
	float timer=1;
	boolean visible=false;
	@Override
	public void action(MainLoop context) {
		Map<String, Object> conf = context.theGM.conf;
		Object move = conf.get("move");
		timer+=context.elapsed;
		if (timer>1)
		{
			timer=0;
			visible=!visible;
			context.theRM.setThinkingVisible(visible);
		}
		if (move!=null)
		{
			Player turn = (Player)conf.get("turn");
			context.theRM.place((Integer)move, turn);
			conf.remove("move");
			turn = (turn==Player.CIRCLE) ? Player.CROSS : Player.CIRCLE;
			conf.put("turn", turn);
			context.theRM.setThinkingVisible(false);			
			onExit(context, conf, turn);
		}
	}

}
