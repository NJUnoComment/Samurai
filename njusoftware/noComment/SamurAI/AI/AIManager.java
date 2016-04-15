
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
	private GameManager gameManger;
	private static final List<String> ACTION_INDEX = new ArrayList<String>(
			Arrays.asList(new String[] { "OS", "OE", "ON", "OW", "MS", "ME", "MN", "MW" }));
	private final static int MAX_DEPTH = 6;
	private final static int BOUND = Integer.MAX_VALUE - 1;
	private Board curBoard;
	private Samurai curSamurai;
	private Move bestMove;
	private Move curMoveBranch;

	public AIManager(GameManager gameManager) {
		this.gameManger = gameManager;
	}

	public int[] decideActions() throws CloneNotSupportedException {
		bestMove = null;
		curMoveBranch = null;
		curBoard = gameManger.getBoard();
		curSamurai = curBoard.getCurrentSamurai();
		alphaBetaPruning(curBoard, MAX_DEPTH, -BOUND, BOUND);
		return resolveMove();
	}

	/* 决策树搜索 Alpha-Beta剪枝 */
	private int alphaBetaPruning(Board board, int depth, int alpha, int beta) throws CloneNotSupportedException {
		if (depth == 0 || board.isEnd())
			return gameManger.evaluate(board);
		Board nextBoard;
		while (board.hasMoreMove()) {
			Move nextMove = board.nextMove();
			if (depth == MAX_DEPTH)
				// curMoveBranch跟踪当前遍历的走法分支
				curMoveBranch = nextMove;
			if (bestMove == null)
				// bestMove的初始值是最左侧的走法分支
				bestMove = curMoveBranch;
			nextBoard = board.makeMove(nextMove);
			int value = -alphaBetaPruning(nextBoard, depth - 1, -beta, -alpha);// 迭代
			if (value >= beta)
				return value;
			if (value > alpha) {
				// 更新alpha,同时更新bestMove
				alpha = value;
				if (!bestMove.equals(curMoveBranch) && curMoveBranch != null)
					bestMove = curMoveBranch;
			}
		}
		return alpha;
	}

	/* 解析Move */
	private int[] resolveMove() {
		boolean flag = true;
		if (!curSamurai.isVisible())
			flag = this.needsAppear();
		return this.modify(this.generateAction(flag), flag && !curSamurai.isVisible());
	}

	/* 当回合开始时武士是隐身时，判断是否需要现身 */
	private boolean needsAppear() {
		if (bestMove.containsOccupy())
			// 包含了占据操作则需要现身
			return true;

		int[] curPos = curSamurai.getPos();
		int[] offset = bestMove.getMoveResult();
		if (!curBoard.isFriendArea(curPos[0] + offset[0], curPos[1] + offset[1]))
			// 移动的终点非友方区域则需要现身
			return true;

		// 移动起点和终点间有非友方区域时(终点是友方区域)
		switch (offset[0] + offset[1]) {
		case 2:// 移动两次
				// 直线移动只需判断直线上是否有非友方
			if (offset[0] == 0)
				if (curBoard.isFriendArea(curPos[0], curPos[1] + offset[1] >> 1))
					break;
			if (offset[1] == 0)
				if (curBoard.isFriendArea(curPos[0] + offset[0] >> 1, curPos[1]))
					break;
			// 拐弯移动判断拐弯点是否都是非友方
			if (curBoard.isFriendArea(curPos[0], curPos[1] + offset[1])
					|| curBoard.isFriendArea(curPos[0] + offset[0], curPos[1]))
				break;
			return true;

		case 3:// 移动三次
				// 直线移动只需判断直线上是否有非友方
			if (offset[0] == 0)
				if (curBoard.isFriendArea(curPos[0], curPos[1] + offset[1] / 3)
						&& curBoard.isFriendArea(curPos[0], curPos[1] + (offset[1] << 1) / 3))
					break;
			if (offset[1] == 0)
				if (curBoard.isFriendArea(curPos[0] + offset[0] / 3, curPos[1])
						&& curBoard.isFriendArea(curPos[0] + (offset[0] << 1) / 3, curPos[1]))
					break;

			// 拐弯移动判断
			// 辅助点，halfX halfY用于判断矩形是竖着还是横着
			int halfX = offset[0] / 2, halfY = offset[1] / 2;
			// sign用来判断象限
			int sign = Integer.signum(offset[1]) * Integer.signum(offset[0]);
			int[] p1 = new int[] { halfX == 0 ? offset[0] : 0, halfY == 0 ? offset[1] : 0 };// p1与起点在同一短边，与终点在同一长边
			int[] p2 = new int[] { p1[1] * sign, p1[0] * sign };// p2在起点所在长边的中点，即p1关于终点所在象限的象限平分线的对称点
			int[] p3 = new int[] { p1[0], p2[1] };// p3在终点所在长边的中点
			int[] p4 = new int[] { halfX == 0 ? 0 : offset[0], halfY == 0 ? 0 : offset[1] };// p4在p1的对角
			// 以上保证按照顺序，p1 p3 终点在同一边，起点 p2 p4在同一边，p2 p3分别是两条边的中点
			// 闹不明白的话自己画张图
			if ((curBoard.isFriendArea(p1) && curBoard.isFriendArea(p3))
					|| (curBoard.isFriendArea(p2) && curBoard.isFriendArea(p4))
					|| (curBoard.isFriendArea(p2) && curBoard.isFriendArea(p3)))
				break;
			return true;
		}
		return false;
	}

	/* 解析Move生成Action，考虑了是否隐身 */
	private int[] generateAction(boolean flag) {
		int[] result = simpleResolve();

		// 行动开始时处于现身状态，则直接解析
		if (flag)
			return result;

		// 行动开始时处于隐身状态，需要规划路线
		int[] curPos = curSamurai.getPos();
		int[] offset = bestMove.getMoveResult();

		switch (offset[0] + offset[1]) {
		// 移动两次
		case 2:
			// 直线移动直接解析
			if (offset[0] == 0 || offset[1] == 0)
				return result;

			// 拐弯移动判断拐弯点是否都是非友方
			// 由于是Move里以水平方向优先，所以先水平后垂直时，直接解析
			if (curBoard.isFriendArea(curPos[0] + offset[0], curPos[1]))
				return result;

			// 先垂直后水平时，调换顺序
			return new int[] { result[1], result[0] };

		// 移动三次
		case 3:
			// 直线移动直接解析
			if (offset[0] == 0 || offset[1] == 0)
				return result;

			// 拐弯移动判断
			int halfX = offset[0] / 2, halfY = offset[1] / 2;
			int sign = Integer.signum(offset[1]) * Integer.signum(offset[0]);
			int[] p1 = new int[] { halfX == 0 ? offset[0] : 0, halfY == 0 ? offset[1] : 0 };
			int[] p2 = new int[] { p1[1] * sign, p1[0] * sign };
			int[] p3 = new int[] { p1[0], p2[1] };
			int[] p4 = new int[] { halfX == 0 ? 0 : offset[0], halfY == 0 ? 0 : offset[1] };

			// 竖着的矩形，对应result S -> p1 -> p3 -> E
			if (halfX == 0)
				// 路线S -> p1 -> p3 -> E
				if (curBoard.isFriendArea(p1) && curBoard.isFriendArea(p3))
				return result;
				// 路线S -> p2 -> p4 -> E
				else if (curBoard.isFriendArea(p2) && curBoard.isFriendArea(p4))
				return new int[] { result[0], result[2], result[0] };
				// 路线S -> p2 -> p3 -> E
				else
				return new int[] { result[0], result[0], result[2] };

			// 躺着的矩形，对应result S -> p2 -> p4 -> E
			if (halfY == 0)// 这句可以不用写，但是那样就不好看了
				// 路线S -> p2 -> p4 -> E
				if (curBoard.isFriendArea(p2) && curBoard.isFriendArea(p4))
					return result;
				// 路线S -> p1 -> p3 -> E
				else if (curBoard.isFriendArea(p1) && curBoard.isFriendArea(p3))
					return new int[] { result[2], result[0], result[0] };
				// 路线S -> p2 -> p3 -> E
				else
					return new int[] { result[0], result[2], result[0] };

			// 移动一次
		default:
			return result;
		}
	}

	/* 根据Move的名字进行直接解析 */
	private int[] simpleResolve() {
		String[] tmp = bestMove.name().split("_");
		int[] result = new int[tmp.length];
		for (int i = 0; i < tmp.length; i++)
			result[i] = ACTION_INDEX.indexOf(tmp[i]) + 1;
		return result;
	}

	/* 最终调整，加上隐身和现身 */
	private int[] modify(int[] unmodifiedActions, boolean needsAppear) {
		int tmp = 0;
		for (int i : unmodifiedActions)
			tmp += i == 10 ? 1 : (5 <= i && i <= 7 ? 2 : (1 <= i && i <= 4 ? 4 : 0));
		int length = (needsAppear ? 1 : 0) + unmodifiedActions.length + (tmp + (needsAppear ? 1 : 0) < 7 ? 1 : 0);
		int[] result = new int[length];
		if (needsAppear)
			result[0] = 10;
		System.arraycopy(unmodifiedActions, 0, result, needsAppear ? 1 : 0, unmodifiedActions.length);
		if (tmp + (needsAppear ? 1 : 0) < 7)
			result[length - 1] = 9;
		return result;
	}
}
