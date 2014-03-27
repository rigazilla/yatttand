package rigazilla.yatttand;

import java.util.concurrent.Callable;

import org.andengine.util.debug.Debug;

public class BackgroundLife implements Callable<Integer> {


	private MainLoop context;

	public BackgroundLife(MainLoop context) {
		super();
		this.context=context;
	}

	@Override
	public Integer call() throws Exception {
		Debug.e("init");
		GameManager theGM = GameManager.getInstance();
		int i= theGM.aiMove();
		context.theGM.conf.put("move",i);
		Debug.e("end");
//		theGM.checkForWin(ma);
		return i;
	}


}
