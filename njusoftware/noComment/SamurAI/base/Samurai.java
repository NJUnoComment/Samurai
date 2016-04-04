
/**
 * @author clefz created at 2016/3/23
 *
 *         //zqh st 
 *         edited at 2016/3/27
 *         封装武士的状态
 */
package njusoftware.noComment.SamurAI.base;

public class Samurai implements Cloneable {
	private Weapons weapon;
	private int[] pos = new int[2];
	private int remainCurePeriod = 0;
	private boolean isVisible = true;
	private boolean isActive = true;
	// 封装武士的状态

	public int[] getPos() {
		return pos;
	}

	public void setPos(int[] pos) {
		this.pos = pos;
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

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean isActive() {
		return isActive;
	}

	public Weapons getWeapon() {
		return weapon;
	}

	public Samurai(Weapons weapon) {
		this.weapon = weapon;
	}

	@Override
	public Samurai clone() throws CloneNotSupportedException {
		Samurai nextSamurai = (Samurai) super.clone();
		nextSamurai.weapon = this.weapon;
		System.arraycopy(this.pos, 0, nextSamurai.pos, 0, 2);
		nextSamurai.remainCurePeriod = this.remainCurePeriod;
		nextSamurai.isVisible = this.isVisible;
		return nextSamurai;
	}
}
