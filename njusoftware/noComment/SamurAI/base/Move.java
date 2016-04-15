
/**
 * @author clefz created at 2016/3/25
 * 
 *         zqh,st
 *         
 *         edited at 2016/3/27
 *         此类用于穷举所有的可能操作
 *         注意这里的操作是指”行动的结果“
 *         比方说先向北再向东和先向东再向北是不同的动作，但是算作一种操作
 *
 */
package njusoftware.noComment.SamurAI.base;

public enum Move {

	// 先occupy后move
	OS_MS(new int[] { 0, 1 }, Actions.OCCUPY_SOUTHWARD, false), // 南占据，南移动
	OS_MW(new int[] { -1, 0 }, Actions.OCCUPY_SOUTHWARD, false), // 南占据，西移动
	OS_MN(new int[] { 0, -1 }, Actions.OCCUPY_SOUTHWARD, false), // 南占据，北移动
	OS_ME(new int[] { 1, 0 }, Actions.OCCUPY_SOUTHWARD, false), // 南占据，东移动

	OE_MS(new int[] { 0, 1 }, Actions.OCCUPY_EASTWARD, false), // 东占据，南移动
	OE_MW(new int[] { -1, 0 }, Actions.OCCUPY_EASTWARD, false), // 东占据，西移动
	OE_MN(new int[] { 0, -1 }, Actions.OCCUPY_EASTWARD, false), // 东占据，北移动
	OE_ME(new int[] { 1, 0 }, Actions.OCCUPY_EASTWARD, false), // 东占据，东移动

	ON_MS(new int[] { 0, 1 }, Actions.OCCUPY_NORTHWARD, false), // 北占据，南移动
	ON_MW(new int[] { -1, 0 }, Actions.OCCUPY_NORTHWARD, false), // 北占据，西移动
	ON_MN(new int[] { 0, -1 }, Actions.OCCUPY_NORTHWARD, false), // 北占据，北移动
	ON_ME(new int[] { 1, 0 }, Actions.OCCUPY_NORTHWARD, false), // 北占据，东移动

	OW_MS(new int[] { 0, 1 }, Actions.OCCUPY_WESTWARD, false), // 西占据，南移动
	OW_MW(new int[] { -1, 0 }, Actions.OCCUPY_WESTWARD, false), // 西占据，西移动
	OW_MN(new int[] { 0, -1 }, Actions.OCCUPY_WESTWARD, false), // 西占据，北移动
	OW_ME(new int[] { 1, 0 }, Actions.OCCUPY_WESTWARD, false), // 西占据，东移动

	// 先move后occupy
	MS_OS(new int[] { 0, 1 }, Actions.OCCUPY_SOUTHWARD, true), // 南移动，南占据
	MS_OE(new int[] { 0, 1 }, Actions.OCCUPY_EASTWARD, true), // 南移动，东占据
	MS_ON(new int[] { 0, 1 }, Actions.OCCUPY_NORTHWARD, true), // 南移动，北占据
	MS_OW(new int[] { 0, 1 }, Actions.OCCUPY_WESTWARD, true), // 南移动，西占据

	ME_OS(new int[] { 1, 0 }, Actions.OCCUPY_SOUTHWARD, true), // 东移动，南占据
	ME_OE(new int[] { 1, 0 }, Actions.OCCUPY_EASTWARD, true), // 东移动，东占据
	ME_ON(new int[] { 1, 0 }, Actions.OCCUPY_NORTHWARD, true), // 东移动，北占据
	ME_OW(new int[] { 1, 0 }, Actions.OCCUPY_WESTWARD, true), // 东移动，西占据

	MN_OS(new int[] { 0, -1 }, Actions.OCCUPY_SOUTHWARD, true), // 北移动，南占据
	MN_OE(new int[] { 0, -1 }, Actions.OCCUPY_EASTWARD, true), // 北移动，东占据
	MN_ON(new int[] { 0, -1 }, Actions.OCCUPY_NORTHWARD, true), // 北移动，北占据
	MN_OW(new int[] { 0, -1 }, Actions.OCCUPY_WESTWARD, true), // 北移动，西占据

