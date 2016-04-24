
/**
 * @author clefz created at 2016/3/22
 * 
 *        //zqh st clefz
 *         2016/4/8
 *        ������
 */
package njusoftware.noComment.SamurAI.base;

import java.util.Arrays;

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

	public Board(int[][] battleField, int turn) {
		this.battleField = battleField;
		for (int i = 0; i < 6; ++i)
			battleField[GameManager.HOME_POSES[i][1]][GameManager.HOME_POSES[i][0]] = i;
		this.turn = turn;
		samurais = GameManager.SAMURAIS;
	}

	// ����
	final public Board makeMove(Move move) throws CloneNotSupportedException {
		Board nextBoard = this.clone();

		int activeSamuraiID = ConstVar.ACTION_ORDER[nextBoard.turn % 12];// ȡ�ý��л����ʿ��ID
		Samurai activeSamurai = nextBoard.samurais[activeSamuraiID];// ȡ�ý��л����ʿ
		int[] samuraiPos = activeSamurai.getPos();// ȡ����ʿλ��
		nextBoard.turn++;// �غ�������

		// ռ������仯
		int[][] occupyResult;
		if ((occupyResult = move.getOccupyResult(activeSamurai.getWeapon())) != null)
			for (int[] occupied : occupyResult)
				nextBoard.set(occupied[0] + samuraiPos[0], occupied[1] + samuraiPos[1], activeSamuraiID);

		// ��ʿλ�ñ仯
		activeSamurai.move(move.getMoveResult());

		return nextBoard;
	}

	// �Ƿ��и���Ϸ�����
	final public boolean hasMoreMove() {
		if (!getCurrentSamurai().isActive())
			return false;
		while (moveIndex < 60) {
			if (isVaild(POSSIBLE_MOVES[moveIndex]))
				return true;
			moveIndex++;
		}
		moveIndex = 0;
		return false;
	}

	// ��һ���Ϸ�����
	final public Move nextMove() {
		Move move = POSSIBLE_MOVES[moveIndex];
		moveIndex++;
		return move;
	}

	final public boolean isEnd() {
		return turn >= GameManager.TOTAL_TURNS;
	}

	// �ж�ĳ�������Ƿ��ǺϷ���
	final private boolean isVaild(Move move) {
		int[] curPos = this.getCurrentSamurai().getPos();
		int[] offset = move.getMoveResult();
		return curPos[0] + offset[0] < GameManager.WIDTH && curPos[0] + offset[0] >= 0
				&& curPos[1] + offset[1] < GameManager.HEIGHT && curPos[1] + offset[1] >= 0;
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
		// ���ֻ��������ռ�ݵ������Զ����ҵ�λ���ų���ȥ
		if (x < 0 || y < 0 || x >= GameManager.WIDTH || y >= GameManager.HEIGHT)
			return;
		for (int i = 0; i < 6; ++i)
			if (GameManager.HOME_POSES[i][0] == x && GameManager.HOME_POSES[i][1] == y)
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
