import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class SafeSharedData extends SharedData {
	private final ReadWriteLock lock = new ReentrantReadWriteLock( );

	public SafeSharedData(int x , int y) {
		super( x , y );
	}


	public void move(int dx , int dy) {
		final Lock exclusiveLock = lock.writeLock( );
		exclusiveLock.lock( ); // only one thread can write
		try {
			x += dx;
			y += dy;
		} finally {
			exclusiveLock.unlock( );
		}
	}

	public SafeSharedData get() {
		final Lock sharedLock = lock.readLock( );
		sharedLock.lock( ); // many threads can read
		try {
			return new SafeSharedData( x , y );
		} finally {
			sharedLock.unlock( );
		}
	}

	public String toString() {
		return "(" + x + "," + y + ")";
	}
}
