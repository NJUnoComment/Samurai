
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

	/* ���������� Alpha-Beta��֦ */
	private int alphaBetaPruning(Board board, int depth, int alpha, int beta) throws CloneNotSupportedException {
		if (depth == 0 || board.isEnd())
			return gameManger.evaluate(board);
		Board nextBoard;
		while (board.hasMoreMove()) {
			Move nextMove = board.nextMove();
			if (depth == MAX_DEPTH)
				// curMoveBranch���ٵ�ǰ�������߷���֧
				curMoveBranch = nextMove;
			if (bestMove == null)
				// bestMove�ĳ�ʼֵ���������߷���֧
				bestMove = curMoveBranch;
			nextBoard = board.makeMove(nextMove);
			int value = -alphaBetaPruning(nextBoard, depth - 1, -beta, -alpha);// ����
			if (value >= beta)
				return value;
			if (value > alpha) {
				// ����alpha,ͬʱ����bestMove
				alpha = value;
				if (!bestMove.equals(curMoveBranch) && curMoveBranch != null)
					bestMove = curMoveBranch;
			}
		}
		return alpha;
	}

	/* ����Move */
	private int[] resolveMove() {
		boolean flag = true;
		if (!curSamurai.isVisible())
			flag = this.needsAppear();
		return this.modify(this.generateAction(flag), flag && !curSamurai.isVisible());
	}

	/* ���غϿ�ʼʱ��ʿ������ʱ���ж��Ƿ���Ҫ���� */
	private boolean needsAppear() {
		if (bestMove.containsOccupy())
			// ������ռ�ݲ�������Ҫ����
			return true;

		int[] curPos = curSamurai.getPos();
		int[] offset = bestMove.getMoveResult();
		if (!curBoard.isFriendArea(curPos[0] + offset[0], curPos[1] + offset[1]))
			// �ƶ����յ���ѷ���������Ҫ����
			return true;

		// �ƶ������յ���з��ѷ�����ʱ(�յ����ѷ�����)
		switch (offset[0] + offset[1]) {
		case 2:// �ƶ�����
				// ֱ���ƶ�ֻ���ж�ֱ�����Ƿ��з��ѷ�
			if (offset[0] == 0)
				if (curBoard.isFriendArea(curPos[0], curPos[1] + offset[1] >> 1))
					break;
			if (offset[1] == 0)
				if (curBoard.isFriendArea(curPos[0] + offset[0] >> 1, curPos[1]))
					break;
			// �����ƶ��жϹ�����Ƿ��Ƿ��ѷ�
			if (curBoard.isFriendArea(curPos[0], curPos[1] + offset[1])
					|| curBoard.isFriendArea(curPos[0] + offset[0], curPos[1]))
				break;
			return true;

		case 3:// �ƶ�����
				// ֱ���ƶ�ֻ���ж�ֱ�����Ƿ��з��ѷ�
			if (offset[0] == 0)
				if (curBoard.isFriendArea(curPos[0], curPos[1] + offset[1] / 3)
						&& curBoard.isFriendArea(curPos[0], curPos[1] + (offset[1] << 1) / 3))
					break;
			if (offset[1] == 0)
				if (curBoard.isFriendArea(curPos[0] + offset[0] / 3, curPos[1])
						&& curBoard.isFriendArea(curPos[0] + (offset[0] << 1) / 3, curPos[1]))
					break;

			// �����ƶ��ж�
			// �����㣬halfX halfY�����жϾ��������Ż��Ǻ���
			int halfX = offset[0] / 2, halfY = offset[1] / 2;
			// sign�����ж�����
			int sign = Integer.signum(offset[1]) * Integer.signum(offset[0]);
			int[] p1 = new int[] { halfX == 0 ? offset[0] : 0, halfY == 0 ? offset[1] : 0 };// p1�������ͬһ�̱ߣ����յ���ͬһ����
			int[] p2 = new int[] { p1[1] * sign, p1[0] * sign };// p2��������ڳ��ߵ��е㣬��p1�����յ��������޵�����ƽ���ߵĶԳƵ�
			int[] p3 = new int[] { p1[0], p2[1] };// p3���յ����ڳ��ߵ��е�
			int[] p4 = new int[] { halfX == 0 ? 0 : offset[0], halfY == 0 ? 0 : offset[1] };// p4��p1�ĶԽ�
			// ���ϱ�֤����˳��p1 p3 �յ���ͬһ�ߣ���� p2 p4��ͬһ�ߣ�p2 p3�ֱ��������ߵ��е�
			// �ֲ����׵Ļ��Լ�����ͼ
			if ((curBoard.isFriendArea(p1) && curBoard.isFriendArea(p3))
					|| (curBoard.isFriendArea(p2) && curBoard.isFriendArea(p4))
					|| (curBoard.isFriendArea(p2) && curBoard.isFriendArea(p3)))
				break;
			return true;
		}
		return false;
	}

	/* ����Move����Action���������Ƿ����� */
	private int[] generateAction(boolean flag) {
		int[] result = simpleResolve();

		// �ж���ʼʱ��������״̬����ֱ�ӽ���
		if (flag)
			return result;

		// �ж���ʼʱ��������״̬����Ҫ�滮·��
		int[] curPos = curSamurai.getPos();
		int[] offset = bestMove.getMoveResult();

		switch (offset[0] + offset[1]) {
		// �ƶ�����
		case 2:
			// ֱ���ƶ�ֱ�ӽ���
			if (offset[0] == 0 || offset[1] == 0)
				return result;

			// �����ƶ��жϹ�����Ƿ��Ƿ��ѷ�
			// ������Move����ˮƽ�������ȣ�������ˮƽ��ֱʱ��ֱ�ӽ���
			if (curBoard.isFriendArea(curPos[0] + offset[0], curPos[1]))
				return result;

			// �ȴ�ֱ��ˮƽʱ������˳��
			return new int[] { result[1], result[0] };

		// �ƶ�����
		case 3:
			// ֱ���ƶ�ֱ�ӽ���
			if (offset[0] == 0 || offset[1] == 0)
				return result;

			// �����ƶ��ж�
			int halfX = offset[0] / 2, halfY = offset[1] / 2;
			int sign = Integer.signum(offset[1]) * Integer.signum(offset[0]);
			int[] p1 = new int[] { halfX == 0 ? offset[0] : 0, halfY == 0 ? offset[1] : 0 };
			int[] p2 = new int[] { p1[1] * sign, p1[0] * sign };
			int[] p3 = new int[] { p1[0], p2[1] };
			int[] p4 = new int[] { halfX == 0 ? 0 : offset[0], halfY == 0 ? 0 : offset[1] };

			// ���ŵľ��Σ���Ӧresult S -> p1 -> p3 -> E
			if (halfX == 0)
				// ·��S -> p1 -> p3 -> E
				if (curBoard.isFriendArea(p1) && curBoard.isFriendArea(p3))
				return result;
				// ·��S -> p2 -> p4 -> E
				else if (curBoard.isFriendArea(p2) && curBoard.isFriendArea(p4))
				return new int[] { result[0], result[2], result[0] };
				// ·��S -> p2 -> p3 -> E
				else
				return new int[] { result[0], result[0], result[2] };

			// ���ŵľ��Σ���Ӧresult S -> p2 -> p4 -> E
			if (halfY == 0)// �����Բ���д�����������Ͳ��ÿ���
				// ·��S -> p2 -> p4 -> E
				if (curBoard.isFriendArea(p2) && curBoard.isFriendArea(p4))
					return result;
				// ·��S -> p1 -> p3 -> E
				else if (curBoard.isFriendArea(p1) && curBoard.isFriendArea(p3))
					return new int[] { result[2], result[0], result[0] };
				// ·��S -> p2 -> p3 -> E
				else
					return new int[] { result[0], result[2], result[0] };

			// �ƶ�һ��
		default:
			return result;
		}
	}

	/* ����Move�����ֽ���ֱ�ӽ��� */
	private int[] simpleResolve() {
		String[] tmp = bestMove.name().split("_");
		int[] result = new int[tmp.length];
		for (int i = 0; i < tmp.length; i++)
			result[i] = ACTION_INDEX.indexOf(tmp[i]) + 1;
		return result;
	}

	/* ���յ������������������ */
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
