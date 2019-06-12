import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;

public class ChatServerWindow extends JFrame {
	private ServerSocket serverSocket;
	private int numOfConnectedClients;
	private ArrayList< ChatClientThread > clientsArrayList;
	private JTextArea logTextArea;
	private boolean listening, illegalName;

	public ChatServerWindow() throws IOException {
		super( "Server Frame" );
		setFrameProperties( );
		establishServerSocket( );
		createServerSocket( );
		listenToClients( );
		closeServerSocket( );
	}

	//---------Client Communication Functions---------//
	private void listenToClients() {
		while ( listening ) {
			try {
				Socket socket = serverSocket.accept( );

				InputStream inputStream = socket.getInputStream( );
				InputStreamReader inputStreamReader = new InputStreamReader( inputStream );
				BufferedReader reader = new BufferedReader( inputStreamReader );
				PrintWriter writer = new PrintWriter( socket.getOutputStream( ) , true );

				String clientName = reader.readLine( );
				if( illegalClientName( clientName , writer ) )
					continue;

				ChatClientThread chatClientThread = new ChatClientThread( clientName , socket );
				clientsArrayList.add( chatClientThread );

				sendMessageToClients( clientName + ":  has connected" );
				sendOnlineClientsToClients( );
				chatClientThread.start( );

				writeToServerLog( "Clients connected: " + ++ numOfConnectedClients );
			} catch ( IOException e ) {
				writeToServerLog( "Accept failed" );
				System.exit( 1 );
			}
		}
	}

	private boolean illegalClientName(String clientName , PrintWriter writer) {
		final String IllegalName = "Illegal Name";
		try {
			ClientNameTest clientNameTest = new ClientNameTest( clientName );
			clientNameTest.start( );
			clientNameTest.join( );
		} catch ( InterruptedException e ) {
		}

		if( illegalName )
			writer.println( IllegalName );
		return illegalName;

	}

	private void createServerSocket() {
		final int port = 7777;
		try {
			serverSocket = new ServerSocket( port );
		} catch ( IOException e ) {
			writeToServerLog( "Could not listen on port: " + port );
			System.exit( 1 );
		}
		writeToServerLog( "server ready" );

	}

	private void establishServerSocket() {
		clientsArrayList = new ArrayList<>( );
		numOfConnectedClients = 0;
		serverSocket = null;
		listening = true;
	}

	private void sendMessageToClients(String message) {

		for ( ChatClientThread currentClient : clientsArrayList ) {
			try {
				PrintWriter writer = currentClient.getPrintWriter( );
				writer.println( message );
				writer.flush( );
				writeToServerLog( "Sending: " + message );
			} catch ( Exception ex ) {
				writeToServerLog( "Error while sending message to clients." );
				ex.printStackTrace( );
			}
		}
	}

	private void sendOnlineClientsToClients() {
		for ( ChatClientThread currentClient : clientsArrayList ) {
			try {
				PrintWriter writer = currentClient.getPrintWriter( );
				writer.println( clientsArrayList );
				writer.flush( );
				writeToServerLog( "Sending online clients." );
			} catch ( Exception ex ) {
				writeToServerLog( "Error while sending online clients to clients." );
				ex.printStackTrace( );
			}
		}
	}

	private void closeServerSocket() throws IOException {
		serverSocket.close( );
	}

	private void removeClientFromClientsArrayList(String message) {
		int lastIndex = message.indexOf( ":" );
		String clientName = message.substring( 0 , lastIndex );

		for ( int i = 0 ; i < clientsArrayList.size( ) ; i++ ) {
			String currentClientName = clientsArrayList.get( i ).toString( );
			if( currentClientName.contains( clientName ) ) {
				writeToServerLog( "Removing " + currentClientName );
				numOfConnectedClients--;
				clientsArrayList.remove( i );
				return;
			}
		}

	}

	//---------User Interface Functions---------//
	private void setFrameProperties() {
		setSize( 400 , 400 );
		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		getRootPane( ).setBorder( BorderFactory.createTitledBorder( "Server Log" ) );

		logTextArea = new JTextArea( );
		logTextArea.setEditable( false );
		logTextArea.setFont( new Font( "Serif" , Font.PLAIN , 18 ) );

		JScrollPane scrollPane = new JScrollPane( logTextArea );
		add( scrollPane );
		setVisible( true );
	}

	private void writeToServerLog(String message) {
		logTextArea.append( message + "\n" );
	}

	//----------------ChatClient Class-----------------//
	private class ChatClientThread extends Thread {
		private String chatClientName;
		private PrintWriter client;
		private BufferedReader reader;

		private ChatClientThread(String clientName , Socket socket) {
			this.chatClientName = clientName.trim( );
			try {
				client = new PrintWriter( socket.getOutputStream( ) , true );
				reader = new BufferedReader( new InputStreamReader( socket.getInputStream( ) ) );
			} catch ( IOException e ) {
				writeToServerLog( "Couldn't open I/O on connection" );
			}
		}

		public void run() {
			String message;
			final String CLIENT_DISCONNECTED_KEY = ": disconnected";
			try {
				while ( ( message = reader.readLine( ) ) != null ) {

					if( message.contains( CLIENT_DISCONNECTED_KEY ) )
						removeClientFromClientsArrayList( message );
					else if( message.length( ) == 0 )
						continue;

					sendMessageToClients( message );
					sendOnlineClientsToClients( );
					writeToServerLog( "Received: " + message );
				}
				client.close( );
				reader.close( );
			} catch ( SocketException e ) {
				writeToServerLog( "violent disconnect" );
			} catch ( IOException e ) {
				writeToServerLog( "Couldn't read from connection" );
				e.printStackTrace( );
			}
		}

		public String toString() {
			return chatClientName;
		}

		private PrintWriter getPrintWriter() {
			return client;
		}
	}

	//--------------ClientNameTest Class-----------//
	private class ClientNameTest extends Thread {
		private String clientName;

		private ClientNameTest(String clientName) {
			this.clientName = clientName.trim( );
		}

		@Override
		public void run() {
			illegalName = false;
			for ( ChatClientThread chatClientThread : clientsArrayList ) {
				if( chatClientThread.toString( ).equals( clientName ) ) {
					JOptionPane.showMessageDialog( null , "Nickname already taken" );
					illegalName = true;
					writeToServerLog( "Nickname already taken" );
				}
			}
		}
	}
}
