import javax.swing.*;
import java.util.ArrayList;

public class CalculatorLogic {
	//Class Constants
	public static final char PLUS = '+', MINUS = '-', PRODUCT = '*', DIVIDE = '/', DOT = '.';

	/*Returns the arithmetic  result of the passed expression.*/
	public String getResultOf(String expression) {
		ArrayList< Literal > literalsArrayList = new ArrayList<>( );

		fillList( expression , literalsArrayList );
		try {
			removeMinuses( literalsArrayList );
			calculateHighPrecedenceFirstInList( literalsArrayList );
		} catch ( IndexOutOfBoundsException e ) {
			JOptionPane.showMessageDialog( null , "Missing an operand." );
		}
		return String.valueOf( sumNumber( literalsArrayList ) );
	}

	/*Iterates over the passed list and joins '-' and 'expression' to the same element.*/
	private void removeMinuses(ArrayList< Literal > literalsArrayList) {
		for ( int i = 0 ; i < literalsArrayList.size( ) ; i++ ) {
			if( literalsArrayList.get( i ).isMinusOperator( ) ) {
				literalsArrayList.remove( i );
				literalsArrayList.get( i ).flipSign( );
			}
		}
	}

	/*Iterates over the passed list and sum up the literals in the list.*/
	private double sumNumber(ArrayList< Literal > literalsArrayList) {
		double res = 0;
		boolean plus = true;

		for ( Literal literal : literalsArrayList ) {
			if( ! literal.isOperator( ) )
				res += plus ? literal.getNum( ) : literal.getNum( ) * ( - 1 );
			else
				plus = literal.getOperator( ) == PLUS;
		}
		return res;
	}

	/*Iterates over the passed list and calculates expressions with high precedence operator firsts.*/
	private void calculateHighPrecedenceFirstInList(ArrayList< Literal > literalsArrayList) {
		final int NUM_OF_REMOVALS = 3;
		for ( int i = 0 ; i < literalsArrayList.size( ) ; i++ ) {
			if( ! literalsArrayList.get( i ).isHighPrecedenceOperator( ) )
				continue;

			Literal literal;
			char operator = literalsArrayList.get( i ).getOperator( );

			if( operator == PRODUCT ) {
				literal = new Literal( literalsArrayList.get( i - 1 ).getNum( ) *
					literalsArrayList.get( i + 1 ).getNum( ) );
			} else {
				literal = new Literal( literalsArrayList.get( i - 1 ).getNum( ) /
					literalsArrayList.get( i + 1 ).getNum( ) );
			}

			for ( int j = 0 ; j < NUM_OF_REMOVALS ; j++ )
				literalsArrayList.remove( i - 1 );
			literalsArrayList.add( i - 1 , literal );
			i = 0;

		}
	}

	/*Parses the passed String and fills with it the passed list. Each element is a literal from the string.*/
	private void fillList(String expression , ArrayList< Literal > literalsArrayList) {
		int expressionLength = expression.length( );
		for ( int i = 0 ; i < expressionLength ; i++ ) {

			StringBuilder tmp = new StringBuilder( );//tmp holds digits
			while ( i < expressionLength && ( Character.isDigit( expression.charAt( i ) ) || expression.charAt( i ) == DOT ) ) {
				tmp.append( expression.charAt( i++ ) );
			}

			if( tmp.length( ) > 0 )
				literalsArrayList.add( new Literal( Double.parseDouble( tmp.toString( ) ) ) ); // double

			if( i < expressionLength )
				literalsArrayList.add( new Literal( expression.charAt( i ) ) ); // Operator
		}
	}

	/*Literal object for the Literals list*/
	private class Literal {
		double num;
		char operator;
		boolean isNum;

		Literal(double num) {
			this.num = num;
			isNum = true;
		}

		Literal(char op) {
			this.operator = op;
			isNum = false;
		}

		boolean isHighPrecedenceOperator() {
			return operator == PRODUCT || operator == DIVIDE;
		}

		boolean isOperator() {
			return ! isNum;
		}

		boolean isMinusOperator() {
			return operator == MINUS;
		}

		char getOperator() {
			return operator;
		}

		void flipSign() {
			this.num = num * ( - 1 );
		}

		double getNum() {
			return num;
		}

		public String toString() {
			StringBuilder res = new StringBuilder( );
			return isNum ? res.append( num ).toString( ) : res.append( operator ).toString( );
		}
	}
}
