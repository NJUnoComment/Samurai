
/**
 * @author clefz created at 2016/3/25
 * 
 *         ////zqh,st
 *         
 *         edited at 2016/3/27
 *         ��������������еĿ��ܲ���
 *         ע������Ĳ�����ָ���ж��Ľ����
 *         �ȷ�˵�������򶫺����������ǲ�ͬ�Ķ�������������һ�ֲ���
 *
 */
package njusoftware.noComment.SamurAI.base;

public enum Move {
	//��move��occupy
	_MS_OE(new int[]{0,1},Actions.OCCUPY_EASTWARD, true),
	_MS_ON(new int[]{0,1},Actions.OCCUPY_NORTHWARD,true),
	_MS_OW(new int[]{0,1},Actions.OCCUPY_WESTWARD,true),
	_MS_OS(new int[]{0,1},Actions.OCCUPY_SOUTHWARD,true),
	
	_MW_OE(new int[]{-1,0},Actions.OCCUPY_EASTWARD,true),
	_MW_ON(new int[]{-1,0},Actions.OCCUPY_NORTHWARD,true),
	_MW_OW(new int[]{-1,0},Actions.OCCUPY_WESTWARD,true),
	_MW_OS(new int[]{-1,0},Actions.OCCUPY_SOUTHWARD,true),
	
	_MN_OE(new int[]{0,-1},Actions.OCCUPY_EASTWARD,true),
	_MN_ON(new int[]{0,-1},Actions.OCCUPY_NORTHWARD,true),
	_MN_OW(new int[]{0,-1},Actions.OCCUPY_WESTWARD,true),
	_MN_OS(new int[]{0,-1},Actions.OCCUPY_SOUTHWARD,true),
	
	_ME_OE(new int[]{1,0},Actions.OCCUPY_EASTWARD,true),
	_ME_ON(new int[]{1,0},Actions.OCCUPY_NORTHWARD,true),
	_ME_OW(new int[]{1,0},Actions.OCCUPY_WESTWARD,true),
	_ME_OS(new int[]{1,0},Actions.OCCUPY_SOUTHWARD,true),
	
	//��occupy��move
	_OE_MS(new int[]{0,1},Actions.OCCUPY_EASTWARD, true),
	_ON_MS(new int[]{0,1},Actions.OCCUPY_NORTHWARD,true),
	_OW_MS(new int[]{0,1},Actions.OCCUPY_WESTWARD,true),
	_OS_MS(new int[]{0,1},Actions.OCCUPY_SOUTHWARD,true),
	
	_OE_MW(new int[]{-1,0},Actions.OCCUPY_EASTWARD,true),
	_ON_MW(new int[]{-1,0},Actions.OCCUPY_NORTHWARD,true),
	_OW_MW(new int[]{-1,0},Actions.OCCUPY_WESTWARD,true),
	_OS_MW(new int[]{-1,0},Actions.OCCUPY_SOUTHWARD,true),
	
	_OE_MN(new int[]{0,-1},Actions.OCCUPY_EASTWARD,true),
	_ON_MN(new int[]{0,-1},Actions.OCCUPY_NORTHWARD,true),
	_OW_MN(new int[]{0,-1},Actions.OCCUPY_WESTWARD,true),
	_OS_MN(new int[]{0,-1},Actions.OCCUPY_SOUTHWARD,true),
	
	_OE_ME(new int[]{1,0},Actions.OCCUPY_EASTWARD,true),
	_ON_ME(new int[]{1,0},Actions.OCCUPY_NORTHWARD,true),
	_OW_ME(new int[]{1,0},Actions.OCCUPY_WESTWARD,true),
	_OS_ME(new int[]{1,0},Actions.OCCUPY_SOUTHWARD,true),
	
	//��moveһ��
	_MS(new int[]{0,1},null,false),
	_MN(new int[]{0,-1},null,false),
	_ME(new int[]{1,0},null,false),
	_MW(new int[]{-1,0},null,false),
	
	//move����
	_ME_MS(new int[]{1,1},null,false),
	_ME_MN(new int[]{1,-1},null,false),
	_MW_MS(new int[]{-1,1},null,false),
	_MW_MN(new int[]{-1,-1},null,false),
	_MS_MS(new int[]{0,2},null,false),
	_MN_MN(new int[]{0,-2},null,false),
	_ME_ME(new int[]{2,0},null,false),
	_MW_MW(new int[]{-2,0},null,false),
	
	//move����
	_ME_ME_ME(new int[]{3,0},null,false),
	_MW_MW_MW(new int[]{-3,0},null,false),
	_MS_MS_MS(new int[]{0,3},null,false),
	_MN_MN_MN(new int[]{0,-3},null,false),
	_ME_MS_MS(new int[]{1,2},null,false),
	_ME_ME_MS(new int[]{2,1},null,false),
	_ME_MN_MN(new int[]{1,-2},null,false),
	_ME_ME_MN(new int[]{2,-1},null,false),
	_MW_MS_MS(new int[]{-1,2},null,false),
	_MW_MW_MS(new int[]{-2,1},null,false),
	_MW_MN_MN(new int[]{-1,-2},null,false),
	_MW_MW_MN(new int[]{-2,-1},null,false),
	
	//��occupy
	_OE(new int[]{0,0},Actions.OCCUPY_EASTWARD,false),
	_OW(new int[]{0,0},Actions.OCCUPY_WESTWARD,false),
	_OS(new int[]{0,0},Actions.OCCUPY_SOUTHWARD,false),
	_ON(new int[]{0,0},Actions.OCCUPY_NORTHWARD,false);

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
