
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

import static java.lang.Thread.sleep;

public class RobotsWorld extends JPanel {
	// Class Fields
	private ArrayList< Robot > robotsArrayList;
	private ArrayList< Color > colorArrayList;
	private int dim, matBoundary;
	private static int robotsNum = 0;
	private boolean invokedFirstTime = true;
	// Class Constants
	private final int MAX_FRAME_SIZE = 500, MIN_FRAME_SIZE = 20, CELL_SIZE = 20, COLOR_NUMBER = 5;

	public RobotsWorld(int dim) throws IllegalArgumentException {
		robotsArrayList = new ArrayList<>( );
		colorArrayList = new ArrayList<>( );
		addColorsToColorArrayList( );

		if( dim > MAX_FRAME_SIZE || dim < MIN_FRAME_SIZE )
			throw new IllegalArgumentException( "dimension out of boundaries" );
		this.dim = dim;
		this.matBoundary = calculateMatrixBoundaries( );
	}

	/* When invoked method is moving all the robots across the frame*/
	public void moveRobots() throws InterruptedException {
		final int MAX_STEPS = 30, DELAY = 20;
		boolean moved = false;

		for ( int stepCnt = 0 ; stepCnt < MAX_STEPS ; stepCnt++ ) {
			for ( Robot rob : robotsArrayList ) { // moving all robots one step
				if( isLegal( rob.getPos( ) , rob.getDirection( ) ) && noRobotAhead( rob.getPos( ) , rob.getDirection( ) ) ) {
					rob.move( );
					moved = true;
				} else {
					rob.turnRight( );
					moved = false;
				}

				if( ! moved && robIsStuck( rob ) ) // robot cannot move to any direction - ending the run.
					finishedAndExit( rob.getPos( ).toString( ) );

				updateFrame( );
				sleep( DELAY );
			}
		}
	}

	/* Adds a robot in the passed position; if there's already a robot in that position, no robot will be added.
	 * Same happens if the position is not inside the matrix. */
	public void addRobot(Position pos) throws IllegalPosition {

		//Validation checks
		if( isPosLegal( pos ) )
			throw new IllegalPosition( " Illegal position" );
		else if( outSideOfMatrix( pos ) || positionIsOccupied( pos ) )
			return;

		//Adding the robot
		robotsArrayList.add( new Robot( robotsNum , pos , Robot.RIGHT ) );
		repaint( );
		robotsNum++;
	}

	/* Returns true if there is already a robot in that position*/
	private boolean positionIsOccupied(Position pos) {
		for ( Robot rob : robotsArrayList )
			if( pos.equals( rob.getPos( ) ) )
				return true;
		return false;
	}

	/**
	 * Returns the robot in the passed position; if no robot in that position or position is out of the matrix, returns null
	 * In addition the method deletes the robot
	 */
	public Robot removeRobot(Position pos) {
		if( isPosLegal( pos ) || outSideOfMatrix( pos ) )
			return null;

		for ( Robot rob : robotsArrayList ) {
			if( rob.getPos( ).equals( pos ) ) {
				robotsArrayList.remove( rob );
				robotsNum--;
				updateFrame( );
				return rob;
			}
		}
		return null;
	}

	/**
	 * Returns the robot in the passed position; if no robot in that position or position is out of the matrix, returns null
	 */
	public Robot getRobot(Position pos) {
		if( isPosLegal( pos ) || outSideOfMatrix( pos ) )
			return null;

		for ( Robot rob : robotsArrayList )
			if( rob.getPos( ).equals( pos ) )
				return rob;
		return null;
	}

	/* Method is invoked if a robot got stuck; printing the message and closing the app*/
	private void finishedAndExit(String pos) {
		JOptionPane.showMessageDialog( null , "robot at " + pos + " got stuck" );
		System.exit( 0 );
	}

	/* Returns true if and only if the robot can not move to any direction*/
	private boolean robIsStuck(Robot rob) {
		return ! noRobotAhead( rob.getPos( ) , Robot.UP )
			&& ! noRobotAhead( rob.getPos( ) , Robot.DOWN )
			&& ! noRobotAhead( rob.getPos( ) , Robot.RIGHT )
			&& ! noRobotAhead( rob.getPos( ) , Robot.LEFT );
	}

	/* Returns true if there is no robot in the position ahead */
	private boolean noRobotAhead(Position pos , String direction) {
		pos = getNewPos( pos , direction );
		for ( Robot rob : robotsArrayList ) {
			if( pos.equals( rob.getPos( ) ) )
				return false;
		}
		return true;
	}

