
/**
 * @author clefz created at 2016/3/23
 * 
 *         //clefz
 *         2016/4/8
 *         �������ڹ����������棬��Board���в����������������������ʽ������һ�����ܵĺϷ�����
 */
package njusoftware.noComment.SamurAI.base;

import java.io.IOException;
import java.util.Random;

import njusoftware.noComment.SamurAI.AI.AIManager;

public class GameManager {
	private static final Random r = new Random();
	private AIManager AI;
	private Board prevBoard;// ��һ�غϵľ���
	private Board curBoard;// ��ǰ�غϵľ���
	private int curTurn;// ��ǰ�غ���
	private int remainCurePeriod;

	// private static int count = 0;

	public static int WIDTH;
	public static int HEIGHT;
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
		for (int i = 0; i < 6; ++i) {
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
		for (int i = 0; i < HEIGHT; ++i)
			for (int j = 0; j < WIDTH; ++j)
				if (current[i][j] == 9)
					current[i][j] = previous[i][j];
	}

	/* �Ʋ��������ʿλ�� */
	private void inferPosition() {
		// ֻ���Ʋ�з����ɼ���ʿ��λ��
		int tmp = 3 - (SAMURAI_ID > 2 ? 3 : 0);
		for (int i = tmp; i < tmp + 3; i++)
			if (!SAMURAIS[i].isVisible())
				inferPositionOf(i);
	}

	/* �ض���ʿ��λ���Ʋ� */
	final private void inferPositionOf(int samuraiID) {
		int[][] processedMap = processMap(samuraiID);
		int max = 0, count = 0;
		// �ҳ����ֵ������
		for (int i = 0; i < HEIGHT; ++i)
			for (int j = 0; j < WIDTH; ++j) {
				int tmp = processedMap[i][j];
				if (tmp > max) {
					max = tmp;
					count = 0;
				}
				count += tmp == max ? 1 : 0;
			}

		// �����ֵλ�������ѡ��һ��λ�ò�����Ϊ��ʿ��λ��
		int index = r.nextInt(count);
		for (int i = 0; i < HEIGHT; ++i)
			for (int j = 0; j < WIDTH; ++j) {
				if (processedMap[i][j] == max)
					count--;
				if (count != index)
					continue;
				SAMURAIS[samuraiID].setPos(j, i);
				return;
			}
	}

	/* Ԥ�����ͼ�������ʿ�ڵ�ͼ��ĳ��λ�õĿ����ԣ���Ȩ��ֵ���� */
	final private int[][] processMap(int samuraiID) {
		int[] curPos = SAMURAIS[samuraiID].getPos();
		int curX = curPos[0], curY = curPos[1];// ֱ��ȡ���������������Ч��

		boolean[][] markedMap = markNewCapture(samuraiID);

		return postprocess(isCaptureChanged(markedMap) ? preprocess(markedMap, curX, curY) : preprocess(curX, curY),
				curX, curY);

	}

	/* ���ռ�ݷ�Χ�Ƿ����˸ı� */
	final static private boolean isCaptureChanged(boolean[][] markedMap) {
		boolean isCaptureChanged = false;
		for (boolean[] p : markedMap)
			for (boolean i : p)
				if (isCaptureChanged = i)
					break;
		return isCaptureChanged;
	}

	/* ��������λ�ô�����Ȩ�� */
	final static private int[][] postprocess(int[][] result, int curX, int curY) {
		// ��ǰ����λ�ü�һ��Ȩ��
		result[curY][curX] += ConstVar.PREV_POS_POW;

		// ��ǰ����λ�õ���Χ��һ��Ȩ��
		for (int[] tmp : ConstVar.OCCUPIED_MOVE_RANGE)
			if (isInBound(curX, tmp[0], curY, tmp[1]))
				result[curY + tmp[1]][curX + tmp[0]] += ConstVar.POS_SURR_POW;
		return result;
	}

