
/**
 * @author clefz created at 2016/33/22
 *
 *         //请在此处标注编写者姓名 
 *         请在此处填写最终修改时间
 *         用于枚举武器类型，封装了攻击范围
 */
package njusoftware.noComment.SamurAI.base;

public enum Weapons {
	// 记录了攻击范围，以向南为准
	SPEAR(new int[][] { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 0, 4 } }), // 矛
	SWORD(new int[][] { { 0, 1 }, { 0, 2 }, { 1, 1 }, { 1, 0 }, { 2, 0 } }), // 剑
	AXE(new int[][] { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 } });// 斧

	private int[][] attackRange;

	private Weapons(int[][] attackRange) {
		this.attackRange = attackRange;
	}

	public int[][] getAttackRange() {
		int length = attackRange.length;
		int[][] newArray = new int[length][2];
		for (int i = 0; i < length; i++)
			System.arraycopy(attackRange, 0, newArray, 0, 2);
		return newArray;
	}
}
