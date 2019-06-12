import javax.swing.*;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatTester {

	public static void main(String... args) {
		final int CLIENTS_NUMBER = 2;
		ExecutorService executorService  = Executors.newCachedThreadPool();

		//Running Server
		executorService.execute(  new ChatServerWindowThread() );

		//Running Clients
		for(int i = 0 ; i < CLIENTS_NUMBER;  i++){
			executorService.execute(  new  ChatClientWindowThread() );
			sleepBetweenStartUps();
		}
		executorService.shutdown();
	}

	private static void sleepBetweenStartUps(){
		final int DELAY_BETWEEN_CLIENTS_STARTUP = 750;
		try{
			Thread.sleep( DELAY_BETWEEN_CLIENTS_STARTUP );
		}catch ( InterruptedException ignore){}
	}


	private static class ChatClientWindowThread extends Thread{
		@Override
		public void run(){
			new ChatClientWindow();
		}
	}

	private static class ChatServerWindowThread extends Thread{
		@Override
		public void run(){
			try {
				new ChatServerWindow( );
			}catch ( IOException e ){
				JOptionPane.showMessageDialog( null,"Could not set up server" );
				System.exit( 1 );
			}
		}
	}
}
