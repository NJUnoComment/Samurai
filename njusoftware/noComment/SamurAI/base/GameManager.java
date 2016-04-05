
/**
 * @author clefz created at 2016/3/23
 * 
 *         //���ڴ˴���ע��д������ 
 *         ���ڴ˴���д�����޸�ʱ��
 *         �������ڹ����������棬��Board���в����������������������ʽ������һ�����ܵĺϷ�����
 */
package njusoftware.noComment.SamurAI.base;

import java.io.IOException;
import java.util.Arrays;

import njusoftware.noComment.SamurAI.AI.AIManager;

public class GameManager {
	private AIManager AI;
	private Board prevBoard;
	private Board curBoard;// �������ֶ������Ʋ���Ұ�����
	private int curTurn;// ��ǰ�غ���
	private int remainCurePeriod;

	public static int WIDTH = 15;
	public static int HEIGHT = 15;
	public static int TOTAL_TURNS;// �ܻغ���
	public static int CURE_PERIOD;
	public static int[][] HOME_POSES;// �ҵ�λ��
	public static int[][] RANK_AND_SCORE;
	public static int SAMURAI_ID;// ���Ƶ�����һ����ʿ��0-5��ʾ
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

	private GameManager() {
		prevBoard = new Board();
		AI = new AIManager(this);
		for (int i = 0; i < 6; i++) {
			// ���ҵ�λ�üӵ�������
			prevBoard.set(HOME_POSES[i], i);
			// ��ʿ��ʼλ��Ϊ�ҵ�λ��
			SAMURAIS[i].setPos(HOME_POSES[i]);
		}
		System.out.println("0");// ���0��Ϊ��Ӧ
	}

	private void inferMap() {
		int[][] current = curBoard.getBattleField();
		int[][] previous = prevBoard.getBattleField();
		for (int i = 0; i < HEIGHT; i++) {
			for (int j = 0; j < WIDTH; j++) {
				if (current[i][j] == 9)
					current[i][j] = previous[i][j];
			}
		}
	}

	// ��������
	public int evaluate(Board board) {
		return 0;
	}

	public void nextTurn() throws CloneNotSupportedException, IOException {
		Info turnInfo = IOManager.input();
		// ��ǰ�غ���
		curTurn = turnInfo.getTurn();

		// ʣ��ظ��غ�
		remainCurePeriod = turnInfo.getRemainCurePeriod();

		// ��ǰ����
		prevBoard = curBoard == null ? prevBoard : curBoard;
		curBoard = new Board(turnInfo.getBoard(), curTurn = turnInfo.getTurn());
		inferMap();

		// ��ʿ״̬
		int[][] samuraiState = turnInfo.getSamuraiState();
		for (int i = 0; i < 6; i++) {
			if (-1 != samuraiState[i][0])
				SAMURAIS[i].setPos(new int[] { samuraiState[i][0], samuraiState[i][1] });
			SAMURAIS[i].setVisible(0 == samuraiState[i][2]);
			SAMURAIS[i].setActive(-1 == samuraiState[i][2]);
		}

		// ����ʱֱ�����0
		if (remainCurePeriod != 0)
			IOManager.output(new Info().setActions(new int[] { 0 }));
		// ���
		IOManager.output(new Info().setActions(AI.decideActions()));// Ȼ�����
	}

	public static GameManager init() throws IOException {
		Info gameInfo = IOManager.input();
		// ������ҪһЩ����
		WIDTH = gameInfo.getWidth();
		HEIGHT = gameInfo.getHeight();
		TOTAL_TURNS = gameInfo.getTotalTurns();
		CURE_PERIOD = gameInfo.getCurePeriod();
		HOME_POSES = gameInfo.getHomePos();
		SAMURAI_ID = gameInfo.getSamuraiID();
		RANK_AND_SCORE = gameInfo.getRanksAndScores();
		return new GameManager();
	}

	public Board getBoard() {
		return curBoard;
	}
//
//	void print() {
//		int[][] t = curBoard.getBattleField();
//		for (int[] i : t) {
//			for (int j : i)
//				System.out.print((j == 9 ? "x" : j) + " ");
//			System.out.println();
//		}
//	}
//
//	public static void print(Board b) {
//		int[][] t = b.getBattleField();
//		for (int[] i : t) {
//			for (int j : i)
//				System.out.print((j == 9 ? "x" : j) + " ");
//			System.out.println();
//		}
//	}
}
