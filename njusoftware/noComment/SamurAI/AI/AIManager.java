
/**
 * @author clefz created at 2016/3/23
 *
 *         clefz 
 *         2016/4/8
 *         核心AI类，封装AlphaBeta算法
 */
package njusoftware.noComment.SamurAI.AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import njusoftware.noComment.SamurAI.base.*;

public class AIManager {
	private static final List<String> ACTION_INDEX = new ArrayList<String>(
			Arrays.asList(new String[] { "OS", "OE", "ON", "OW", "MS", "ME", "MN", "MW" }));
	private final static int BOUND = Integer.MAX_VALUE;
	private Board currentBoard;
	private Samurai samurai;
	private Move bestMove;
	private Move[] currentBranches;
	private Move[] possibleBestMoves;

	// private static int count = 0;
	public final int[] decideActions() throws CloneNotSupportedException {
		bestMove = null;
		currentBoard = GameManager.getCurrentBoard();
		samurai = GameManager.SAMURAIS[GameManager.SAMURAI_ID];

		int[] movingEnemiesID = calcMovingEnemiesID(currentBoard.getTurn());
		possibleBestMoves = new Move[movingEnemiesID.length];
		currentBranches = new Move[movingEnemiesID.length];
		plan(currentBoard, movingEnemiesID);
		GameManager.setCurrentBoard(currentBoard.makeMove(bestMove));
		// System.out.println("result:" + i);
		// System.out.println(count);
		return resolveMove();
	}

	private static final int[] calcMovingEnemiesID(int turn) {
		int id = GameManager.SAMURAI_ID, enemyIndex = GameManager.ENEMY_INDEX, tmp = id + enemyIndex - 3;
		if (id != 1 && id != 4) {
			return turn % 12 == (id << 1) ? new int[] { enemyIndex, enemyIndex + 1, enemyIndex + 2 }
					: new int[] { enemyIndex + (tmp == 0 ? 1 : 0), enemyIndex + (tmp == 2 ? 1 : 2) };
		} else {
			if (id == 1)
				return turn == 8 ? new int[] { 3, 4, 5 } : new int[] { 3, 5 };
			else
				return turn == 2 ? new int[] { 0, 1, 2 } : new int[] { 0, 2 };
		}
	}

	private final void plan(Board board, int[] movingEnemiesID) throws CloneNotSupportedException {
		int length = movingEnemiesID.length;
		for (int i = 0; i < length; ++i)
			alphaBetaPruning(board, movingEnemiesID[i], i, -BOUND, BOUND, 2);

		bestMove = possibleBestMoves[0];
		for (int i = 1, result = Evaluator.evaluate(currentBoard.makeMove(possibleBestMoves[0])); i < length; ++i) {
			int value = Evaluator.evaluate(currentBoard.makeMove(possibleBestMoves[i]));
			if (value > result) {
				result = value;
				bestMove = possibleBestMoves[i];
			}
		}
	}

	private final int alphaBetaPruning(Board board, int movingEnemyID, int ordinal, int alpha, int beta, int depth)
			throws CloneNotSupportedException {
		if (depth == 0 || board.isEnd())
			return Evaluator.evaluate(board);
		Board nextBoard;
		int value;
		while (board.hasMoreMove()) {
			Move nextMove = board.nextMove();
			if (depth == 2) {
				currentBranches[ordinal] = nextMove;
				if (possibleBestMoves[ordinal] == null)
					possibleBestMoves[ordinal] = nextMove;
			}
			if (depth == 2)
				nextBoard = board.makeMove(nextMove);
			else
				nextBoard = board.makeMove(nextMove, movingEnemyID);
			value = -alphaBetaPruning(nextBoard, movingEnemyID, ordinal, -beta, -alpha, depth - 1);
			if (value >= beta)
				return beta;
			if (value > alpha) {
				// 更新alpha,同时更新bestMove
				alpha = value;
				possibleBestMoves[ordinal] = currentBranches[ordinal];
			}
		}
		return alpha;
	}

	/* 解析Move */
	private final int[] resolveMove() {
		boolean flag = true;
		if (!samurai.isVisible())
			flag = this.needsAppear();
		return modify(this.generateAction(flag), !samurai.isVisible() && flag);
	}

