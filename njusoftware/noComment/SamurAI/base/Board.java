
/**
 * @author clefz created at 2016/3/22
 * 
 *        //zqh st
 *         2016/04/02
 *        此类是局面类
 */
package njusoftware.noComment.SamurAI.base;

import java.util.Arrays;

public class Board implements Cloneable {
	private int[][] battleField;// 此字段包含战斗区域信息
	private int turn;
	private static final Move[] POSSIBLE_MOVES = Move.values();
	private int moveIndex = 0;// 用于迭代器的索引
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

	// 下一个合法操作
	public Move nextValidMove() {
		return POSSIBLE_MOVES[moveIndex];
	}

	public void set(int[] pos, int val) {
		battleField[pos[0]][pos[1]] = val;
	}

	// 判断是否某个操作是否是合法的
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
		// 一堆操作
		// 在Board上标记占据的位置
		int[] samuraiPos = samurais[turn % 12].getPos();
		int[] finalOccupyPos = new int[2];
		if (move.getOccupyResult(samurais[turn % 12].getWeapon()) != null) {

			int[][] occupyResult = move.getOccupyResult(samurais[turn % 12].getWeapon());
			for (int i = 0; i < occupyResult.length; i++) {

				int[] occupied = occupyResult[i];
				finalOccupyPos[0] = occupied[0] + samuraiPos[0];
				finalOccupyPos[1] = occupied[1] + samuraiPos[1];

				nextBoard.set(finalOccupyPos, (turn % 12));
			}
		}
		// 修改武士的最终位置
		int[] moveResult = move.getMoveResult();
		int[] finalSamuraiPos = new int[2];
		finalSamuraiPos[0] = moveResult[0] + samuraiPos[0];
		finalSamuraiPos[1] = moveResult[1] + samuraiPos[1];
		samurais[turn % 12].setPos(finalSamuraiPos);

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
