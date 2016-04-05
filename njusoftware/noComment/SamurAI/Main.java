package njusoftware.noComment.SamurAI;

import njusoftware.noComment.SamurAI.base.*;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws CloneNotSupportedException, IOException {
		GameManager gameManager = GameManager.init();
		while (true)
			gameManager.nextTurn();
	}
}
