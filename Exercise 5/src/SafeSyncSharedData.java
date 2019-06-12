public class SafeSyncSharedData extends SharedData {

	private boolean toMove;

	public SafeSyncSharedData(int x , int y) {
		super( x , y );
		toMove = false;
	}

	public synchronized SafeSyncSharedData get() {
		while ( toMove ) {
			try {
				wait( );
			} catch ( InterruptedException e ) {

			}
		}
		toMove = true;
		notifyAll( );
		return new SafeSyncSharedData( x , y );
	}

	public synchronized void move(int dx , int dy) {
		while ( ! toMove ) {
			try {
				wait( );
			} catch ( InterruptedException e ) {
				e.printStackTrace( );
			}
		}
		x += dx;
		y += dy;
		toMove = false;
		notifyAll( );
	}

	public String toString() {
		return "(" + x + "," + y + ")";
	}

}


