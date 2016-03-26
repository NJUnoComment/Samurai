
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
	SPEAR(new int[][] { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 0, 4 } }), 
	SWORD(new int[][] { { 0, 1 }, { 0, 2 }, { 1, 1 }, { 1, 0 }, { 2, 0 } }), 
	AXE(new int[][] { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 } });

	private int[][] attackRange;

	private Weapons(int[][] attackRange) {
		this.attackRange = attackRange;
	}
	
	public int[][] getAttackRange() {
		// 请不要主动调用这个方法
		// 获取攻击范围以如下方式
		// int[][] attackRange = [Actions的实例].getAttackRange([Weapon的实例]);
		return attackRange;
	}
}
