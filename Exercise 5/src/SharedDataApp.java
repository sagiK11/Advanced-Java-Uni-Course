import javax.swing.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SharedDataApp extends JFrame {

	public SharedDataApp() {
		Scanner scanner = new Scanner( System.in );
		boolean exit = false;
		do {
			promptMenuForUser( );
			String clauseToTest = scanner.nextLine( );

			switch ( clauseToTest ) {
				case "a":
					clauseA( );
					break;
				case "b":
					clauseB( );
					break;
				case "c":
					clauseC( );
					break;
				default:
					exit = true;
			}
		} while ( ! exit );
	}

	private void promptMenuForUser() {
		System.out.println( "For testing Clause A enter a" );
		System.out.println( "For testing Clause B enter b" );
		System.out.println( "For testing Clause C enter c" );
		System.out.println( "For exiting enter any other key" );
	}


	private void clauseA() {
		ExecutorService executorService = Executors.newCachedThreadPool( );
		SharedData sharedData = new SharedData( 0 , 0 );

		SharedDataThread writeSharedDataThread = SharedDataThread.newWriterThread( sharedData );
		SharedDataThread readSharedDataThread = SharedDataThread.newReaderThread( sharedData );

		System.out.println( "<--------Clause A begins-------->" );
		executorService.execute( writeSharedDataThread );
		executorService.execute( readSharedDataThread );

		shutDownThreads( executorService , "Clause A" );
	}

	private void clauseB() {
		ExecutorService executorService = Executors.newCachedThreadPool( );
		SafeSyncSharedData sharedData = new SafeSyncSharedData( 0 , 0 );

		SharedDataThread writeSharedDataThread = SharedDataThread.newWriterThread( sharedData );
		SharedDataThread readSharedDataThread = SharedDataThread.newReaderThread( sharedData );

		System.out.println( "<--------Clause B begins-------->" );
		executorService.execute( writeSharedDataThread );
		executorService.execute( readSharedDataThread );


		shutDownThreads( executorService , "Clause B" );

	}

	private void clauseC() {
		ExecutorService executorService = Executors.newCachedThreadPool( );
		SafeSharedData sharedData = new SafeSharedData( 0 , 0 );

		SharedDataThread writeSharedDateThread1 = SharedDataThread.newWriterThread( sharedData );
		SharedDataThread writeSharedDataThread2 = SharedDataThread.newWriterThread( sharedData );
		SharedDataThread writeSharedDataThread3 = SharedDataThread.newWriterThread( sharedData );
		SharedDataThread writeSharedDataThread4 = SharedDataThread.newWriterThread( sharedData );

		SharedDataThread readSharedDataThread1 = SharedDataThread.newReaderThread( sharedData );
		SharedDataThread readSharedDataThread2 = SharedDataThread.newReaderThread( sharedData );
		SharedDataThread readSharedDataThread3 = SharedDataThread.newReaderThread( sharedData );
		SharedDataThread readSharedDataThread4 = SharedDataThread.newReaderThread( sharedData );

		System.out.println( "<--------Clause C begins-------->" );
		executorService.execute( writeSharedDateThread1 );
		executorService.execute( writeSharedDataThread2 );
		executorService.execute( writeSharedDataThread3 );
		executorService.execute( writeSharedDataThread4 );

		executorService.execute( readSharedDataThread1 );
		executorService.execute( readSharedDataThread2 );
		executorService.execute( readSharedDataThread3 );
		executorService.execute( readSharedDataThread4 );

		shutDownThreads( executorService , "Clause C" );
	}

	private void shutDownThreads(ExecutorService executorService , String clause) {
		executorService.shutdown( );
		try {
			boolean threadsFinished = executorService.awaitTermination( 1 , TimeUnit.MINUTES );
			System.out.println( threadsFinished ? "<--------" + clause + " finished-------->" : "time elapsed" );
		} catch ( InterruptedException e ) {

		}
	}
}
