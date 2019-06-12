
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadArrayListApp extends JFrame {
	private ThreadsArrayList threadsArrayList;
	private JTextArea threadsStateTextArea;
	private ExecutorService executorService;

	public ThreadArrayListApp() {
		super( "Question 1" );
		executorService = Executors.newCachedThreadPool( );
		setUserInterface( );
		runAppAlgorithm( );
	}

	private void runAppAlgorithm() {
		int threadsNumber = getInputFromUser( "Threads Number" );
		int TestsNumber = getInputFromUser( "Tests Number" );
		threadsArrayList = new ThreadsArrayList( threadsNumber );

		InitializeThreads( threadsNumber );
		runThreadsTests( TestsNumber );
		shutDownThreads( );
	}

	private void InitializeThreads(int threadsNumber) {
		for ( int i = 0 ; i < threadsNumber ; i++ )
			threadsArrayList.add( new NumberThread( threadsArrayList , i ) );
	}

	private void runThreadsTests(int TestsNumber) {
		printStartingState( );

		for ( int i = 1 ; i < TestsNumber + 1 ; i++ ) {
			runThreadsArrayList( );
			threadsArrayList.waitForAll( );
			threadsArrayList.resetThreads( );
			printCurrentState( i );
			sleep( );
		}

	}

	private void runThreadsArrayList() {
		for ( int j = 0 ; j < threadsArrayList.size( ) ; ++ j )
			executorService.execute( threadsArrayList.get( j ) );
	}

	private void printStartingState() {
		threadsStateTextArea.append( threadsArrayList.getCurrentState( 0 ) );
	}

	private void printCurrentState(int stateNum) {
		threadsStateTextArea.append( threadsArrayList.getCurrentState( stateNum ) );
	}

	private void shutDownThreads() {
		executorService.shutdown( );
	}

	private Integer getInputFromUser(String desiredInput) {
		final int EXIT_FAILURE = 1;
		Integer inputFromUser = null;
		String num = "";

		try {
			num = JOptionPane.showInputDialog( this , "Please Enter " + desiredInput );
			inputFromUser = Integer.parseInt( num );
		} catch ( NumberFormatException e ) {
			if( num == null )
				JOptionPane.showMessageDialog( this , "Bye Bye." );
			else
				JOptionPane.showMessageDialog( this , "Please enter numbers only." );
			System.exit( EXIT_FAILURE );
		}

		return inputFromUser;
	}

	private void sleep() {
		final int DELAY = 1700;
		try {
			Thread.sleep( DELAY );
		} catch ( InterruptedException e ) {
			JOptionPane.showMessageDialog( null , "Interrupted!" );
		}
	}

	private void setUserInterface() {
		final int FRAME_WIDTH = 700, FRAME_HEIGHT = 450;

		setResizable( false );
		setLocationRelativeTo( null );
		setSize( FRAME_WIDTH , FRAME_HEIGHT );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );

		JPanel centerPanel = new JPanel( );
		centerPanel.setLayout( new BoxLayout( centerPanel , BoxLayout.Y_AXIS ) );
		centerPanel.setBorder( BorderFactory.createEmptyBorder( 12 , 16 , 10 , 12 ) );

		threadsStateTextArea = new JTextArea( );
		threadsStateTextArea.setEditable( false );
		threadsStateTextArea.setFont( new Font( "Serif" , Font.PLAIN , 18 ) );

		JScrollPane scrollPane = new JScrollPane( threadsStateTextArea );
		scrollPane.setBorder( BorderFactory.createTitledBorder( "Threads State" ) );
		scrollPane.setPreferredSize( new Dimension( FRAME_WIDTH , FRAME_HEIGHT ) );

		centerPanel.add( scrollPane );
		add( centerPanel , BorderLayout.CENTER );

		setVisible( true );
	}

}
