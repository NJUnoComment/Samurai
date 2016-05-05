package njusoftware.noComment.SamurAI.AI;

import njusoftware.noComment.SamurAI.base.*;

/*
 * ������
 * */
public class Evaluator {
	/* �������� */
	public static final int evaluate(Board board) {
		// count++;
		// System.out.println(count);
		// ������������Զ���ҷ��ӽ�������
		int captureDiff = diffCapture(board);
		int attackRisk = evaluateRisk(board);
		int positionEvaluation = evaluatePosition(board);
//		System.out.println(attackRisk);
//		System.out.println(captureDiff);
//		GameManager.print(board.getBattleField());
		// System.out.println(positionEvaluation);
		// System.err.println("----------------------");
		return (ConstVar.CAPTURE_POW * captureDiff + ConstVar.RISK_POW * attackRisk
				+ ConstVar.POSITION_POW * positionEvaluation);
	}

	/* ����ռ������� */
	public static final int diffCapture(Board board) {
		int[][] battleField = board.getBattleField();
		int result = 0;
		for (int[] row : battleField)
			for (int grid : row)
				result += GameManager.isFriendID(grid) ? 1 : (grid == 8 ? 0 : -1);
		// System.out.print(result);
		return result;
	}

	/* �ж�˫�����ܱ�������Σ�� */
	private static final int evaluateRisk(Board board) {
		Samurai[] samurais = board.samurais;
		int result = 0;
		for (int i = 0; i < 3; i++)
			result += 13 * samurais[i + GameManager.ENEMY_INDEX].getAtkedRisk();
		result -= samurais[GameManager.SAMURAI_ID].getAtkedRisk();

		return result;
	}

	/* ���ᱻ��������ʿ�����ϱ��������� */
	public static final void attachRisk(Board board, int atkingSamuraiID, int[][] atkRange) {
		if ((atkingSamuraiID < 3) ^ (GameManager.FRIEND_INDEX == 3))
			for (int enemyIndex = GameManager.ENEMY_INDEX, bound = enemyIndex + 3, i = 0; i < bound; i++)
				calcRisk(board, i, atkingSamuraiID, atkRange);
		else
			calcRisk(board, GameManager.SAMURAI_ID, atkingSamuraiID, atkRange);
	}

	/* ����һ����ʿ����һ����ʿ�����ķ���ֵ */
	private static final void calcRisk(Board board, int negativeSamuraiID, int positiveSamuraiID, int[][] atkRange) {
		Samurai[] samurais = board.samurais;
		Samurai positiveSamurai = samurais[positiveSamuraiID], negativeSamurai = samurais[negativeSamuraiID];
		boolean isFriendAttacking = (positiveSamuraiID < 3) ^ (GameManager.FRIEND_INDEX == 3);
		boolean isEnemyVisible = isFriendAttacking ? negativeSamurai.isVisible() : positiveSamurai.isVisible();

		if (isEnemyVisible)
			visibleEnemy(negativeSamurai, positiveSamurai, atkRange, isFriendAttacking);
		else
			invisibleEnemy(negativeSamurai, positiveSamurai, atkRange, isFriendAttacking);
	}

	/* �з����ɼ�ʱ */
	private static final void invisibleEnemy(Samurai negativeSamurai, Samurai positiveSamurai, int[][] atkRange,
			boolean isFriendAttacking) {
		int[] pos1 = negativeSamurai.getPos(), pos2 = positiveSamurai.getPos();
		int[] delta = new int[] { pos1[0] - pos2[0], pos1[1] - pos2[1] };

		int[][] potentialDelta = ConstVar.getPotentialPos(delta);

		int result = 0;
		for (int[] tmpPos : potentialDelta)
			if (containsPoint(tmpPos, atkRange))
				result++;

		negativeSamurai.setAtkedRisk(isFriendAttacking ? result : (result * (13 - positiveSamurai.getAtkedRisk())));
	}

	/* �з��ɼ�ʱ */
	private static final void visibleEnemy(Samurai negativeSamurai, Samurai positiveSamurai, int[][] atkRange,
			boolean isFriendAttacking) {
		int[] pos1 = negativeSamurai.getPos(), pos2 = positiveSamurai.getPos();
		int[] delta = new int[] { pos1[0] - pos2[0], pos1[1] - pos2[1] };

		if (containsPoint(delta, atkRange))
			negativeSamurai.setAtkedRisk(isFriendAttacking ? 13 : 13 * (13 - positiveSamurai.getAtkedRisk()));
	}

	private static final int evaluatePosition(Board board) {
		int[] pos = board.samurais[GameManager.SAMURAI_ID].getPos();
		int[][] battleField = board.getBattleField();
		// System.err.println(pos[0]+","+pos[1]);
		if (!GameManager.isFriendID(battleField[pos[1]][pos[0]]))
			return 0;

		int[][] manhattanDistance = null;
		for (int i = 1; i < 8; i++) {
			manhattanDistance = ConstVar.manhattanDistance(i);
			for (int[] tmpPos : manhattanDistance)
				if (GameManager.inBound(pos[0], tmpPos[0], pos[1], tmpPos[1]))
					if (!GameManager.isFriendID(battleField[pos[1] + tmpPos[1]][pos[0] + tmpPos[0]]))
						return -i;
		}
		return 0;
	}

	/* �ж����Ƿ�����ڷ�Χ�� */
	public static final boolean containsPoint(int[] point, int[][] range) {
		for (int[] tmpPos : range)
			if (pointEqual(point, tmpPos))
				return true;
		return false;
	}

	/* �ж�������� */
	public static final boolean pointEqual(int[] p1, int[] p2) {
		// ����ʹ��Arrays.equals�����鳤��ȷ����2
		return p1[0] == p2[0] && p1[1] == p2[1];
	}
}