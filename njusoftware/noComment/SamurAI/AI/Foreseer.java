package njusoftware.noComment.SamurAI.AI;

import java.util.Random;

import njusoftware.noComment.SamurAI.base.Board;
import njusoftware.noComment.SamurAI.base.ConstVar;
import njusoftware.noComment.SamurAI.base.GameManager;
import njusoftware.noComment.SamurAI.base.Samurai;

public class Foreseer {
	private static final Random r = new Random();
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

		// �����ֵλ�������ѡ��һ��λ�ò�����Ϊ��ʿ��λ��
		int index = r.nextInt(count);
		for (int i = tmpY; i < GameManager.HEIGHT; ++i)
			for (int j = tmpX; j < GameManager.WIDTH; ++j) {
				count -= processedMap[i][j] == max ? 1 : 0;
				if (count != index)
					continue;
				SAMURAIS[samuraiID].setPos(j, i);
				return;
			}
	}

	/* Ԥ�����ͼ�������ʿ�ڵ�ͼ��ĳ��λ�õĿ����ԣ���Ȩ��ֵ���� */
	private static final int[][] processMap(int samuraiID, Board previousBoard, Board currentBoard) {
		int[] curPos = SAMURAIS[samuraiID].getPos();
		int curX = curPos[0], curY = curPos[1];// ֱ��ȡ���������������Ч��

		boolean[][] markedMap = markNewCapture(samuraiID, previousBoard, currentBoard);

		return postprocess(isCaptureChanged(markedMap) ? preprocess(markedMap, curX, curY) : preprocess(curX, curY),
				curX, curY);

	}

	/* ���������ռ���� */
	private static final boolean[][] markNewCapture(int samuraiID, Board previousBoard, Board currentBoard) {
		boolean[][] result = new boolean[GameManager.HEIGHT][GameManager.WIDTH];
		int[][] curBattleField = currentBoard.getBattleField();
		int[][] prevBattleField = previousBoard.getBattleField();
		for (int i = 0; i < GameManager.HEIGHT; ++i)
			for (int j = 0; j < GameManager.WIDTH; ++j)
				// �����غϸ���ʿ�����ĵؿ��ʶ����
				result[i][j] = curBattleField[i][j] == samuraiID && prevBattleField[i][j] != samuraiID;
		return result;
	}

	/* ���ռ�ݷ�Χ�Ƿ����˸ı� */
	private static final boolean isCaptureChanged(boolean[][] markedMap) {
		boolean flag = false;
		for (boolean[] p : markedMap)
			for (boolean i : p)
				if (flag = i)
					break;
		return flag;
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
		int flag1 = (enemyPos1[0] < curX ? 1 : 0) & (enemyPos1[1] < curY ? 2 : 0);
		int flag2 = (enemyPos2[0] < curX ? 1 : 0) & (enemyPos2[1] < curY ? 2 : 0);
		int flag3 = (enemyPos3[0] < curX ? 1 : 0) & (enemyPos3[1] < curY ? 2 : 0);

		for (int i = 0; i < 4; ++i)
			for (int[] p : ConstVar.SEPERATED_MOVE_RANGE[i])
				// ����е�����ĳһ�����У������������е�Ȩ��ֵ����ȥ1
				if (GameManager.inBound(curX, p[0], curY, p[1]))
					result[curY + p[1]][curX + p[0]] = ConstVar.DEFAULT_MOV_RANGE_POW
							- ((flag1 == i ? 1 : 0) + (flag2 == i ? 1 : 0) + (flag3 == 0 ? 1 : 0));

		return result;
	}

	/* ��������λ�ô�����Ȩ�� */
	private static final int[][] postprocess(int[][] result, int curX, int curY) {
		// ��ǰ����λ�ü�һ��Ȩ��
		result[curY][curX] += ConstVar.PREV_POS_POW;

		// ��ǰ����λ�õ���Χ��һ��Ȩ��
		for (int[] tmp : ConstVar.OCP_MOVE_RANGE)
			if (GameManager.inBound(curX, tmp[0], curY, tmp[1]))
				result[curY + tmp[1]][curX + tmp[0]] += ConstVar.POS_SUR_POW;
		return result;
	}
}
