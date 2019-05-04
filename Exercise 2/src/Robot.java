import java.util.ArrayList;

public class Robot {
	private int id;
	private Position pos;
	private String direction;
	private static ArrayList< Integer > idArrayList = new ArrayList<>( );
	public final static String LEFT = "LEFT", RIGHT = "RIGHT", UP = "UP", DOWN = "DOWN";

	public Robot(int id , Position pos , String direction) {
		for ( Integer IDInSystem : idArrayList ) {
			if( IDInSystem == id )
				throw new IllegalArgumentException( "id already exists" );
		}
		idArrayList.add( id );
		this.id = id;
		this.pos = pos;
		this.direction = direction;
	}

	/* Moves this robot to the cell its pointing to*/
	public void move() {
		final int STEP = 20;

		switch ( this.direction ) {
			case UP:
				this.pos.setY( pos.getY( ) - STEP );
				return;
			case DOWN:
				this.pos.setY( pos.getY( ) + STEP );
				return;
			case RIGHT:
				this.pos.setX( pos.getX( ) + STEP );
				return;
			default:
				this.pos.setX( pos.getX( ) - STEP );
		}
	}

	/* Turns the robot left*/
	public void turnLeft() {

		switch ( this.direction ) {
			case UP:
				this.direction = LEFT;
				return;
			case DOWN:
				this.direction = RIGHT;
				return;
			case RIGHT:
				this.direction = UP;
				return;
			default:
				this.direction = DOWN;
		}
	}

	/* Turns the robot right */
	public void turnRight() {

		switch ( this.direction ) {
			case UP:
				this.direction = RIGHT;
				return;
			case DOWN:
				this.direction = LEFT;
				return;
			case RIGHT:
				this.direction = DOWN;
				return;
			default:
				this.direction = UP;
		}
	}

	public int getId() {
		return this.id;
	}

	public Position getPos() {
		return new Position( pos.getX( ) , pos.getY( ) );
	}

	public void setDirection(String direction) {
		switch ( direction ) {
			case UP:
				this.direction = UP;
				return;
			case DOWN:
				this.direction = DOWN;
				return;
			case RIGHT:
				this.direction = RIGHT;
				return;
			default:
				this.direction = LEFT;
		}
	}

	public String getDirection() {
		return direction;
	}

	public String toString() {
		return "robot id = " + this.id + "\nrobot pos = " + this.pos + "\ndirection = " + direction + "\n---------";
	}

}
