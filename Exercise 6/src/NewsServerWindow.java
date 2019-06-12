import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;

public class NewsServerWindow extends JFrame {
	private JTextArea logTextArea, incomingNewsTextArea;
	private ArrayList< NewsClient > clientsArrayList;
	private String incomingNewsString;
	private JFrame incomingNewsFrame;
	private DatagramSocket socket;
	private Font standardFont;

	public NewsServerWindow() {
		super( "News Server Log" );
		setFrameProperties( );
		createIncomingNewsThread( );
		createServerSocket( );
		broadCast( );
	}

	//--------Client Communication Functions----//
	private void createServerSocket() {
		final int BROAD_CAST_PORT = 7777;
		clientsArrayList = new ArrayList<>( );

		try {
			writeToServerLog( "Creating Server" );
			socket = new DatagramSocket( BROAD_CAST_PORT );
			writeToServerLog( "Server Ready" );
		} catch ( SocketException e ) {
			writeToServerLog( "Socket Exception while trying to create server" );
			e.printStackTrace( );
		}
	}

	private void broadCast() {
		final int BUFFER_SIZE = 256;
		boolean broadcasting = true;
		DatagramPacket datagramPacket;

		while ( broadcasting ) {
			try {
				byte[] buffer = new byte[ BUFFER_SIZE ];

				//receive request
				writeToServerLog( "waiting for request..." );
				datagramPacket = new DatagramPacket( buffer , BUFFER_SIZE );
				socket.receive( datagramPacket ); // ---------> we wait here until client connects
				writeToServerLog( "request accepted" );
				addNewClientToArrayList( new NewsClient( socket , datagramPacket , buffer ) );
				clearIncomingTextArea( );

			} catch ( IOException e ) {
				writeToServerLog( "Exception while receiving packet" );
				e.printStackTrace( );
				broadcasting = false;
			}
		}
		socket.close( );
	}

	private void sendMessageToSubscribers() {
		boolean emptyMessage = incomingNewsTextArea.getText( ).length( ) == 0;
		if( ! emptyMessage )
			new IncomingNewsThread( ).start( );
	}

	//------User Interface Functions-----//
	private void setFrameProperties() {
		setSize( 350 , 450 );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		getRootPane( ).setBorder( BorderFactory.createTitledBorder( "Log" ) );
		setResizable( false );

		standardFont = new Font( "Serif" , Font.PLAIN , 18 );
		logTextArea = new JTextArea( );
		logTextArea.setFont( standardFont );
		logTextArea.setEditable( false );

		JScrollPane scrollPane = new JScrollPane( logTextArea );
		add( scrollPane );
		setVisible( true );
	}

	private void createIncomingNewsThread() {
		incomingNewsFrame = new JFrame( "News To Spread" );
		incomingNewsFrame.setSize( 350 , 330 );
		incomingNewsFrame.setResizable( false );
		incomingNewsFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		incomingNewsFrame.setLayout( new BorderLayout( 5 , 5 ) );

		addMessagePanelToIncomingNewsFrame( );

		incomingNewsFrame.setVisible( true );
	}

	private void addMessagePanelToIncomingNewsFrame() {
		JPanel messagePanel = new JPanel( );
		String borderTitle = "Press Enter To Send To All Subscribers";
		Border centerPanelBorder = BorderFactory.createTitledBorder( borderTitle );
		messagePanel.setBorder( centerPanelBorder );
		messagePanel.setLayout( new BorderLayout( 5 , 5 ) );

		incomingNewsTextArea = new JTextArea( );
		incomingNewsTextArea.setFont( standardFont );
		incomingNewsTextArea.addKeyListener( new KeyStrokeListener( ) );

		JScrollPane scrollPane = new JScrollPane( incomingNewsTextArea );
		JPanel southPanel = new JPanel( );
		southPanel.setLayout( new GridLayout( 1 , 2 ) );

		JButton newLineButton = new JButton( "New Line" );
		JButton sendMessageButton = new JButton( "Send" );

		sendMessageButton.addActionListener( e -> sendMessageToSubscribers( ) );
		newLineButton.addActionListener( e -> addNewLineToNewsTextArea( ) );

		southPanel.add( newLineButton );
		southPanel.add( sendMessageButton );

		messagePanel.add( scrollPane , BorderLayout.CENTER );
		messagePanel.add( southPanel , BorderLayout.SOUTH );

		incomingNewsFrame.add( messagePanel , BorderLayout.CENTER );
	}

	private void addNewLineToNewsTextArea() {
		incomingNewsTextArea.append( "\n" );
		incomingNewsTextArea.requestFocus( );
	}

	private void clearIncomingTextArea() {
		incomingNewsTextArea.setText( "" );
		incomingNewsTextArea.requestFocus( );
	}

	private void writeToServerLog(String message) {
		logTextArea.append( message + "\n" );
	}

	//---Aux Functions----//
	private void addNewClientToArrayList(NewsClient newsClient) {
		clientsArrayList.add( newsClient );
	}

	//---News Client class --- //
	private class NewsClient {
		private DatagramSocket socket;
		private DatagramPacket datagramPacket;
		private byte[] buffer;

		private NewsClient(DatagramSocket socket , DatagramPacket datagramPacket , byte[] buffer) {
			this.socket = socket;
			this.datagramPacket = datagramPacket;
			this.buffer = buffer;
		}

		private void receiveNews() {
			// Prepare Response
			buffer = incomingNewsString.getBytes( );

			//Send Response to "address" and "port"
			InetAddress address = datagramPacket.getAddress( );
			int port = datagramPacket.getPort( );
			datagramPacket = new DatagramPacket( buffer , buffer.length , address , port );
			try {
				socket.send( datagramPacket );
			} catch ( IOException e ) {
				writeToServerLog( "IO Exception in Client Thread" );
				e.printStackTrace( );
			}
		}

	}

	//---Incoming News Thread class --- //
	private class IncomingNewsThread extends Thread {

		public void run() {
			incomingNewsString = incomingNewsTextArea.getText( );
			appendNewLineToIncomingNewsString( );

			for ( NewsClient client : clientsArrayList ) {
				client.receiveNews( );
			}
			clearIncomingTextArea( );
		}

		private void appendNewLineToIncomingNewsString() {
			final int lastChar = incomingNewsString.length( ) - 1;
			final char enterChar = '\n';
			if( lastChar > 0 && incomingNewsString.charAt( lastChar ) != enterChar )
				incomingNewsString += enterChar;
		}
	}

	//---Key Stroke Listener Class----//
	private class KeyStrokeListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode( );
			if( key == KeyEvent.VK_ENTER ) {
				sendMessageToSubscribers( );
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {

		}
	}
}

