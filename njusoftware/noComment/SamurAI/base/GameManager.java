
/**
 * @author clefz created at 2016/3/23
 * 
 *         //clefz
 *         2016/4/8
 *         �������ڹ����������棬��Board���в����������������������ʽ������һ�����ܵĺϷ�����
 */
package njusoftware.noComment.SamurAI.base;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

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
	public static final int[] ACTION_ORDER = new int[] { 0, 3, 4, 1, 2, 5, 3, 0, 1, 4, 5, 2 }; // �ж���˳��������samurais���±�
	public static final Samurai[] SAMURAIS;// ��ʿ
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

	/* ͨ���Ʋ�ȥ����Ұ���� */
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

	/* �Ʋ��������ʿλ�� */
	private void inferPosition() {
		int tmp = (1 - SAMURAI_ID / 3) * 3;
		for (int i = tmp; i < tmp + 3; i++)
			// ֻ���Ʋ�з����ɼ���ʿ��λ��
			if (!SAMURAIS[i].isVisible())
				inferPositionOf(i);
	}

	/* �ض���ʿ��λ���Ʋ� */
	private void inferPositionOf(int samuraiID) {
		int[][] processedMap = preprocessMap(samuraiID);
		int max = 0;
		int count = 0;
		// �ҳ����ֵ������
		for (int i = 0; i < HEIGHT; i++)
			for (int j = 0; j < WIDTH; j++) {
				int tmp = processedMap[i][j];
				if (tmp > max) {
					max = tmp;
					count = 0;
				}
				count += tmp == max ? 1 : 0;
			}

		// �����ֵλ�������һ��λ�ò�����
		int index = (int) (Math.random() * (count - 1)) + 1;
		for (int i = 0; i < HEIGHT; i++)
			for (int j = 0; j < WIDTH; j++) {
				if (count != index)
					continue;
				count -= processedMap[i][j] == max ? 1 : 0;
				SAMURAIS[samuraiID].setPos(new int[] { i, j });
				return;
			}

	}

	/* Ԥ�����ͼ�������ʿ�ڵ�ͼ��ĳ��λ�õĿ����ԣ���Ȩ��ֵ���� */
	private int[][] preprocessMap(int samuraiID) {
		int[] curPos = SAMURAIS[samuraiID].getPos();
		int curX = curPos[0], curY = curPos[1];// ֱ��ȡ�������������Ч��

		int[][] result = new int[HEIGHT][WIDTH];
		boolean[][] markedMap1 = markNewCapture(samuraiID);

		// ���ռ�ݷ�Χ�Ƿ����˸ı�
		boolean occupiedChanges = false;
		for (boolean[] p : markedMap1)
			for (boolean i : p)
				if (occupiedChanges = i)
					break;

		/* ռ�����������˵���� */
		if (occupiedChanges) {
			for (int i = 0; i < HEIGHT; i++)
				for (int j = 0; j < WIDTH; j++)
					if (markedMap1[i][j])// �ҵ������仯�ĵط�
						for (int[] tmp : ConstVar.SURROUNDINGS) // ���ڷ����仯��λ����Χ�ĸ���
							if (i + tmp[1] >= 0 && j + tmp[0] >= 0 && i + tmp[1] < HEIGHT && j + tmp[0] < WIDTH)// �ڲ�Խ��������
								// ���ڲ�����������ĸ��Ӹ�һ��Ȩ�أ���������������ĸ��Ӹ���һ��Ȩ��
								// ע��˴��ǲ����е��ӵ�
								result[i + tmp[1]][j + tmp[0]] = markedMap1[i + tmp[1]][j + tmp[0]]
										? ConstVar.NEW_CAPTURE_POW : ConstVar.NEW_SURR_POW;

			// ��ǰ����λ�ü�һ��Ȩ��
			result[curX][curY] += ConstVar.PREV_POS_POW;

			// ��ǰ����λ�õ���Χ��һ��Ȩ��
			for (int[] tmp : ConstVar.OCCUPIED_MOVE_RANGE)
				if (curY + tmp[1] >= 0 && curX + tmp[0] >= 0 && curY + tmp[1] < HEIGHT && curX + tmp[0] < WIDTH)
					result[curY + tmp[1]][curX + tmp[0]] += ConstVar.POS_SURR_POW;

			return result;
		}

		/* ռ������δ���ӵ���� */
		int tmp = SAMURAI_ID / 3 * 3;// �з��ĵз�Ҳ�����ҷ�

		// ��ǰ����λ�ü�һ��Ȩ��
		result[curY][curX] = ConstVar.PREV_POS_POW;

		// ��ǰ����λ�õ���Χ��һ��Ĭ��Ȩ��
		for (int[] p : ConstVar.MOVE_RANGE)
			if (curY + p[1] >= 0 && curX + p[0] >= 0 && curY + p[1] < HEIGHT && curX + p[0] < WIDTH)
				result[curY + p[1]][curX + p[0]] = ConstVar.POS_SURR_POW;

		// �����ҷ�ÿһ����ʿ��ָ���з���ʿ����һ�������ڣ���ConstVar�зֿ��ƶ���Χ������±���б�ʶ
		int[] enemyPos1 = SAMURAIS[tmp].getPos();
		int[] enemyPos2 = SAMURAIS[tmp + 1].getPos();
		int[] enemyPos3 = SAMURAIS[tmp + 2].getPos();
		int flag1 = (enemyPos1[0] < curX ? 1 : 0) & (enemyPos1[1] < curY ? 2 : 0);
		int flag2 = (enemyPos2[0] < curX ? 1 : 0) & (enemyPos2[1] < curY ? 2 : 0);
		int flag3 = (enemyPos3[0] < curX ? 1 : 0) & (enemyPos3[1] < curY ? 2 : 0);

		for (int i = 0; i < 4; i++)
			for (int[] p : ConstVar.SEPERATED_MOVE_RANGE[i])
				// ����е�����ĳһ�����У������������е�Ȩ��ֵ����ȥ1
				if (curY + p[1] >= 0 && curX + p[0] >= 0 && curY + p[1] < HEIGHT && curX + p[0] < WIDTH)
					result[curY + p[1]][curX + p[0]] -= (flag1 == i ? 1 : 0) + (flag2 == i ? 1 : 0)
							+ (flag3 == 0 ? 1 : 0);

		return result;
	}

	/* ���������ռ���� */
	private boolean[][] markNewCapture(int samuraiID) {
		boolean[][] result = new boolean[HEIGHT][WIDTH];
		int[][] curBattleField = curBoard.getBattleField();
		int[][] prevBattleField = prevBoard.getBattleField();
		for (int i = 0; i < HEIGHT; i++)
			for (int j = 0; j < WIDTH; j++)
				// �����غϸ���ʿ�����ĵؿ��ʶ����
				result[i][j] = curBattleField[i][j] == samuraiID && prevBattleField[i][j] != samuraiID;
		return result;
	}

	/* �������� */
	public int evaluate(Board board) {
		return Math.abs(diffCapture());
	}

	/* ����ռ������� */
	public int diffCapture() {
		int[][] battleField = curBoard.getBattleField();
		int result = 0;
		for (int[] row : battleField)
			for (int grid : row)
				result += (grid < 3 ? 1 : (grid != 8 ? -1 : 0));
		return result;
	}

	public void nextTurn() throws CloneNotSupportedException, IOException {
		Info turnInfo = IOManager.input();

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
		inferPosition();

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

	// void print() {
	// int[][] t = curBoard.getBattleField();
	// for (int[] i : t) {
	// for (int j : i)
	// System.out.print((j == 9 ? "x" : j) + " ");
	// System.out.println();
	// }
	// }
	//
	// public static void print(Board b) {
	// int[][] t = b.getBattleField();
	// for (int[] i : t) {
	// for (int j : i)
	// System.out.print((j == 9 ? "x" : j) + " ");
	// System.out.println();
	// }
	// }

	public static void main(String[] args) throws IOException, CloneNotSupportedException {
		Move[] moves = Move.values();
		GameManager gm = GameManager.init();
		gm.nextTurn();
		System.out.println(gm.evaluate(gm.curBoard));
		// gm.print();
		// int[] p = gm.curBoard.makeMove(moves[0]).samurais[0].getPos();
		// System.out.println(p[0] +","+ p[1]);
		// long s = System.nanoTime();
		// for (Move m : moves) {
		// // System.out.println(m.name());
		// gm.curBoard.makeMove(m);
		// // System.out.println();
		// }
		// long e = System.nanoTime();
		// System.out.println(e - s);
	}
}
