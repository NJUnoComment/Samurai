package njusoftware.noComment.SamurAI;

import njusoftware.noComment.SamurAI.base.*;

import java.io.IOException;

import njusoftware.noComment.SamurAI.AI.*;

public class Main {
	public static void main(String[] args) throws CloneNotSupportedException, IOException {
		// ����������������
		// GameManager�Ĺ�������private�ģ����Բ���ͨ��new���½�
		// ����ͨ��init()�����̬���������������������ȡ��Ϸ��ʼʱ��������Ϸ��Ϣ����Ϊ��Щ��Ϣÿ����Ϸ�����ܻ�䣩
		// Ȼ�������д������һЩ������Ȼ�����������ȥnewһ��GameManager������
		// ����Ϊ�˷�װ����Ҫ�²�
		GameManager gameManager = GameManager.init();
		while (true) {
			// ����Ҳ��һ����һ�ж���GameManager���ﴦ�����Ҳ�������IOManager����
			gameManager.nextTurn();
		}
	}
}
