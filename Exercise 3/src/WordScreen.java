
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class WordScreen extends JPanel {
	//Class fields
	private ArrayList< String > wordsArrayList;
	private static String currentWord, unusedLetters;
	private boolean[] flaggedLettersArray;
	private JTextArea titleTextArea, wordTextArea, unusedLettersTextArea, southTextArea;
	//Class Constants
	private final int WIDTH = 400, HEIGHT = 300;

	public WordScreen(ArrayList< String > wordsArrayList) {
		this.wordsArrayList = wordsArrayList;
		setPreferredSize( new Dimension( WIDTH , HEIGHT ) );
		unusedLetters = "a b c d e f g h i j k l m n o p q r s t u v w x y z";
		currentWord = getRandomWord( );
		flaggedLettersArray = new boolean[ currentWord.length( ) ];


		setLayout( new BorderLayout( ) );
		setTextAreas( );

		JPanel upperCenter = new JPanel( );
		upperCenter.setLayout( new BorderLayout( ) );
		upperCenter.add( titleTextArea , BorderLayout.NORTH );

		JPanel lowerCenter = new JPanel( );
		lowerCenter.setLayout( new BorderLayout( ) );
		lowerCenter.add( wordTextArea , BorderLayout.NORTH );
		lowerCenter.add( unusedLettersTextArea , BorderLayout.CENTER );
		lowerCenter.add( southTextArea , BorderLayout.SOUTH );

		add( upperCenter , BorderLayout.NORTH );
		add( lowerCenter , BorderLayout.CENTER );

	}

	/*Initializing the text area objects */
	private void setTextAreas() {
		titleTextArea = new JTextArea( "\nHang man Game!\n" );
		titleTextArea.setFont( new Font( "Serif," , Font.BOLD | Font.ITALIC , 32 ) );
		titleTextArea.setEditable( false );
		titleTextArea.setBackground( new Color( 74 , 164 , 224 ) );
		titleTextArea.setMargin( new Insets( 6 , 6 , 6 , 6 ) );

		wordTextArea = new JTextArea( "" );
		wordTextArea.setFont( new Font( "Serif" , Font.PLAIN , 24 ) );
		wordTextArea.setEditable( false );
		wordTextArea.setBackground( new Color( 74 , 164 , 224 ) );
		wordTextArea.setMargin( new Insets( 6 , 6 , 6 , 6 ) );

		unusedLettersTextArea = new JTextArea( unusedLetters );
		unusedLettersTextArea.setFont( new Font( "Serif" , Font.PLAIN , 20 ) );
		unusedLettersTextArea.setEditable( false );
		unusedLettersTextArea.setBackground( new Color( 74 , 164 , 224 ) );
		unusedLettersTextArea.setMargin( new Insets( 6 , 6 , 6 , 6 ) );

		southTextArea = new JTextArea( );
		southTextArea.setBackground( new Color( 45 , 105 , 145 ) );
		southTextArea.setEditable( false );
		southTextArea.setFont( new Font( "Serif" , Font.PLAIN , 45 ) );

		for ( int i = 0 ; i < currentWord.length( ) ; i++ )
			wordTextArea.append( " _ " );
	}

	/*Updating the screen according to the letter*/
	public int updateScreen(String letter) {
		final int OFFSET1 = 3, OFFSET2 = 1, OFFSET3 = 2;
		int wordLength = currentWord.length( ), match = 0;
		StringBuilder tmp = new StringBuilder( wordTextArea.getText( ) );

		for ( int i = 0 ; i < wordLength ; i++ ) {
			if( currentWord.charAt( i ) == letter.charAt( 0 ) && ! flaggedLettersArray[ i ] ) {
				tmp.replace( i * OFFSET1 + OFFSET2 , i * OFFSET1 + OFFSET3 , letter );
				wordTextArea.setText( tmp.toString( ) );
				match++;
				flaggedLettersArray[ i ] = true; // flagging this cell  - so we wont count this letter twice.
			}
		}
		unusedLettersTextArea.setText( unusedLettersTextArea.getText( ).replace( letter.charAt( 0 ) , '-' ) );
		return match;
	}

	/*Returns a random word from the words list entered from the file the user entered*/
	private String getRandomWord() {
		String randomWord = null;

		try {
			randomWord = wordsArrayList.get( new Random( ).nextInt( wordsArrayList.size( ) ) );
		} catch ( IllegalArgumentException e ) {
			HangedManGame.fileError( );
		}
		return randomWord;
	}

	/*Returns the current word being guessed*/
	public String getCurrentWord() {
		return currentWord;
	}

}
