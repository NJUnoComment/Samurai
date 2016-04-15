
/**
 * @author clefz created at 2016/3/25
 * 
 *         zqh,st
 *         
 *         edited at 2016/3/27
 *         ��������������еĿ��ܲ���
 *         ע������Ĳ�����ָ���ж��Ľ����
 *         �ȷ�˵�������򶫺����������ǲ�ͬ�Ķ�������������һ�ֲ���
 *
 */
package njusoftware.noComment.SamurAI.base;

public enum Move {

	// ��occupy��move
	OS_MS(new int[] { 0, 1 }, Actions.OCCUPY_SOUTHWARD, false), // ��ռ�ݣ����ƶ�
	OS_MW(new int[] { -1, 0 }, Actions.OCCUPY_SOUTHWARD, false), // ��ռ�ݣ����ƶ�
	OS_MN(new int[] { 0, -1 }, Actions.OCCUPY_SOUTHWARD, false), // ��ռ�ݣ����ƶ�
	OS_ME(new int[] { 1, 0 }, Actions.OCCUPY_SOUTHWARD, false), // ��ռ�ݣ����ƶ�

	OE_MS(new int[] { 0, 1 }, Actions.OCCUPY_EASTWARD, false), // ��ռ�ݣ����ƶ�
	OE_MW(new int[] { -1, 0 }, Actions.OCCUPY_EASTWARD, false), // ��ռ�ݣ����ƶ�
	OE_MN(new int[] { 0, -1 }, Actions.OCCUPY_EASTWARD, false), // ��ռ�ݣ����ƶ�
	OE_ME(new int[] { 1, 0 }, Actions.OCCUPY_EASTWARD, false), // ��ռ�ݣ����ƶ�

	ON_MS(new int[] { 0, 1 }, Actions.OCCUPY_NORTHWARD, false), // ��ռ�ݣ����ƶ�
	ON_MW(new int[] { -1, 0 }, Actions.OCCUPY_NORTHWARD, false), // ��ռ�ݣ����ƶ�
	ON_MN(new int[] { 0, -1 }, Actions.OCCUPY_NORTHWARD, false), // ��ռ�ݣ����ƶ�
	ON_ME(new int[] { 1, 0 }, Actions.OCCUPY_NORTHWARD, false), // ��ռ�ݣ����ƶ�

	OW_MS(new int[] { 0, 1 }, Actions.OCCUPY_WESTWARD, false), // ��ռ�ݣ����ƶ�
	OW_MW(new int[] { -1, 0 }, Actions.OCCUPY_WESTWARD, false), // ��ռ�ݣ����ƶ�
	OW_MN(new int[] { 0, -1 }, Actions.OCCUPY_WESTWARD, false), // ��ռ�ݣ����ƶ�
	OW_ME(new int[] { 1, 0 }, Actions.OCCUPY_WESTWARD, false), // ��ռ�ݣ����ƶ�

	// ��move��occupy
	MS_OS(new int[] { 0, 1 }, Actions.OCCUPY_SOUTHWARD, true), // ���ƶ�����ռ��
	MS_OE(new int[] { 0, 1 }, Actions.OCCUPY_EASTWARD, true), // ���ƶ�����ռ��
	MS_ON(new int[] { 0, 1 }, Actions.OCCUPY_NORTHWARD, true), // ���ƶ�����ռ��
	MS_OW(new int[] { 0, 1 }, Actions.OCCUPY_WESTWARD, true), // ���ƶ�����ռ��

	ME_OS(new int[] { 1, 0 }, Actions.OCCUPY_SOUTHWARD, true), // ���ƶ�����ռ��
	ME_OE(new int[] { 1, 0 }, Actions.OCCUPY_EASTWARD, true), // ���ƶ�����ռ��
	ME_ON(new int[] { 1, 0 }, Actions.OCCUPY_NORTHWARD, true), // ���ƶ�����ռ��
	ME_OW(new int[] { 1, 0 }, Actions.OCCUPY_WESTWARD, true), // ���ƶ�����ռ��

	MN_OS(new int[] { 0, -1 }, Actions.OCCUPY_SOUTHWARD, true), // ���ƶ�����ռ��
	MN_OE(new int[] { 0, -1 }, Actions.OCCUPY_EASTWARD, true), // ���ƶ�����ռ��
	MN_ON(new int[] { 0, -1 }, Actions.OCCUPY_NORTHWARD, true), // ���ƶ�����ռ��
	MN_OW(new int[] { 0, -1 }, Actions.OCCUPY_WESTWARD, true), // ���ƶ�����ռ��

	MW_OS(new int[] { -1, 0 }, Actions.OCCUPY_SOUTHWARD, true), // ���ƶ�����ռ��
	MW_OE(new int[] { -1, 0 }, Actions.OCCUPY_EASTWARD, true), // ���ƶ�����ռ��
	MW_ON(new int[] { -1, 0 }, Actions.OCCUPY_NORTHWARD, true), // ���ƶ�����ռ��
	MW_OW(new int[] { -1, 0 }, Actions.OCCUPY_WESTWARD, true), // ���ƶ�����ռ��

	// ��occupy
	OS(new int[] { 0, 0 }, Actions.OCCUPY_SOUTHWARD, false), // ��
	OE(new int[] { 0, 0 }, Actions.OCCUPY_EASTWARD, false), // ��
	ON(new int[] { 0, 0 }, Actions.OCCUPY_NORTHWARD, false), // ��
	OW(new int[] { 0, 0 }, Actions.OCCUPY_WESTWARD, false), // ��

	// move����
	MS_MS_MS(new int[] { 0, 3 }, null, false), // ������
	ME_MS_MS(new int[] { 1, 2 }, null, false), // ������
	ME_ME_MS(new int[] { 2, 1 }, null, false), // ������
	ME_ME_ME(new int[] { 3, 0 }, null, false), // ������
	ME_ME_MN(new int[] { 2, -1 }, null, false), // ������
	ME_MN_MN(new int[] { 1, -2 }, null, false), // ������
	MN_MN_MN(new int[] { 0, -3 }, null, false), // ������
	MW_MS_MS(new int[] { -1, 2 }, null, false), // ������
	MW_MN_MN(new int[] { -1, -2 }, null, false), // ������
	MW_MW_MS(new int[] { -2, 1 }, null, false), // ������
	MW_MW_MN(new int[] { -2, -1 }, null, false), // ������
	MW_MW_MW(new int[] { -3, 0 }, null, false), // ������

	// move����
	MS_MS(new int[] { 0, 2 }, null, false), // ����
	ME_MS(new int[] { 1, 1 }, null, false), // ����
	ME_ME(new int[] { 2, 0 }, null, false), // ����
	ME_MN(new int[] { 1, -1 }, null, false), // ����
	MN_MN(new int[] { 0, -2 }, null, false), // ����
	MW_MS(new int[] { -1, 1 }, null, false), // ����
	MW_MN(new int[] { -1, -1 }, null, false), // ����
	MW_MW(new int[] { -2, 0 }, null, false), // ����

	// ��moveһ��
	MS(new int[] { 0, 1 }, null, false), // ��
	ME(new int[] { 1, 0 }, null, false), // ��
	MN(new int[] { 0, -1 }, null, false), // ��
	MW(new int[] { -1, 0 }, null, false); // ��

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

	public boolean containsOccupy() {
		return occupyToward != null;
	}
}
