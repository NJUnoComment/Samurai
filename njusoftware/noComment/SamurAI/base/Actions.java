
/**
 * @author clefz
 *
 *         //请在此处标注编写者姓名 
 *         请在此处填写最终修改时间
 *         枚举行动类型，封装了旋转范围的方法
 */
package njusoftware.noComment.SamurAI.base;

public enum Actions {
	OCCUPY_SOUTHWARD, OCCUPY_EASTWARD, OCCUPY_NORTHWARD, OCCUPY_WESTWARD, MOVE_SOUTHWARD, MOVE_EASTWARD, MOVE_NORTHWARD, MOVE_WESTWARD, HIDE, APPEAR;

	/* 根据行动的方向旋转范围 */
	public int[][] getAttackRange(final Weapons weapon) {
		int[][] attackRange = weapon.getAttackRange();
		switch (this) {
		case OCCUPY_SOUTHWARD:
			break;
		case OCCUPY_NORTHWARD:
			// 向北即关于原点
			nega(attackRange);
			break;
		case OCCUPY_EASTWARD:
			// 向东关于原点再关于x=y
			nega(attackRange);
		case OCCUPY_WESTWARD:
			// 向西关于x=y
			turn(attackRange);
			break;
		default:
			break;
		}
		return attackRange;
	}

	public int[][] getMoveRange() {
		// 移动同理
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

	/* 做关于y=x对称，即x，y互换 */
	private static void turn(int[][] attackRange) {
		for (int[] tmp : attackRange) {
			// 位运算是效率最高的交换方法
			tmp[0] = tmp[1] ^ tmp[0];
			tmp[1] = tmp[1] ^ tmp[0];
			tmp[0] = tmp[0] ^ tmp[1];
		}
	}

	/* 做关于原点对称，即x，y取反 */
	private static void nega(int[][] attackRange) {
		for (int[] tmp : attackRange) {
			tmp[0] = -tmp[0];
			tmp[1] = -tmp[1];
		}
	}

}
