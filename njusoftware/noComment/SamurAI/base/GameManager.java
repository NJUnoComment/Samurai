
/**
 * @author clefz created at 2016/3/23
 * 
 *         //请在此处标注编写者姓名 
 *         请在此处填写最终修改时间
 *         此类用于管理整个局面，对Board进行操作，并且以类迭代器的形式给出下一步可能的合法操作
 */
package njusoftware.noComment.SamurAI.base;

import njusoftware.noComment.SamurAI.AI.AIManager;

public class GameManager {
	private AIManager AI;
	private Board pervBoard;
	private Board curBoard;// 这两个字段用于推测视野外情况
	private int curTurn;// 当前回合数
	
	public static int totalTurns;// 总回合数
	public static int[][] homePoses;// 家的位置
	public static int controlIndex;// 控制的是哪一个武士，0-5表示
	public static final Samurai[] SAMURAIS;// 武士
	public static final int[] ACTION_ORDER = new int[] { 0, 3, 4, 1, 2, 5, 3, 0, 1, 4, 5, 2 }; // 行动的顺序，数字是samurais的下标
	// 根据回合数来确定那个行动的这么写:
	// samurais[ACTION_ORDER[turn%12]]
	
	

	static {
		SAMURAIS = new Samurai[6];
		SAMURAIS[0] = new Samurai(Weapons.SPEAR);
		SAMURAIS[1] = new Samurai(Weapons.SWORD);
		SAMURAIS[2] = new Samurai(Weapons.AXE);
		SAMURAIS[3] = new Samurai(Weapons.SPEAR);
		SAMURAIS[4] = new Samurai(Weapons.SWORD);
		SAMURAIS[5] = new Samurai(Weapons.AXE);
	}

	/* 下面两个方法使用类迭代器方法来遍历合法的行动 */
	// 判断是否还有更多的合法操作


	// 评估函数，极其重要，难点
	public int evaluate(Board board) {
		return 0;
	}

	public Board getBoard() {
		return curBoard;
	}

	public void nextTurn() throws CloneNotSupportedException {
		//
		Info turnInfo = IOManager.input();// 读入回合信息
		// 一堆处理
		int[] actions = AI.decideActions();
		Info output = new Info();// 得出最后的行动序列并且封装成Info
		// output.setXXX(actions);
		// output.setType(Info.OUTPUT_INFO);
		IOManager.output(output);// 然后输出
	}

	private GameManager(Info gameInfo) {
		totalTurns = 0;
		controlIndex = 0;
		AI = new AIManager(this);
		// 还有一堆
		System.out.println("0");// 输出0作为回应
	}

	public static GameManager init() {
		Info gameInfo = IOManager.input();
		// 可能需要一些操作
		return new GameManager(gameInfo);
	}
}
