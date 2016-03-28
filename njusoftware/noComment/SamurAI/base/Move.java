
/**
 * @author clefz created at 2016/3/25
 * 
 *         ////���ڴ˴���ע��д������ 
 *         ���ڴ˴���д�����޸�ʱ��
 *         ��������������еĿ��ܲ���
 *         ע������Ĳ�����ָ���ж��Ľ����
 *         �ȷ�˵�������򶫺����������ǲ�ͬ�Ķ�������������һ�ֲ���
 *
 */
package njusoftware.noComment.SamurAI.base;

public enum Move {
	// ÿ�غϵĲ����Ƿǳ����޵ģ����Կ�����һ��������������ȫ��д����
	// ��Ҫ���ô���Ϊ��Ч�����Ǹ��ð취
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
		int[][] occupyResult = occupyToward.getAttackRange(weapon);// ���ȶ������Ĺ�����Χ���ݳ��������ת
		if (!moveBeforeOccupy)// �����ռ�����ƶ����򲻸ı�����õ��Ľ��
			return occupyResult;
		// ������ƶ��ٹ������������õ���ռ�ݽ������ƫ��
		for (int[] tmp : occupyResult) {
			tmp[0] += moveResult[0];
			tmp[1] += moveResult[1];
		}
		return occupyResult;
	}
}
