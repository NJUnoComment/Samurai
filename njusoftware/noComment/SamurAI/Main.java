package njusoftware.noComment.SamurAI;

import njusoftware.noComment.SamurAI.base.*;

import java.io.IOException;

import njusoftware.noComment.SamurAI.AI.*;

public class Main {
	public static void main(String[] args) throws CloneNotSupportedException, IOException {
		// 处理流程是这样的
		// GameManager的构造器是private的，所以不是通过new来新建
		// 而是通过init()这个静态方法，在这个方法里面会读取游戏开始时给出的游戏信息（因为这些信息每局游戏都可能会变）
		// 然后对其进行处理，获得一些参数，然后用这个参数去new一个GameManager并返回
		// 这是为了封装，不要吐槽
		GameManager gameManager = GameManager.init();
		while (true) {
			// 这里也是一样，一切都在GameManager类里处理，输出也由其调用IOManager进行
			gameManager.nextTurn();
		}
	}
}
