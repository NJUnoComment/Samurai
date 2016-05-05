
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

	public final int[][] getMoveRange() {
		// �ƶ�ͬ��
		int[][] moveRange = new int[][] { { 0, 1 } };
		switch (this) {
		case MOVE_WESTWARD:
			rotate(moveRange);
		case MOVE_NORTHWARD:
			rotate(moveRange);
		case MOVE_EASTWARD:
			rotate(moveRange);
		default:
			break;
		}
		return moveRange;
	}

	/* �����ж��ķ�����ת��Χ */
	public final int[][] getAttackRange(final Weapons weapon) {
		int[][] attackRange = weapon.getAttackRange();
		switch (this) {
		case OCCUPY_WESTWARD:
			rotate(attackRange);
		case OCCUPY_NORTHWARD:
			rotate(attackRange);
		case OCCUPY_EASTWARD:
			rotate(attackRange);
		default:
			break;
		}
		return attackRange;
	}

	/* ������y=x�Գƣ���x��y���� */
	private static final void rotate(int[][] attackRange) {
		for (int[] tmp : attackRange) {
			tmp[0] = -tmp[0];
			tmp[0] = tmp[1] ^ tmp[0];
			tmp[1] = tmp[1] ^ tmp[0];
			tmp[0] = tmp[0] ^ tmp[1];
		}
	}
}
