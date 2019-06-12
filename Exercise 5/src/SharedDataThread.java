
public class SharedDataThread implements Runnable {
	private SharedData sharedData;
	private boolean actingAsWriterThread;

	public SharedDataThread(SharedData sharedData) {
		this.sharedData = sharedData;
	}

	public static SharedDataThread newWriterThread(SharedData sharedData) {
		SharedDataThread sharedDataThread = new SharedDataThread( sharedData );
		sharedDataThread.setActingAsWriterThread( );
		return sharedDataThread;
	}

	public static SharedDataThread newReaderThread(SharedData sharedData) {
		SharedDataThread sharedDataThread = new SharedDataThread( sharedData );
		sharedDataThread.setActingAsReaderThread( );
		return sharedDataThread;
	}

	public void run() {
		final int PRODUCING_NUMBER = 10, CONSUMING_NUMBER = 10, MAX_VALUE = 50;

		if( actingAsWriterThread ) {
			for ( int i = 0 ; i < PRODUCING_NUMBER ; i++ ) {
				int x = ( int ) ( Math.random( ) * MAX_VALUE );
				int y = ( int ) ( Math.random( ) * MAX_VALUE );
				sharedData.move( x , y );
				System.out.println( getThreadName( ) + " is Moving to " + sharedData );
				sleep( );
			}
		} else {
			for ( int i = 0 ; i < CONSUMING_NUMBER ; i++ ) {
				System.out.println( getThreadName( ) + " is Getting " + sharedData.get( ) );
				sleep( );
			}
		}
	}

	private void setActingAsWriterThread() {
		this.actingAsWriterThread = true;
	}

	private void setActingAsReaderThread() {
		this.actingAsWriterThread = false;
	}

	private void sleep() {
		final int DELAY = 1000;
		try {
			Thread.sleep( DELAY );
		} catch ( InterruptedException e ) {

		}
	}

	private String getThreadName() {
		return "\nThread <-" + Thread.currentThread( ).getName( ).substring( 14 , 15 ) + "->";
	}

}
