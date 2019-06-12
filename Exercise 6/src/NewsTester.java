import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsTester {
	public static void main(String... args) {
		final int CLIENTS_NUMBER = 2;
		ExecutorService executorService = Executors.newCachedThreadPool( );

		//Running Server
		executorService.execute( new NewsServerWindowThread( ) );

		//Running Clients
		for ( int i = 0 ; i < CLIENTS_NUMBER ; i++ ) {
			executorService.execute( new NewsClientWindowThread( ) );
			sleepBetweenStartUps( );
		}
		executorService.shutdown( );
	}

	private static void sleepBetweenStartUps() {
		final int DELAY_BETWEEN_CLIENTS_STARTUP = 750;
		try {
			Thread.sleep( DELAY_BETWEEN_CLIENTS_STARTUP );
		} catch ( InterruptedException e ) {
		}
	}

	private static class NewsServerWindowThread extends Thread {
		public void run() {
			new NewsServerWindow( );
		}
	}

	private static class NewsClientWindowThread extends Thread {
		public void run() {
			new NewsClientWindow( );
		}
	}
}
