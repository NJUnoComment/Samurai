
/**
 * @author clefz created at 2016/3/23
 *
 *         //���ڴ˴���ע��д������ 
 *         ���ڴ˴���д�����޸�ʱ��
 *         ����AI�࣬��װAlphaBeta�㷨����������
 */
package njusoftware.noComment.SamurAI.AI;

import java.util.Stack;

import njusoftware.noComment.SamurAI.base.Board;
import njusoftware.noComment.SamurAI.base.GameManager;
import njusoftware.noComment.SamurAI.base.Move;

public class AIManager {
	private GameManager gameManger;
	private static Stack<Move> stack;
	private final static int MAX_DEPTH = 12;
	private final static int BOUND = Integer.MAX_VALUE - 1;

	public AIManager(GameManager gameManager) {
		this.gameManger = gameManager;
		stack = new Stack<>();
	}

	public int[] decideActions() throws CloneNotSupportedException {
		alphaBetaPruning(gameManger.getBoard(), MAX_DEPTH, -BOUND, BOUND);
	}

	private int alphaBetaPruning(Board board, int depth, int alpha, int beta) throws CloneNotSupportedException {
		if (depth == 0 || board.isEnd())
			return gameManger.evaluate(board);
		Board nextBoard;
		int best = -BOUND - 1;
		while (board.hasMoreValidMove()) {
			Move nextValidMove = board.nextValidMove();
			nextBoard = board.makeMove(nextValidMove);
			stack.push(nextValidMove);
			int value = -alphaBetaPruning(nextBoard, depth - 1, -beta, -alpha);// ����
			if (value > best)
				best = value;
			if (best > alpha)
				alpha = best;// ����alpha
			if (best >= beta)
				break;// ��֦
		}
		if (depth != MAX_DEPTH - 1)
			stack.pop();
		return best;
	}
}
