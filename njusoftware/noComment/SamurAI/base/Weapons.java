
/**
 * @author clefz created at 2016/33/22
 *
 *         //���ڴ˴���ע��д������ 
 *         ���ڴ˴���д�����޸�ʱ��
 *         ����ö���������ͣ���װ�˹�����Χ
 */
package njusoftware.noComment.SamurAI.base;

public enum Weapons {
	// ��¼�˹�����Χ��������Ϊ׼
	SPEAR(new int[][] { { 0, 1 }, { 0, 2 }, { 0, 3 }, { 0, 4 } }), 
	SWORD(new int[][] { { 0, 1 }, { 0, 2 }, { 1, 1 }, { 1, 0 }, { 2, 0 } }), 
	AXE(new int[][] { { -1, -1 }, { -1, 0 }, { -1, 1 }, { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 } });

	private int[][] attackRange;

	private Weapons(int[][] attackRange) {
		this.attackRange = attackRange;
	}
	
	public int[][] getAttackRange() {
		// �벻Ҫ���������������
		// ��ȡ������Χ�����·�ʽ
		// int[][] attackRange = [Actions��ʵ��].getAttackRange([Weapon��ʵ��]);
		return attackRange;
	}
}
