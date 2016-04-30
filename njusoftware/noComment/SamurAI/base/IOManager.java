
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

	/* 输入的入口方法 */
	public static Info input(boolean needsGameInfo) throws IOException {
		bfr.readLine();
		if (needsGameInfo)
			return gameInfoInput();
		return turnInfoInput();
	}

	/* 回合信息 */
	private static Info turnInfoInput() throws IOException {
		String[] res;

		// 回合数
		res = read();
		int turn = Integer.parseInt(res[0]);

		// 回复时间
		res = read();
		int remainCurePeriod = Integer.parseInt(res[0]);

		// 武士状态
		int[][] samuraiState = new int[6][3];
		for (int i = 0; i < 6; ++i) {
			res = read();
			samuraiState[i][0] = Integer.parseInt(res[0]);
			samuraiState[i][1] = Integer.parseInt(res[1]);
			samuraiState[i][2] = Integer.parseInt(res[2]);
		}

		if (GameManager.FRIEND_INDEX == 3)
			samuraiState = changeOrder(samuraiState);

		// 局面
		int[][] board = new int[GameManager.HEIGHT][GameManager.WIDTH];
		for (int i = 0; i < GameManager.HEIGHT; ++i) {
			res = read();
			for (int j = 0; j < GameManager.WIDTH; ++j)
				board[i][j] = Integer.parseInt(res[j + 1]);
		}


		if (GameManager.FRIEND_INDEX == 3)
			for (int i = 0; i < GameManager.HEIGHT; ++i)
				for (int j = 0; j < GameManager.WIDTH; ++j)
					if (board[i][j] < 6)
						board[i][j] += board[i][j] < 3 ? 3 : -3;

		return new Info().setTurn(turn).setRemainCurePeriod(remainCurePeriod).setSamuraiState(samuraiState)
				.setBattleField(board);
	}

	/* 游戏信息 */
	private static Info gameInfoInput() throws IOException {
		String[] res = read();

		int totalTurns = Integer.parseInt(res[0]);
		int samuraiID = Integer.parseInt(res[1]) * 3 + Integer.parseInt(res[2]);
		int width = Integer.parseInt(res[3]);
		int height = Integer.parseInt(res[4]);
		int curePeriod = Integer.parseInt(res[5]);

		int[][] homePos = new int[6][2];
		for (int i = 0; i < 6; ++i) {
			res = read();
			homePos[i][0] = Integer.parseInt(res[0]);
			homePos[i][1] = Integer.parseInt(res[1]);
		}

		if (samuraiID > 2)
			homePos = changeOrder(homePos);

		int[][] ranksAndScores = new int[6][2];
		for (int i = 0; i < 6; ++i) {
			res = read();
			ranksAndScores[i][0] = Integer.parseInt(res[0]);
			ranksAndScores[i][1] = Integer.parseInt(res[1]);
		}

		return new Info().setTotalTurns(totalTurns).setSamuraiID(samuraiID).setWidth(width).setHeight(height)
				.setCurePeriod(curePeriod).setHomePos(homePos).setRanksAndScores(ranksAndScores);
	}

	/* 在B方时要调换次序 */
	private final static int[][] changeOrder(int[][] origin) {
		return new int[][] { origin[3], origin[4], origin[5], origin[0], origin[1], origin[2] };
	}

	/* 读单行 */
	private final static String[] read() {
		String line = "";
		try {
			for (line = bfr.readLine(); line.startsWith("#"); line = bfr.readLine())
				;
		} catch (Exception e) {
			e.getStackTrace();
			System.exit(-1);
		}
		// System.err.println(line);
		return line.split("\\s");
	}

	/* 输出 */
	public static void output(Info outputInfo) {
		int[] actions = outputInfo.getActions();
		StringBuilder outputStr = new StringBuilder();
		for (int i : actions)
			outputStr.append(i).append(" ");
		outputStr.append(0);// 以0结尾
		System.out.println(outputStr.toString());
	}
}