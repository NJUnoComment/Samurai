
/**
 * @author clefz created at 2016/3/22
 * 
 *        //zqh st
 *         2016/04/02
 *        //zqh
 *         2016/04/06
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
		int actionSamuraiID = GameManager.ACTION_ORDER[turn % 12];
		// 在Board上标记占据的位置
		int[] samuraiPos = samurais[actionSamuraiID].getPos();
		int[] finalOccupyPos = new int[2];
		int[][] occupyResult = move.getOccupyResult(samurais[actionSamuraiID].getWeapon());
		if (occupyResult != null) {
			for (int[] occupied : occupyResult) {
				if (occupied.equals(GameManager.HOME_POSES)) {
					continue;
				} else {
					finalOccupyPos[0] = occupied[0] + samuraiPos[0];
					finalOccupyPos[1] = occupied[1] + samuraiPos[1];
					nextBoard.set(finalOccupyPos, actionSamuraiID);
				}
			}
		}
		// 修改武士的最终位置
		samurais[actionSamuraiID].move(move.getMoveResult());

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
