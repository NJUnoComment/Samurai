
/**
 * @author clefz
 *
 *         clefz
 *         2016/3/20
 *         枚举行动类型，封装了旋转范围的方法
 */
package njusoftware.noComment.SamurAI.base;

public enum Actions {
	OCCUPY_SOUTHWARD, // 南占据
	OCCUPY_EASTWARD, // 东占据
	OCCUPY_NORTHWARD, // 北占据
	OCCUPY_WESTWARD, // 西占据
	MOVE_SOUTHWARD, // 南移动
	MOVE_EASTWARD, // 东移动
	MOVE_NORTHWARD, // 北移动
	MOVE_WESTWARD, // 西移动
	HIDE, // 隐藏
	APPEAR;// 现身

	public int[][] getMoveRange() {
		// 移动同理
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

	/* 根据行动的方向旋转范围 */
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

	/* 做关于y=x对称，即x，y互换 */
	private static void turn(int[][] attackRange) {
		for (int[] tmp : attackRange) {
			// 位运算是效率最高的交换方法
			tmp[0] = -tmp[0];
			tmp[0] = tmp[1] ^ tmp[0];
			tmp[1] = tmp[1] ^ tmp[0];
			tmp[0] = tmp[0] ^ tmp[1];
		}
	}
}
