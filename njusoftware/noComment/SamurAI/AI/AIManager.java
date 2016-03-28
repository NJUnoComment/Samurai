
/**
 * @author clefz created at 2016/3/23
 *
 *         clefz 
 *         2016/3/27 23:17
 *         ����AI�࣬��װAlphaBeta�㷨
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

	/* ���������� - Alpha-Beta��֦ */
	private int alphaBetaPruning(Board board, int depth, int alpha, int beta) throws CloneNotSupportedException {
		if (depth == 0 || board.isEnd())
			return gameManger.evaluate(board);
		Board nextBoard;
		int best = -BOUND - 1;
		while (board.hasMoreValidMove()) {
			Move nextMove = board.nextValidMove();
			if (depth == 1)
				// curMoveBranch���ٵ�ǰ�������߷���֧
				curMoveBranch = nextMove;
			if (bestMove.equals(null))
				// bestMove�ĳ�ʼֵ���������߷���֧
				bestMove = nextMove;
			nextBoard = board.makeMove(nextMove);
			int value = -alphaBetaPruning(nextBoard, depth - 1, -beta, -alpha);// ����
			if (value > best)
				best = value;
			if (best > alpha) {
				// ����alpha,ͬʱ����bestMove
				alpha = best;
				if (!bestMove.equals(curMoveBranch))
					bestMove = curMoveBranch;
			}
			if (best >= beta)
				break;// ��֦
		}
		return best;
	}

	private int[] resolveMove(Move move) {
		return null;
	}
}
