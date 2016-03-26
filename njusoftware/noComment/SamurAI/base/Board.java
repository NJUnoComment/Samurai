
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

	private int moveIndex = 0;// ���ڵ�����������

	public boolean hasMoreValidMove() {
		while (moveIndex < 60) {
			moveIndex++;
			if (isVaild(Move.possibleMoves[moveIndex]))
				return true;
		}
		moveIndex = 0;
		return false;
	}

	// ��һ���Ϸ�����
	public Move nextValidMove() {
		return Move.possibleMoves[moveIndex];
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

	// �һ����ΪʲôҪ�����clone����
	@Override
	protected Board clone() throws CloneNotSupportedException {
		Board nextBoard = (Board) super.clone();
		nextBoard.battleField = this.battleField.clone();
		// ������һ�Ѳ���
		return nextBoard;
	}
}
