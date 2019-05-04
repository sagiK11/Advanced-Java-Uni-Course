
import javax.swing.*;
import java.awt.*;

public class GameApp extends JPanel {
	//Class fields
	private static JFrame window;
	private static GameApp gameApp;
	private static WordsBank wordsBank;
	private static boolean playing = true;
	private static int letterCnt = 0, triesCnt = 0;
	private static String currentWord, unusedLetters, usedLetters;
	//Constants
	private static final int LINE_LENGTH = 20, LINE_SPACE = 40;
	private static final int Y_AXIS = 100, TRIES_Y_AXIS = 60;
	private static final int UNUSED_LETTERS_Y_AXIS_HEAD_LINE = 140;
	private static final int UNUSED_LETTERS_Y_AXIS = 170;
	private static final int REC_WIDTH = 420;
	private static final int OFFSET = 10;
	private static final int NEW_LETTER = - 1, CHAR_LENGTH = 1;
	private static final String ANOTHER_GAME = "y";

	public void runGameApp() {
		window = new JFrame( );
		gameApp = new GameApp( );
		wordsBank = new WordsBank( );
		playGame( );
	}

	private static void playGame() {
		initializeGame( );

		while ( playing ) {
			if( letterCnt == currentWord.length( ) )
				askForAnotherGame( );
			else
				askForAnotherChar( );
		}
		endGame( );
	}

	private static void endGame() {
		window.dispose( );
	}

	private static void initializeGame() {
		final int screen_width = 450, screen_height = 220, xLocation = 500, yLocation = 70, txtSize = 14;
		gameApp.setFont( new Font( "DialogInput" , Font.PLAIN , txtSize ) );
		window.add( gameApp );
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		window.setSize( screen_width , screen_height );
		window.setTitle( "Question 1" );
		window.setResizable( false );
		window.setLocation( xLocation , yLocation );
		window.setVisible( true );
		unusedLetters = "a b c d e f g h i j k l m n o p q r s t u v w x y z";
		usedLetters = "";
		triesCnt = 0;
		letterCnt = 0;
		currentWord = wordsBank.getRandomWord( );
		System.out.println( "word = " + currentWord ); //debug
	}

	private static void askForAnotherGame() {
		JOptionPane.showMessageDialog( gameApp , "Success!" );
		String answer = JOptionPane.showInputDialog( gameApp , "play another one?  enter y, else enter any key" );
		if( answer != null && answer.equals( ANOTHER_GAME ) ) {
			window.dispose( );
			playGame( );
		} else {
			JOptionPane.showMessageDialog( null , "bye bye" );
			playing = false;
		}
	}

	private static void askForAnotherChar() {
		String txt = JOptionPane.showInputDialog( null , "enter a letter:   " );
		if( ! isLegal( txt ) )
			return;
		triesCnt++;

		//updating tries counter
		gameApp.getGraphics( ).clearRect( LINE_LENGTH , LINE_SPACE , Y_AXIS , LINE_SPACE );
		gameApp.getGraphics( ).drawString( "Tries:  " + triesCnt , LINE_LENGTH , TRIES_Y_AXIS );
		unusedLetters = unusedLetters.replace( txt , "-" );

		// searching for the letter & adding it if needs to.
		for ( int i = 0, x1 = LINE_LENGTH + OFFSET ; i < currentWord.length( ) ; i++ , x1 += LINE_SPACE )
			if( currentWord.charAt( i ) == txt.charAt( 0 ) ) {
				gameApp.getGraphics( ).drawString( txt , x1 , Y_AXIS - OFFSET );// adding the letter in the screen
				if( usedLetters.indexOf( txt.charAt( 0 ) ) == NEW_LETTER )
					letterCnt++;
			}
		usedLetters += txt;

		//updating used letters
		gameApp.getGraphics( ).clearRect( LINE_LENGTH , UNUSED_LETTERS_Y_AXIS_HEAD_LINE , REC_WIDTH , TRIES_Y_AXIS );
		gameApp.getGraphics( ).drawString( unusedLetters , LINE_LENGTH , UNUSED_LETTERS_Y_AXIS );
	}

	private static boolean isLegal(String txt) {
		if( txt == null ) {
			JOptionPane.showMessageDialog( null , "cancel clicked - exiting" );
			System.exit( 0 );
		}
		return txt.length( ) == CHAR_LENGTH;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent( g );

		for ( int i = 0, x1 = LINE_LENGTH ; i < currentWord.length( ) ; i++ , x1 += LINE_SPACE ) {
			g.drawLine( x1 , Y_AXIS , x1 + LINE_LENGTH , Y_AXIS );
		}

		g.drawString( "tries:  " + triesCnt , LINE_LENGTH , TRIES_Y_AXIS );
		g.drawString( "unused letters: " , LINE_LENGTH , UNUSED_LETTERS_Y_AXIS_HEAD_LINE );
		g.drawString( unusedLetters , LINE_LENGTH , UNUSED_LETTERS_Y_AXIS );
	}
}
