
import java.util.Scanner;

public class BigIntApp {

	public void runBigIntApp() {
		Scanner scan = new Scanner( System.in );
		String answer = "", exit = "yes";
		boolean exceptionOccurred;

		do {
			exceptionOccurred = false;
			System.out.println( "enter first number " );
			String firstNum = scan.nextLine( );
			System.out.println( "enter second number " );
			String secondNum = scan.nextLine( );
			try {
				doSomeMath( firstNum , secondNum );
			} catch ( IllegalArgumentException e ) {
				System.out.println( "illegal number, please enter only digits" );
				exceptionOccurred = true;
			}
			if( ! exceptionOccurred ) {
				System.out.println( "would you like to exit? enter yes; else enter any key" );
				answer = scan.nextLine( );
			}
		} while ( ! answer.equals( exit ) );
	}

	private static void doSomeMath(String firstNum , String secondNum) {
		BigInt first = new BigInt( firstNum ), second = new BigInt( secondNum );

		System.out.println( first + " + " + second + " = " + first.plus( second ) );
		System.out.println( first + " - " + second + " = " + first.minus( second ) );
		System.out.println( first + " * " + second + " = " + first.multiply( second ) );
		System.out.println( first + " / " + second + " = " + first.divide( second ) );
	}

}
