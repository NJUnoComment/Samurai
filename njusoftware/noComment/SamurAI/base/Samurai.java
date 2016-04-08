
/**
 * @author clefz created at 2016/3/23
 *
 *         //zqh st clefz 
 *         edited at 2016/4/8
 *         ·â×°ÎäÊ¿µÄ×´Ì¬
 */
package njusoftware.noComment.SamurAI.base;

public class Samurai implements Cloneable {
	private Weapons weapon;
	private int[] pos;
	private int remainCurePeriod = 0;
	private boolean isVisible = true;
	private boolean isActive = true;

	public Samurai(Weapons weapon) {
		this.weapon = weapon;
		this.pos = new int[2];
	}

	public int[] getPos() {
		return pos;
	}

	public int getRemainCurePeriod() {
		return remainCurePeriod;
	}

	public Weapons getWeapon() {
		return weapon;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setPos(int[] pos) {
		this.pos = pos;
	}

	public void setRemainCurePeriod(int remainCurePeriod) {
		this.remainCurePeriod = remainCurePeriod;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void move(int[] moveTo) {
		pos[0] += moveTo[1];
		pos[1] += moveTo[0];
	}

	@Override
	public Samurai clone() throws CloneNotSupportedException {
		Samurai nextSamurai = (Samurai) super.clone();
		nextSamurai.weapon = this.weapon;
		int[] nextPos = new int[2];
		System.arraycopy(this.pos, 0, nextPos, 0, 2);
		nextSamurai.pos = nextPos;
		nextSamurai.remainCurePeriod = this.remainCurePeriod;
		nextSamurai.isVisible = this.isVisible;
		nextSamurai.isActive = this.isActive;
		return nextSamurai;
	}
}
