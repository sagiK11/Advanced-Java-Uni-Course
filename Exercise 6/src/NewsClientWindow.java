
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;
import java.util.Date;

public class NewsClientWindow extends JFrame {
	private JButton connectButton, disconnectButton, clearButton;
	private JTextArea newsTextArea;
	private DatagramSocket datagramSocket;
	private DatagramPacket datagramPacket;
	private boolean listening, socketIsClosed;
	private byte[] buffer;

	public NewsClientWindow() {
		super( "Question 2" );
		setFrameProperties( );
		createDatagramSocket( );
	}

	//--Server Communication Functions---//
	private void createDatagramSocket() {
		try {
			datagramSocket = new DatagramSocket( );
		} catch ( SocketException e ) {
			System.out.println( "Socket Exception" );
			e.printStackTrace( );
		}
	}

	private void connectToServer() {
		final int PORT = 7777, BUFFER_SIZE = 256;
		;
		if( socketIsClosed )
			createDatagramSocket( );

		String host = JOptionPane.showInputDialog( null , "Enter Server Name" );
		if( host == null ) { // User Clicked Cancel Button
			switchToDisconnectedMode( );
			return;
		}
		try {
			//Prepare Request
			buffer = new byte[ BUFFER_SIZE ];
			InetAddress address = InetAddress.getByName( host );

			//Send Request
			datagramPacket = new DatagramPacket( buffer , BUFFER_SIZE , address , PORT );
			datagramSocket.send( datagramPacket );

		} catch ( UnknownHostException | SocketException e ) {
			JOptionPane.showMessageDialog( null , "Unknown Server Name" );
			switchToDisconnectedMode( );
		} catch ( IOException e ) {
			e.printStackTrace( );
		}
		new ServerListener( ).start( );
	}

	private void switchToConnectedMode() {
		listening = true;
		connectButton.setEnabled( false );
		disconnectButton.setEnabled( true );
		newsTextArea.requestFocus( );
		connectToServer( );
		if( listening )
			writeToNewsTextArea( "Connected\n********" );
	}

	private void switchToDisconnectedMode() {
		listening = false;
		clearButton.requestFocus( );
		connectButton.setEnabled( true );
		disconnectButton.setEnabled( false );
		datagramSocket.close( );
		socketIsClosed = true;
		writeToNewsTextArea( "********\nDisconnected" );
	}

	private void listenToServer() {

		while ( listening ) {
			try {
				//get response
				datagramPacket = new DatagramPacket( buffer , buffer.length );
				datagramSocket.receive( datagramPacket );

				//display response
				buffer = datagramPacket.getData( );
				int packetLength = datagramPacket.getLength( );

				Date date = new Date( );
				String dateString = date.toString( ).substring( 0 , 19 );

				String received = ( new String( buffer ) ).substring( 0 , packetLength );
				writeToNewsTextArea( dateString + ": \n" + received + "-------------------" );

			} catch ( SocketException e ) {
				socketIsClosed = true;
			} catch ( IOException e ) {
				e.printStackTrace( );
			}
		}

	}

	private void writeToNewsTextArea(String news) {
		newsTextArea.append( news + "\n" );
	}

	private class ServerListener extends Thread {
		public void run() {
			listenToServer( );
		}
	}

	//-----User Interface Functions-----//
	private void setFrameProperties() {
		getRootPane( ).setBorder( BorderFactory.createEmptyBorder( 10 , 10 , 10 , 10 ) );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setLayout( new BorderLayout( 5 , 5 ) );
		setLocationRelativeTo( null );
		setResizable( false );
		setSize( 450 , 530 );

		addNorthPanel( );
		addCenterPanel( );
		setVisible( true );
	}

	private void addNorthPanel() {
		JPanel northPanel = new JPanel( );
		northPanel.setBorder( BorderFactory.createTitledBorder( "Actions" ) );
		northPanel.setLayout( new GridLayout( 1 , 0 , 10 , 10 ) );
		connectButton = new JButton( "Connect" );
		connectButton.addActionListener( e -> switchToConnectedMode( ) );

		disconnectButton = new JButton( "Disconnect" );
		disconnectButton.setEnabled( false );
		disconnectButton.addActionListener( e -> switchToDisconnectedMode( ) );

		clearButton = new JButton( "Clear " );
		clearButton.addActionListener( e -> clearTextArea( ) );

		northPanel.add( connectButton );
		northPanel.add( disconnectButton );
		northPanel.add( clearButton );
		add( northPanel , BorderLayout.NORTH );
	}

	private void addCenterPanel() {
		JPanel centerPanel = new JPanel( );
		centerPanel.setBorder( BorderFactory.createTitledBorder( "News" ) );
		centerPanel.setLayout( new BorderLayout( ) );

		newsTextArea = new JTextArea( );
		newsTextArea.setEditable( false );
		newsTextArea.setFont( new Font( "Serif" , Font.PLAIN , 18 ) );

		JScrollPane scrollPane = new JScrollPane( newsTextArea );

		centerPanel.add( scrollPane , BorderLayout.CENTER );
		add( centerPanel , BorderLayout.CENTER );
	}

	private void clearTextArea() {
		newsTextArea.setText( "" );
	}
}