	/*Returns new position in relation to the current position and the robot direction*/
	private Position getNewPos(Position pos , String direction) {
		final int STEP = 20;

		switch ( direction ) {
			case Robot.UP:
				pos.setY( pos.getY( ) - STEP );
				break;
			case Robot.DOWN:
				pos.setY( pos.getY( ) + STEP );
				break;
			case Robot.RIGHT:
				pos.setX( pos.getX( ) + STEP );
				break;
			default:
				pos.setX( pos.getX( ) - STEP );
				break;
		}
		return pos;
	}

	/* Returns true if the position in the cell ahead is inside the borders of the matrix */
	private boolean isLegal(Position pos , String direction) {
		switch ( direction ) {
			case Robot.UP:
				return ( pos.getY( ) - CELL_SIZE ) >= 0;
			case Robot.DOWN:
				return ( pos.getY( ) + CELL_SIZE ) <= matBoundary - CELL_SIZE;
			case Robot.RIGHT:
				return ( pos.getX( ) + CELL_SIZE ) <= matBoundary - CELL_SIZE;
			default:
				return ( pos.getX( ) - CELL_SIZE ) >= 0;
		}
	}

	/* Returns true if the passed position out of the matrix boundaries*/
	private boolean outSideOfMatrix(Position pos) {
		return pos.getY( ) >= matBoundary || pos.getX( ) >= matBoundary;
	}

	/* Sets random direction for the robots in the beginning of the run*/
	private void setRandomDirectionForRobots() {
		String[] allDirections = { Robot.UP , Robot.DOWN , Robot.RIGHT , Robot.LEFT };

		for ( Robot rob : robotsArrayList ) {
			rob.setDirection( allDirections[ new Random( ).nextInt( allDirections.length ) ] );
		}
	}

	/* Returns the direction of the robot  as a pointed arrow */
	private String translateToArrow(String direction) {
		switch ( direction ) {
			case Robot.UP:
				return "^";
			case Robot.DOWN:
				return "V";
			case Robot.RIGHT:
				return ">";
			default:
				return "<";
		}
	}

	/* Returns true if and only if the position that has been passed is a multiply of the cell size*/
	private boolean isPosLegal(Position pos) {
		return pos.getX( ) % CELL_SIZE != 0 || pos.getY( ) % CELL_SIZE != 0;
	}

	/* Updates the screen with repaint method*/
	private void updateFrame() {
		repaint( );
	}

	/**
	 * Utility function for filling the colors list with 5 colors
	 */
	private void addColorsToColorArrayList() {
		colorArrayList.add( Color.pink );
		colorArrayList.add( Color.BLUE );
		colorArrayList.add( Color.GREEN );
		colorArrayList.add( Color.cyan );
		colorArrayList.add( Color.RED );
	}

	public void paintComponent(Graphics graphics) {
		super.paintComponent( graphics );
		this.setBackground( Color.WHITE );

		// Building the matrix
		for ( int y = 0 ; y <= matBoundary ; y += CELL_SIZE ) // rows
			graphics.drawLine( 0 , y , matBoundary , y );
		for ( int x = CELL_SIZE ; x <= matBoundary ; x += CELL_SIZE ) // columns
			graphics.drawLine( x , 0 , x , matBoundary );

		// Adding the robots
		int i = 0;
		for ( Robot rob : robotsArrayList ) {
			graphics.setColor( colorArrayList.get( i % COLOR_NUMBER ) ); // setting color
			graphics.drawOval( rob.getPos( ).getX( ) , rob.getPos( ).getY( ) , CELL_SIZE , CELL_SIZE ); // drawing the circle
			graphics.fillOval( rob.getPos( ).getX( ) , rob.getPos( ).getY( ) , CELL_SIZE , CELL_SIZE ); // filling  the circle
			graphics.setColor( Color.BLACK ); // setting color for direction

			//drawing the direction
			if( invokedFirstTime )
				setRandomDirectionForRobots( );
			invokedFirstTime = false;
			final int OFFSET_X = 8, OFFSET_Y = 15;
			graphics.drawString( translateToArrow( rob.getDirection( ) ) ,
				rob.getPos( ).getX( ) + OFFSET_X , rob.getPos( ).getY( ) + OFFSET_Y );
			++ i;
		}
	}

	private int calculateMatrixBoundaries() {
		return this.dim % CELL_SIZE == 0 ? this.dim : this.dim - ( this.dim % CELL_SIZE );
	}

}
