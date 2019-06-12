
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class NotesApp extends JFrame {

	private File notesFile;
	private JButton addFileButton, fetchNoteButton, saveButton;
	private JComboBox< String > dayDate, monDate, yearDate;
	private JTextArea noteTextArea, dateTextArea;
	private NotesAppDateFormat noteDate;
	private HashMap< NotesAppDateFormat, String > notesHashMap;
	private final int FRAME_WIDTH = 450, FRAME_HEIGHT = 450;

	public NotesApp() {
		super( "Question 2" );
		setSize( FRAME_WIDTH , FRAME_HEIGHT );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setLocationRelativeTo( null );
		setResizable( false );
	}

	public void runNotesApp() {
		JPanel frame = new JPanel( );
		frame.setLayout( new BoxLayout( frame , BoxLayout.Y_AXIS ) );
		frame.setBorder( BorderFactory.createEmptyBorder( 12 , 16 , 10 , 12 ) );

		Listener listener = new Listener( );
		notesHashMap = new HashMap<>( );

		//Upper app
		JPanel upperButtons = new JPanel( );
		addButtonsToUpperPanel( upperButtons , listener );

		//Center app
		dateTextArea = new JTextArea( );
		noteTextArea = new JTextArea( );
		JScrollPane noteScrollPane = new JScrollPane( noteTextArea );
		setTextArea( noteScrollPane );

		//Lower app
		JPanel lowerButtons = new JPanel( );
		addButtonsToLowerPanel( lowerButtons , listener );

		//Add app parts
		frame.add( upperButtons );
		frame.add( dateTextArea );
		frame.add( noteScrollPane );
		frame.add( lowerButtons );
		add( frame );

		setVisible( true );
	}

	private void addButtonsToUpperPanel(JPanel upperButtons , Listener listener) {
		//Set Fields Content
		String[] days = { "1" , "2" , "3" , "4" , "5" , "6" , "7" , "8" , "9" , "10" , "11" , "12" , "13" , "14" , "15" ,
			"16" , "17" , "18" , "19" , "20" , "21" , "22" , "23" , "24" , "25" , "26" , "27" , "28" , "29" , "30" , "31" };

		String[] months = { "January" , "February" , "March" , "April" , "May" , "June" , "July" ,
			"August" , "September" , "October" , "November" , "December" };

		String[] years = { "2019" , "2020" , "2021" , "2022" , "2023" , "2024" ,
			"2025" , "2026" , "2027" , "2028" , "2029" , "2030" };

		// Create Components
		addFileButton = new JButton( "Add File" );
		fetchNoteButton = new JButton( "Fetch" );
		dayDate = new JComboBox<>( days );
		monDate = new JComboBox<>( months );
		yearDate = new JComboBox<>( years );

		upperButtons.setBorder( BorderFactory.createTitledBorder( "Select File and Date" ) );
		addFileButton.addActionListener( listener );
		fetchNoteButton.addActionListener( listener );

		//add buttons
		upperButtons.add( addFileButton );
		upperButtons.add( dayDate );
		upperButtons.add( monDate );
		upperButtons.add( yearDate );
		upperButtons.add( fetchNoteButton );
	}

	private void setTextArea(JScrollPane noteScrollPane) {
		dateTextArea.setFont( new Font( "Serif" , Font.PLAIN | Font.BOLD , 24 ) );
		dateTextArea.setBorder( BorderFactory.createTitledBorder( "Note Date" ) );
		dateTextArea.setEditable( false );

		noteTextArea.setFont( new Font( "Serif" , Font.PLAIN , 18 ) );
		noteScrollPane.setBorder( BorderFactory.createTitledBorder( "Enter Note" ) );
		noteScrollPane.setPreferredSize( new Dimension( FRAME_WIDTH , FRAME_HEIGHT ) );
	}

	private void addButtonsToLowerPanel(JPanel lowerButtons , Listener listener) {
		saveButton = new JButton( "Save" );
		saveButton.addActionListener( listener );
		saveButton.setMargin( new Insets( 6 , 30 , 6 , 30 ) );
		lowerButtons.add( saveButton );
	}

	private void addFile() {
		JFileChooser fileChooser = new JFileChooser( );
		fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );

		int result = fileChooser.showOpenDialog( this );
		if( result == JFileChooser.CANCEL_OPTION )
			System.exit( 1 );

		Path path = fileChooser.getSelectedFile( ).toPath( );
		if( ( ! Files.exists( path ) ) )
			fileError( "Could Not Find File" );
		else if( fileNotValid( path ) )
			fileError( "File must end with '.txt' " );
		else
			notesFile = new File( path.toUri( ) );
		System.out.println( path.getFileName( ).toString( ) );

		JOptionPane.showMessageDialog( this , "File Added!" , "Added" , JOptionPane.INFORMATION_MESSAGE );
	}

	private void fetchNote() throws IOException, NullPointerException {
		noteDate = getNewNoteAppDateFormat( );
		ObjectInputStream in = new ObjectInputStream( new FileInputStream( notesFile ) );

		System.out.println( "reading from file" );
		try {
			notesHashMap = ( HashMap< NotesAppDateFormat, String > ) in.readObject( );
		} catch ( ClassNotFoundException e ) {
			fileError( "File Error occurred" );
		} catch ( EOFException e ) {
			// this block was left empty on purpose.
		} finally {
			in.close( );
		}

		dateTextArea.setText( noteDate.toString( ) );
		noteTextArea.setText( notesHashMap.get( noteDate ) );
	}

	private void saveToFile() throws IOException {
		noteDate = getNewNoteAppDateFormat( );
		String noteText = noteTextArea.getText( );

		if( noteText.length( ) == 0 ) {
			JOptionPane.showMessageDialog( this , "empty notes will not be saved" );
			return;
		}

		if( notesFile == null ) {
			notesFile = new File( "myNotes.txt" );
			JOptionPane.showMessageDialog( this , "Created a new file named : 'myNotes' " );
		}

		ObjectOutputStream out = new ObjectOutputStream( new FileOutputStream( notesFile ) );

		//Adding the note and date to the hash in the file
		notesHashMap.put( noteDate , noteText );
		out.writeObject( notesHashMap );

		out.close( );
		JOptionPane.showMessageDialog( this , "Note Saved!" , "Saved" , JOptionPane.PLAIN_MESSAGE );

	}

	private NotesAppDateFormat getNewNoteAppDateFormat() {
		return new NotesAppDateFormat(
			dayDate.getSelectedIndex( ) + 1 ,
			monDate.getSelectedIndex( ) + 1 ,
			yearDate.getSelectedItem( ).toString( )
		);
	}

	private void fileError(String msg) {
		JOptionPane.showMessageDialog( null , msg , "Error" , JOptionPane.ERROR_MESSAGE );
		System.exit( 1 );
	}

	private boolean fileNotValid(Path path) {
		return ! path.getFileName( ).toString( ).toLowerCase( ).endsWith( ".txt" );
	}

	private class Listener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			Object obj = e.getSource( );
			try {
				if( obj == addFileButton )
					addFile( );
				else if( obj == fetchNoteButton )
					fetchNote( );
				else if( obj == saveButton )
					saveToFile( );
			} catch ( FileNotFoundException | NullPointerException exc ) {
				JOptionPane.showMessageDialog( null , "Could not find file" );
			} catch ( EOFException eof ) {
				JOptionPane.showMessageDialog( null , "File is Empty" );
			} catch ( IOException exc ) {
				JOptionPane.showMessageDialog( null , "File Error" );
			}

		}
	}


}
