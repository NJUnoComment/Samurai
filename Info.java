
/**
 * @author clefz created at 2016/3/22
 *
 *         //zqh st csy 
 *         edited at 2016/3/27
 *         �������ڷ�װ��IOManager��GameManager֮�䴫�ݵ���Ϣ
 */
package njusoftware.noComment.SamurAI.base;

public class Info {
	static final int GAME_INFO = 0x1111, TURN_INFO = 0x2222, OUTPUT_INFO = 0x3333; // �������������������Ϣ����
	private int kind = GAME_INFO;// ��Ϣ����

	/* �����ֶΰ����غ���Ϣ */
	private Board primalBorad;// δ������ľ�����Ϣ
	private int[][] board;
	private int[][] samuraiState;// ��ʿ״̬
	private int remainCurePeriod;// ʣ��ظ�ʱ��
	private int turn;// �غ�����

	/* �����ֶΰ�����Ϸ��Ϣ */
	private int totalTurns;// �ܻغ���
	private int samuraiID;// ���Ƶ���ʿID������Ϸ��ID������ID�ϳ�
	private int length;// ս������ĳ�
	private int width;// ս������Ŀ�
	private int curePeriod;// �ظ�ʱ��
	private int[][] homePos;// �ҵ�λ��
	private int[][] ranksAndScores;// �����ͷ���

	/* �����ֶΰ��������Ϣ */
	private int[] actions;// ����
	
	/*�����ֶΰ�����ǵ�getter��setter*/
	public int getKind() {
		return kind;
	}

	public Info setKind(int kind) {
		this.kind = kind;
		return this;
	}
	
    /*�����ֶΰ����غϵ�getter��setter*/
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

	/*�����ֶΰ�����Ϸ��getter��setter*/
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

	/*�����ֶΰ��������getter��setter*/
	public int[] getActions() {
		return actions;
	}

	public Info setActions(int[] actions) {
		this.actions = actions;
		return this;
	}

	// Ϊ�˷�����ý���setterȫ��ʹ��������
	
	
}

