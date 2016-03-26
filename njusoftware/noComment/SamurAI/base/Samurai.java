
/**
 * @author clefz created at 2016/3/23
 *
 *         //请在此处标注编写者姓名 
 *         请在此处填写最终修改时间
 *         封装武士的状态
 */
package njusoftware.noComment.SamurAI.base;

public class Samurai {
	private final Weapons weapon;
	private int[] pos = new int[2];
	private int[] homePos = new int[2];
	private int remainCurePeriod = 0;
	private boolean isVisible = true;

	public Samurai(Weapons weapon) {
		this.weapon = weapon;
	}

	// 一堆getter和setter
}
