package njusoftware.noComment.SamurAI.AI;

import njusoftware.noComment.SamurAI.base.Board;
import njusoftware.noComment.SamurAI.base.ConstVar;
import njusoftware.noComment.SamurAI.base.GameManager;
import njusoftware.noComment.SamurAI.base.Samurai;

public class Foreseer {
	private static final Samurai[] SAMURAIS = GameManager.SAMURAIS;

	/* 通过推测去除视野限制 */
	public static final void inferMap(Board previousBoard, Board currentBoard) {
		int[][] current = currentBoard.getBattleField();
		int[][] previous = previousBoard.getBattleField();
		for (int i = 0; i < GameManager.HEIGHT; ++i)
			for (int j = 0; j < GameManager.WIDTH; ++j)
				if (current[i][j] == 9)
					current[i][j] = previous[i][j];
	}

	/* 推测隐身的武士位置 */
	public static final void inferPosition(Board previousBoard, Board currentBoard) {
		// 只需推测敌方可动的、未受伤的、不可见武士的位置
		int tmp = GameManager.ENEMY_INDEX;
		for (int i = tmp; i < tmp + 3; ++i)
			if (!SAMURAIS[i].isVisible() && SAMURAIS[i].isAlive() && SAMURAIS[i].isActive())
				inferPositionOf(i, previousBoard, currentBoard);
	}

	/* 特定武士的位置推测 */
	private static final void inferPositionOf(int samuraiID, Board previousBoard, Board currentBoard) {
		int[][] processedMap = processMap(samuraiID, previousBoard, currentBoard);
		// GameManager.print(processedMap);
		// System.err.println();
		int max = 0, count = 0;
		int tmpX = 0, tmpY = 0;
		// 找出最大值并计数
		for (int i = 0; i < GameManager.HEIGHT; ++i)
			for (int j = 0; j < GameManager.WIDTH; ++j) {
				int tmp = processedMap[i][j];
				if (tmp > max) {
					max = tmp;
					count = 0;
					tmpY = i;
					tmpX = j;
				}
				count += tmp == max ? 1 : 0;
			}

		if (count == 1) {
			SAMURAIS[samuraiID].setPosition(tmpX, tmpY);
			return;
		}

		for (int i = tmpY, index = count >> 1; i < GameManager.HEIGHT; ++i)
			for (int j = tmpX; j < GameManager.WIDTH; ++j) {
				count -= (processedMap[i][j] == max) ? 1 : 0;
				if (count > index)
					continue;
				SAMURAIS[samuraiID].setPosition(j, i);
				return;
			}
	}

	/* 预处理地图，标记武士在地图上某个位置的可能性，以权重值表现 */
	private static final int[][] processMap(int samuraiID, Board previousBoard, Board currentBoard) {
		int[] curPos = SAMURAIS[samuraiID].getPos();
		int curX = curPos[0], curY = curPos[1];// 直接取出横纵坐标以提高效率

		boolean[][] markedMap = markNewCapture(samuraiID, previousBoard, currentBoard);
		boolean flag = isCaptureChanged(markedMap);
		return postprocess(flag ? preprocess(markedMap, curX, curY) : preprocess(curX, curY), curX, curY, flag);
	}

	/* 标记新增的占领区 */
	private static final boolean[][] markNewCapture(int samuraiID, Board previousBoard, Board currentBoard) {
		boolean[][] result = new boolean[GameManager.HEIGHT][GameManager.WIDTH];
		int[][] curBattleField = currentBoard.getBattleField();
		int[][] prevBattleField = previousBoard.getBattleField();
		for (int i = 0; i < GameManager.HEIGHT; ++i)
			for (int j = 0; j < GameManager.WIDTH; ++j)
				// 将本回合该武士新增的地块标识出来
				result[i][j] = (curBattleField[i][j] == samuraiID) && (prevBattleField[i][j] != samuraiID);

		return result;
	}

	/* 检查占据范围是否发生了改变 */
	private static final boolean isCaptureChanged(boolean[][] markedMap) {
		for (boolean[] p : markedMap)
			for (boolean i : p)
				if (i)
					return true;

		return false;
	}

	/* 预处理，用于新增了占领区的情况 */
	private static final int[][] preprocess(boolean[][] markedMap, int curX, int curY) {
		int[][] result = new int[GameManager.HEIGHT][GameManager.WIDTH];

		for (int i = 0; i < GameManager.HEIGHT; ++i)
			for (int j = 0; j < GameManager.WIDTH; ++j)
				if (markedMap[i][j]) {// 找到发生变化的地方
					result[i][j] = ConstVar.NEW_CAPTURE_POW;// 该地块本身给一个权重
					for (int[] tmp : ConstVar.SURROUNDINGS) // 对于发生变化的位置周围的格子
						if (GameManager.inBound(j, tmp[0], i, tmp[1]))// 在不越界的条件下
							// 对于不是新增区域的格子给一个权重，对于是新增区域的格子给另一个权重
							// 注意此处是不进行叠加的
							if (markedMap[i + tmp[1]][j + tmp[0]])
								result[i + tmp[1]][j + tmp[0]] = ConstVar.NEW_SUR_POW;
				}
		return result;
	}

	/* 预处理，用于未新增占领区的情况 */
	private static final int[][] preprocess(int curX, int curY) {
		int[][] result = new int[GameManager.HEIGHT][GameManager.WIDTH];
		int tmp = GameManager.FRIEND_INDEX;// 敌方的敌方也就是我方
		// 计算我方每一个武士在指定敌方武士的那一个象限内，以ConstVar中分块移动范围数组的下标进行标识
		int[] enemyPos1 = SAMURAIS[tmp].getPos();
		int[] enemyPos2 = SAMURAIS[tmp + 1].getPos();
		int[] enemyPos3 = SAMURAIS[tmp + 2].getPos();
		int flag1 = enemyPos1[0] < curX ? (enemyPos1[1] <= curY ? 2 : 1)
				: (enemyPos1[1] < curY ? 3 : (enemyPos1[0] == curX ? 1 : 0));
		int flag2 = enemyPos2[0] < curX ? (enemyPos2[1] <= curY ? 2 : 1)
				: (enemyPos2[1] < curY ? 3 : (enemyPos2[0] == curX ? 1 : 0));
		int flag3 = enemyPos3[0] < curX ? (enemyPos3[1] <= curY ? 2 : 1)
				: (enemyPos3[1] < curY ? 3 : (enemyPos3[0] == curX ? 1 : 0));

		for (int i = 0; i < 4; i++)
			for (int[] p : ConstVar.SEPERATED_MOVE_RANGE[i])
				// 如果有敌人在某一象限中，则把这个象限中的权重值都减去1
				if (GameManager.inBound(curX, p[0], curY, p[1])) {
					result[curY + p[1]][curX + p[0]] = ConstVar.DEFAULT_MOV_RANGE_POW
							- ((flag1 == i ? 1 : 0) + (flag2 == i ? 1 : 0) + (flag3 == i ? 1 : 0));
				}

		return result;
	}

	/* 后处理，加上位置带来的权重 */
	private static final int[][] postprocess(int[][] result, int curX, int curY, boolean flag) {
		// 先前所在位置加一个权重
		result[curY][curX] += ConstVar.PREV_POS_POW;

		if (!flag)
			return result;

		// 先前所在位置的周围加一个权重
		for (int[] tmp : ConstVar.manhattanDistance(1))
			if (GameManager.inBound(curX, tmp[0], curY, tmp[1]))
				result[curY + tmp[1]][curX + tmp[0]] += ConstVar.POS_SUR_POW;

		return result;
	}
}
