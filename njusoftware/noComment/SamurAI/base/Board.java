
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
		this.turn = turn;
		samurais = GameManager.SAMURAIS;
	}

	// ����
	public Board makeMove(Move move) throws CloneNotSupportedException {
		Board nextBoard = this.clone();

		int activeSamuraiID = GameManager.ACTION_ORDER[turn % 12];// ȡ�ý��л����ʿ��ID
		Samurai activeSamurai = nextBoard.samurais[activeSamuraiID];// ȡ�ý��л����ʿ
		int[] samuraiPos = activeSamurai.getPos();// ȡ����ʿλ��
		nextBoard.turn++;// �غ�������

		// ռ������仯
		int[][] occupyResult = move.getOccupyResult(activeSamurai.getWeapon());

		if (occupyResult == null)
			return nextBoard;

		for (int[] occupied : occupyResult)
			nextBoard.set(new int[] { occupied[1] + samuraiPos[0], occupied[0] + samuraiPos[1] }, activeSamuraiID);

		// ��ʿλ�ñ仯
		activeSamurai.move(move.getMoveResult());

		return nextBoard;
	}

	// �Ƿ��и���Ϸ�����
	public boolean hasMoreMove() {
		if (samurais[GameManager.ACTION_ORDER[turn % 12]].isActive() == false)
			return false;
		while (moveIndex < POSSIBLE_MOVES.length) {
			moveIndex++;
			if (isVaild(POSSIBLE_MOVES[moveIndex]))
				return true;
		}
		moveIndex = 0;
		return false;
	}

	// ��һ���Ϸ�����
	public Move nextMove() {
		return POSSIBLE_MOVES[moveIndex];
	}

	public boolean isEnd() {
		return turn >= GameManager.TOTAL_TURNS;
	}

	// �ж�ĳ�������Ƿ��ǺϷ���
	private boolean isVaild(Move move) {
		int[] curPos = this.getCurrentSamurai().getPos();
		int[] offset = move.getMoveResult();
		return curPos[0] + offset[0] <= GameManager.WIDTH && curPos[0] + offset[0] >= 0
				&& curPos[1] + offset[1] <= GameManager.HEIGHT && curPos[1] + offset[1] >= 0;
	}

	public int[][] getBattleField() {
		return this.battleField;
	}

	public Samurai getCurrentSamurai() {
		return this.samurais[GameManager.ACTION_ORDER[turn % 12]];
	}

	public boolean isFriendArea(int x, int y) {
		return battleField[y][x] / 3 == GameManager.SAMURAI_ID / 3;
	}

	public boolean isFriendArea(int[] pos) {
		return isFriendArea(pos[0], pos[1]);
	}

	public void set(int[] pos, int val) {
		if (pos[0] < 0 || pos[1] < 0 || pos[0] >= GameManager.HEIGHT || pos[1] >= GameManager.WIDTH)
			return;
		for (int i = 0; i < 6; i++)
			if (GameManager.HOME_POSES[i][0] == pos[0])
				if (GameManager.HOME_POSES[i][1] == pos[1])
					return;
		battleField[pos[0]][pos[1]] = val;
	}

	public void setBattleField(final int[][] theBattleField) {
		this.battleField = theBattleField;
	}

	@Override
	protected Board clone() throws CloneNotSupportedException {
		int width = GameManager.WIDTH;

		Board nextBoard = (Board) super.clone();

		// ��¡������Ϣ
		int[][] nextBattleField = new int[GameManager.HEIGHT][];
		for (int i = 0; i < GameManager.HEIGHT; i++) {
			nextBattleField[i] = new int[width];
			System.arraycopy(this.battleField[i], 0, nextBattleField[i], 0, width);
		}
		nextBoard.battleField = nextBattleField;

		// ��¡��ʿ
		Samurai[] nextSamurais = new Samurai[6];
		for (int i = 0; i < 6; i++)
			nextSamurais[i] = this.samurais[i].clone();
		nextBoard.samurais = nextSamurais;

		// ָ�����
		nextBoard.moveIndex = 0;

		return nextBoard;
	}

	// public void print() {
	// //������
	// System.out.println();
	// for (int[] t : battleField) {
	// for (int i : t)
	// System.out.print(i + " ");
	// System.out.println();
	// }
	// }
}
