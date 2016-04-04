
/**
 * @author clefz created at 2016/3/22
 * 
 *         csy clefz
 *         last edited at 2016/3/28
 *         此类用于输入输出
 */
package njusoftware.noComment.SamurAI.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class IOManager {
	private static BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));

	// 输入方法，从标准输入读取并处理字符串，将得到的信息封装成Info进行传递
	public static Info input() throws IOException {
		String headLine = bfr.readLine();
		if (headLine.contains("Game"))
			return gameInfoInput();
		return turnInfoInput();
	}

	// 读取并封装回合信息
	private static Info turnInfoInput() throws IOException {
		// 回合数
		bfr.readLine();
		int turn = Integer.parseInt(bfr.readLine().split("\\s+")[0]);

		// 剩余回复时间
		bfr.readLine();
		int remainCurePeriod = Integer.parseInt(bfr.readLine().split("\\s+")[0]);

		// 武士状态
		int[][] samuraiStates = new int[6][3];
		String tmp = bfr.readLine();
		for (int i = 0; !tmp.startsWith("#"); tmp = bfr.readLine(), i++) {
			String[] samuraiState = tmp.split("\\s+");
			samuraiStates[i][0] = Integer.parseInt(samuraiState[0]);
			samuraiStates[i][1] = Integer.parseInt(samuraiState[1]);
			samuraiStates[i][2] = Integer.parseInt(samuraiState[2]);
		}

		// 盘面
		int[][] board = new int[GameManager.HEIGHT][GameManager.WIDTH];
		for (int i = 0; i < GameManager.HEIGHT; i++) {
			String[] curRow = bfr.readLine().split("\\s+");
			for (int j = 0; j < GameManager.WIDTH; i++)
				board[i][j] = Integer.parseInt(curRow[j]);
		}
		return new Info().setType(Info.TURN_INFO).setTurn(turn).setRemainCurePeriod(remainCurePeriod)
				.setSamuraiState(samuraiStates).setBoard(board);
	}

	// 读取并封装游戏信息
	private static Info gameInfoInput() throws IOException {
		// 游戏参数
		String[] parameters = bfr.readLine().split("\\s+");
		int totalTurns = Integer.parseInt(parameters[0]);
		int samuraiID = Integer.parseInt(parameters[1]) * 3 + Integer.parseInt(parameters[2]);
		int width = Integer.parseInt(parameters[3]);
		int height = Integer.parseInt(parameters[4]);
		int curePeriod = Integer.parseInt(parameters[5]);

		// 家的位置
		bfr.readLine();
		int[][] homePos = new int[6][2];
		String tmp = bfr.readLine();
		for (int i = 0; !tmp.startsWith("#"); tmp = bfr.readLine(), i++) {
			String[] pos = tmp.split("\\s+");
			homePos[i][0] = Integer.parseInt(pos[0]);
			homePos[i][1] = Integer.parseInt(pos[1]);
		}

		// 排名和积分
		int[][] ranksAndScores = new int[6][2];
		for (int i = 0; i < 6; i++) {
			String[] rankAndScore = bfr.readLine().split("\\s+");
			ranksAndScores[i][0] = Integer.parseInt(rankAndScore[0]);
			ranksAndScores[i][1] = Integer.parseInt(rankAndScore[1]);
		}
		return new Info().setType(Info.GAME_INFO).setTotalTurns(totalTurns).setSamuraiID(samuraiID).setWidth(width)
				.setHeight(height).setCurePeriod(curePeriod).setHomePos(homePos).setRanksAndScores(ranksAndScores);
	}

	// 输出方法，解封装Info并通过标准输出输出
	public static void output(Info outputInfo) {
		if (outputInfo.getType() != Info.OUTPUT_INFO)
			return;
		int[] actions = outputInfo.getActions();
		StringBuilder outputStr = new StringBuilder();
		for (int i : actions)
			outputStr.append(i);
		outputStr.append(0);// 以0结尾
		System.out.println(outputStr.toString());
	}

	public static void main(String[] args) throws IOException {
		Info i = IOManager.input();

	}
}