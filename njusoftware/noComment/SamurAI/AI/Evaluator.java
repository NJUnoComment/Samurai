package njusoftware.noComment.SamurAI.AI;

import njusoftware.noComment.SamurAI.base.Board;
import njusoftware.noComment.SamurAI.base.ConstVar;
import njusoftware.noComment.SamurAI.base.GameManager;
import njusoftware.noComment.SamurAI.base.Samurai;
import njusoftware.noComment.SamurAI.base.Weapons;

/*
 * 评估类
 * */
public class Evaluator {
	/* 评估函数 */
	public static final int evaluate(Board board) {
		// count++;
		// System.out.println(count);
		// 评估函数将永远从我方视角来评估
		// 由于游戏不是双方按每一回合交错进行的，所以如果上一回合是友方回合，
		// 则要将评估值加上负号，来抵消剪枝算法中的负号
		// 所有偶数回合不换边，即偶数回合加负号
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

	/* 计算占领面积差 */
	private static final int diffCapture(Board board) {
		int[][] battleField = board.getBattleField();
		int tmp = GameManager.FRIEND_INDEX;
		int result = 0;
		for (int[] row : battleField)
			for (int grid : row)
				result += grid == 8 ? 0 : (!(grid > 2 ^ tmp == 3) ? 1 : -1);

		return result;
	}

	/* 判定双方可能被攻击的危险 */
	private static final int evaluateRisk(Board board) {
		int turn = board.getTurn();// 注意对于进行过一次move的board，回合数已经被加一
		int tmpValue = (turn & 1) == 0 ? 4 : 5;// 每两次行动之间的间隔有两种不同的情况，判定方法会有所不同
		int startIndex = turn % 12;// 开始进行判定的武士的标号
		int[] ordinals = new int[tmpValue];// 取出所有需要进行判定的武士的标号
		for (int i = 0; i < tmpValue; i++)
			ordinals[i] = ConstVar.ACTION_ORDER[(startIndex + i) % 12];

		Samurai[] samurais = board.samurais;

		int[] values = new int[6];// 存放风险评估值，按照自然顺序而不是行动顺序

		// 如果不能理解两种不同的情况，请自行列表分析
		// 根据标号将风险值放到values的相应位置
		// 没什么规律所以不用循环了，全部写了出来
		// 所有武士处于受伤状态时不会受到影响
		// 这一段实在不够优雅，但是为了效率不得已为之
		if (tmpValue == 4) {
			// 第一个武士不受其他影响
			values[ordinals[0]] = 0;

			// 第二个与第一个敌对，受第一个影响
			values[ordinals[1]] = samurais[ordinals[1]].isAlive()
					? atkRiskBy(samurais[ordinals[1]], samurais[ordinals[0]]) : 0;

			// 第三个与第一个敌对，受第一个影响
			values[ordinals[2]] = samurais[ordinals[2]].isAlive()
					? atkRiskBy(samurais[ordinals[2]], samurais[ordinals[0]]) : 0;

			// 第三个与第一个友好，受二三影响
			values[ordinals[3]] = samurais[ordinals[3]].isAlive()
					? atkRiskBy(samurais[ordinals[3]], samurais[ordinals[1]])
							+ atkRiskBy(samurais[ordinals[3]], samurais[ordinals[2]])
					: 0;

			// 不在判定序列中的武士，其中一个是刚刚进行过行动的武士
			// 它与第一个友好，受二三影响
			int lastOne = ConstVar.ACTION_ORDER[startIndex > 0 ? startIndex - 1 : 11];
			values[lastOne] = samurais[lastOne].isAlive() ? atkRiskBy(samurais[lastOne], samurais[ordinals[1]])
					+ atkRiskBy(samurais[lastOne], samurais[ordinals[2]]) : 0;

			// 另一个与刚刚进行过行动的武士相同武器但是敌对
			// 它与第一个敌对，受一四影响
			int lastTwo = lastOne > 2 ? lastOne - 3 : lastOne + 3;
			values[lastTwo] = samurais[lastTwo].isAlive() ? atkRiskBy(samurais[lastTwo], samurais[ordinals[0]])
					+ atkRiskBy(samurais[lastTwo], samurais[ordinals[3]]) : 0;
		} else {
			// 第一个武士不受其他影响，第二个与第一个友好，不受影响
			values[ordinals[0]] = values[ordinals[1]] = 0;

			// 第三个与第一个敌对，受一二影响
			values[ordinals[2]] = samurais[ordinals[2]].isAlive()
					? atkRiskBy(samurais[ordinals[2]], samurais[ordinals[0]])
							+ atkRiskBy(samurais[ordinals[2]], samurais[ordinals[1]])
					: 0;

			// 第四个与第一个敌对，受一二影响
			values[ordinals[3]] = samurais[ordinals[3]].isAlive()
					? atkRiskBy(samurais[ordinals[3]], samurais[ordinals[0]])
							+ atkRiskBy(samurais[ordinals[3]], samurais[ordinals[1]])
					: 0;

			// 第五个与第一个友好，受三四影响
			values[ordinals[4]] = samurais[ordinals[4]].isAlive()
					? atkRiskBy(samurais[ordinals[4]], samurais[ordinals[2]])
							+ atkRiskBy(samurais[ordinals[4]], samurais[ordinals[3]])
					: 0;

			// 不在判定序列中的武士即刚刚进行过行动的武士
			// 它与第一个敌对，受一二五影响
			int lastOne = ConstVar.ACTION_ORDER[startIndex > 0 ? startIndex - 1 : 11];
			values[lastOne] = samurais[lastOne].isAlive() ? atkRiskBy(samurais[lastOne], samurais[ordinals[0]])
					+ atkRiskBy(samurais[lastOne], samurais[ordinals[1]])
					+ atkRiskBy(samurais[lastOne], samurais[ordinals[4]]) : 0;
		}

		// for (int i = 0; i < 6; i++)
		// System.out.print(values[i] + " ");

		int result = values[0] + values[1] + values[2] - values[3] - values[4] - values[5];
		return result * (GameManager.SAMURAI_ID > 2 ? -1 : 1);// 根据我方在哪个队伍调整符号
	}

	/* 计算一个武士受另一个武士攻击的风险值 */
	private static final int atkRiskBy(Samurai negative, Samurai positive) {
		int[] pos1 = negative.getPos();
		int[] pos2 = positive.getPos();
		int[] delta = new int[] { pos2[0] - pos1[0], pos2[1] - pos1[1] };
		Weapons weapon = positive.getWeapon();

		// 先大致判断范围，在范围外直接返回0
		switch (weapon) {
		case SPEAR:
			if (Math.abs(delta[0]) + Math.abs(delta[1]) > 5)
				return 0;
		case SWORD:
		case AXE:
			if (Math.abs(delta[0]) + Math.abs(delta[1]) > 3)
				return 0;
		}

		// 取得攻击风险区分布
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

		// 遍历查找，如果存在下标+1即为风险值
		int length = atkDistribution.length;
		for (int i = 0; i < length; ++i)
			for (int[] pos : atkDistribution[i])
				if (pointEqual(delta, pos))
					return ++i;

		return 0;
	}

	/* 判定数对相等 */
	final static public boolean pointEqual(int[] p1, int[] p2) {
		// 不必使用Arrays.equals，数组长度确定是2
		return p1[0] == p2[0] && p1[1] == p2[1];
	}
}