
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;


public class HangedManGame extends JFrame {
	//Class Fields
	private JPanel west;
	private WordScreen wordScreen;
	private HangedManPainting hangedManPainting;
	private Controls controls;
	private ArrayList< String > wordsArrayList;
	private int goodGuesses;
	//Class Constants
	private final int FRAME_WIDTH = 750, FRAME_HEIGHT = 400, RESTART_GAME = 0, CHAR_LENGTH = 1;


	public HangedManGame() {
		super( "Question 1" );
	}

	public void runHangedManGame() {
		getFileFromUser( );
		playGame( );
	}

	/*Initializing and setting the frame and panels*/
	private void playGame() {
		setSize( FRAME_WIDTH , FRAME_HEIGHT );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setLocationRelativeTo( null );
		setResizable( false );
		setLayout( new BorderLayout( ) );
		goodGuesses = 0;

		initializeJPanels( );
		add( west , BorderLayout.WEST );
		add( wordScreen , BorderLayout.CENTER );
		add( controls , BorderLayout.SOUTH );
		add( hangedManPainting , BorderLayout.EAST );
		setVisible( true );
	}

	/*Opens a small window for the user to grab his file with the words*/
	private void getFileFromUser() {
		JFileChooser fileChooser = new JFileChooser( );
		fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );

		int result = fileChooser.showOpenDialog( this );
		if( result == JFileChooser.CANCEL_OPTION )
			System.exit( 1 );

		Path path = fileChooser.getSelectedFile( ).toPath( );
		if( ( ! Files.exists( path ) ) )
			fileError( );
		parseFile( path );

	}

	/*Parses the file and extracting the words and populate the words list with it*/
	private void parseFile(Path path) {
		Scanner scan = null;
		wordsArrayList = new ArrayList<>( );

		try {
			scan = new Scanner( new File( path.toUri( ) ) );
		} catch ( Exception e ) {
			fileError( );
		}

		//parsing words
		while ( scan.hasNext( ) )
			wordsArrayList.add( scan.next( ) );

	}

	/*Initializing the panels and creating instances of the logic of the app*/
	private void initializeJPanels() {
		west = new JPanel( ); // Empty cosmetic panel
		west.setPreferredSize( new Dimension( 70 , FRAME_HEIGHT ) );
		west.setBackground( new Color( 45 , 105 , 145 ) );

		wordScreen = new WordScreen( wordsArrayList );
		hangedManPainting = new HangedManPainting( );
		controls = new Controls( this );
	}

	/*Updates the screen according to the letter entered*/
	public void enterClicked(String letter) {
		boolean manIsFullyHanged = false;
		if( letter == null || letter.length( ) != CHAR_LENGTH )
			return;

		int matches = wordScreen.updateScreen( letter );
		goodGuesses += matches;

		if( matches == 0 && hangedManPainting.addPart( ) )
			manIsFullyHanged = true;

		if( goodGuesses == wordScreen.getCurrentWord( ).length( ) || manIsFullyHanged )
			gameOver( manIsFullyHanged );

	}

	/*Ending or restarting the game by the user choice*/
	private void gameOver(boolean manIsFullyHanged) {
		if( manIsFullyHanged )
			JOptionPane.showMessageDialog( this , "Game Over!" );
		else
			JOptionPane.showMessageDialog( this , "Well Done!" , "Success" , JOptionPane.PLAIN_MESSAGE );

		int answer = JOptionPane.showConfirmDialog( this , "Play another game? " );

		if( answer == RESTART_GAME )
			restartGame( );
		else
			exitGame( );
	}

	private void exitGame() {
		JOptionPane.showMessageDialog( this , "Bye Bye!" );
		System.exit( 0 );
	}

	private void restartGame() {
		Container c = getContentPane( );
		c.removeAll( );
		playGame( );
	}

	/*Utility function*/
	public static void fileError() {
		JOptionPane.showMessageDialog( null , "File Error" , "error" , JOptionPane.ERROR_MESSAGE );
		System.exit( 1 );
	}

}
