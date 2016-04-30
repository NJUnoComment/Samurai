
/**
 * @author clefz created at 2016/3/23
 *
 *         clefz 
 *         2016/4/8
 *         ����AI�࣬��װAlphaBeta�㷨
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
				// ����alpha,ͬʱ����bestMove
				alpha = value;
				possibleBestMoves[ordinal] = currentBranches[ordinal];
			}
		}
		return alpha;
	}

	/* ����Move */
	private final int[] resolveMove() {
		boolean flag = true;
		if (!samurai.isVisible())
			flag = this.needsAppear();
		return modify(this.generateAction(flag), !samurai.isVisible() && flag);
	}

	/* ���غϿ�ʼʱ��ʿ������ʱ���ж��Ƿ���Ҫ���� */
	private final boolean needsAppear() {
		if (bestMove.containsOccupy())
			// ������ռ�ݲ�������Ҫ����
			return true;

		int[] curPos = samurai.getPos();
		int curX = curPos[0], curY = curPos[1];
		int[] offset = bestMove.getMoveResult();
		int deltaX = offset[0], deltaY = offset[1];
		int signX = Integer.signum(deltaX), signY = Integer.signum(deltaY);

		if (!currentBoard.isFriendArea(curX + deltaX, curY + deltaY))
			// �ƶ����յ���ѷ���������Ҫ����
			return true;

		// �ƶ������յ���з��ѷ�����ʱ(�յ����ѷ�����)
		switch (Math.abs(deltaX) + Math.abs(deltaY)) {
		// �ƶ�����
		case 2:
			// ֱ���ƶ�ֻ���ж�ֱ�����Ƿ��з��ѷ�
			if (deltaX == 0 || deltaY == 0)
				if (currentBoard.isFriendArea(curX + deltaX >> 1, curY + deltaY >> 1))
					break;

			// �����ƶ��жϹ�����Ƿ��Ƿ��ѷ�
			if (currentBoard.isFriendArea(curX, curY + deltaY) || currentBoard.isFriendArea(curX + deltaX, curY))
				break;

			return true;

		// �ƶ�����
		case 3:
			// ֱ���ƶ�ֻ���ж�ֱ�����Ƿ��з��ѷ�
			if (deltaX == 0)
				if (currentBoard.isFriendArea(curX, curY + signY) && currentBoard.isFriendArea(curX, curY + signY << 1))
					break;
			if (deltaY == 0)
				if (currentBoard.isFriendArea(curX + signX, curY) && currentBoard.isFriendArea(curX + signX << 1, curY))
					break;

			// �����ƶ��ж�
			// �����㣬halfX halfY�����жϾ��������Ż��Ǻ���
			int halfX = deltaX / 2, halfY = deltaY / 2;
			// sign�����ж�����
			int sign = signX * signY;
			int[] p1 = new int[] { halfX == 0 ? deltaX : 0, halfY == 0 ? deltaY : 0 };// p1�������ͬһ�̱ߣ����յ���ͬһ����
			int[] p2 = new int[] { p1[1] * sign, p1[0] * sign };// p2��������ڳ��ߵ��е㣬��p1�����յ��������޵�����ƽ���ߵĶԳƵ�
			int[] p3 = new int[] { p1[0], p2[1] };// p3���յ����ڳ��ߵ��е�
			int[] p4 = new int[] { halfX == 0 ? 0 : deltaX, halfY == 0 ? 0 : deltaY };// p4��p1�ĶԽ�
			// ���ϱ�֤����˳��p1 p3 �յ���ͬһ�ߣ���� p2 p4��ͬһ�ߣ�p2 p3�ֱ��������ߵ��е�
			// �ֲ����׵Ļ��Լ�����ͼ
			if ((currentBoard.isFriendArea(p1) && currentBoard.isFriendArea(p3))
					|| (currentBoard.isFriendArea(p2) && currentBoard.isFriendArea(p4))
					|| (currentBoard.isFriendArea(p2) && currentBoard.isFriendArea(p3)))
				break;
			return true;
		}
		return false;
	}

	/* ����Move����Action���������Ƿ����� */
	private final int[] generateAction(boolean flag) {
		int[] result = simpleResolve();
		// �ж���ʼָ�Ѿ������˿��ֵ��������
		// �ж���ʼʱ��������״̬����ֱ�ӽ���
		if (flag)
			return result;

		// �ж���ʼʱ��������״̬����Ҫ�滮·��
		// �˴�ָ�Ѿ��������жϣ��ó����۲���Ҫ����
		int[] curPos = samurai.getPos();
		int[] offset = bestMove.getMoveResult();
		int deltaX = offset[0], deltaY = offset[1];

		switch (Math.abs(deltaX) + Math.abs(deltaY)) {
		// �ƶ�����
		case 2:
			// ֱ���ƶ�ֱ�ӽ���
			if (deltaX == 0 || deltaY == 0)
				return result;

			// �����ƶ��жϹ�����Ƿ��Ƿ��ѷ�
			// ������Move����ˮƽ�������ȣ�������ˮƽ��ֱʱ��ֱ�ӽ���
			if (currentBoard.isFriendArea(curPos[0] + deltaX, curPos[1]))
				return result;

			// �ȴ�ֱ��ˮƽʱ������˳��
			return new int[] { result[1], result[0] };

		// �ƶ�����
		case 3:
			// ֱ���ƶ�ֱ�ӽ���
			if (deltaX == 0 || deltaY == 0)
				return result;

			// �����ƶ��ж�
			int halfX = deltaX / 2, halfY = deltaY / 2;
			int sign = Integer.signum(deltaX) * Integer.signum(deltaY);
			int[] p1 = new int[] { halfX == 0 ? deltaX : 0, halfY == 0 ? deltaY : 0 };
			int[] p2 = new int[] { p1[1] * sign, p1[0] * sign };
			int[] p3 = new int[] { p1[0], p2[1] };
			int[] p4 = new int[] { halfX == 0 ? 0 : deltaX, halfY == 0 ? 0 : deltaY };

			// ���ŵľ��Σ���Ӧresult S -> p1 -> p3 -> E
			if (halfX == 0) {
				// ·��S -> p1 -> p3 -> E
				if (currentBoard.isFriendArea(p1) && currentBoard.isFriendArea(p3))
					return result;
				// ·��S -> p2 -> p4 -> E
				else if (currentBoard.isFriendArea(p2) && currentBoard.isFriendArea(p4))
					return new int[] { result[0], result[2], result[0] };
				// ·��S -> p2 -> p3 -> E
				else
					return new int[] { result[0], result[0], result[2] };
			}

			// ���ŵľ��Σ���Ӧresult S -> p2 -> p4 -> E
			if (halfY == 0) {// �����Բ���д�����������Ͳ��ÿ���
				// ·��S -> p2 -> p4 -> E
				if (currentBoard.isFriendArea(p2) && currentBoard.isFriendArea(p4))
					return result;
				// ·��S -> p1 -> p3 -> E
				else if (currentBoard.isFriendArea(p1) && currentBoard.isFriendArea(p3))
					return new int[] { result[2], result[0], result[0] };
				// ·��S -> p2 -> p3 -> E
				else
					return new int[] { result[0], result[2], result[0] };
			}
			// �ƶ�һ��
		default:
			return result;
		}

	}

	/* ����Move�����ֽ���ֱ�ӽ��� */
	private final int[] simpleResolve() {
		String[] tmp = bestMove.name().split("_");
		int[] result = new int[tmp.length];
		for (int i = 0; i < tmp.length; ++i)
			result[i] = ACTION_INDEX.indexOf(tmp[i]) + 1;
		return result;
	}

	/* ���յ������������������ */
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
