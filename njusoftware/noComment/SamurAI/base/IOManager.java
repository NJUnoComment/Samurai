
/**
 * @author clefz created at 2016/3/22
 * 
 *         csy clefz
 *         last edited at 2016/4/5
 *         此类用于输入输出
 */
package njusoftware.noComment.SamurAI.base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public abstract class IOManager {
	private static BufferedReader bfr = new BufferedReader(new InputStreamReader(System.in));

	// 输入
	public static Info input(boolean needGameInfo) throws IOException {
		bfr.readLine();
		if (needGameInfo)
			return gameInfoInput();
		return turnInfoInput();
	}

	// 回合信息
	private static Info turnInfoInput() throws IOException {
		// 回合数
		bfr.readLine();
		int turn = Integer.parseInt(bfr.readLine().split("\\s")[0]);

		// 剩余回复时间
		bfr.readLine();
		int remainCurePeriod = Integer.parseInt(bfr.readLine().split("\\s")[0]);

		// 武士状态
		bfr.readLine();
		int[][] samuraiStates = new int[6][3];
		String tmp = bfr.readLine();
		String[] samuraiState;
		for (int i = 0; i < 6; tmp = bfr.readLine(), i++) {
			samuraiState = tmp.split("\\s");
			samuraiStates[i][0] = Integer.parseInt(samuraiState[0]);
			samuraiStates[i][1] = Integer.parseInt(samuraiState[1]);
			samuraiStates[i][2] = Integer.parseInt(samuraiState[2]);
		}

		// 盘面
		int[][] board = new int[GameManager.HEIGHT][GameManager.WIDTH];
		// bfr.readLine();
		String[] curRow;
		for (int i = 0; i < GameManager.HEIGHT; ++i) {
			curRow = bfr.readLine().split("\\s");
			for (int j = 0; j < GameManager.WIDTH; ++j)
				board[i][j] = Integer.parseInt(curRow[j + 1]);
		}
		return new Info().setTurn(turn).setRemainCurePeriod(remainCurePeriod).setSamuraiState(samuraiStates)
				.setBoard(board);
	}

	// 游戏信息
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
		for (int i = 0; i < 6; tmp = bfr.readLine(), i++) {
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
		return new Info().setTotalTurns(totalTurns).setSamuraiID(samuraiID).setWidth(width).setHeight(height)
				.setCurePeriod(curePeriod).setHomePos(homePos).setRanksAndScores(ranksAndScores);
	}

	// 输出
	public static void output(Info outputInfo) {
		int[] actions = outputInfo.getActions();
		StringBuilder outputStr = new StringBuilder();
		for (int i : actions)
			outputStr.append(i).append(" ");
		outputStr.append(0);// 以0结尾
		System.out.println(outputStr.toString());
	}
}