import java.util.Random;

public class SortedGroupApp {
	private final int MAX_SIZE = 50;
	private SortedGroup< Person > personSortedGroup = new SortedGroup<>( );
	private Random random = new Random( );
	private final int randomSize = random.nextInt( MAX_SIZE ) + 1;
	private Person[] personArray = new Person[ randomSize ];
	private int printsCnt;

	public void runSortedGroupApp() {

		addToPersonSortedGroup( );

		printPersonSortedGroup( );

		printPersonsAboveMinHeight( );

		printPersonSortedGroup( );

		removePersonsInPersonSortedGroup( );

		validateRemovalFromPersonSortedGroup( );
	}

	private void validateRemovalFromPersonSortedGroup() {
		System.out.println( "v-----printing list - needs to be empty-------v" );
		personSortedGroup.printSortedGroup( );
		System.out.println( "^-----finished printing list-------^" );
	}

	private void removePersonsInPersonSortedGroup() {
		System.out.println( "\n<-----removing all people from the list------->" );
		for ( Person person : personArray )
			personSortedGroup.remove( person );
	}

	private void printPersonsAboveMinHeight() {
		final int MIN_HEIGHT = 175;
		;
		Person minHeightPerson = new Person( 1 , "175" , MIN_HEIGHT );
		SortedGroup< Person > aboveMinHeightPersonArrayList;
		aboveMinHeightPersonArrayList = ReduceMethod.reduce( personSortedGroup , minHeightPerson );

		System.out.println( "v-----printing people above " + MIN_HEIGHT + " cm started-------v" );
		aboveMinHeightPersonArrayList.printSortedGroup( );
		System.out.println( "^-----printing people above " + MIN_HEIGHT + " cm finished-------^" );
	}

	private void printPersonSortedGroup() {
		System.out.println( "v-----printing all people in the list #" + printsCnt + " started-------v" );
		personSortedGroup.printSortedGroup( );
		System.out.println( "^-----printing all people in the list #" + printsCnt + " finished-------^" );
		++ printsCnt;
	}

	private void addToPersonSortedGroup() {
		final int MAX_HEIGHT = 210;
		for ( int i = 0 ; i < personArray.length ; i++ ) {
			Person person = new Person( i , String.valueOf( i ) , random.nextInt( MAX_HEIGHT ) );
			personArray[ i ] = person;
			personSortedGroup.add( person );
		}
	}
}
