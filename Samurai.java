
/**
 * @author clefz created at 2016/3/23
 *
 *         //zqh st 
 *         edited at 2016/3/27
 *         封装武士的状态
 */
package njusoftware.noComment.SamurAI.base;

public class Samurai {
	private final Weapons weapon;
	private int[] pos = new int[2];
	private int[] homePos = new int[2];
	private int remainCurePeriod = 0;
	private boolean isVisible = true;
	
	//封装武士的状态

	public int[] getPos() {
		return pos;
	}

	public void setPos(int[] pos) {
		this.pos = pos;
	}

	public int[] getHomePos() {
		return homePos;
	}

	public void setHomePos(int[] homePos) {
		this.homePos = homePos;
	}

	public int getRemainCurePeriod() {
		return remainCurePeriod;
	}

	public void setRemainCurePeriod(int remainCurePeriod) {
		this.remainCurePeriod = remainCurePeriod;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public Weapons getWeapon() {
		return weapon;
	}

	public Samurai(Weapons weapon) {
		this.weapon = weapon;
	}

	// 一堆getter和setter
}
