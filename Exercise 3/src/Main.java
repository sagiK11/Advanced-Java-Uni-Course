

public class Main {

	public static void main(String[] args) {
		questionOne(); // GUI hanged man game
		questionTwo( ); //GUI calculator
	}

	private static void questionOne() {
		new HangedManGame( ).runHangedManGame( );
	}

	private static void questionTwo() {
		new CalculatorApp( ).runCalculatorApp( );
	}

}
