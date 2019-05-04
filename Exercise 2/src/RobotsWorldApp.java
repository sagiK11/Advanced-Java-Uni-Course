
import javax.swing.*;

public class RobotsWorldApp {
	private static final int MIN_SIZE = 20, MAX_SIZE = 500;

	public void runRobotsWorldApp() {
		int size = 0, EXTRA_FRAME_WIDTH = 100;
		size = getInputSize( );

		JFrame frame = new JFrame( "Question 2" );
		RobotsWorld robotsWorld = new RobotsWorld( size );

		frame.add( robotsWorld );
		frame.setSize( size + EXTRA_FRAME_WIDTH , size + EXTRA_FRAME_WIDTH );
		frame.setLocationRelativeTo( null );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.setVisible( true );

		addRobotsAndRun( robotsWorld );
	}

	private static void addRobotsAndRun(RobotsWorld world) {

		try {
			world.addRobot( new Position( 20 , 20 ) );
			world.addRobot( new Position( 40 , 20 ) );
			world.addRobot( new Position( 60 , 40 ) );
			world.addRobot( new Position( 80 , 20 ) );
			world.addRobot( new Position( 20 , 40 ) );
		} catch ( IllegalPosition e ) {
			JOptionPane.showMessageDialog( null , "please enter a position that divides by 20" );
		}
		try {
			world.moveRobots( );
		} catch ( InterruptedException e ) {
			e.printStackTrace( );
		}
	}

	private static int getInputSize() {
		int size = 0;
		do {
			String inputSize = JOptionPane.showInputDialog( "please enter size of the window" );

			try {
				size = Integer.parseInt( inputSize );
			} catch ( NumberFormatException e ) {
				JOptionPane.showMessageDialog( null , "Illegal number" );
				System.exit( 1 );
			}
		} while ( size < MIN_SIZE || size > MAX_SIZE );
		return size;
	}
}