	/* 当回合开始时武士是隐身时，判断是否需要现身 */
	private final boolean needsAppear() {
		if (bestMove.containsOccupy())
			// 包含了占据操作则需要现身
			return true;

		int[] curPos = samurai.getPos();
		int curX = curPos[0], curY = curPos[1];
		int[] offset = bestMove.getMoveResult();
		int deltaX = offset[0], deltaY = offset[1];
		int signX = Integer.signum(deltaX), signY = Integer.signum(deltaY);

		if (!currentBoard.isFriendArea(curX + deltaX, curY + deltaY))
			// 移动的终点非友方区域则需要现身
			return true;

		// 移动起点和终点间有非友方区域时(终点是友方区域)
		switch (Math.abs(deltaX) + Math.abs(deltaY)) {
		// 移动两次
		case 2:
			// 直线移动只需判断直线上是否有非友方
			if (deltaX == 0 || deltaY == 0)
				if (currentBoard.isFriendArea(curX + deltaX >> 1, curY + deltaY >> 1))
					break;

			// 拐弯移动判断拐弯点是否都是非友方
			if (currentBoard.isFriendArea(curX, curY + deltaY) || currentBoard.isFriendArea(curX + deltaX, curY))
				break;

			return true;

		// 移动三次
		case 3:
			// 直线移动只需判断直线上是否有非友方
			if (deltaX == 0)
				if (currentBoard.isFriendArea(curX, curY + signY) && currentBoard.isFriendArea(curX, curY + signY << 1))
					break;
			if (deltaY == 0)
				if (currentBoard.isFriendArea(curX + signX, curY) && currentBoard.isFriendArea(curX + signX << 1, curY))
					break;

			// 拐弯移动判断
			// 辅助点，halfX halfY用于判断矩形是竖着还是横着
			int halfX = deltaX / 2, halfY = deltaY / 2;
			// sign用来判断象限
			int sign = signX * signY;
			int[] p1 = new int[] { halfX == 0 ? deltaX : 0, halfY == 0 ? deltaY : 0 };// p1与起点在同一短边，与终点在同一长边
			int[] p2 = new int[] { p1[1] * sign, p1[0] * sign };// p2在起点所在长边的中点，即p1关于终点所在象限的象限平分线的对称点
			int[] p3 = new int[] { p1[0], p2[1] };// p3在终点所在长边的中点
			int[] p4 = new int[] { halfX == 0 ? 0 : deltaX, halfY == 0 ? 0 : deltaY };// p4在p1的对角
			// 以上保证按照顺序，p1 p3 终点在同一边，起点 p2 p4在同一边，p2 p3分别是两条边的中点
			// 闹不明白的话自己画张图
			if ((currentBoard.isFriendArea(p1) && currentBoard.isFriendArea(p3))
					|| (currentBoard.isFriendArea(p2) && currentBoard.isFriendArea(p4))
					|| (currentBoard.isFriendArea(p2) && currentBoard.isFriendArea(p3)))
				break;
			return true;
		}
		return false;
	}

	/* 解析Move生成Action，考虑了是否隐身 */
	private final int[] generateAction(boolean flag) {
		int[] result = simpleResolve();
		// 行动开始指已经经过了开局的现身操作
		// 行动开始时处于现身状态，则直接解析
		if (flag)
			return result;

		// 行动开始时处于隐身状态，需要规划路线
		// 此处指已经经过了判断，得出结论不需要现身
		int[] curPos = samurai.getPos();
		int[] offset = bestMove.getMoveResult();
		int deltaX = offset[0], deltaY = offset[1];

		switch (Math.abs(deltaX) + Math.abs(deltaY)) {
		// 移动两次
		case 2:
			// 直线移动直接解析
			if (deltaX == 0 || deltaY == 0)
				return result;

			// 拐弯移动判断拐弯点是否都是非友方
			// 由于是Move里以水平方向优先，所以先水平后垂直时，直接解析
			if (currentBoard.isFriendArea(curPos[0] + deltaX, curPos[1]))
				return result;

			// 先垂直后水平时，调换顺序
			return new int[] { result[1], result[0] };

		// 移动三次
		case 3:
			// 直线移动直接解析
			if (deltaX == 0 || deltaY == 0)
				return result;

			// 拐弯移动判断
			int halfX = deltaX / 2, halfY = deltaY / 2;
			int sign = Integer.signum(deltaX) * Integer.signum(deltaY);
			int[] p1 = new int[] { halfX == 0 ? deltaX : 0, halfY == 0 ? deltaY : 0 };
			int[] p2 = new int[] { p1[1] * sign, p1[0] * sign };
			int[] p3 = new int[] { p1[0], p2[1] };
			int[] p4 = new int[] { halfX == 0 ? 0 : deltaX, halfY == 0 ? 0 : deltaY };

			// 竖着的矩形，对应result S -> p1 -> p3 -> E
			if (halfX == 0) {
				// 路线S -> p1 -> p3 -> E
				if (currentBoard.isFriendArea(p1) && currentBoard.isFriendArea(p3))
					return result;
				// 路线S -> p2 -> p4 -> E
				else if (currentBoard.isFriendArea(p2) && currentBoard.isFriendArea(p4))
					return new int[] { result[0], result[2], result[0] };
				// 路线S -> p2 -> p3 -> E
				else
					return new int[] { result[0], result[0], result[2] };
			}

			// 躺着的矩形，对应result S -> p2 -> p4 -> E
			if (halfY == 0) {// 这句可以不用写，但是那样就不好看了
				// 路线S -> p2 -> p4 -> E
				if (currentBoard.isFriendArea(p2) && currentBoard.isFriendArea(p4))
					return result;
				// 路线S -> p1 -> p3 -> E
				else if (currentBoard.isFriendArea(p1) && currentBoard.isFriendArea(p3))
					return new int[] { result[2], result[0], result[0] };
				// 路线S -> p2 -> p3 -> E
				else
					return new int[] { result[0], result[2], result[0] };
			}
			// 移动一次
		default:
			return result;
		}

	}

	/* 根据Move的名字进行直接解析 */
	private final int[] simpleResolve() {
		String[] tmp = bestMove.name().split("_");
		int[] result = new int[tmp.length];
		for (int i = 0; i < tmp.length; ++i)
			result[i] = ACTION_INDEX.indexOf(tmp[i]) + 1;
		return result;
	}

	/* 最终调整，加上隐身和现身 */
	private static final int[] modify(int[] unmodifiedActions, boolean needsAppear) {
		int tmp = 0, tmp1 = needsAppear ? 1 : 0;
		for (int i : unmodifiedActions)
			tmp += 5 <= i && i <= 8 ? 2 : (1 <= i && i <= 4 ? 4 : 0);
		int length = tmp1 + unmodifiedActions.length + ((tmp + tmp1) < 7 ? 1 : 0);
		int[] result = new int[length];
		if (needsAppear)
			result[0] = 10;
		System.arraycopy(unmodifiedActions, 0, result, tmp1, unmodifiedActions.length);
		if (tmp + tmp1 < 7)
			result[length - 1] = 9;
		return result;
	}
}
