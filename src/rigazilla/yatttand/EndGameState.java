package rigazilla.yatttand;

public class EndGameState implements GameState {
	int substate = 0;

	@Override
	public void action(MainLoop context) {
		if (context.theGM.conf.get("replay") != null) {
			switch (substate) {
			case 0:
				context.theRM.showReplayButton();
				context.theGM.conf.remove("replay");
				substate = 1;
				break;
			case 1:
				context.theRM.removeReplayButton();
				context.theGM.conf.clear();
				context.theRM.mainActivity.getEngine().setScene(context.theRM.menuScene);
				context.theRM.activateTouchMenu();
				context.setState(new MenuGameState());
				
				break;
			default:
				break;

			}
		}
	}

}
