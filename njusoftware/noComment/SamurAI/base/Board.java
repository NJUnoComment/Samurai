
/**
 * @author clefz created at 2016/3/22
 * 
 *        //zqh st clefz
 *         2016/4/8
 *        局面类
 */
package njusoftware.noComment.SamurAI.base;

import java.util.Arrays;

public class Board implements Cloneable {
	private int[][] battleField;// 此字段包含战斗区域信息
	private int turn;// 回合数
	public Samurai[] samurais;
	private static final Move[] POSSIBLE_MOVES = Move.values();
	private int moveIndex = 0;// 用于迭代器的索引

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

	// 走棋
	public Board makeMove(Move move) throws CloneNotSupportedException {
		Board nextBoard = this.clone();

		int activeSamuraiID = GameManager.ACTION_ORDER[turn % 12];// 取得进行活动的武士的ID
		Samurai activeSamurai = nextBoard.samurais[activeSamuraiID];// 取得进行活动的武士
		int[] samuraiPos = activeSamurai.getPos();// 取得武士位置
		nextBoard.turn++;// 回合数增加

		// 占据区域变化
		int[][] occupyResult = move.getOccupyResult(activeSamurai.getWeapon());

		if (occupyResult == null)
			return nextBoard;

		for (int[] occupied : occupyResult)
			nextBoard.set(new int[] { occupied[1] + samuraiPos[0], occupied[0] + samuraiPos[1] }, activeSamuraiID);

		// 武士位置变化
		activeSamurai.move(move.getMoveResult());

		return nextBoard;
	}

	// 是否有更多合法操作
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

	// 下一个合法操作
	public Move nextMove() {
		return POSSIBLE_MOVES[moveIndex];
	}

	public boolean isEnd() {
		return turn >= GameManager.TOTAL_TURNS;
	}

	// 判断某个操作是否是合法的
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

		// 克隆局面信息
		int[][] nextBattleField = new int[GameManager.HEIGHT][];
		for (int i = 0; i < GameManager.HEIGHT; i++) {
			nextBattleField[i] = new int[width];
			System.arraycopy(this.battleField[i], 0, nextBattleField[i], 0, width);
		}
		nextBoard.battleField = nextBattleField;

		// 克隆武士
		Samurai[] nextSamurais = new Samurai[6];
		for (int i = 0; i < 6; i++)
			nextSamurais[i] = this.samurais[i].clone();
		nextBoard.samurais = nextSamurais;

		// 指针归零
		nextBoard.moveIndex = 0;

		return nextBoard;
	}

	// public void print() {
	// //测试用
	// System.out.println();
	// for (int[] t : battleField) {
	// for (int i : t)
	// System.out.print(i + " ");
	// System.out.println();
	// }
	// }
}
