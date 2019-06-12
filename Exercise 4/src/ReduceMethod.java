import java.util.Iterator;

public class ReduceMethod {


	static < T extends Comparable< T > > SortedGroup< T > reduce(SortedGroup< T > sGroup , T obj) {
		SortedGroup< T > resSortedGroup = new SortedGroup<>( );

		for ( Iterator< T > it = sGroup.iterator( ) ; it.hasNext( ) ; ) {
			T tmp = it.next( );
			if( tmp.compareTo( obj ) > 0 )
				resSortedGroup.add( tmp );
		}
		return resSortedGroup;
	}
}
