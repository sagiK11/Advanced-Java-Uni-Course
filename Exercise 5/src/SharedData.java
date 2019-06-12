
public class SharedData {
	protected int x, y;

	public SharedData(int x , int y) {
		this.x = x;
		this.y = y;
	}

	public void move(int dx , int dy) {
		x += dx;
		y += dy;
	}

	public SharedData get() {
		return new SharedData( x , y );
	}

	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
