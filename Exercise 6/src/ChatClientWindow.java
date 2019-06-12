import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;


public class ChatClientWindow extends JFrame {
	private JPanel mainPanel;
	private JTextArea messagesTextArea, typingTextArea, onlineUsersTextArea;
	private JTextField userNameTextField;
	private JButton connectButton, disconnectButton, newLineButton;
	private JLabel statusLabel;
	private Socket chatSocket;
	private PrintWriter writer;
	private BufferedReader reader;
	private String hostName;
	private boolean connectedToServer;

	public ChatClientWindow() {
		super( "Question 1" );
		setFrameProperties( );
		setVisible( true );
	}

	// -----Server Communication Functions------//
	private void connectToServer() {
		String host = JOptionPane.showInputDialog( null , "Enter Server Name" );
		if( host == null ) {//User Clicked Cancel
			switchToDisconnectedMode( );
			return;
		}
		try {
			chatSocket = new Socket( host , 7777 );
			writer = new PrintWriter( chatSocket.getOutputStream( ) , true );
			reader = new BufferedReader( new InputStreamReader( chatSocket.getInputStream( ) ) );
			notifyChatServerHostNameConnected( );
		} catch ( ConnectException e ) {
			JOptionPane.showMessageDialog( this , "Server is offline " );
			switchToDisconnectedMode( );
			return;
		} catch ( UnknownHostException e ) {
			JOptionPane.showMessageDialog( this , "Unknown host" );
			switchToDisconnectedMode( );
			return;
		} catch ( IOException e ) {
			JOptionPane.showMessageDialog( this , "Unknown error - try again" );
			switchToDisconnectedMode( );
			return;
		}
		switchToConnectedMode( );
		listenToServerThread( );
	}

	private void disconnectFromServer() {
		String disconnect = ( hostName + ": disconnected" );
		try {
			writer.println( disconnect );
			writer.flush( );
		} finally {
			switchToDisconnectedMode( );
		}
	}

	private void notifyChatServerHostNameConnected() {
		String userNameFromTextField = userNameTextField.getText( );
		hostName = userNameFromTextField.isEmpty( ) ? "Anonymous" : userNameFromTextField;
		userNameTextField.setEditable( false );
		writer.println( hostName );
		writer.flush( );
	}

	private void listenToServerThread() {
		Thread listeningThread = new Thread( new ListeningThread( ) );
		listeningThread.start( );
	}

	private class ListeningThread implements Runnable {
		@Override
		public void run() {
			listenToServer( );
		}
	}

	private void listenToServer() {
		try {
			String msgFromServer, IllegalNameMark = "Illegal Name", onlineUsersMark = "[";

			while ( ( msgFromServer = reader.readLine( ) ) != null ) {
				if( msgFromServer.equals( IllegalNameMark ) )
					disconnectFromServer( );
				else if( ! msgFromServer.contains( onlineUsersMark ) )
					updateMessagesInPanel( msgFromServer );
				else
					updateOnlineUsersInPanel( msgFromServer );
			}
		} catch ( SocketException e ) {
			disconnectFromServer( );
		} catch ( IOException e ) {
			System.out.println( "failed reading from server" );
			e.printStackTrace( );
			System.exit( 1 );
		}
	}

	private void sendMessage() {
		if( connectedToServer ) {
			String hostMessage = typingTextArea.getText( );
			writer.println( hostName + ": " + hostMessage );
			writer.flush( );
			clearTypingTextArea( );
		} else {
			JOptionPane.showMessageDialog( null , "You are not connected" );
		}
	}

	private void clearTypingTextArea() {
		typingTextArea.requestFocus( );
		typingTextArea.setText( "" );
	}

	private void switchToConnectedMode() {
		connectedToServer = true;
		connectButton.setEnabled( false );
		newLineButton.setEnabled( true );
		disconnectButton.setEnabled( true );
		typingTextArea.setEnabled( true );
		typingTextArea.requestFocus( );
		updateConnectionStatusInPanel( );
	}

	private void switchToDisconnectedMode() {
		connectedToServer = false;
		connectButton.setEnabled( true );
		connectButton.requestFocus( );
		disconnectButton.setEnabled( false );
		newLineButton.setEnabled( false );
		userNameTextField.setEditable( true );
		onlineUsersTextArea.setText( "" );
		typingTextArea.setEnabled( false );
		updateConnectionStatusInPanel( );
	}

