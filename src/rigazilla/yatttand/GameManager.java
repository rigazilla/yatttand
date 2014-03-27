package rigazilla.yatttand;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.andengine.entity.primitive.Line;
import org.andengine.util.color.Color;

public class GameManager {
	public enum Player {
		FREE, CIRCLE, CROSS
	};

	public static final int BOARDSIZE = 9;

	public static Player[] board;

	private static GameManager theInstance;
	public Player getCurrentTurn() {
		return (Player)conf.get("turn");
	}

	public void setCurrentTurn(Player currentTurn) {
		conf.put("turn", currentTurn);
	}

	public EnumMap<Player, Boolean> isAi = new EnumMap<GameManager.Player, Boolean>(
			Player.class);
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static Set[] winning = { new HashSet(Arrays.asList(0, 1, 2)),
			new HashSet(Arrays.asList(3, 4, 5)),
			new HashSet(Arrays.asList(6, 7, 8)),
			new HashSet(Arrays.asList(0, 3, 6)),
			new HashSet(Arrays.asList(1, 4, 7)),
			new HashSet(Arrays.asList(2, 5, 8)),
			new HashSet(Arrays.asList(0, 4, 8)),
			new HashSet(Arrays.asList(2, 4, 6)) };
	private boolean gameOver;


	public boolean isGameOver() {
		return gameOver;
	}

	private GameManager() {
		initBoard();
		board = new Player[9];
	}

	private void initBoard() {
//		for (int i = 0; i < board.length; i++) {
//			board[i] = Player.FREE;
//		}
	}

	public static GameManager getInstance() {
		if (theInstance == null) {
			theInstance = new GameManager();
		}
		return theInstance;
	}

	public void clickOnCell(int i) {
		board[i] = this.getCurrentTurn();
	}


	public void goOn() {
		Player currentTurn=this.getCurrentTurn();
			conf.put("turn",(currentTurn == Player.CIRCLE) ? Player.CROSS
					: Player.CIRCLE);			
	}

	public boolean isWinner(Player p) {
		for (int i = 0; i < winning.length; i++)
			if (getSignsOnBoard(p).containsAll(winning[i])) {
				return true;
			}
		return false;
	}

	private static Set<Integer> getSignsOnBoard(Player p) {
		Set<Integer> s = new HashSet<Integer>();
		for (int i = 0; i < board.length; i++) {
			if (board[i] == p)
				s.add(i);
		}
		return s;
	}

	public static Object[] gameStatus() {
		for (Player p : Player.values()) {
			if (p == Player.FREE)
				continue;
			for (int i = 0; i < winning.length; i++) {
				if (getSignsOnBoard(p).containsAll(winning[i])) {
					return new Object[] { p, i };
				}
			}
		}
		if (getSignsOnBoard(Player.FREE).size() == 0)
			return new Object[] { Player.FREE, null };
		return null;
	}

	public void cleanup() {
//		initBoard();
		conf.put("turn", Player.CIRCLE);

	}

	public void setGameOver(boolean b) {
		this.gameOver = b;

	}

	public int aiMove() {
		Integer best = null;
		Player currentTurn=this.getCurrentTurn();
		Object[] retVal = getBestMoveAndValue(currentTurn, currentTurn,-2,+2);

		best = (Integer) retVal[1];
		return best;
	}

	private Object[] getBestMoveAndValue(Player p, Player turn, double alpha, double beta) {
		Object[] retVal = new Object[2];
		Integer best1 = null;
		Double ret = p==turn ? -2.0  : 2.0;
		for (Integer i : getSignsOnBoard(Player.FREE)) {
			boolean stop=false;
			board[i] = p;
			if (p == turn) {
				alpha=Math.max(alpha, maxmin(p , board, turn, alpha, beta));
				if (beta<=alpha)
				{
					best1 = i;
					stop=true;
					ret=alpha;
				}
				if (ret<alpha)
				{
					ret=alpha;
					best1=i;
				}
			} else {
					beta=Math.min(beta, maxmin(p , board, turn, alpha, beta));
					if (beta<=alpha)
					{
						best1 = i;
						stop=true;
						ret=beta;
					}
					if (ret>beta)
					{
						ret=beta;
						best1=i;
					}
			}
			board[i] = Player.FREE;
			if (stop) break;
		}

		retVal[0] = ret;
		retVal[1] = best1;
		return retVal;
	}

//	private void printBoard() {
//		Debug.i("-----------------------------------");
//		Debug.i(board[0].name() + " " + board[1].name() + " " + board[2].name());
//		Debug.i(board[3].name() + " " + board[4].name() + " " + board[5].name());
//		Debug.i(board[6].name() + " " + board[7].name() + " " + board[8].name());
//		Debug.i("-----------------------------------");
//	}

	private double maxmin(Player player, Player[] board2, Player currentTurn2, double alpha, double beta) {
		if (player == currentTurn2) {
			if (isWinner(player)) {
				return 1;
			}
			if (isWinner(player == Player.CIRCLE ? Player.CROSS : Player.CIRCLE)) {
				return -1;
			}
			if (getSignsOnBoard(Player.FREE).size() == 0) {
				return 0;
			}
			return (Double)getBestMoveAndValue(player != Player.CROSS ? Player.CROSS : Player.CIRCLE, currentTurn2, alpha, beta)[0]/1.1;
		} else {
			if (isWinner(player)) {
				return -1;
			}
			if (isWinner(this.getCurrentTurn() == Player.CIRCLE ? Player.CROSS
					: Player.CIRCLE)) {
				return 1;
			}
			if (getSignsOnBoard(Player.FREE).size() == 0) {
				return 0;
			}
			return (Double)getBestMoveAndValue(player != Player.CROSS ? Player.CROSS : Player.CIRCLE, currentTurn2, alpha, beta)[0]/1.1;
		}

	}

	void checkForWin(MainActivity mainActivity) {
		Object[] winInfo = GameManager.getInstance().gameStatus();
		if (winInfo != null) {
			GameManager.Player whoWin = (GameManager.Player) winInfo[0];
			if (whoWin==Player.FREE)
			{
				setGameOver(true);
				return;
			}
			int whereWon = (Integer) winInfo[1];
			if (whoWin == Player.CIRCLE) {
				mainActivity.theRM.wLine = new Line(ResourceManager.winnerLine[whereWon][0],
						ResourceManager.winnerLine[whereWon][1],
						ResourceManager.winnerLine[whereWon][2],
						ResourceManager.winnerLine[whereWon][3], 16,
						mainActivity.getEngine().getVertexBufferObjectManager());
				mainActivity.theRM.wLine.setColor(Color.RED);
				mainActivity.theRM.boardLayer.attachChild(mainActivity.theRM.wLine);
			}
			if (whoWin == Player.CROSS) {
				mainActivity.theRM.wLine = new Line(ResourceManager.winnerLine[whereWon][0],
						ResourceManager.winnerLine[whereWon][1],
						ResourceManager.winnerLine[whereWon][2],
						ResourceManager.winnerLine[whereWon][3], 16,
						mainActivity.getEngine().getVertexBufferObjectManager());
				mainActivity.theRM.wLine.setColor(Color.RED);
				mainActivity.theRM.boardLayer.attachChild(mainActivity.theRM.wLine);
	
			}
			setGameOver(true);
		}
	}

	public Map<String, Object> conf;

}
