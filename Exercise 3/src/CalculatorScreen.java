import javax.swing.*;
import java.awt.*;

public class CalculatorScreen extends JPanel {
	//Class Fields
	private JTextArea mathExpressionTextArea, resultExpressionTextArea;
	private final String MATH_EXPRESSION_STARTING_TEXT = "";
	private final String RESULT_EXPRESSION_STARTING_TEXT = "0";

	public CalculatorScreen() {
		setLayout( new BorderLayout( ) );
		setTextsArea( );

		add( mathExpressionTextArea , BorderLayout.CENTER );
		add( resultExpressionTextArea , BorderLayout.SOUTH );
		setVisible( true );
	}

	/*Sets the text areas properties*/
	private void setTextsArea() {
		mathExpressionTextArea = new JTextArea( 2 , 1 );
		mathExpressionTextArea.setBackground( new Color( 224 , 224 , 224 ) );
		mathExpressionTextArea.setEditable( false );
		mathExpressionTextArea.setSize( new Dimension( 270 , 30 ) );
		mathExpressionTextArea.setFont( new Font( "Serif" , Font.PLAIN , 32 ) );
		mathExpressionTextArea.setText( MATH_EXPRESSION_STARTING_TEXT );

		resultExpressionTextArea = new JTextArea( );
		resultExpressionTextArea.setBackground( Color.lightGray );
		resultExpressionTextArea.setEditable( false );
		resultExpressionTextArea.setSize( new Dimension( 250 , 60 ) );
		resultExpressionTextArea.setFont( new Font( "Serif" , Font.PLAIN , 48 ) );
		resultExpressionTextArea.setText( RESULT_EXPRESSION_STARTING_TEXT );
	}

	/*Deletes the last figure from the expression */
	public void deleteLastFigure() {
		String currText = mathExpressionTextArea.getText( );
		if( currText.length( ) > 0 )
			mathExpressionTextArea.setText( currText.substring( 0 , currText.length( ) - 1 ) );
	}

	/*Clears the expression screen*/
	public void deleteAll() {
		mathExpressionTextArea.setText( "" );
	}

	/*Adds to the expression text area figures/symbols */
	public void appendMathExpressionText(String text) {
		final int EXPRESSION_MAX_LENGTH = 20;
		String futureText = mathExpressionTextArea.getText( ) + text;

		if( ! isLegal( futureText ) )
			return;

		if( futureText.length( ) < EXPRESSION_MAX_LENGTH )
			mathExpressionTextArea.append( text );
	}

	/*Flips the sign of the right most number*/
	public void negate() {
		String prefix = mathExpressionTextArea.getText( ), suffix = prefix;
		int i = 0;

		for ( i = suffix.length( ) - 1; i > 0 ; i-- )//getting to the operator
			if( isOperator( suffix.charAt( i ) ) )
				break;

		if( edgeCases( prefix , suffix , i ) )  //no operator or operator at the end of the expression.
			return;

		char operator = prefix.charAt( i );
		prefix = prefix.substring( 0 , i );
		suffix = suffix.substring( i + 1 , suffix.length( ) );

		//Appending/Changing the the expression
		switch ( operator ) {
			case CalculatorLogic.PLUS:
				mathExpressionTextArea.setText( prefix + "-" + suffix );
				return;
			case CalculatorLogic.MINUS:
				if( isOperator( prefix.charAt( i - 1 ) ) )
					mathExpressionTextArea.setText( prefix + suffix );
				else
					mathExpressionTextArea.setText( prefix + "+" + suffix );
				return;
			case CalculatorLogic.PRODUCT:
				mathExpressionTextArea.setText( prefix + "*-" + suffix );
				return;
			default:
				mathExpressionTextArea.setText( prefix + "/-" + suffix );
		}
	}

	/*Utility method for detecting edge cases for negate method*/
	private boolean edgeCases(String prefix , String suffix , int i) {
		if( i == 0 ) {// edge case - no operator
			if( prefix.charAt( i ) == CalculatorLogic.MINUS )
				mathExpressionTextArea.setText( prefix.substring( 1 , prefix.length( ) ) );//removing the minus sign
			else
				mathExpressionTextArea.setText( "-" + prefix ); // adding the minus sign.
			return true;
		} else if( i == 1 && prefix.charAt( i ) == CalculatorLogic.DOT ) { // negate with only floating point.
			return true;
		} else // edge case - operator is the last char
			return i == suffix.length( ) - 1;
	}

	/*Tests if the newly added text is valid*/
	private boolean isLegal(String futureText) {
		boolean doubleOperators = false, doubleDots = false;

		if( unaryOperatorError( futureText ) )
			return false;

		//Looks for: '++' or '--' or '**' or '//' or '..' and returns false if it finds.
		for ( int i = 0 ; i < futureText.length( ) ; i++ ) {
			char currChar = futureText.charAt( i );
			if( ! Character.isDigit( currChar ) && currChar == CalculatorLogic.DOT ) { // floating point
				if( doubleDots ) // double floating point.
					return false;
				doubleDots = true;
				doubleOperators = false;
			} else if( ! Character.isDigit( currChar ) ) { // operator
				if( doubleOperators )
					return false;
				doubleDots = false;
				doubleOperators = true;
			} else { // digit
				doubleOperators = false;
			}
		}
		return true;
	}

	/*Utility  method for edge cases method of negate method*/
	private boolean unaryOperatorError(String futureText) {
		char firstChar = futureText.charAt( 0 );
		return ! Character.isDigit( firstChar ) && firstChar != CalculatorLogic.DOT
			&& firstChar != CalculatorLogic.MINUS;
	}

	/*Sets the passed argument text to the result area screen*/
	public void setResultExpressionText(String text) {
		resultExpressionTextArea.setText( text );
	}

	/*Returns the text in the expression text area*/
	public String getMathExpressionText() {
		return mathExpressionTextArea.getText( );
	}

	/*Returns true if the passed char is a valid operator*/
	private boolean isOperator(char c) {
		return c == CalculatorLogic.PLUS || c == CalculatorLogic.MINUS
			|| c == CalculatorLogic.PRODUCT || c == CalculatorLogic.DIVIDE;
	}


}
