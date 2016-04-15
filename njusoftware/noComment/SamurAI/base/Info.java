
/**
 * @author clefz created at 2016/3/22
 *
 *         //zqh st csy clefz
 *         edited at 2016/4/5
 *         此类用于封装在IOManager和GameManager之间传递的信息
 */
package njusoftware.noComment.SamurAI.base;

public class Info {

	/* 以下字段包含回合信息 */
	private int[][] board;
	private int[][] samuraiState;// 武士状态
	private int remainCurePeriod;// 剩余回复时间
	private int turn;// 回合序数

	/* 以下字段包含游戏信息 */
	private int totalTurns;// 总回合数
	private int samuraiID;// 控制的武士ID，由游戏方ID和武器ID合成
	private int height;// 战斗区域的长
	private int width;// 战斗区域的宽
	private int curePeriod;// 回复时间
	private int[][] homePos;// 家的位置
	private int[][] ranksAndScores;// 排名和分数

	/* 以下字段包含输出信息 */
	private int[] actions;// 动作

	public int[][] getBattleField() {
		return board;
	}

	public int getTurn() {
		return turn;
	}

	public int[][] getSamuraiState() {
		return samuraiState;
	}

	public int getRemainCurePeriod() {
		return remainCurePeriod;
	}

	public Info setBattleField(int[][] board) {
		this.board = board;
		return this;
	}

	public Info setSamuraiState(int[][] samuraiState) {
		this.samuraiState = samuraiState;
		return this;
	}

	public Info setRemainCurePeriod(int remainCurePeriod) {
		this.remainCurePeriod = remainCurePeriod;
		return this;
	}

	public Info setTurn(int turn) {
		this.turn = turn;
		return this;
	}

	public int getTotalTurns() {
		return totalTurns;
	}

	public int getSamuraiID() {
		return samuraiID;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public int getCurePeriod() {
		return curePeriod;
	}

	public int[][] getHomePos() {
		return homePos;
	}

	public int[][] getRanksAndScores() {
		return ranksAndScores;
	}

	public Info setTotalTurns(int totalTurns) {
		this.totalTurns = totalTurns;
		return this;
	}

	public Info setSamuraiID(int samuraiID) {
		this.samuraiID = samuraiID;
		return this;
	}

	public Info setHeight(int height) {
		this.height = height;
		return this;
	}

	public Info setWidth(int width) {
		this.width = width;
		return this;
	}

	public Info setCurePeriod(int curePeriod) {
		this.curePeriod = curePeriod;
		return this;
	}

	public Info setHomePos(int[][] homePos) {
		this.homePos = homePos;
		return this;
	}

	public Info setRanksAndScores(int[][] ranksAndScores) {
		this.ranksAndScores = ranksAndScores;
		return this;
	}

	public int[] getActions() {
		return actions;
	}

	public Info setActions(int[] actions) {
		this.actions = actions;
		return this;
	}

}
