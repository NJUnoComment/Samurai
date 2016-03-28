
/**
 * @author clefz created at 2016/3/25
 * 
 *         ////请在此处标注编写者姓名 
 *         请在此处填写最终修改时间
 *         此类用于穷举所有的可能操作
 *         注意这里的操作是指”行动的结果“
 *         比方说先向北再向东和先向东再向北是不同的动作，但是算作一种操作
 *
 */
package njusoftware.noComment.SamurAI.base;

public enum Move {
	// 每回合的操作是非常有限的，所以可以用一个数组来把他们全部写出来
	// 不要觉得蠢，为了效率这是个好办法
	_MS_OE(new int[] { 0, -1 }, Actions.OCCUPY_EASTWARD, true);

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
}
