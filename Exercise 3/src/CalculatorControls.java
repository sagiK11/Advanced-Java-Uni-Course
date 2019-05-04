import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorControls extends JPanel {
	//Class Fields
	CalculatorScreen screen;
	CalculatorLogic logic;
	private JButton[] JButtonsList;
	private JButton buttonZero, buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive,
		buttonSix, buttonSeven, buttonEight, buttonNine, buttonDot, buttonNegate, buttonEqual,
		buttonPlus, buttonMinus, buttonMultiply, buttonDivide, buttonDeleteLast, buttonDeleteAll,
		buttonFutureButton;
	//Class Constants
	private final int ROW_NUMBER = 4, COL_NUMBER = 5, BUTTONS_NUMBER = 20;

	/*Constructor*/
	public CalculatorControls(CalculatorScreen screen , CalculatorLogic logic) {
		this.screen = screen;
		this.logic = logic;
		setLayout( new GridLayout( COL_NUMBER , ROW_NUMBER ) );
		initializeButtons( );
		addButtons( );
		setVisible( true );
	}


	/*Adding the buttons onto the this panel*/
	private void addButtons() {
		add( buttonFutureButton );
		add( buttonDeleteAll );
		add( buttonDeleteLast );
		add( buttonDivide );
		add( buttonSeven );
		add( buttonEight );
		add( buttonNine );
		add( buttonMultiply );
		add( buttonFour );
		add( buttonFive );
		add( buttonSix );
		add( buttonMinus );
		add( buttonOne );
		add( buttonTwo );
		add( buttonThree );
		add( buttonPlus );
		add( buttonZero );
		add( buttonDot );
		add( buttonNegate );
		add( buttonEqual );
	}

	/*Utility method for initialize the calculator buttons*/
	private void initializeButtons() {
		JButtonsList = new JButton[ BUTTONS_NUMBER ];
		JButtonsList[ 0 ] = ( buttonZero = new JButton( "0" ) );
		JButtonsList[ 1 ] = ( buttonOne = new JButton( "1" ) );
		JButtonsList[ 2 ] = ( buttonTwo = new JButton( "2" ) );
		JButtonsList[ 3 ] = ( buttonThree = new JButton( "3" ) );
		JButtonsList[ 4 ] = ( buttonFour = new JButton( "4" ) );
		JButtonsList[ 5 ] = ( buttonFive = new JButton( "5" ) );
		JButtonsList[ 6 ] = ( buttonSix = new JButton( "6" ) );
		JButtonsList[ 7 ] = ( buttonSeven = new JButton( "7" ) );
		JButtonsList[ 8 ] = ( buttonEight = new JButton( "8" ) );
		JButtonsList[ 9 ] = ( buttonNine = new JButton( "9" ) );
		JButtonsList[ 10 ] = ( buttonDot = new JButton( "." ) );
		JButtonsList[ 11 ] = ( buttonNegate = new JButton( "+/-" ) );
		JButtonsList[ 12 ] = ( buttonMinus = new JButton( "-" ) );
		JButtonsList[ 13 ] = ( buttonPlus = new JButton( "+" ) );
		JButtonsList[ 14 ] = ( buttonEqual = new JButton( "=" ) );
		JButtonsList[ 15 ] = ( buttonMultiply = new JButton( "*" ) );
		JButtonsList[ 16 ] = ( buttonDivide = new JButton( "/" ) );
		JButtonsList[ 17 ] = ( buttonDeleteLast = new JButton( "C" ) );
		JButtonsList[ 18 ] = ( buttonDeleteAll = new JButton( "CE" ) );
		JButtonsList[ 19 ] = ( buttonFutureButton = new JButton( "" ) );

		for ( JButton button : JButtonsList ) {
			button.addActionListener( new Listener( ) );
			button.setFont( new Font( "Serif" , Font.PLAIN , 18 ) );
		}
	}

	/*Listener for the buttons*/
	private class Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object src = e.getSource( );

			for ( JButton button : JButtonsList ) {
				if( src == button ) {
					if( src == buttonEqual )
						screen.setResultExpressionText( logic.getResultOf( screen.getMathExpressionText( ) ) );
					else if( src == buttonDeleteLast )
						screen.deleteLastFigure( );
					else if( src == buttonDeleteAll )
						screen.deleteAll( );
					else if( src == buttonNegate )
						screen.negate( );
					else if( src == buttonFutureButton )
						return;
					else
						screen.appendMathExpressionText( button.getText( ) );
				}
			}

		}
	}
}
