import javax.swing.*;
import java.awt.*;

public class CalculatorApp extends JFrame {
	//Class Constants
	private final int FRAME_WIDTH = 320, FRAME_HEIGHT = 450, GAP = 5;

	public CalculatorApp() {
		super( "Question 2" );
		setSize( FRAME_WIDTH , FRAME_HEIGHT );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setLocationRelativeTo( null );
		setResizable( false );
		setLayout( new BorderLayout( GAP , GAP ) );
	}

	public void runCalculatorApp() {
		addComponents( );
		setVisible( true );
	}

	/*Adds the layouts in the main frame*/
	private void addComponents() {
		JPanel frame = new JPanel( );
		frame.setBorder( BorderFactory.createTitledBorder( BorderFactory.createEmptyBorder( 6 , 6 , 6 , 6 ) , "CalculatorApp" ) );
		frame.setLayout( new BorderLayout( GAP , GAP ) );

		CalculatorScreen screen = new CalculatorScreen( );
		CalculatorLogic logic = new CalculatorLogic( );
		CalculatorControls controls = new CalculatorControls( screen , logic );

		frame.add( screen , BorderLayout.NORTH );
		frame.add( controls , BorderLayout.CENTER );
		add( frame );
	}

}