	// ------User Interface Functions------//
	private void setFrameProperties() {
		setSize( 800 , 550 );
		setLocationRelativeTo( null );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		addLayoutsToWindow( );
		addFrameListener( );
	}

	private void addLayoutsToWindow() {
		mainPanel = new JPanel( );
		mainPanel.setLayout( new BoxLayout( mainPanel , BoxLayout.X_AXIS ) );
		mainPanel.setBorder( BorderFactory.createTitledBorder( "Chat Window" ) );

		addLeftLayout( );
		addRightLayout( );

		add( mainPanel );
	}

	private void addLeftLayout() {
		JPanel leftPanel = new JPanel( );
		leftPanel.setMaximumSize( new Dimension( 210 , getHeight( ) ) );
		leftPanel.setLayout( new BoxLayout( leftPanel , BoxLayout.Y_AXIS ) );

		addOnlineAndOfflinePanel( leftPanel );
		addOnlineUsersPanel( leftPanel );

		mainPanel.add( leftPanel );
	}

	private void addOnlineAndOfflinePanel(JPanel leftPanel) {
		JPanel onlineAndOfflinePanel = new JPanel( );
		onlineAndOfflinePanel.setLayout( new GridLayout( 2 , 2 , 3 , 10 ) );
		onlineAndOfflinePanel.setBorder( BorderFactory.createTitledBorder( "Actions" ) );
		onlineAndOfflinePanel.setMaximumSize( new Dimension( 210 , 150 ) );

		JLabel userNameLabel = new JLabel( "User name: " );
		userNameTextField = new JTextField( );
		userNameTextField.addKeyListener( new KeyStrokeListener( ) );

		connectButton = new JButton( "Connect" );
		connectButton.setEnabled( true );
		connectButton.addActionListener( e -> connectToServer( ) );

		disconnectButton = new JButton( "Disconnect" );
		disconnectButton.setEnabled( false );
		disconnectButton.addActionListener( e -> disconnectFromServer( ) );

		onlineAndOfflinePanel.add( userNameLabel );
		onlineAndOfflinePanel.add( userNameTextField );
		onlineAndOfflinePanel.add( connectButton );
		onlineAndOfflinePanel.add( disconnectButton );

		leftPanel.add( onlineAndOfflinePanel );
	}

	private void addOnlineUsersPanel(JPanel leftPanel) {
		JPanel onlineUsersPanel = new JPanel( );
		onlineUsersPanel.setLayout( new BoxLayout( onlineUsersPanel , BoxLayout.Y_AXIS ) );

		onlineUsersTextArea = new JTextArea( );
		onlineUsersTextArea.setEditable( false );
		onlineUsersTextArea.setFont( new Font( "Serif" , Font.BOLD , 18 ) );

		JScrollPane scrollPane = new JScrollPane( onlineUsersTextArea );
		scrollPane.setBorder( BorderFactory.createTitledBorder( "Active Users" ) );

		onlineUsersPanel.add( scrollPane );
		leftPanel.add( onlineUsersPanel );
	}

	private void addRightLayout() {
		JPanel rightPanel = new JPanel( );
		rightPanel.setMaximumSize( new Dimension( 590 , getHeight( ) ) );
		rightPanel.setLayout( new BoxLayout( rightPanel , BoxLayout.Y_AXIS ) );

		addTitleAndStatus( rightPanel );
		addMessagesTextArea( rightPanel );
		addTypingTextArea( rightPanel );

		mainPanel.add( rightPanel );
	}

	private void addTypingTextArea(JPanel rightPanel) {
		JPanel typingPanel = new JPanel( );
		String typingTextAreaTitle = "Type Your Message - press Enter To Send";
		Dimension typingPanelDim = new Dimension( rightPanel.getWidth( ) , 85 );
		Font typingPanelFont = new Font( "serif" , Font.PLAIN , 18 );
		typingTextArea = new JTextArea( );
		JScrollPane typingScrollPane = new JScrollPane( typingTextArea );
		newLineButton = new JButton( "New Line" );

		newLineButton.setMargin( new Insets( 25 , 10 , 25 , 10 ) );
		newLineButton.setFont( typingPanelFont );
		newLineButton.addActionListener( e -> addNewLineToTypingTextArea( ) );
		newLineButton.setEnabled( false );

		typingPanel.setBorder( BorderFactory.createTitledBorder( typingTextAreaTitle ) );
		typingPanel.setLayout( new BoxLayout( typingPanel , BoxLayout.X_AXIS ) );
		typingPanel.setMinimumSize( typingPanelDim );
		typingPanel.setPreferredSize( typingPanelDim );

		typingTextArea.addKeyListener( new KeyStrokeListener( ) );
		typingTextArea.setFont( typingPanelFont );
		typingTextArea.setEnabled( false );

		typingPanel.add( typingScrollPane );
		typingPanel.add( newLineButton );
		rightPanel.add( typingPanel );
	}

