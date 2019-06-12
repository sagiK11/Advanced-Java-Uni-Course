import java.util.ArrayList;
import java.util.Iterator;

public class SortedGroup< E extends Comparable > {
	private ArrayList< E > sortedGroupArrayList = new ArrayList< E >( );

	public void add(E obj) {

		if( obj == null )
			return;

		if( sortedGroupArrayList.isEmpty( ) )
			sortedGroupArrayList.add( obj );
		else {
			int position = getOrderStatistic( obj );
			sortedGroupArrayList.add( position , obj );
		}
	}

	public int remove(E obj) {
		int numberOfObjRemoved = 0;

		if( sortedGroupArrayList.isEmpty( ) || obj == null )
			return 0;
		else {
			//removing  matching objects
			for ( int i = 0 ; i < sortedGroupArrayList.size( ) ; i++ ) {
				if( obj.equals( sortedGroupArrayList.get( i ) ) ) {
					numberOfObjRemoved++;
					sortedGroupArrayList.remove( i-- );
				}
			}
			return numberOfObjRemoved;
		}
	}

	private < E extends Comparable > int getOrderStatistic(E obj) {
		int left = 0, right = sortedGroupArrayList.size( ) - 1, mid = 0, comparisonResult = 0;

		while ( left <= right ) {

			mid = ( left + right ) / 2;
			comparisonResult = obj.compareTo( sortedGroupArrayList.get( mid ) );

			if( comparisonResult > 0 && mid + 1 < sortedGroupArrayList.size( ) &&
				( obj.compareTo( sortedGroupArrayList.get( mid + 1 ) ) <= 0 ) ) // obj in neither max or min.
				return mid + 1;
			else if( comparisonResult > 0 && mid == sortedGroupArrayList.size( ) - 1 ) // obj is max
				return mid + 1;
			else if( comparisonResult <= 0 && mid == 0 ) // obj is min
				return mid;

			if( comparisonResult > 0 )
				left = mid + 1;
			else
				right = mid - 1;
		}
		return - 1;
	}

	public Iterator< E > iterator() {
		return sortedGroupArrayList.iterator( );
	}


	public void printSortedGroup() {
		for ( Object o : sortedGroupArrayList )
			System.out.println( o );
	}

}
