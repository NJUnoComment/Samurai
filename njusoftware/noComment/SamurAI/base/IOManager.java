
/**
 * @author clefz created at 2016/3/22
 * 
 *         //csy 
 *         edited at 2016/3/27
 *         ���������������
 */
package njusoftware.noComment.SamurAI.base;

import java.util.Scanner;

public class IOManager {

	private IOManager() {
		// �˴�Ϊ�գ�������ʵ��������
	}

	// ���뷽�����ӱ�׼�����ȡ�������ַ��������õ�����Ϣ��װ��Info���д���
	public static Info input() {
		// ��Ҫ�ֳɶ�ȡ�տ�ʼ����Ϸ��Ϣ��ÿ�غϵĻغ���Ϣ���ַ���������Info�ı���ֶ������ñ��
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

	// ������������װInfo��ͨ����׼������
	public static void output(Info outputInfo) {
		int[] outputInt = outputInfo.getActions();
		String outputStr = "";
		for (int i : outputInt) {
			outputStr += i + "" + " ";
		}
		System.out.println("# Sample actions\n" + outputStr);
	}

	// ��װ��Ϸ��Ϣ�ķ���
	public static Info gameInput(String[] input) {
		Info gameInfo = new Info();
		String[] line;
		int[] lineContents;
		int[][] contents = null;

		// �����2�е���Ϸ��Ϣ
		line = input[1].split(" ");
		lineContents = new int[line.length];
		for (int i = 0; i < line.length; i++) {
			lineContents[i] = Integer.parseInt(line[i]);
		}
		gameInfo.setTotalTurns(lineContents[0]).setSamuraiID(lineContents[1], lineContents[2])
				.setLength(lineContents[3]).setWidth(lineContents[4]).setCurePeriod(lineContents[5]);

		// �����4~9�еļҵ�λ�ú͵�11~16�еĵȼ����ܷ�,ÿ�δ���6��
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

	// ��װ�غ���Ϣ�ķ���
	public static Info turnInput(String[] input) {
		Info turnInfo = new Info();
		String[] line;
		int[] lineContents;
		int[][] contents = null;

		// �����3�еĵ�ǰ�غ�����
		turnInfo.setTurn(Integer.parseInt(input[2]));

		// �����5�е�ʣ��ָ�ʱ��
		turnInfo.setCurePeriod(Integer.parseInt(input[4]));

		// �����7~12�е���ʿ״̬
		for (int i = 0; i < 6; i++) {
			line = input[i + 6].split(" ");
			lineContents = new int[line.length];
			for (int j = 0; j < line.length; j++) {
				lineContents[j] = Integer.parseInt(line[j]);
			}
			contents[i] = lineContents;
		}
		turnInfo.setSamuraiState(contents);

		// �����14~28�е�ս��״��
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
