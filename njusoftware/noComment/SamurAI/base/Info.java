
/**
 * @author clefz created at 2016/3/22
 *
 *         //请在此处标注编写者姓名 
 *         请在此处填写最终修改时间
 *         此类用于封装在IOManager和GameManager之间传递的信息
 */
package njusoftware.noComment.SamurAI.base;

public class Info {
	private static final int GAME_INFO = 0x1111, TURN_INFO = 0x2222, OUTPUT_INFO = 0x3333; // 这三个常数用来标记信息类型
	private int kind = GAME_INFO;// 信息类型

	/* 以下字段包含回合信息 */
	private Board primalBorad;// 未处理过的局面信息
	private int[][] samuraiState;// 武士状态
	private int remainCurePeriod;// 剩余回复时间
	private int turn;// 回合序数

	/* 以下字段包含游戏信息 */
	private int totalTurns;// 总回合数
	private int samuraiID;// 控制的武士ID，由游戏方ID和武器ID合成
	private int battleFieldSize;// 战斗区域大小
	private int curePeriod;// 回复时间
	private int[][] homePos;// 家的位置
	private int[][] ranksAndScores;// 排名和分数

	/* 以下字段包含输出信息 */
	private int[] actions;// 动作

	// 下面是一堆getter和setter
	// 为了方便调用建议setter全部使用连用形
}