	/* Ԥ��������������ռ��������� */
	final static private int[][] preprocess(boolean[][] markedMap, int curX, int curY) {
		int[][] result = new int[HEIGHT][WIDTH];

		for (int i = 0; i < HEIGHT; ++i)
			for (int j = 0; j < WIDTH; ++j)
				if (markedMap[i][j]) {// �ҵ������仯�ĵط�
					result[i][j] = ConstVar.NEW_CAPTURE_POW;// �õؿ鱾���һ��Ȩ��
					for (int[] tmp : ConstVar.SURROUNDINGS) // ���ڷ����仯��λ����Χ�ĸ���
						if (isInBound(i, tmp[1], j, tmp[0]))// �ڲ�Խ���������
							// ���ڲ�����������ĸ��Ӹ�һ��Ȩ�أ���������������ĸ��Ӹ���һ��Ȩ��
							// ע��˴��ǲ����е��ӵ�
							if (markedMap[i + tmp[1]][j + tmp[0]])
								result[i + tmp[1]][j + tmp[0]] = ConstVar.NEW_SURR_POW;
				}
		return result;
	}

	/* Ԥ��������δ����ռ��������� */
	final static private int[][] preprocess(int curX, int curY) {
		int[][] result = new int[HEIGHT][WIDTH];
		int tmp = SAMURAI_ID > 2 ? 3 : 0;// �з��ĵз�Ҳ�����ҷ�

		// �����ҷ�ÿһ����ʿ��ָ���з���ʿ����һ�������ڣ���ConstVar�зֿ��ƶ���Χ������±���б�ʶ
		int[] enemyPos1 = SAMURAIS[tmp].getPos();
		int[] enemyPos2 = SAMURAIS[tmp + 1].getPos();
		int[] enemyPos3 = SAMURAIS[tmp + 2].getPos();
		int flag1 = (enemyPos1[0] < curX ? 1 : 0) & (enemyPos1[1] < curY ? 2 : 0);
		int flag2 = (enemyPos2[0] < curX ? 1 : 0) & (enemyPos2[1] < curY ? 2 : 0);
		int flag3 = (enemyPos3[0] < curX ? 1 : 0) & (enemyPos3[1] < curY ? 2 : 0);

		for (int i = 0; i < 4; ++i)
			for (int[] p : ConstVar.SEPERATED_MOVE_RANGE[i])
				// ����е�����ĳһ�����У������������е�Ȩ��ֵ����ȥ1
				if (isInBound(curX, p[0], curY, p[1]))
					result[curY + p[1]][curX + p[0]] -= (flag1 == i ? 1 : 0) + (flag2 == i ? 1 : 0)
							+ (flag3 == 0 ? 1 : 0);

		return result;
	}

	/* ���������ռ���� */
	final private boolean[][] markNewCapture(int samuraiID) {
		boolean[][] result = new boolean[HEIGHT][WIDTH];
		int[][] curBattleField = curBoard.getBattleField();
		int[][] prevBattleField = prevBoard.getBattleField();
		for (int i = 0; i < HEIGHT; ++i)
			for (int j = 0; j < WIDTH; ++j)
				// �����غϸ���ʿ�����ĵؿ��ʶ����
				result[i][j] = curBattleField[i][j] == samuraiID && prevBattleField[i][j] != samuraiID;
		return result;
	}

	/* �Ƿ�Խ�� */
	final static private boolean isInBound(int x, int deltaX, int y, int deltaY) {
		return x + deltaX >= 0 && y + deltaY >= 0 && x + deltaX < HEIGHT && y + deltaY < WIDTH;
	}

	/* ����ռ������� */
	final static private int diffCapture(Board board) {
		int[][] battleField = board.getBattleField();
		int tmp = SAMURAI_ID < 3 ? 0 : 3;
		int result = 0;
		for (int[] row : battleField)
			for (int grid : row)
				result += grid == 8 ? 0 : (!(grid > 2 ^ tmp == 3) ? 1 : -1);
		return result;
	}

	/* �������� */
	final public int evaluate(Board board) {
		// count++;
		// System.out.println(count);
		// ������������Զ���ҷ��ӽ�������
		// ������Ϸ����˫����ÿһ�غϽ�����еģ�
		// ���������һ�غ����ѷ��غϣ���Ҫ������ֵ���ϸ���
		// ��������֦�㷨�еĸ���
		// ����ż���غϲ����ߣ���ż���غϼӸ���
		return diffCapture(board) * (board.getTurn() & 1) == 0 ? -1 : 1;
	}

