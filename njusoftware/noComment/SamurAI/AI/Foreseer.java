package njusoftware.noComment.SamurAI.AI;

import njusoftware.noComment.SamurAI.base.Board;
import njusoftware.noComment.SamurAI.base.ConstVar;
import njusoftware.noComment.SamurAI.base.GameManager;
import njusoftware.noComment.SamurAI.base.Samurai;

public class Foreseer {
	private static final Samurai[] SAMURAIS = GameManager.SAMURAIS;

	/* ͨ���Ʋ�ȥ����Ұ���� */
	public static final void inferMap(Board previousBoard, Board currentBoard) {
		int[][] current = currentBoard.getBattleField();
		int[][] previous = previousBoard.getBattleField();
		for (int i = 0; i < GameManager.HEIGHT; ++i)
			for (int j = 0; j < GameManager.WIDTH; ++j)
				if (current[i][j] == 9)
					current[i][j] = previous[i][j];
	}

	/* �Ʋ��������ʿλ�� */
	public static final void inferPosition(Board previousBoard, Board currentBoard) {
		// ֻ���Ʋ�з��ɶ��ġ�δ���˵ġ����ɼ���ʿ��λ��
		int tmp = GameManager.ENEMY_INDEX;
		for (int i = tmp; i < tmp + 3; ++i)
			if (!SAMURAIS[i].isVisible() && SAMURAIS[i].isAlive() && SAMURAIS[i].isActive())
				inferPositionOf(i, previousBoard, currentBoard);
	}

	/* �ض���ʿ��λ���Ʋ� */
	private static final void inferPositionOf(int samuraiID, Board previousBoard, Board currentBoard) {
		int[][] processedMap = processMap(samuraiID, previousBoard, currentBoard);
		// GameManager.print(processedMap);
		// System.err.println();
		int max = 0, count = 0;
		int tmpX = 0, tmpY = 0;
		// �ҳ����ֵ������
		for (int i = 0; i < GameManager.HEIGHT; ++i)
			for (int j = 0; j < GameManager.WIDTH; ++j) {
				int tmp = processedMap[i][j];
				if (tmp > max) {
					max = tmp;
					count = 0;
					tmpY = i;
					tmpX = j;
				}
				count += tmp == max ? 1 : 0;
			}

		if (count == 1) {
			SAMURAIS[samuraiID].setPosition(tmpX, tmpY);
			return;
		}

		for (int i = tmpY, index = count >> 1; i < GameManager.HEIGHT; ++i)
			for (int j = tmpX; j < GameManager.WIDTH; ++j) {
				count -= (processedMap[i][j] == max) ? 1 : 0;
				if (count > index)
					continue;
				SAMURAIS[samuraiID].setPosition(j, i);
				return;
			}
	}

	/* Ԥ�����ͼ�������ʿ�ڵ�ͼ��ĳ��λ�õĿ����ԣ���Ȩ��ֵ���� */
	private static final int[][] processMap(int samuraiID, Board previousBoard, Board currentBoard) {
		int[] curPos = SAMURAIS[samuraiID].getPos();
		int curX = curPos[0], curY = curPos[1];// ֱ��ȡ���������������Ч��

		boolean[][] markedMap = markNewCapture(samuraiID, previousBoard, currentBoard);
		boolean flag = isCaptureChanged(markedMap);
		return postprocess(flag ? preprocess(markedMap, curX, curY) : preprocess(curX, curY), curX, curY, flag);
	}

	/* ���������ռ���� */
	private static final boolean[][] markNewCapture(int samuraiID, Board previousBoard, Board currentBoard) {
		boolean[][] result = new boolean[GameManager.HEIGHT][GameManager.WIDTH];
		int[][] curBattleField = currentBoard.getBattleField();
		int[][] prevBattleField = previousBoard.getBattleField();
		for (int i = 0; i < GameManager.HEIGHT; ++i)
			for (int j = 0; j < GameManager.WIDTH; ++j)
				// �����غϸ���ʿ�����ĵؿ��ʶ����
				result[i][j] = (curBattleField[i][j] == samuraiID) && (prevBattleField[i][j] != samuraiID);

		return result;
	}

