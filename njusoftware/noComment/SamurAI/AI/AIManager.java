
/**
 * @author clefz created at 2016/3/23
 *
 *         clefz 
 *         2016/3/27 23:17
 *         核心AI类，封装AlphaBeta算法
 */
package njusoftware.noComment.SamurAI.AI;

import njusoftware.noComment.SamurAI.base.*;

public class AIManager {
	private GameManager gameManger;
	private final static int MAX_DEPTH = 12;
	private final static int BOUND = Integer.MAX_VALUE - 1;
	private Move bestMove;
	private Move curMoveBranch;

	public AIManager(GameManager gameManager) {
		this.gameManger = gameManager;
	}

	public int[] decideActions() throws CloneNotSupportedException {
		bestMove = null;
		curMoveBranch = null;
		alphaBetaPruning(gameManger.getBoard(), MAX_DEPTH, -BOUND, BOUND);
		return resolveMove(bestMove);
	}

	/* 决策树搜索 - Alpha-Beta剪枝 */
	private int alphaBetaPruning(Board board, int depth, int alpha, int beta) throws CloneNotSupportedException {
		if (depth == 0 || board.isEnd())
			return gameManger.evaluate(board);
		Board nextBoard;
		int best = -BOUND - 1;
		while (board.hasMoreValidMove()) {
			Move nextMove = board.nextValidMove();
			if (depth == 1)
				// curMoveBranch跟踪当前遍历的走法分支
				curMoveBranch = nextMove;
			if (bestMove.equals(null))
				// bestMove的初始值是最左侧的走法分支
				bestMove = nextMove;
			nextBoard = board.makeMove(nextMove);
			int value = -alphaBetaPruning(nextBoard, depth - 1, -beta, -alpha);// 迭代
			if (value > best)
				best = value;
			if (best > alpha) {
				// 更新alpha,同时更新bestMove
				alpha = best;
				if (!bestMove.equals(curMoveBranch))
					bestMove = curMoveBranch;
			}
			if (best >= beta)
				break;// 剪枝
		}
		return best;
	}

	private int[] resolveMove(Move move) {
		return null;
	}
}