	public void nextTurn() throws CloneNotSupportedException, IOException {
		Info turnInfo = IOManager.input(false);

		// ʣ��ظ��غ�
		remainCurePeriod = turnInfo.getRemainCurePeriod();

		// ��ǰ����
		prevBoard = curBoard == null ? prevBoard : curBoard;
		curBoard = new Board(turnInfo.getBattleField(), curTurn = turnInfo.getTurn());
		inferMap();

		// ��ʿ״̬
		int[][] samuraiState = turnInfo.getSamuraiState();
		if (SAMURAI_ID < 3)
			for (int i = 0; i < 6; ++i) {
				if (-1 != samuraiState[i][0])
					SAMURAIS[i].setPos(samuraiState[i][0], samuraiState[i][1]);
				SAMURAIS[i].setVisible(0 == samuraiState[i][2]);
				SAMURAIS[i].setActive(-1 != samuraiState[i][2]);
			}
		else
			for (int i = 0; i < 6; ++i) {
				if (-1 != samuraiState[i][0])
					SAMURAIS[i < 3 ? i + 3 : i - 3].setPos(samuraiState[i][0], samuraiState[i][1]);
				SAMURAIS[i < 3 ? i + 3 : i - 3].setVisible(0 == samuraiState[i][2]);
				SAMURAIS[i < 3 ? i + 3 : i - 3].setActive(-1 != samuraiState[i][2]);
			}
		inferPosition();

		// ����ʱֱ�����0
		if (remainCurePeriod != 0)
			IOManager.output(new Info().setActions(new int[] { 0 }));
		else
			// ���
			IOManager.output(new Info().setActions(AI.decideActions()));
	}

	public static GameManager init() throws IOException {
		Info gameInfo = IOManager.input(true);
		TOTAL_TURNS = gameInfo.getTotalTurns();
		SAMURAI_ID = gameInfo.getSamuraiID();
		CURE_PERIOD = gameInfo.getCurePeriod();
		WIDTH = gameInfo.getWidth();
		HEIGHT = gameInfo.getHeight();

		if (SAMURAI_ID < 3)
			HOME_POSES = gameInfo.getHomePos();
		else {
			HOME_POSES = new int[6][];
			int[][] tmp = gameInfo.getHomePos();
			for (int i = 0; i < 6; i++)
				HOME_POSES[i < 3 ? i + 3 : i - 3] = tmp[i];
		}

		return new GameManager();
	}

	public Board getBoard() {
		return curBoard;
	}

	public static void print(int[][] i) {
		for (int[] p : i) {
			for (int q : p)
				System.err.print(q + " ");
			System.err.println();
		}
	}

	// public static void main(String[] args) throws IOException,
	// CloneNotSupportedException {
	// // GameManager.init();
	// // SAMURAIS[5].setPos(5, 0);
	// // int[][] b = new int[][] {
	// // { 8, 8, 8, 8, 8, 5, 8, 8, 8, 8, 8, 8, 8, 8, 4 },
	// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 4, 4, 8 },
	// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 4, 4 },
	// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 4 },
	// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
	// // { 0, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
	// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
	// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
	// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
	// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 3 },
	// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
	// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
	// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
	// // { 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8 },
	// // { 1, 8, 8, 8, 8, 8, 8, 8, 8, 2, 8, 8, 8, 8, 8 }, };
	// // Board k = new Board(b, 5);
	// // Board c=k.makeMove(Move.MS_OS);
	// // System.out.println(c.getBattleField().length);
	// // GameManager.print(c.getBattleField());
	// // System.out.println(GameManager.diffCapture(c));
	// // System.out.println(GameManager.diffCapture(c));
	// // System.out.println(GameManager.diffCapture(new Board(b, 0)));
	// GameManager gm = GameManager.init();
	// gm.nextTurn();
	// System.out.println(gm.curBoard.isFriendArea(4, 0));
	// // GameManager.print(gm.prevBoard.getBattleField());
	// // gm.nextTurn();
	// // GameManager.print(gm.curBoard.getBattleField());
	// // int[][] poses = new int[6][2];
	// // for (int i = 0; i < 6; ++i)
	// // poses[i] = GameManager.SAMURAIS[i].getPos();
	// // for (int i = 0; i < 6; ++i)
	// // System.out.println(i + ":" + poses[i][0] + "," + poses[i][1]);
	// }
}
