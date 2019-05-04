
public class Position { // -----Beta Class----//
	//Class Fields
	private int x, y;

	public Position(int x , int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Position && ( ( Position ) obj ).getX( ) == getX( ) && ( ( Position ) obj ).getY( ) == getY( );
	}

	public String toString() {
		return "(" + x + "," + y + ")";
	}


}
