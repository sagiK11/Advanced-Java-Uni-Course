

public class Main {

	public static void main(String[] args) {
		questionOne( ); // synchronization  with shared data.
		questionTwo( ); // different types of safe synchronization with shared data.
	}

	private static void questionOne() {
		new ThreadArrayListApp( );
	}

	private static void questionTwo() {
		new SharedDataApp( );
	}
}