	MW_OS(new int[] { -1, 0 }, Actions.OCCUPY_SOUTHWARD, true), // 西移动，南占据
	MW_OE(new int[] { -1, 0 }, Actions.OCCUPY_EASTWARD, true), // 西移动，东占据
	MW_ON(new int[] { -1, 0 }, Actions.OCCUPY_NORTHWARD, true), // 西移动，北占据
	MW_OW(new int[] { -1, 0 }, Actions.OCCUPY_WESTWARD, true), // 西移动，西占据

	// 仅occupy
	OS(new int[] { 0, 0 }, Actions.OCCUPY_SOUTHWARD, false), // 南
	OE(new int[] { 0, 0 }, Actions.OCCUPY_EASTWARD, false), // 东
	ON(new int[] { 0, 0 }, Actions.OCCUPY_NORTHWARD, false), // 北
	OW(new int[] { 0, 0 }, Actions.OCCUPY_WESTWARD, false), // 西

	// move三步
	MS_MS_MS(new int[] { 0, 3 }, null, false), // 南南南
	ME_MS_MS(new int[] { 1, 2 }, null, false), // 东南南
	ME_ME_MS(new int[] { 2, 1 }, null, false), // 东东南
	ME_ME_ME(new int[] { 3, 0 }, null, false), // 东东东
	ME_ME_MN(new int[] { 2, -1 }, null, false), // 东东北
	ME_MN_MN(new int[] { 1, -2 }, null, false), // 东北北
	MN_MN_MN(new int[] { 0, -3 }, null, false), // 北北北
	MW_MS_MS(new int[] { -1, 2 }, null, false), // 西南南
	MW_MN_MN(new int[] { -1, -2 }, null, false), // 西北北
	MW_MW_MS(new int[] { -2, 1 }, null, false), // 西西南
	MW_MW_MN(new int[] { -2, -1 }, null, false), // 西西北
	MW_MW_MW(new int[] { -3, 0 }, null, false), // 西西西

	// move两步
	MS_MS(new int[] { 0, 2 }, null, false), // 南南
	ME_MS(new int[] { 1, 1 }, null, false), // 东南
	ME_ME(new int[] { 2, 0 }, null, false), // 东东
	ME_MN(new int[] { 1, -1 }, null, false), // 东北
	MN_MN(new int[] { 0, -2 }, null, false), // 北北
	MW_MS(new int[] { -1, 1 }, null, false), // 西南
	MW_MN(new int[] { -1, -1 }, null, false), // 西北
	MW_MW(new int[] { -2, 0 }, null, false), // 西西

	// 仅move一步
	MS(new int[] { 0, 1 }, null, false), // 南
	ME(new int[] { 1, 0 }, null, false), // 东
	MN(new int[] { 0, -1 }, null, false), // 北
	MW(new int[] { -1, 0 }, null, false); // 西

	private int[] moveResult;
	private Actions occupyToward;
	private boolean moveBeforeOccupy;

	private Move(int[] moveResult, Actions occupyToward, boolean moveBeforeOccupy) {
		this.moveResult = moveResult;
		this.occupyToward = occupyToward;
		this.moveBeforeOccupy = moveBeforeOccupy;
	}

	public int[] getMoveResult() {
		return moveResult;
	}

	public int[][] getOccupyResult(Weapons weapon) {
		if (occupyToward == null)
			return null;
		int[][] occupyResult = occupyToward.getAttackRange(weapon);// 首先对武器的攻击范围根据朝向进行旋转
		if (!moveBeforeOccupy)// 如果先占据再移动，则不改变上面得到的结果
			return occupyResult;
		// 如果先移动再攻击，则对上面得到的占据结果进行偏移
		for (int[] tmp : occupyResult) {
			tmp[0] += moveResult[0];
			tmp[1] += moveResult[1];
		}
		return occupyResult;
	}

	public boolean containsOccupy() {
		return occupyToward != null;
	}
}
