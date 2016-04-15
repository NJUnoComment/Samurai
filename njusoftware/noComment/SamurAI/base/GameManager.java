
/**
 * @author clefz created at 2016/3/23
 * 
 *         //clefz
 *         2016/4/8
 *         此类用于管理整个局面，对Board进行操作，并且以类迭代器的形式给出下一步可能的合法操作
 */
package njusoftware.noComment.SamurAI.base;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

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
	public static final int[] ACTION_ORDER = new int[] { 0, 3, 4, 1, 2, 5, 3, 0, 1, 4, 5, 2 }; // 行动的顺序，数字是samurais的下标
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

	/* 通过推测去除视野限制 */
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

	/* 推测隐身的武士位置 */
	private void inferPosition() {
		int tmp = 3 - (SAMURAI_ID > 2 ? 3 : 0);
		for (int i = tmp; i < tmp + 3; i++)
			// 只需推测敌方不可见武士的位置
			if (!SAMURAIS[i].isVisible())
				inferPositionOf(i);
	}

	/* 特定武士的位置推测 */
	private void inferPositionOf(int samuraiID) {
		int[][] processedMap = processMap(samuraiID);
		int max = 0, count = 0;
		// 找出最大值并计数
		for (int i = 0; i < HEIGHT; i++)
			for (int j = 0; j < WIDTH; j++) {
				int tmp = processedMap[i][j];
				if (tmp > max) {
					max = tmp;
					count = 0;
				}
				count += tmp == max ? 1 : 0;
			}

		// 在最大值位置里随机选择一个位置并设置为武士的位置
		int index = (int) (Math.random() * (count - 1)) + 1;
		for (int i = 0; i < HEIGHT; i++)
			for (int j = 0; j < WIDTH; j++) {
				count -= processedMap[i][j] == max ? 1 : 0;
				if (count != index)
					continue;
				SAMURAIS[samuraiID].setPos(new int[] { i, j });
				return;
			}
	}

	/* 预处理地图，标记武士在地图上某个位置的可能性，以权重值表现 */
	private int[][] processMap(int samuraiID) {
		int[] curPos = SAMURAIS[samuraiID].getPos();
		int curX = curPos[0], curY = curPos[1];// 直接取出横纵坐标以提高效率

		int[][] result;
		boolean[][] markedMap = markNewCapture(samuraiID);

		/* 占领区域增加了的情况 */
		if (isCaptureChanged(markedMap))
			result = preprocess(markedMap, curX, curY);
		result = preprocess(curX, curY);

		return postprocess(result, curX, curY);
		/* 占领区域未增加的情况 */

	}

	/* 检查占据范围是否发生了改变 */
	final static private boolean isCaptureChanged(boolean[][] markedMap) {
		boolean isCaptureChanged = false;
		for (boolean[] p : markedMap)
			for (boolean i : p)
				if (isCaptureChanged = i)
					break;
		return isCaptureChanged;
	}

	/* 后处理，加上位置带来的权重 */
	final static private int[][] postprocess(int[][] result, int curX, int curY) {
		// 先前所在位置加一个权重
		result[curY][curX] += ConstVar.PREV_POS_POW;

		// 先前所在位置的周围加一个权重
		for (int[] tmp : ConstVar.OCCUPIED_MOVE_RANGE)
			if (isInBound(curX, tmp[0], curY, tmp[1]))
				result[curY + tmp[1]][curX + tmp[0]] += ConstVar.POS_SURR_POW;
		return result;
	}

	/* 预处理，用于新增了占领区的情况 */
	final static private int[][] preprocess(boolean[][] markedMap, int curX, int curY) {
		int[][] result = new int[HEIGHT][WIDTH];

		for (int i = 0; i < HEIGHT; i++)
			for (int j = 0; j < WIDTH; j++)
				if (markedMap[i][j])// 找到发生变化的地方
					for (int[] tmp : ConstVar.SURROUNDINGS) // 对于发生变化的位置周围的格子
						if (isInBound(i, tmp[1], j, tmp[0]))// 在不越界的条件下
							// 对于不是新增区域的格子给一个权重，对于是新增区域的格子给另一个权重
							// 注意此处是不进行叠加的
							result[i + tmp[1]][j + tmp[0]] = markedMap[i + tmp[1]][j + tmp[0]]
									? ConstVar.NEW_CAPTURE_POW : ConstVar.NEW_SURR_POW;

		return result;
	}

	/* 预处理，用于未新增占领区的情况 */
	final static private int[][] preprocess(int curX, int curY) {
		int[][] result = new int[HEIGHT][WIDTH];
		int tmp = SAMURAI_ID > 2 ? 3 : 0;// 敌方的敌方也就是我方

		// 计算我方每一个武士在指定敌方武士的那一个象限内，以ConstVar中分块移动范围数组的下标进行标识
		int[] enemyPos1 = SAMURAIS[tmp].getPos();
		int[] enemyPos2 = SAMURAIS[tmp + 1].getPos();
		int[] enemyPos3 = SAMURAIS[tmp + 2].getPos();
		int flag1 = (enemyPos1[0] < curX ? 1 : 0) & (enemyPos1[1] < curY ? 2 : 0);
		int flag2 = (enemyPos2[0] < curX ? 1 : 0) & (enemyPos2[1] < curY ? 2 : 0);
		int flag3 = (enemyPos3[0] < curX ? 1 : 0) & (enemyPos3[1] < curY ? 2 : 0);

		for (int i = 0; i < 4; i++)
			for (int[] p : ConstVar.SEPERATED_MOVE_RANGE[i])
				// 如果有敌人在某一象限中，则把这个象限中的权重值都减去1
				if (isInBound(curX, p[0], curY, p[1]))
					result[curY + p[1]][curX + p[0]] -= (flag1 == i ? 1 : 0) + (flag2 == i ? 1 : 0)
							+ (flag3 == 0 ? 1 : 0);

		return result;
	}

	/* 标记新增的占领区 */
	private boolean[][] markNewCapture(int samuraiID) {
		boolean[][] result = new boolean[HEIGHT][WIDTH];
		int[][] curBattleField = curBoard.getBattleField();
		int[][] prevBattleField = prevBoard.getBattleField();
		for (int i = 0; i < HEIGHT; i++)
			for (int j = 0; j < WIDTH; j++)
				// 将本回合该武士新增的地块标识出来
				result[i][j] = curBattleField[i][j] == samuraiID && prevBattleField[i][j] != samuraiID;
		return result;
	}

	/* 是否越界 */
	final static private boolean isInBound(int x, int deltaX, int y, int deltaY) {
		return x + deltaX >= 0 && y + deltaY >= 0 && x + deltaX < HEIGHT && y + deltaY < WIDTH;
	}

	/* 评估函数 */
	public int evaluate(Board board) {
		return diffCapture(board);
	}

	/* 计算占领面积差 */
	public int diffCapture(Board board) {
		int[][] battleField = board.getBattleField();
		int result = 0;
		for (int[] row : battleField)
			for (int grid : row)
				result += (grid < 3 ? 1 : (grid != 8 ? -1 : 0));
		return result;
	}

	public void nextTurn() throws CloneNotSupportedException, IOException {
		Info turnInfo = IOManager.input(false);

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
			SAMURAIS[i].setActive(-1 != samuraiState[i][2]);
		}
		inferPosition();

		// 受伤时直接输出0
		if (remainCurePeriod != 0)
			IOManager.output(new Info().setActions(new int[] { 0 }));

		// 输出
		IOManager.output(new Info().setActions(AI.decideActions()));
	}

	public static GameManager init() throws IOException {
		Info gameInfo = IOManager.input(true);
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
}
