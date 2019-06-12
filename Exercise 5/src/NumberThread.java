
import java.util.Random;

public class NumberThread extends Thread {

	private int myNumber, myIndexInArrayList;
	private ThreadsArrayList threadsArrayList;
	private NumberThread myLeft, myRight;
	private boolean rightCheckedMe, leftCheckedMe;

	public NumberThread(ThreadsArrayList threadsArrayList , int indexInArrayList) {
		this.myNumber = getRandomNumberBetweenOneToBoundary( );
		this.threadsArrayList = threadsArrayList;
		this.myIndexInArrayList = indexInArrayList;
		resetCheckedNeighbors( );
	}

	public void run() {
		findMyNeighbors( );
		changeMyNumber( );
	}

	private synchronized void changeMyNumber() {

		int myLeftNum = myLeft.getNum( );
		int myRightNum = myRight.getNum( );
		myLeft.rightCheckedMe = true;
		myRight.leftCheckedMe = true;

		while ( ! rightCheckedMe || ! leftCheckedMe ) {
			waitForRightAndLeftToCheckMe( );
		}

		if( myLeftNum > myNumber && myNumber < myRightNum )
			++ myNumber;
		else if( myLeftNum < myNumber && myNumber > myRightNum )
			-- myNumber;

		notifyAll( );
		threadsArrayList.finished( );
	}

	private void waitForRightAndLeftToCheckMe() {
		final int WAITING_TIME = 1200;
		;
		try {
			wait( WAITING_TIME );
		} catch ( InterruptedException e ) {

		}
	}

	public void findMyNeighbors() {
		myLeft = myIndexInArrayList > 0 ?
			threadsArrayList.get( myIndexInArrayList - 1 ) : threadsArrayList.getLast( );

		myRight = myIndexInArrayList < threadsArrayList.size( ) - 1 ?
			threadsArrayList.get( myIndexInArrayList + 1 ) : threadsArrayList.getFirst( );
	}

	private int getRandomNumberBetweenOneToBoundary() {
		final int BOUNDARY = 30;
		return new Random( ).nextInt( BOUNDARY ) + 1;
	}

	public void resetCheckedNeighbors() {
		rightCheckedMe = leftCheckedMe = false;
	}

	public int getNum() {
		return myNumber;
	}

	public String toString() {
		return " " + myNumber + " ";
	}

}
