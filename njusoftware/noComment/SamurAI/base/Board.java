
/**
 * @author clefz created at 2016/3/22
 * 
 *        //zqh st
 *         2016/04/02
 *        �����Ǿ�����
 */
package njusoftware.noComment.SamurAI.base;

import java.io.IOException;
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

		// ��ʿλ�ñ仯
		activeSamurai.move(move.getMoveResult());

		// ռ������仯
		int[][] occupyResult = move.getOccupyResult(activeSamurai.getWeapon());

		if (occupyResult == null)
			return nextBoard;

		for (int[] occupied : occupyResult)
			nextBoard.set(new int[] { occupied[1] + samuraiPos[0], occupied[0] + samuraiPos[1] }, activeSamuraiID);

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

	// �ж��Ƿ�ĳ�������Ƿ��ǺϷ���
	private boolean isVaild(Move move) {
		return false;
	}

	public int[][] getBattleField() {
		return this.battleField;
	}

	public void set(int[] pos, int val) {
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
}