	private void addMessagesTextArea(JPanel rightPanel) {
		JPanel msgPanel = new JPanel( );
		Dimension msgPanelDim = new Dimension( rightPanel.getWidth( ) , 300 );
		Font msgPanelFont = new Font( "serif" , Font.PLAIN , 18 );
		messagesTextArea = new JTextArea( );
		JScrollPane msgScrollPane = new JScrollPane( messagesTextArea );

		messagesTextArea.setFont( msgPanelFont );
		messagesTextArea.setEditable( false );

		msgPanel.setLayout( new BoxLayout( msgPanel , BoxLayout.X_AXIS ) );
		msgPanel.setBorder( BorderFactory.createTitledBorder( "Messages" ) );
		msgPanel.setMinimumSize( msgPanelDim );
		msgPanel.setPreferredSize( msgPanelDim );
		msgPanel.add( msgScrollPane );

		rightPanel.add( msgPanel );
	}

	private void addTitleAndStatus(JPanel rightPanel) {
		JPanel titleAndStatusPanel = new JPanel( );

		JLabel titleLabel = new JLabel( "                                      Chat App                            " );
		titleLabel.setFont( new Font( "serif" , Font.BOLD | Font.ITALIC , 24 ) );

		statusLabel = new JLabel( "offline" );
		statusLabel.setFont( new Font( "serif" , Font.BOLD | Font.ITALIC , 16 ) );
		updateConnectionStatusInPanel( );

		titleAndStatusPanel.add( titleLabel );
		titleAndStatusPanel.add( statusLabel );
		rightPanel.add( titleAndStatusPanel );
	}

	private void addNewLineToTypingTextArea() {
		typingTextArea.append( "\n" );
		typingTextArea.requestFocus( );
	}

	private void updateMessagesInPanel(String msgFromServer) {
		messagesTextArea.append( msgFromServer + "\n" );
	}

	private void updateOnlineUsersInPanel(String msgFromServer) {
		String[] onlineUsersArray = parseOnlineUsersFromServer( msgFromServer );
		for ( String onlineUser : onlineUsersArray )
			onlineUsersTextArea.append( " " + onlineUser.trim( ) + "\n" );
	}

	private void updateConnectionStatusInPanel() {
		if( connectedToServer ) {
			statusLabel.setText( "Online" );
			statusLabel.setForeground( new Color( 16 , 188 , 82 ) ); // green
		} else {
			statusLabel.setText( "Offline" );
			statusLabel.setForeground( Color.RED );
		}
	}

	private String[] parseOnlineUsersFromServer(String msgFromServer) {
		onlineUsersTextArea.setText( "" );// clearing the current people in the panel.
		msgFromServer = msgFromServer.substring( 1 ); // removing '['
		msgFromServer = msgFromServer.substring( 0 , msgFromServer.length( ) - 1 ); // removing ']'
		String[] onlineUsersArray = msgFromServer.split( "," );
		return onlineUsersArray;
	}

	private void addFrameListener() {
		addWindowListener( new WindowAdapter( ) {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				if( connectedToServer )
					disconnectFromServer( );
				try {
					if( chatSocket != null )
						chatSocket.close( );
				} catch ( IOException e ) {
				}
			}
		} );
	}

	//------ Key Stroke Listener Class--------//
	private class KeyStrokeListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {

		}

		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode( );
			Object source = e.getSource( );
			boolean enterKeyPressed = ( key == KeyEvent.VK_ENTER );

			if( source == typingTextArea && enterKeyPressed ) {
				if( typingTextArea.getText( ).trim( ).length( ) == 0 ) // Empty Messages case
					clearTypingTextArea( );
				else
					new MessageSenderThread( ).start( );
			} else if( source == userNameTextField && enterKeyPressed ) {
				connectToServer( );
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {

		}
	}

	//------ Message Sender Thread Class--------//
	private class MessageSenderThread extends Thread {
		public void run() {
			sendMessage( );
		}
	}

}
