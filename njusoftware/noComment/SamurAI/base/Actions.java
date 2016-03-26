
/**
 * @author clefz
 *
 *         //���ڴ˴���ע��д������ 
 *         ���ڴ˴���д�����޸�ʱ��
 *         ö���ж����ͣ���װ����ת��Χ�ķ���
 */
package njusoftware.noComment.SamurAI.base;

public enum Actions {
	OCCUPY_SOUTHWARD, OCCUPY_EASTWARD, OCCUPY_NORTHWARD, OCCUPY_WESTWARD, MOVE_SOUTHWARD, MOVE_EASTWARD, MOVE_NORTHWARD, MOVE_WESTWARD, HIDE, APPEAR;

	/* �����ж��ķ�����ת��Χ */
	public int[][] getAttackRange(final Weapons weapon) {
		int[][] attackRange = weapon.getAttackRange();
		switch (this) {
		case OCCUPY_SOUTHWARD:
			break;
		case OCCUPY_NORTHWARD:
			// �򱱼�����ԭ��
			nega(attackRange);
			break;
		case OCCUPY_EASTWARD:
			// �򶫹���ԭ���ٹ���x=y
			nega(attackRange);
		case OCCUPY_WESTWARD:
			// ��������x=y
			turn(attackRange);
			break;
		default:
			break;
		}
		return attackRange;
	}

	public int[][] getMoveRange() {
		// �ƶ�ͬ��
		int[][] moveRange = new int[][] { { 0, 1 } };
		switch (this) {
		case MOVE_SOUTHWARD:
			break;
		case MOVE_NORTHWARD:
			nega(moveRange);
			break;
		case MOVE_EASTWARD:
			nega(moveRange);
		case MOVE_WESTWARD:
			turn(moveRange);
			break;
		default:
			break;
		}
		return moveRange;
	}

	public int getOrdinal() {
		return ordinal() + 1;
	}

	/* ������y=x�Գƣ���x��y���� */
	private static void turn(int[][] attackRange) {
		for (int[] tmp : attackRange) {
			// λ������Ч����ߵĽ�������
			tmp[0] = tmp[1] ^ tmp[0];
			tmp[1] = tmp[1] ^ tmp[0];
			tmp[0] = tmp[0] ^ tmp[1];
		}
	}

	/* ������ԭ��Գƣ���x��yȡ�� */
	private static void nega(int[][] attackRange) {
		for (int[] tmp : attackRange) {
			tmp[0] = -tmp[0];
			tmp[1] = -tmp[1];
		}
	}

}
