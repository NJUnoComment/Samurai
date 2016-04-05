
/**
 * @author clefz created at 2016/3/23
 * 
 *         //请在此处标注编写者姓名 
 *         请在此处填写最终修改时间
 *         此类用于管理整个局面，对Board进行操作，并且以类迭代器的形式给出下一步可能的合法操作
 */
package njusoftware.noComment.SamurAI.base;

import java.io.IOException;
import java.util.Arrays;

import njusoftware.noComment.SamurAI.AI.AIManager;

public class GameManager {
	private AIManager AI;
	private Board prevBoard;
	private Board curBoard;// 这两个字段用于推测视野外情况
	private int curTurn;// 当前回合数
	private int remainCurePeriod;

	public static int WIDTH = 15;
	public static int HEIGHT = 15;
	public static int TOTAL_TURNS;// 总回合数
	public static int CURE_PERIOD;
	public static int[][] HOME_POSES;// 家的位置
	public static int[][] RANK_AND_SCORE;
	public static int SAMURAI_ID;// 控制的是哪一个武士，0-5表示
	public static final Samurai[] SAMURAIS;// 武士
	public static final int[] ACTION_ORDER = new int[] { 0, 3, 4, 1, 2, 5, 3, 0, 1, 4, 5, 2 }; // 行动的顺序，数字是samurais的下标
	// 根据回合数来确定那个行动的这么写:
	// samurais[ACTION_ORDER[turn%12]]

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
		prevBoard = new Board();
		AI = new AIManager(this);
		for (int i = 0; i < 6; i++) {
			// 将家的位置加到盘面上
			prevBoard.set(HOME_POSES[i], i);
			// 武士初始位置为家的位置
			SAMURAIS[i].setPos(HOME_POSES[i]);
		}
		System.out.println("0");// 输出0作为回应
	}

	private void inferMap() {
		int[][] current = curBoard.getBattleField();
		int[][] previous = prevBoard.getBattleField();
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				if (current[i][j] == 9)
					current[i][j] = previous[i][j];
			}
		}
	}

	// 评估函数
	public int evaluate(Board board) {
		return 0;
	}

	public void nextTurn() throws CloneNotSupportedException, IOException {
		Info turnInfo = IOManager.input();
		// 当前回合数
		curTurn = turnInfo.getTurn();

		// 剩余回复回合
		remainCurePeriod = turnInfo.getRemainCurePeriod();

		// 当前盘面
		prevBoard = curBoard == null ? prevBoard : curBoard;
		curBoard = new Board(turnInfo.getBoard(), curTurn = turnInfo.getTurn());
		inferMap();

		// 武士状态
		int[][] samuraiState = turnInfo.getSamuraiState();
		for (int i = 0; i < 6; i++) {
			if (-1 != samuraiState[i][0])
				SAMURAIS[i].setPos(new int[] { samuraiState[i][0], samuraiState[i][1] });
			SAMURAIS[i].setVisible(0 == samuraiState[i][2]);
			SAMURAIS[i].setActive(-1 == samuraiState[i][2]);
		}

		// 受伤时直接输出0
		if (remainCurePeriod != 0)
			IOManager.output(new Info().setActions(new int[] { 0 }));
		// 输出
		IOManager.output(new Info().setActions(AI.decideActions()));// 然后输出
	}

	public static GameManager init() throws IOException {
		Info gameInfo = IOManager.input();
		// 可能需要一些操作
		WIDTH = gameInfo.getWidth();
		HEIGHT = gameInfo.getHeight();
		TOTAL_TURNS = gameInfo.getTotalTurns();
		CURE_PERIOD = gameInfo.getCurePeriod();
		HOME_POSES = gameInfo.getHomePos();
		SAMURAI_ID = gameInfo.getSamuraiID();
		RANK_AND_SCORE = gameInfo.getRanksAndScores();
		return new GameManager();
	}

	public Board getBoard() {
		return curBoard;
	}
//
//	void print() {
//		int[][] t = curBoard.getBattleField();
//		for (int[] i : t) {
//			for (int j : i)
//				System.out.print((j == 9 ? "x" : j) + " ");
//			System.out.println();
//		}
//	}
//
//	public static void print(Board b) {
//		int[][] t = b.getBattleField();
//		for (int[] i : t) {
//			for (int j : i)
//				System.out.print((j == 9 ? "x" : j) + " ");
//			System.out.println();
//		}
//	}
}
