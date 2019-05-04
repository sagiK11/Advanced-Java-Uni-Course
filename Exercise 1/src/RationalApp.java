
import java.util.Scanner;

public class RationalApp {
	private static Scanner scan;
	private static Rational firstRational, secondRational;
	private static boolean running = true, badInput = false;

	public void runRationalApp() {
		scan = new Scanner( System.in );
		do {
			badInput = false;
			getRationalsFromUser( );
			doSomeMath( );
			askForAnotherTry( );
		} while ( running );
	}

	private static void askForAnotherTry() {
		if( badInput )
			return;

		System.out.println( "enter 1 for running app again, else enter any number" );
		running = scan.nextInt( ) == 1;
	}

	private static void doSomeMath() {
		if( badInput )
			return;

		System.out.println( "\nRational 1 = " + firstRational.reduce( ) + " Rational 2 = " + secondRational.reduce( ) );
		System.out.println( "the two rationals equals = " + firstRational.equals( secondRational ) );
		System.out.println( "the first rational is greater than the second  = " + firstRational.greaterThan( secondRational ) );
		System.out.println( "the sum of the two rationals = " + firstRational.plus( secondRational ) );
		System.out.println( "the difference between the first rational to the second = " + firstRational.minus( secondRational ) );
		System.out.println( "the product of the first rational and the second = " + firstRational.multiply( secondRational ) + "\n" );
	}

	private static void getRationalsFromUser() {
		firstRational = getRational( );
		if( badInput )
			return;
		System.out.println( "the first rational = " + firstRational.reduce( ) );

		secondRational = getRational( );
		if( badInput )
			return;
		System.out.println( "the second rational = " + secondRational.reduce( ) );
	}

	private static Rational getRational() {
		Integer numerator = 0, denominator = 0;

		System.out.println( "please enter the numerator " );
		numerator = scan.nextInt( );
		System.out.println( "please enter the denominator " );
		denominator = scan.nextInt( );

		if( denominator <= 0 )
			badInput( );

		return new Rational( numerator , denominator );
	}

	private static void badInput() {
		System.out.println( "denominator can not less than or equal to 0, try again" );
		badInput = true;
	}
}
