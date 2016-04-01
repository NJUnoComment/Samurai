
/**
 * @author clefz
 *
 *         clefz
 *         2016/3/20
 *         ö���ж����ͣ���װ����ת��Χ�ķ���
 */
package njusoftware.noComment.SamurAI.base;

public enum Actions {
	OCCUPY_SOUTHWARD, // ��ռ��
	OCCUPY_EASTWARD, // ��ռ��
	OCCUPY_NORTHWARD, // ��ռ��
	OCCUPY_WESTWARD, // ��ռ��
	MOVE_SOUTHWARD, // ���ƶ�
	MOVE_EASTWARD, // ���ƶ�
	MOVE_NORTHWARD, // ���ƶ�
	MOVE_WESTWARD, // ���ƶ�
	HIDE, // ����
	APPEAR;// ����

	public int[][] getMoveRange() {
		// �ƶ�ͬ��
		int[][] moveRange = new int[][] { { 0, 1 } };
		switch (this) {
		case MOVE_WESTWARD:
			turn(moveRange);
		case MOVE_NORTHWARD:
			turn(moveRange);
		case MOVE_EASTWARD:
			turn(moveRange);
		default:
			break;
		}
		return moveRange;
	}

	/* �����ж��ķ�����ת��Χ */
	public int[][] getAttackRange(final Weapons weapon) {
		int[][] attackRange = weapon.getAttackRange();
		switch (this) {
		case OCCUPY_WESTWARD:
			turn(attackRange);
		case OCCUPY_NORTHWARD:
			turn(attackRange);
		case OCCUPY_EASTWARD:
			turn(attackRange);
		default:
			break;
		}
		return attackRange;
	}

	public int getOrdinal() {
		return ordinal() + 1;
	}

	/* ������y=x�Գƣ���x��y���� */
	private static void turn(int[][] attackRange) {
		for (int[] tmp : attackRange) {
			// λ������Ч����ߵĽ�������
			tmp[0] = -tmp[0];
			tmp[0] = tmp[1] ^ tmp[0];
			tmp[1] = tmp[1] ^ tmp[0];
			tmp[0] = tmp[0] ^ tmp[1];
		}
	}
}