	/* ���ռ�ݷ�Χ�Ƿ����˸ı� */
	private static final boolean isCaptureChanged(boolean[][] markedMap) {
		for (boolean[] p : markedMap)
			for (boolean i : p)
				if (i)
					return true;

		return false;
	}

	/* Ԥ��������������ռ��������� */
	private static final int[][] preprocess(boolean[][] markedMap, int curX, int curY) {
		int[][] result = new int[GameManager.HEIGHT][GameManager.WIDTH];

		for (int i = 0; i < GameManager.HEIGHT; ++i)
			for (int j = 0; j < GameManager.WIDTH; ++j)
				if (markedMap[i][j]) {// �ҵ������仯�ĵط�
					result[i][j] = ConstVar.NEW_CAPTURE_POW;// �õؿ鱾���һ��Ȩ��
					for (int[] tmp : ConstVar.SURROUNDINGS) // ���ڷ����仯��λ����Χ�ĸ���
						if (GameManager.inBound(j, tmp[0], i, tmp[1]))// �ڲ�Խ���������
							// ���ڲ�����������ĸ��Ӹ�һ��Ȩ�أ���������������ĸ��Ӹ���һ��Ȩ��
							// ע��˴��ǲ����е��ӵ�
							if (markedMap[i + tmp[1]][j + tmp[0]])
								result[i + tmp[1]][j + tmp[0]] = ConstVar.NEW_SUR_POW;
				}
		return result;
	}

	/* Ԥ��������δ����ռ��������� */
	private static final int[][] preprocess(int curX, int curY) {
		int[][] result = new int[GameManager.HEIGHT][GameManager.WIDTH];
		int tmp = GameManager.FRIEND_INDEX;// �з��ĵз�Ҳ�����ҷ�
		// �����ҷ�ÿһ����ʿ��ָ���з���ʿ����һ�������ڣ���ConstVar�зֿ��ƶ���Χ������±���б�ʶ
		int[] enemyPos1 = SAMURAIS[tmp].getPos();
		int[] enemyPos2 = SAMURAIS[tmp + 1].getPos();
		int[] enemyPos3 = SAMURAIS[tmp + 2].getPos();
		int flag1 = enemyPos1[0] < curX ? (enemyPos1[1] <= curY ? 2 : 1)
				: (enemyPos1[1] < curY ? 3 : (enemyPos1[0] == curX ? 1 : 0));
		int flag2 = enemyPos2[0] < curX ? (enemyPos2[1] <= curY ? 2 : 1)
				: (enemyPos2[1] < curY ? 3 : (enemyPos2[0] == curX ? 1 : 0));
		int flag3 = enemyPos3[0] < curX ? (enemyPos3[1] <= curY ? 2 : 1)
				: (enemyPos3[1] < curY ? 3 : (enemyPos3[0] == curX ? 1 : 0));

		for (int i = 0; i < 4; i++)
			for (int[] p : ConstVar.SEPERATED_MOVE_RANGE[i])
				// ����е�����ĳһ�����У������������е�Ȩ��ֵ����ȥ1
				if (GameManager.inBound(curX, p[0], curY, p[1])) {
					result[curY + p[1]][curX + p[0]] = ConstVar.DEFAULT_MOV_RANGE_POW
							- ((flag1 == i ? 1 : 0) + (flag2 == i ? 1 : 0) + (flag3 == i ? 1 : 0));
				}

		return result;
	}

	/* ��������λ�ô�����Ȩ�� */
	private static final int[][] postprocess(int[][] result, int curX, int curY, boolean flag) {
		// ��ǰ����λ�ü�һ��Ȩ��
		result[curY][curX] += ConstVar.PREV_POS_POW;

		if (!flag)
			return result;

		// ��ǰ����λ�õ���Χ��һ��Ȩ��
		for (int[] tmp : ConstVar.manhattanDistance(1))
			if (GameManager.inBound(curX, tmp[0], curY, tmp[1]))
				result[curY + tmp[1]][curX + tmp[0]] += ConstVar.POS_SUR_POW;

		return result;
	}
}
