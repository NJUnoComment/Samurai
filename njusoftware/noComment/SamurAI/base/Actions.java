
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

	public final int[][] getMoveRange() {
		// 移动同理
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

	/* 根据行动的方向旋转范围 */
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

	/* 做关于y=x对称，即x，y互换 */
	private static final void rotate(int[][] attackRange) {
		for (int[] tmp : attackRange) {
			tmp[0] = -tmp[0];
			tmp[0] = tmp[1] ^ tmp[0];
			tmp[1] = tmp[1] ^ tmp[0];
			tmp[0] = tmp[0] ^ tmp[1];
		}
	}
}
