
/**
 * @author clefz created at 2016/3/22
 *
 *         //���ڴ˴���ע��д������ 
 *         ���ڴ˴���д�����޸�ʱ��
 *         �������ڷ�װ��IOManager��GameManager֮�䴫�ݵ���Ϣ
 */
package njusoftware.noComment.SamurAI.base;

public class Info {
	private static final int GAME_INFO = 0x1111, TURN_INFO = 0x2222, OUTPUT_INFO = 0x3333; // �������������������Ϣ����
	private int kind = GAME_INFO;// ��Ϣ����

	/* �����ֶΰ����غ���Ϣ */
	private Board primalBorad;// δ������ľ�����Ϣ
	private int[][] samuraiState;// ��ʿ״̬
	private int remainCurePeriod;// ʣ��ظ�ʱ��
	private int turn;// �غ�����

	/* �����ֶΰ�����Ϸ��Ϣ */
	private int totalTurns;// �ܻغ���
	private int samuraiID;// ���Ƶ���ʿID������Ϸ��ID������ID�ϳ�
	private int battleFieldSize;// ս�������С
	private int curePeriod;// �ظ�ʱ��
	private int[][] homePos;// �ҵ�λ��
	private int[][] ranksAndScores;// �����ͷ���

	/* �����ֶΰ��������Ϣ */
	private int[] actions;// ����

	// ������һ��getter��setter
	// Ϊ�˷�����ý���setterȫ��ʹ��������
}
