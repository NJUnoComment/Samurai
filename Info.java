
/**
 * @author clefz created at 2016/3/22
 *
 *         //zqh st csy 
 *         edited at 2016/3/27
 *         此类用于封装在IOManager和GameManager之间传递的信息
 */
package njusoftware.noComment.SamurAI.base;

public class Info {
	static final int GAME_INFO = 0x1111, TURN_INFO = 0x2222, OUTPUT_INFO = 0x3333; // 这三个常数用来标记信息类型
	private int kind = GAME_INFO;// 信息类型

	/* 以下字段包含回合信息 */
	private Board primalBorad;// 未处理过的局面信息
	private int[][] board;
	private int[][] samuraiState;// 武士状态
	private int remainCurePeriod;// 剩余回复时间
	private int turn;// 回合序数

	/* 以下字段包含游戏信息 */
	private int totalTurns;// 总回合数
	private int samuraiID;// 控制的武士ID，由游戏方ID和武器ID合成
	private int length;// 战斗区域的长
	private int width;// 战斗区域的宽
	private int curePeriod;// 回复时间
	private int[][] homePos;// 家的位置
	private int[][] ranksAndScores;// 排名和分数

	/* 以下字段包含输出信息 */
	private int[] actions;// 动作
	
	/*以下字段包含标记的getter和setter*/
	public int getKind() {
		return kind;
	}

	public Info setKind(int kind) {
		this.kind = kind;
		return this;
	}
	
    /*以下字段包含回合的getter和setter*/
	public Board getPrimalBorad() {
		return primalBorad;
	}

	public Info setPrimalBorad(Board primalBorad) {
		this.primalBorad = primalBorad;
		return this;
	}

	public Info setBoard(int[][] board) {
		this.board = board;
		return this;
	}

	public int[][] getBoard() {
		return board;
	}

	public int[][] getSamuraiState() {
		return samuraiState;
	}

	public Info setSamuraiState(int[][] samuraiState) {
		this.samuraiState = samuraiState;
		return this;
	}

	public int getRemainCurePeriod() {
		return remainCurePeriod;
	}

	public Info setRemainCurePeriod(int remainCurePeriod) {
		this.remainCurePeriod = remainCurePeriod;
		return this;
	}

	public int getTurn() {
		return turn;
	}

	public Info setTurn(int turn) {
		this.turn = turn;
		return this;
	}

	/*以下字段包含游戏的getter和setter*/
	public int getTotalTurns() {
		return totalTurns;
	}

	public Info setTotalTurns(int totalTurns) {
		this.totalTurns = totalTurns;
		return this;
	}

	public int getSamuraiID() {
		return samuraiID;
	}

	public Info setSamuraiID(int gameID,int weaponID) {
		this.samuraiID = gameID*3+weaponID;
		return this;
	}

	public int getLength() {
		return length;
	}

	public Info setLength(int length) {
		this.length = length;
		return this;
	}
	public int getWidth() {
		return width;
	}

	public Info setWidth(int width) {
		this.width = width;
		return this;
	}


	public int getCurePeriod() {
		return curePeriod;
	}

	public Info setCurePeriod(int curePeriod) {
		this.curePeriod = curePeriod;
		return this;
	}

	public int[][] getHomePos() {
		return homePos;
	}

	public Info setHomePos(int[][] homePos) {
		this.homePos = homePos;
		return this;
	}

	public int[][] getRanksAndScores() {
		return ranksAndScores;
	}

	public Info setRanksAndScores(int[][] ranksAndScores) {
		this.ranksAndScores = ranksAndScores;
		return this;
	}

	/*以下字段包含输出的getter和setter*/
	public int[] getActions() {
		return actions;
	}

	public Info setActions(int[] actions) {
		this.actions = actions;
		return this;
	}

	// 为了方便调用建议setter全部使用连用形
	
	
}

