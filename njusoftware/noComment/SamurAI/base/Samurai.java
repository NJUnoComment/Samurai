
/**
 * @author clefz created at 2016/3/23
 *
 *         //zqh st clefz 
 *         edited at 2016/4/8
 *         封装武士的状态
 */
package njusoftware.noComment.SamurAI.base;

public class Samurai implements Cloneable {
	private Weapons weapon;
	private int[] pos;
	// 每个武士将会对自己是否受伤进行维护，此字段标识武士将在哪个回合恢复完成
	private int curedTurn = 0;
	private boolean isVisible = true;
	private boolean isAlive = true;
	private boolean isActive = true;

	public Samurai(Weapons weapon) {
		this.weapon = weapon;
		this.pos = new int[2];
	}

	public int[] getPos() {
		return pos;
	}

	public int getCuredTurn() {
		return curedTurn;
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

	public boolean isAlive() {
		return isAlive;
	}

	public void setPos(int[] pos) {
		System.arraycopy(pos, 0, this.pos, 0, 2);
	}

	public void setPos(int x, int y) {
		pos[0] = x;
		pos[1] = y;
	}

	public void setCuredTurn(int curedTurn) {
		this.curedTurn = curedTurn;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public void move(int[] moveTo) {
		pos[0] += moveTo[0];
		pos[1] += moveTo[1];
	}

	@Override
	public Samurai clone() throws CloneNotSupportedException {
		Samurai nextSamurai = (Samurai) super.clone();
		nextSamurai.weapon = this.weapon;
		int[] nextPos = new int[2];
		System.arraycopy(this.pos, 0, nextPos, 0, 2);
		nextSamurai.pos = nextPos;
		nextSamurai.curedTurn = this.curedTurn;
		nextSamurai.isVisible = this.isVisible;
		nextSamurai.isAlive = this.isAlive;
		nextSamurai.isActive = this.isActive;
		return nextSamurai;
	}
}
