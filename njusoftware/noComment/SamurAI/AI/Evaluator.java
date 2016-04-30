package njusoftware.noComment.SamurAI.AI;

import njusoftware.noComment.SamurAI.base.Board;
import njusoftware.noComment.SamurAI.base.ConstVar;
import njusoftware.noComment.SamurAI.base.GameManager;
import njusoftware.noComment.SamurAI.base.Samurai;
import njusoftware.noComment.SamurAI.base.Weapons;

/*
 * ������
 * */
public class Evaluator {
	/* �������� */
	public static final int evaluate(Board board) {
		// count++;
		// System.out.println(count);
		// ������������Զ���ҷ��ӽ�������
		// ������Ϸ����˫����ÿһ�غϽ�����еģ����������һ�غ����ѷ��غϣ�
		// ��Ҫ������ֵ���ϸ��ţ���������֦�㷨�еĸ���
		// ����ż���غϲ����ߣ���ż���غϼӸ���
		int captureDiff = diffCapture(board);
		int attackRisk = evaluateRisk(board);
		int aliveComparision = compareAlive(board);

		return (ConstVar.CAPTURE_POW * captureDiff + ConstVar.RISK_POW * attackRisk
				+ ConstVar.ALIVE_POW * aliveComparision);
	}

	private static final int compareAlive(Board board) {
		Samurai[] samurais = board.samurais;
		int result = 0;
		for (int i = 0; i < 6; ++i)
			result += samurais[i].isAlive() ? 0 : (i < 3 ? 1 : -1);
		return result * (GameManager.FRIEND_INDEX > 2 ? -1 : 1);
	}

	/* ����ռ������� */
	private static final int diffCapture(Board board) {
		int[][] battleField = board.getBattleField();
		int tmp = GameManager.FRIEND_INDEX;
		int result = 0;
		for (int[] row : battleField)
			for (int grid : row)
				result += grid == 8 ? 0 : (!(grid > 2 ^ tmp == 3) ? 1 : -1);

		return result;
	}

