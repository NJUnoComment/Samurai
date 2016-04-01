
/**
 * @author clefz created at 2016/3/22
 * 
 *        //���ڴ˴���ע��д������ 
 *         ���ڴ˴���д�����޸�ʱ��
 *        �����Ǿ�����
 */
package njusoftware.noComment.SamurAI.base;

import java.util.Arrays;

public class Board implements Cloneable {
	private int[][] battleField;// ���ֶΰ���ս��������Ϣ
	private int turn;
	private static final Move[] POSSIBLE_MOVES = Move.values();
	private int moveIndex = 0;// ���ڵ�����������
	private Samurai[] samurais;

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

	public boolean hasMoreValidMove() {
		while (moveIndex < POSSIBLE_MOVES.length) {
			moveIndex++;
			if (isVaild(POSSIBLE_MOVES[moveIndex]))
				return true;
		}
		moveIndex = 0;
		return false;
	}

	// ��һ���Ϸ�����
	public Move nextValidMove() {
		return POSSIBLE_MOVES[moveIndex];
	}

	public void set(int[] pos, int val) {
		battleField[pos[0]][pos[1]] = val;
	}

	// �ж��Ƿ�ĳ�������Ƿ��ǺϷ���
	private boolean isVaild(Move move) {
		return false;
	}

	public void setBattleField(final int[][] theBattleField) {
		this.battleField = theBattleField;
	}

	public int[][] getBattleField() {
		return this.battleField;
	}

	public boolean isEnd() {
		return turn >= GameManager.TOTAL_TURNS;
	}

	public Board makeMove(Move move) throws CloneNotSupportedException {
		Board nextBoard = this.clone();
		// һ�Ѳ���
		turn++;
		return nextBoard;
	}

	@Override
	protected Board clone() throws CloneNotSupportedException {
		int width = GameManager.WIDTH;

		Board nextBoard = (Board) super.clone();

		int[][] nextBattleField = new int[GameManager.HEIGHT][];
		for (int i = 0; i < GameManager.HEIGHT; i++) {
			nextBattleField[i] = new int[width];
			System.arraycopy(this.battleField[i], 0, nextBattleField[i], 0, width);
		}
		nextBoard.battleField = nextBattleField;

		for (int i = 0; i < 6; i++)
			nextBoard.samurais[i] = this.samurais[i].clone();

		nextBoard.moveIndex = 0;

		return nextBoard;
	}
}
