
/**
 * @author clefz created at 2016/3/22
 *
 *         //zqh st csy clefz
 *         edited at 2016/4/5
 *         �������ڷ�װ��IOManager��GameManager֮�䴫�ݵ���Ϣ
 */
package njusoftware.noComment.SamurAI.base;

public class Info {

	/* �����ֶΰ����غ���Ϣ */
	private int[][] board;
	private int[][] samuraiState;// ��ʿ״̬
	private int remainCurePeriod;// ʣ��ظ�ʱ��
	private int turn;// �غ�����

	/* �����ֶΰ�����Ϸ��Ϣ */
	private int totalTurns;// �ܻغ���
	private int samuraiID;// ���Ƶ���ʿID������Ϸ��ID������ID�ϳ�
	private int height;// ս������ĳ�
	private int width;// ս������Ŀ�
	private int curePeriod;// �ظ�ʱ��
	private int[][] homePos;// �ҵ�λ��
	private int[][] ranksAndScores;// �����ͷ���

	/* �����ֶΰ��������Ϣ */
	private int[] actions;// ����

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
