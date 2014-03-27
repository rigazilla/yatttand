package rigazilla.yatttand;

public class GameStartState implements GameState {

	@Override
	public void action(MainLoop context) {
		context.setState(new MenuGameState());
	}

}
