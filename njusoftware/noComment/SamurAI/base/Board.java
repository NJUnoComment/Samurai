
/**
 * @author clefz created at 2016/3/22
 * 
 *        //zqh st clefz
 *         2016/4/8
 *        ������
 */
package njusoftware.noComment.SamurAI.base;

import java.util.Arrays;

import njusoftware.noComment.SamurAI.AI.Evaluator;

public class Board implements Cloneable {
	private int[][] battleField;// ���ֶΰ���ս��������Ϣ
	private int turn;// �غ���
	public Samurai[] samurais;
	private static final Move[] POSSIBLE_MOVES = Move.values();
	private int moveIndex = 0;// ���ڵ�����������

	public Board() {
		turn = 0;
		battleField = new int[GameManager.HEIGHT][GameManager.WIDTH];
		for (int[] tmp : battleField)
			Arrays.fill(tmp, 8);
		samurais = GameManager.SAMURAIS;
	}

	public Board(final int[][] battleField, int turn) {
		this.turn = turn;
		this.battleField = battleField;
		for (int i = 0; i < 6; ++i)
			this.battleField[GameManager.HOME_POSES[i][1]][GameManager.HOME_POSES[i][0]] = i;
		samurais = GameManager.SAMURAIS;
	}

	// ����
	public final Board makeMove(Move move) throws CloneNotSupportedException {
		return makeMove(move, GameManager.SAMURAI_ID);
	}

	public final Board makeMove(Move move, int samuraiID) throws CloneNotSupportedException {
		Board nextBoard = this.clone();
		nextBoard.refreshSamuraisState();

		Samurai movingSamurai = nextBoard.samurais[samuraiID];

		int[][] occupyResult;
		if ((occupyResult = move.getOccupyResult(movingSamurai.getWeapon())) != null) {
			// ��������AI���㷨�����ܻ����move��ֻ���Լ��͵з�
			// ���й�����ֻ���Լ������Դ˴������˼�
			if (samuraiID == GameManager.SAMURAI_ID)
				attack(nextBoard, occupyResult);

			int[] position = movingSamurai.getPos();
			for (int[] occupiedGrid : occupyResult)
				// ռ������仯
				nextBoard.set(occupiedGrid[0] + position[0], occupiedGrid[1] + position[1], samuraiID);
		}

		// ��ʿλ�ñ仯
		movingSamurai.move(move.getMoveResult());

		nextBoard.turn++;// �غ�������
		return nextBoard;
	}

	public final void attack(Board nextBoard, int[][] occupyResult) {
		int[] position = GameManager.SAMURAIS[GameManager.SAMURAI_ID].getPos();
		int enemyID = GameManager.ENEMY_INDEX;// �з�

		int[][] deltaPosition = new int[3][2];// �������λ��
		for (int i = enemyID, k = enemyID + 3; i < k; ++i)
			if (samurais[i].isAlive() && samurais[i].isActive() && samurais[i].isVisible()) {
				int[] enemyPosition = samurais[i].getPos();
				deltaPosition[i - enemyID][0] = enemyPosition[0] - position[0];
				deltaPosition[i - enemyID][1] = enemyPosition[1] - position[1];
			} else
				deltaPosition[i - enemyID] = null;

		Samurai[] samurais = nextBoard.samurais;
		for (int[] occupiedGrid : occupyResult)
			for (int i = 0; i < 3; i++) {
				if (deltaPosition[i] == null)
					continue;
				int tmp = i + enemyID;
				if (!samurais[tmp].isAlive())
					continue;
				Samurai injuredSamurai = samurais[tmp];
				if (Evaluator.pointEqual(deltaPosition[i], occupiedGrid)) {
					injuredSamurai.setAlive(false);
					injuredSamurai.setPos(GameManager.HOME_POSES[tmp]);
					injuredSamurai.setCuredTurn(GameManager.CURE_PERIOD + nextBoard.turn);
				}
			}
	}

	// ˢ����ʿ�Ƿ����˵�״̬
	public final void refreshSamuraisState() {
		for (Samurai samurai : samurais)
			if (!samurai.isAlive() && samurai.isActive())
				if (samurai.getCuredTurn() <= turn) {
					samurai.setAlive(true);
					samurai.setCuredTurn(0);
				}
	}

	// �Ƿ��и���Ϸ�����
	public final boolean hasMoreMove() {
		if (moveIndex == 61) {
			moveIndex = 0;
			return false;
		}
		if (!getCurrentSamurai().isAlive() || !getCurrentSamurai().isActive()) {
			moveIndex = 60;
			return true;
		}
		while (moveIndex < 60) {
			if (isVaild(POSSIBLE_MOVES[moveIndex]))
				return true;
			moveIndex++;
		}
		moveIndex = 0;
		return false;
	}

	// ��һ���Ϸ�����
	public final Move nextMove() {
		Move move = POSSIBLE_MOVES[moveIndex];
		moveIndex++;
		return move;
	}

	public final boolean isEnd() {
		return turn >= GameManager.TOTAL_TURNS;
	}

	// �ж�ĳ�������Ƿ��ǺϷ���
	private final boolean isVaild(Move move) {
		int[] curPos = this.getCurrentSamurai().getPos();
		int[] offset = move.getMoveResult();
		return GameManager.inBound(curPos[0], offset[0], curPos[1], offset[1]);
	}

	public int[][] getBattleField() {
		return this.battleField;
	}

	public Samurai getCurrentSamurai() {
		return this.samurais[ConstVar.ACTION_ORDER[turn % 12]];
	}

	public int getTurn() {
		return turn;
	}

	public boolean isFriendArea(int x, int y) {
		return !(battleField[y][x] == 8) && !(battleField[y][x] < 3 ^ GameManager.SAMURAI_ID < 3);
	}

	public boolean isFriendArea(int[] pos) {
		return isFriendArea(pos[0], pos[1]);
	}

	public void set(int[] pos, int val) {
		// ������������Ȼ�����ˣ��������ǲ�ͬ��
		// ���ֻ�������üҵ�λ��
		this.battleField[pos[1]][pos[0]] = val;
	}

	public void set(int x, int y, int val) {
		// ���ֻ��������ռ�ݵ������Զ����ҵ�λ���ų�
		if (x < 0 || y < 0 || x >= GameManager.WIDTH || y >= GameManager.HEIGHT)
			return;
		for (int i = 0; i < 6; ++i)
			if (x == GameManager.HOME_POSES[i][0] && y == GameManager.HOME_POSES[i][1])
				return;
		this.battleField[y][x] = val;
	}

	public void setBattleField(final int[][] theBattleField) {
		this.battleField = theBattleField;
	}

	@Override
	protected Board clone() throws CloneNotSupportedException {
		int width = GameManager.WIDTH;
		int height = GameManager.HEIGHT;

		Board nextBoard = (Board) super.clone();

		// ��¡������Ϣ
		int[][] nextBattleField = new int[height][];
		for (int i = 0; i < height; i++) {
			nextBattleField[i] = new int[width];
			System.arraycopy(this.battleField[i], 0, nextBattleField[i], 0, width);
		}
		nextBoard.battleField = nextBattleField;

		// ��¡��ʿ
		Samurai[] nextSamurais = new Samurai[6];
		for (int i = 0; i < 6; ++i)
			nextSamurais[i] = this.samurais[i].clone();
		nextBoard.samurais = nextSamurais;

		// ָ�����
		nextBoard.moveIndex = 0;

		return nextBoard;
	}
}
