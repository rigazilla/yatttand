package rigazilla.yatttand;

public class StartGameState implements GameState {

	@Override
	public void action(MainLoop context) {
		context.setState(new MenuGameState());
	}

}