	/* �ж�˫�����ܱ�������Σ�� */
	private static final int evaluateRisk(Board board) {
		int turn = board.getTurn();// ע����ڽ��й�һ��move��board���غ����Ѿ�����һ
		int tmpValue = (turn & 1) == 0 ? 4 : 5;// ÿ�����ж�֮��ļ�������ֲ�ͬ��������ж�������������ͬ
		int startIndex = turn % 12;// ��ʼ�����ж�����ʿ�ı��
		int[] ordinals = new int[tmpValue];// ȡ��������Ҫ�����ж�����ʿ�ı��
		for (int i = 0; i < tmpValue; i++)
			ordinals[i] = ConstVar.ACTION_ORDER[(startIndex + i) % 12];

		Samurai[] samurais = board.samurais;

		int[] values = new int[6];// ��ŷ�������ֵ��������Ȼ˳��������ж�˳��

		// �������������ֲ�ͬ��������������б����
		// ���ݱ�Ž�����ֵ�ŵ�values����Ӧλ��
		// ûʲô�������Բ���ѭ���ˣ�ȫ��д�˳���
		// ������ʿ��������״̬ʱ�����ܵ�Ӱ��
		// ��һ��ʵ�ڲ������ţ�����Ϊ��Ч�ʲ�����Ϊ֮
		if (tmpValue == 4) {
			// ��һ����ʿ��������Ӱ��
			values[ordinals[0]] = 0;

			// �ڶ������һ���жԣ��ܵ�һ��Ӱ��
			values[ordinals[1]] = samurais[ordinals[1]].isAlive()
					? atkRiskBy(samurais[ordinals[1]], samurais[ordinals[0]]) : 0;

			// ���������һ���жԣ��ܵ�һ��Ӱ��
			values[ordinals[2]] = samurais[ordinals[2]].isAlive()
					? atkRiskBy(samurais[ordinals[2]], samurais[ordinals[0]]) : 0;

			// ���������һ���Ѻã��ܶ���Ӱ��
			values[ordinals[3]] = samurais[ordinals[3]].isAlive()
					? atkRiskBy(samurais[ordinals[3]], samurais[ordinals[1]])
							+ atkRiskBy(samurais[ordinals[3]], samurais[ordinals[2]])
					: 0;

			// �����ж������е���ʿ������һ���Ǹոս��й��ж�����ʿ
			// �����һ���Ѻã��ܶ���Ӱ��
			int lastOne = ConstVar.ACTION_ORDER[startIndex > 0 ? startIndex - 1 : 11];
			values[lastOne] = samurais[lastOne].isAlive() ? atkRiskBy(samurais[lastOne], samurais[ordinals[1]])
					+ atkRiskBy(samurais[lastOne], samurais[ordinals[2]]) : 0;

			// ��һ����ոս��й��ж�����ʿ��ͬ�������ǵж�
			// �����һ���жԣ���һ��Ӱ��
			int lastTwo = lastOne > 2 ? lastOne - 3 : lastOne + 3;
			values[lastTwo] = samurais[lastTwo].isAlive() ? atkRiskBy(samurais[lastTwo], samurais[ordinals[0]])
					+ atkRiskBy(samurais[lastTwo], samurais[ordinals[3]]) : 0;
		} else {
			// ��һ����ʿ��������Ӱ�죬�ڶ������һ���Ѻã�����Ӱ��
			values[ordinals[0]] = values[ordinals[1]] = 0;

			// ���������һ���жԣ���һ��Ӱ��
			values[ordinals[2]] = samurais[ordinals[2]].isAlive()
					? atkRiskBy(samurais[ordinals[2]], samurais[ordinals[0]])
							+ atkRiskBy(samurais[ordinals[2]], samurais[ordinals[1]])
					: 0;

			// ���ĸ����һ���жԣ���һ��Ӱ��
			values[ordinals[3]] = samurais[ordinals[3]].isAlive()
					? atkRiskBy(samurais[ordinals[3]], samurais[ordinals[0]])
							+ atkRiskBy(samurais[ordinals[3]], samurais[ordinals[1]])
					: 0;

			// ��������һ���Ѻã�������Ӱ��
			values[ordinals[4]] = samurais[ordinals[4]].isAlive()
					? atkRiskBy(samurais[ordinals[4]], samurais[ordinals[2]])
							+ atkRiskBy(samurais[ordinals[4]], samurais[ordinals[3]])
					: 0;

			// �����ж������е���ʿ���ոս��й��ж�����ʿ
			// �����һ���жԣ���һ����Ӱ��
			int lastOne = ConstVar.ACTION_ORDER[startIndex > 0 ? startIndex - 1 : 11];
			values[lastOne] = samurais[lastOne].isAlive() ? atkRiskBy(samurais[lastOne], samurais[ordinals[0]])
					+ atkRiskBy(samurais[lastOne], samurais[ordinals[1]])
					+ atkRiskBy(samurais[lastOne], samurais[ordinals[4]]) : 0;
		}

		// for (int i = 0; i < 6; i++)
		// System.out.print(values[i] + " ");

		int result = values[0] + values[1] + values[2] - values[3] - values[4] - values[5];
		return result * (GameManager.SAMURAI_ID > 2 ? -1 : 1);// �����ҷ����ĸ������������
	}

	/* ����һ����ʿ����һ����ʿ�����ķ���ֵ */
	private static final int atkRiskBy(Samurai negative, Samurai positive) {
		int[] pos1 = negative.getPos();
		int[] pos2 = positive.getPos();
		int[] delta = new int[] { pos2[0] - pos1[0], pos2[1] - pos1[1] };
		Weapons weapon = positive.getWeapon();

		// �ȴ����жϷ�Χ���ڷ�Χ��ֱ�ӷ���0
		switch (weapon) {
		case SPEAR:
			if (Math.abs(delta[0]) + Math.abs(delta[1]) > 5)
				return 0;
		case SWORD:
		case AXE:
			if (Math.abs(delta[0]) + Math.abs(delta[1]) > 3)
				return 0;
		}

		// ȡ�ù����������ֲ�
		int[][][] atkDistribution = null;
		switch (weapon) {
		case SPEAR:
			atkDistribution = ConstVar.SPEAR_ATK_DISTRIBUTION;
			break;
		case SWORD:
			atkDistribution = ConstVar.SWORD_ATK_DISTRIBUTION;
			break;
		case AXE:
			atkDistribution = ConstVar.AXE_ATK_DISTRIBUTION;
			break;
		}

		// �������ң���������±�+1��Ϊ����ֵ
		int length = atkDistribution.length;
		for (int i = 0; i < length; ++i)
			for (int[] pos : atkDistribution[i])
				if (pointEqual(delta, pos))
					return ++i;

		return 0;
	}

	/* �ж�������� */
	final static public boolean pointEqual(int[] p1, int[] p2) {
		// ����ʹ��Arrays.equals�����鳤��ȷ����2
		return p1[0] == p2[0] && p1[1] == p2[1];
	}
}