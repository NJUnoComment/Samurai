
/**
 * @author clefz created at 2016/3/22
 * 
 *        //请在此处标注编写者姓名 
 *         请在此处填写最终修改时间
 *        此类是局面类
 */
package njusoftware.noComment.SamurAI.base;

public class Board implements Cloneable {
	private int[][] battleField;// 此字段包含战斗区域信息
	private int turn;
	private static final Move[] POSSIBLE_MOVES = Move.values();
	private int moveIndex = 0;// 用于迭代器的索引

	public boolean hasMoreValidMove() {
		while (moveIndex < POSSIBLE_MOVES.length) {
			moveIndex++;
			if (isVaild(POSSIBLE_MOVES[moveIndex]))
				return true;
		}
		moveIndex = 0;
		return false;
	}

	// 下一个合法操作
	public Move nextValidMove() {
		return POSSIBLE_MOVES[moveIndex];
	}

	// 判断是否某个操作是否是合法的
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
		// 一堆操作
		turn++;
		return nextBoard;
	}

	@Override
	protected Board clone() throws CloneNotSupportedException {
		Board nextBoard = (Board) super.clone();
		nextBoard.battleField = this.battleField.clone();
		// 还会有一堆操作
		return nextBoard;
	}
}
