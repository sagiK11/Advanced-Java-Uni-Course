import java.util.ArrayList;

public class ThreadsArrayList {
	private ArrayList< NumberThread > threadsArrayList;
	private int threadsFinished;

	public ThreadsArrayList(int size) {
		final int DEFAULT_SIZE = 10;
		threadsArrayList = new ArrayList<>( size > 0 ? size : DEFAULT_SIZE );
		threadsFinished = 0;
	}

	public synchronized void finished() {
		++ threadsFinished;
		notifyAll( );
	}

	public synchronized void waitForAll() {
		while ( threadsFinished < threadsArrayList.size( ) )
			try {
				wait( );
			} catch ( InterruptedException e ) {

			}
	}

	public void resetThreads() {
		threadsFinished = 0;
		for ( NumberThread numberThread : threadsArrayList ) {
			numberThread.resetCheckedNeighbors( );
		}
	}

	public void add(NumberThread thread) {
		threadsArrayList.add( thread );
	}

	public NumberThread get(int index) {
		return threadsArrayList.get( index );
	}

	public int size() {
		return threadsArrayList.size( );
	}

	public NumberThread getFirst() {
		return threadsArrayList.get( 0 );
	}

	public NumberThread getLast() {
		return threadsArrayList.get( threadsArrayList.size( ) - 1 );
	}

	public String getCurrentState(int stateNum) {
		String initialState = "Initial State                   ";
		String currentState = "Test Number :  " + stateNum + "          ";
		StringBuilder res = new StringBuilder( stateNum == 0 ? initialState : currentState );

		for ( NumberThread thread : threadsArrayList )
			res.append( thread );

		return res.append( "\n" ).toString( );
	}


}
