
/**
 * @author clefz created at 2016/3/22
 * 
 *        //���ڴ˴���ע��д������ 
 *         ���ڴ˴���д�����޸�ʱ��
 *        �����Ǿ�����
 */
package njusoftware.noComment.SamurAI.base;

public class Board implements Cloneable {
	private int[][] battleField;// ���ֶΰ���ս��������Ϣ
	private int turn;
	private static final Move[] POSSIBLE_MOVES = Move.values();
	private int moveIndex = 0;// ���ڵ�����������

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

	// �ж��Ƿ�ĳ�������Ƿ��ǺϷ���
	private boolean isVaild(Move move) {
		return false;
	}

	public void setBattleField(int[][] theBattleField) {
		this.battleField = theBattleField;
	}

	public int[][] getBattleField() {
		return this.battleField;
	}

	public boolean isEnd() {
		return turn >= GameManager.totalTurns;
	}

	public Board makeMove(Move move) throws CloneNotSupportedException {
		Board nextBoard = this.clone();
		// һ�Ѳ���
		turn++;
		return nextBoard;
	}

	@Override
	protected Board clone() throws CloneNotSupportedException {
		Board nextBoard = (Board) super.clone();
		nextBoard.battleField = this.battleField.clone();
		// ������һ�Ѳ���
		return nextBoard;
	}
}
