
/**
 * @author clefz created at 2016/3/23
 * 
 *         //���ڴ˴���ע��д������ 
 *         ���ڴ˴���д�����޸�ʱ��
 *         �������ڹ����������棬��Board���в����������������������ʽ������һ�����ܵĺϷ�����
 */
package njusoftware.noComment.SamurAI.base;

import java.io.IOException;

import njusoftware.noComment.SamurAI.AI.AIManager;

public class GameManager {
	private AIManager AI;
	private Board prevBoard;
	private Board curBoard;// �������ֶ������Ʋ���Ұ�����
	private int curTurn;// ��ǰ�غ���

	public static int width;
	public static int height;
	public static int totalTurns;// �ܻغ���
	public static int[][] homePoses;// �ҵ�λ��
	public static int controlIndex;// ���Ƶ�����һ����ʿ��0-5��ʾ
	public static final Samurai[] SAMURAIS;// ��ʿ
	public static final int[] ACTION_ORDER = new int[] { 0, 3, 4, 1, 2, 5, 3, 0, 1, 4, 5, 2 }; // �ж���˳��������samurais���±�
	// ���ݻغ�����ȷ���Ǹ��ж�����ôд:
	// samurais[ACTION_ORDER[turn%12]]

	static {
		SAMURAIS = new Samurai[6];
		SAMURAIS[0] = new Samurai(Weapons.SPEAR);
		SAMURAIS[1] = new Samurai(Weapons.SWORD);
		SAMURAIS[2] = new Samurai(Weapons.AXE);
		SAMURAIS[3] = new Samurai(Weapons.SPEAR);
		SAMURAIS[4] = new Samurai(Weapons.SWORD);
		SAMURAIS[5] = new Samurai(Weapons.AXE);
	}

	private GameManager(Info gameInfo) {
		totalTurns = 0;
		controlIndex = 0;
		AI = new AIManager(this);
		// ����һ��
		System.out.println("0");// ���0��Ϊ��Ӧ
	}

	private void inferMap() {
		int[][] current = curBoard.getBattleField();
		int[][] previous = prevBoard.getBattleField();
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (current[i][j] == 9)
					current[i][j] = previous[i][j];
			}
		}
	}

	// ����������������Ҫ���ѵ�
	public int evaluate(Board board) {
		return 0;
	}

	public void nextTurn() throws CloneNotSupportedException {
		//
//		Info turnInfo = IOManager.input();// ����غ���Ϣ
		// һ�Ѵ���
		int[] actions = AI.decideActions();
		Info output = new Info();// �ó������ж����в��ҷ�װ��Info
		// output.setXXX(actions);
		// output.setType(Info.OUTPUT_INFO);
		IOManager.output(output);// Ȼ�����
	}

	public static GameManager init() throws IOException {
		Info gameInfo = IOManager.input();
		// ������ҪһЩ����
		return new GameManager(gameInfo);
	}

	public Board getBoard() {
		return curBoard;
	}
}
