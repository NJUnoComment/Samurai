
/**
 * @author clefz created at 2016/3/23
 * 
 *         //clefz
 *         2016/4/8
 *         此类用于管理整个局面，对Board进行操作，并且以类迭代器的形式给出下一步可能的合法操作
 */
package njusoftware.noComment.SamurAI.base;

import java.io.IOException;

import njusoftware.noComment.SamurAI.AI.AIManager;
import njusoftware.noComment.SamurAI.AI.Foreseer;

public class GameManager {
	private AIManager AI;
	private static Board previousBoard;// 上一回合的局面
	private static Board currentBoard;// 当前回合的局面
	private static int curTurn;// 当前回合数
	private int remainCurePeriod;

	// private static int count = 0;

	public static int WIDTH;
	public static int HEIGHT;
	public static int TOTAL_TURNS;// 总回合数
	public static int CURE_PERIOD;
	public static int[][] HOME_POSES;// 家的位置
	public static int[][] RANK_AND_SCORE;
	public static int SAMURAI_ID;// 控制的是哪一个武士，0-5表示
	public static int FRIEND_INDEX;
	public static int ENEMY_INDEX;

	public static final Samurai[] SAMURAIS;// 武士
	static {
		SAMURAIS = new Samurai[6];
		SAMURAIS[0] = new Samurai(Weapons.SPEAR);
		SAMURAIS[1] = new Samurai(Weapons.SWORD);
		SAMURAIS[2] = new Samurai(Weapons.AXE);
		SAMURAIS[3] = new Samurai(Weapons.SPEAR);
		SAMURAIS[4] = new Samurai(Weapons.SWORD);
		SAMURAIS[5] = new Samurai(Weapons.AXE);
	}

	private GameManager() {
		previousBoard = currentBoard = new Board();
		AI = new AIManager();
		for (int i = 0; i < 6; ++i) {
			// 将家的位置加到盘面上
			previousBoard.set(HOME_POSES[i], i);
			// 武士初始位置为家的位置
			SAMURAIS[i].setPos(HOME_POSES[i]);
		}
		System.out.println("0");// 输出0作为回应
	}

	/* 是否越界 */
	public static final boolean inBound(int x, int deltaX, int y, int deltaY) {
		return x + deltaX >= 0 && y + deltaY >= 0 && x + deltaX < HEIGHT && y + deltaY < WIDTH;
	}

	public void nextTurn() throws CloneNotSupportedException, IOException {
		extractInfo(IOManager.input(false));

		// 受伤时直接输出0
		if (remainCurePeriod != 0)
			IOManager.output(new Info().setActions(new int[] { 0 }));
		else
			// 输出
			IOManager.output(new Info().setActions(AI.decideActions()));

	}

	private final void extractInfo(Info info) {
		// 剩余回复回合
		remainCurePeriod = info.getRemainCurePeriod();

		// 当前盘面
		previousBoard = currentBoard;
		currentBoard = new Board(info.getBattleField(), curTurn = info.getTurn());
		Foreseer.inferMap(previousBoard, currentBoard);

		// 武士状态
		int[][] samuraiState = info.getSamuraiState();
		for (int i = 0; i < 6; ++i) {
			if (-1 != samuraiState[i][0])// 不可见武士不改变位置
				SAMURAIS[i].setPos(samuraiState[i][0], samuraiState[i][1]);
			SAMURAIS[i].setVisible(0 == samuraiState[i][2]);
			SAMURAIS[i].setActive(-1 != samuraiState[i][2]);
		}
		Foreseer.inferPosition(previousBoard, currentBoard);

		currentBoard.refreshSamuraisState();
	}

	public final static GameManager init() throws IOException {
		Info gameInfo = IOManager.input(true);
		TOTAL_TURNS = gameInfo.getTotalTurns();
		SAMURAI_ID = gameInfo.getSamuraiID();
		CURE_PERIOD = gameInfo.getCurePeriod();
		WIDTH = gameInfo.getWidth();
		HEIGHT = gameInfo.getHeight();
		HOME_POSES = gameInfo.getHomePos();
		FRIEND_INDEX = SAMURAI_ID > 2 ? 3 : 0;
		ENEMY_INDEX = 3 - SAMURAI_ID;

		return new GameManager();
	}

	public static Board getPreviousBoard() {
		return previousBoard;
	}

	public static Board getCurrentBoard() {
		return currentBoard;
	}

	public static void print(int[][] i) {
		for (int[] p : i) {
			for (int q : p)
				System.err.print(q + " ");
			System.err.println();
		}
	}

	public static void main(String[] args) throws IOException, CloneNotSupportedException {
		GameManager gm = GameManager.init();
		gm.nextTurn();
		// int[] pos = GameManager.SAMURAIS[2].getPos();
		// System.out.println(pos[0] + "," + pos[1]);
		// Evaluator.figureAtkRangeDistribution(GameManager.curBoard);
		// // SAMURAIS[5].setPos(5, 0);
		// // int[][] b = new int[][] {
		// // { 8, 8, 8, 8, 8, 5, 8, 8, 8, 8, 8, 8, 8, 8, 4 },
		// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 4, 4, 8 },
		// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 4, 4 },
		// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 4 },
		// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
		// // { 0, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
		// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
		// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
		// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
		// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 3 },
		// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
		// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
		// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
		// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
		// // { 1, 8, 8, 8, 8, 8, 8, 8, 8, 2, 8, 8, 8, 8, 8 }, };
		// // Board k = new Board(b, 5);
		// // Board c=k.makeMove(Move.MS_OS);
		// // System.out.println(c.getBattleField().length);
		// // GameManager.print(c.getBattleField());
		// // System.out.println(GameManager.diffCapture(c));
		// // System.out.println(GameManager.diffCapture(c));
		// // System.out.println(GameManager.diffCapture(new Board(b, 0)));
		// GameManager gm = GameManager.init();
		// gm.nextTurn();
		// System.out.println(gm.curBoard.isFriendArea(4, 0));
		// // GameManager.print(gm.prevBoard.getBattleField());
		// // gm.nextTurn();
		// // GameManager.print(gm.curBoard.getBattleField());
		// // int[][] poses = new int[6][2];
		// // for (int i = 0; i < 6; ++i)
		// // poses[i] = GameManager.SAMURAIS[i].getPos();
		// // for (int i = 0; i < 6; ++i)
		// // System.out.println(i + ":" + poses[i][0] + "," + poses[i][1]);
	}
}
