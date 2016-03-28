
/**
 * @author clefz created at 2016/3/22
 * 
 *         //csy 
 *         edited at 2016/3/27
 *         此类用于输入输出
 */
package njusoftware.noComment.SamurAI.base;

import java.util.Scanner;

public class IOManager {

	private IOManager() {
		// 此处为空，不允许实例化该类
	}

	// 输入方法，从标准输入读取并处理字符串，将得到的信息封装成Info进行传递
	public static Info input() {
		// 需要分成读取刚开始的游戏信息和每回合的回合信息两种方法，并在Info的标记字段上做好标记
		Scanner sc = new Scanner(System.in);
		String[] input = null;
		int i = 0;
		while (sc.nextLine() != null) {
			input[i] = sc.nextLine();
			i++;
		}
		if (input[0].equals("# Game Info")) {
			return gameInput(input);
		} else {
			return turnInput(input);
		}
	}

	// 输出方法，解封装Info并通过标准输出输出
	public static void output(Info outputInfo) {
		int[] outputInt = outputInfo.getActions();
		String outputStr = "";
		for (int i : outputInt) {
			outputStr += i + "" + " ";
		}
		System.out.println("# Sample actions\n" + outputStr);
	}

	// 封装游戏信息的方法
	public static Info gameInput(String[] input) {
		Info gameInfo = new Info();
		String[] line;
		int[] lineContents;
		int[][] contents = null;

		// 处理第2行的游戏信息
		line = input[1].split(" ");
		lineContents = new int[line.length];
		for (int i = 0; i < line.length; i++) {
			lineContents[i] = Integer.parseInt(line[i]);
		}
		gameInfo.setTotalTurns(lineContents[0]).setSamuraiID(lineContents[1], lineContents[2])
				.setLength(lineContents[3]).setWidth(lineContents[4]).setCurePeriod(lineContents[5]);

		// 处理第4~9行的家的位置和第11~16行的等级和总分,每次处理6行
		for (int i = 3; i < 11; i = i + 7) {
			for (int j = 0; j < 6; j++) {
				line = input[j + i].split(" ");
				lineContents = new int[line.length];
				for (int k = 0; k < line.length; k++) {
					lineContents[k] = Integer.parseInt(line[k]);
				}
				contents[j] = lineContents;
			}
			if (i == 3) {
				gameInfo.setHomePos(contents);
			} else {
				gameInfo.setRanksAndScores(contents);
			}
		}

		return gameInfo;
	}

	// 封装回合信息的方法
	public static Info turnInput(String[] input) {
		Info turnInfo = new Info();
		String[] line;
		int[] lineContents;
		int[][] contents = null;

		// 处理第3行的当前回合序数
		turnInfo.setTurn(Integer.parseInt(input[2]));

		// 处理第5行的剩余恢复时间
		turnInfo.setCurePeriod(Integer.parseInt(input[4]));

		// 处理第7~12行的武士状态
		for (int i = 0; i < 6; i++) {
			line = input[i + 6].split(" ");
			lineContents = new int[line.length];
			for (int j = 0; j < line.length; j++) {
				lineContents[j] = Integer.parseInt(line[j]);
			}
			contents[i] = lineContents;
		}
		turnInfo.setSamuraiState(contents);

		// 处理第14~28行的战场状况
		for (int i = 0; i < 15; i++) {
			line = input[i + 13].split(" ");
			lineContents = new int[line.length];
			for (int j = 0; j < line.length; j++) {
				lineContents[j] = Integer.parseInt(line[j]);
			}
			contents[i] = lineContents;
		}
		turnInfo.setBoard(contents);

		return turnInfo;
	}
}
