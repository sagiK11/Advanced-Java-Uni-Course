
import java.util.ArrayList;

public class BigInt implements Comparable{
	//Class Fields
	private ArrayList<Integer> numberAsArrayList;
	private String numberAsString;
	//Class Constants`
	private final int BASE = 10,  EQUAL = 0,  THIS_IS_BIGGER = 1 , OTHER_IS_BIGGER = -1;
	private final char POSITIVE = '+', NEGATIVE = '-';
	enum Options {
		BOTH_POSITIVE("PP"), THIS_POS_OTHER_NEG("PN") ,
		THIS_NEG_OTHER_POS("NP"), BOTH_NEGATIVE("NN");
		private String option;
		Options(String option){
			this.option = option;
		}
		public String toString(){
			return option;
		}
	}

	public BigInt(String num){
		this.numberAsString = num;
		verifyNumberLegit(num);
		this.numberAsArrayList = new ArrayList<>( num.length());
		fillNumberArrayList(num);
	}

	private void fillNumberArrayList(String num) {
		int i = 0;

		if( num.charAt( i ) == NEGATIVE)
			this.numberAsArrayList.add( Character.getNumericValue( num.charAt(++i)) * (-1) ); // flipping the sign
		else if( num.charAt( i ) == POSITIVE)
			this.numberAsArrayList.add( Character.getNumericValue( num.charAt( ++i )) ); // skipping the '+'

		for( ; i < num.length(); i++){ // filling the arrayList
			this.numberAsArrayList.add( Character.getNumericValue( num.charAt( i )) );
		}
	}

	private void verifyNumberLegit(String num) {
		for(int i = 0; i < num.length(); i++){ // illegal number verifying
			char ch = num.charAt( i );
			if( i > 0 && ! (Character.isDigit( ch ) ) )
				throw new IllegalArgumentException("illegal number: " + num);
			else if(  ch != POSITIVE && ch != NEGATIVE && ! (Character.isDigit( ch ) ) )
				throw new IllegalArgumentException("illegal number: " + num);
		}
	}

	public BigInt(BigInt other){
		this( other.numberAsString );
	}

	/**Returns the sum of this BigInt and the passed BigInt */
	public BigInt plus( BigInt other ){
		int thisSize = getSize() -1, otherSize = other.getSize() -1, sum = 0 ;
		boolean hasCarry  = false;
		StringBuilder res = new StringBuilder();
		String rel = getRelationBetweenTheInts( other );

		if( oppositeSign( rel )  ) // checks for unequal sign
			return oppositeSigns( other , rel ); // changing signs for doing the right math.

		for(; thisSize >= 0 || otherSize >= 0 ; thisSize--, otherSize--){

			if(thisSize >= 0 && otherSize >= 0 ) { // adding right's most digits in both numbers
				sum =Math.abs( this.getIndex( thisSize )) + Math.abs(other.getIndex( otherSize ));
			}else if( thisSize >= 0 )// adding right's most digits in this number only
				sum = Math.abs(  this.getIndex( thisSize ) );
			else // adding right's most digits in other number only
				sum =  Math.abs( other.getIndex( otherSize ) );

			sum += hasCarry ? 1 : 0;  // if in the last iteration we had carry, we need to consider it now
			hasCarry =  sum / BASE != 0; // hasCarry is for the next iteration
			res.append( hasCarry ? sum % BASE : sum); // adding the result to the string.
		}
		res.append(  hasCarry ? "1" : "" ); // edge case where both numbers has  same length and we have carry

		if( rel.equals( Options.BOTH_NEGATIVE.toString()  ) )
			return new BigInt( res.append( NEGATIVE).reverse().toString());
		return new BigInt( res.reverse().toString() );
	}

	/* Returns the difference of this BigInt and the passed BigInt */
	public BigInt minus(BigInt other){
		if( compareTo( other ) == EQUAL)
			return new BigInt( "0" );

		String rel = getRelationBetweenTheInts( other );
		if(rel.equals( Options.BOTH_POSITIVE.toString() )){
			if( other.compareTo( this ) > 0)
				return  makeNegative( other.minus( this,true ) ); 
			else
				return this.minus(other,true );
		}else if( rel.equals(Options.THIS_POS_OTHER_NEG.toString())){
			return this.plus( makePositive( other ) );
		}else if( rel.equals( Options.THIS_NEG_OTHER_POS.toString() )){
			return makeNegative( makePositive( this ).plus( other ) );
		}else{
			if(this.compareTo( other ) > 0)
				return makePositive( other ).minus( makePositive(  this ), false );
			else
				return makeNegative( makePositive( this ).minus( makePositive( other ), true ) );
		}
	}
	/* Returns the final calculation of the difference between this object and the one passed as argument*/
	private BigInt minus(BigInt other, boolean thisIsBigger) {
		int thisSize = getSize() -1, otherSize = other.getSize() -1, diff = 0 ;
		boolean borrowed  = false;
		StringBuilder res = new StringBuilder();

		for(; thisSize >= 0 || otherSize >= 0 || borrowed; thisSize--, otherSize--){
			if(thisSize >= 0 && otherSize >= 0   ) { // subtracting right's most digits in both numbers;
				int thisCurr = getIndex( thisSize ), otherCurr =  other.getIndex( otherSize );
				diff  = thisIsBigger ? thisCurr - otherCurr : otherCurr + thisCurr ;
			}else if( thisSize >= 0 )// subtracting right's most digits in this number only
				diff =  this.getIndex( thisSize );
			else // subtracting right's most digits in other number only
				diff = other.getIndex( otherSize );

			diff -= borrowed ? 1 : 0;  // if in the last iteration we borrowed, we need to consider it now
			borrowed = diff < 0; // borrowed is for the next iteration
			res.append( borrowed ? diff + BASE : diff ); // adding the result to the string.
		}
		res = thisIsBigger ? res.reverse() : res.append( NEGATIVE ).reverse();
		return new BigInt( deleteLeadingZeros( res ).toString() );
	}

	/* Returns the product of this BigInt and the passed BigInt */
	public BigInt multiply(BigInt other){
		int thisSize = getSize() , otherSize = other.getSize() , k = 0 ;
		boolean thisIsNegative = this.numberAsString.indexOf( NEGATIVE ) != -1;
		boolean otherIsNegative = other.numberAsString.indexOf( NEGATIVE ) != -1;;
		int[] thisBigIntAsArray = new int[thisSize],  otherBigIntAsArray = new int[otherSize] , prod = new int[thisSize + otherSize];
		fillArrays( thisBigIntAsArray ,otherBigIntAsArray , other); // filling arrays with appropriate numbers

		for(int j = 0 ; j < otherSize ; j++){
			int carry = 0;
			for(int i = 0 ; i < thisSize; i++){
				prod[ i + j] += carry + ( thisBigIntAsArray[i] * otherBigIntAsArray[j] ) ;
				carry = prod[ i +j ] / BASE;
				prod[ i + j] = prod[ i + j ] % BASE;
			}
			prod[ j + thisSize] += carry;
		}
		for( k =prod.length-1 ; k >= 0 && prod[ k ] == 0 ; k--); // getting rid of leading zeros
		if( k < 0) 	return new BigInt( "0" ); // condition is true if and only if the int is zero

		// changing the sign if needs to
		if( (thisIsNegative && !otherIsNegative) || (! thisIsNegative && otherIsNegative))
			prod[ k ] *= (-1);

		StringBuilder res = new StringBuilder( "");
		for( ; k >= 0 ; k--)
			res.append(  prod[ k ] );
		return new BigInt( res.toString() );

	}

	/* Returns the quotient of this BigInt and the passed BigInt */
	public BigInt divide(BigInt other){
		if( isZero( other )) throw new ArithmeticException( "division by 0" );

		String relation = getRelationBetweenTheInts( other );
		// this block purpose - > making the division by both positive BigInt objects
		if( relation.equals( Options.BOTH_NEGATIVE.toString() ))
			return makePositive( this ).divide( makePositive( other ) );
		else if( relation.equals( Options.THIS_POS_OTHER_NEG .toString()))
			return makeNegative( this.divide( makePositive( other ) ) );
		else if( relation.equals( Options.THIS_NEG_OTHER_POS .toString()))
			return makeNegative(  makePositive( this ).divide( other ) );
		else
			return this.dividePositive( other ); // this is the actual division method
	}
	/*Returns the result of dividing this object and the passed object; where both are positive*/
	private BigInt dividePositive( BigInt other ) {
		int compareResult = this.compareTo( other );
		BigInt quotient  = new BigInt("0"),  remainder  = new BigInt(this), one = new BigInt("1");

		while( compareResult >= 0 ){
			quotient  = quotient.plus( one );
			remainder   = remainder.minus( other );
			compareResult = remainder.compareTo( other );
		}
		return quotient;
	}

	@Override
	public boolean equals(Object obj){
		return obj instanceof BigInt  && this.compareTo( obj ) == EQUAL;
	}

	@Override
	public int compareTo(Object obj)  {
		BigInt big = (BigInt) obj;

		if( this.getSign() == big.getSign() ) {
			return equalSign( big, this.getSign() ) ;
		}else if( this.getSign() == POSITIVE && big.getSign() == NEGATIVE) {
			return THIS_IS_BIGGER;
		}else // negative and other is positive
			return OTHER_IS_BIGGER;
	}

	public String toString(){
		return this.numberAsString;
	}

	//PRIVATE UTILITIES METHODS
	/** Checks which number is bigger, where both has equal sign **/
	private int equalSign(BigInt  big, char sign ) {
		if(this.getSize() > big.getSize())
			return sign == POSITIVE ? THIS_IS_BIGGER : OTHER_IS_BIGGER;
		else if( this.getSize() < big.getSize())
			return sign == POSITIVE ? OTHER_IS_BIGGER : THIS_IS_BIGGER;
		else{ // equal number of digits
			for(int i = 0; i <getSize(); i++){
				if(this.getIndex(i) > big.getIndex(i))
					return sign == POSITIVE ? THIS_IS_BIGGER : OTHER_IS_BIGGER;
				else if( this.getIndex(i) < big.getIndex(i))
					return sign == POSITIVE ? OTHER_IS_BIGGER : THIS_IS_BIGGER;
			}
			return EQUAL;
		}
	}

	/* Returns the number of digits in this object*/
	private int getSize(){
		return this.numberAsArrayList.size();
	}

	/* Returns the integer in the pass index*/
	private int getIndex(int index) throws NullPointerException{
		return this.numberAsArrayList.get( index );
	}

	/* Returns the sign of this object*/
	private char getSign() {
		char res = '0';

		try {
			res =  this.numberAsArrayList.get( 0 ) < 0 ? NEGATIVE : POSITIVE;
		}catch ( IndexOutOfBoundsException e ){
			System.out.println( "empty bigInt - try again" );
			System.exit( 0  );
		}
		return res;
	}

	/* Returns new and positive Copy of the pass object */
	private BigInt makePositive(BigInt bigint){
		if(bigint.numberAsString.charAt( 0 ) != NEGATIVE)
			return  bigint;
		return new BigInt(bigint.numberAsString.substring( 1 ));
	}

	/* Returns new and negative Copy of the pass object */
	private BigInt makeNegative( BigInt bigInt ) {
		if(bigInt.toString().charAt( 0)  == POSITIVE)
			return new BigInt( "-" + bigInt.toString().substring( 1 )) ; //removing '+' and adding '-'
		return new BigInt("-" + bigInt.toString());
	}

	/* Returns  one of fours possible options of relations between this object and the passed one*/
	private String getRelationBetweenTheInts(BigInt other) {
		char thisSign  = getSign(), otherSign = other.getSign();

		if(thisSign == POSITIVE && otherSign == POSITIVE)
			return Options.BOTH_POSITIVE.toString();
		else if(thisSign == POSITIVE && otherSign == NEGATIVE)
			return Options.THIS_POS_OTHER_NEG.toString();
		else if(thisSign == NEGATIVE && otherSign == POSITIVE)
			return Options.THIS_NEG_OTHER_POS.toString();
		return Options.BOTH_NEGATIVE.toString();
	}

	/* Removes leading zeros from the passed object */
	private StringBuilder deleteLeadingZeros(StringBuilder res) {
		final int ZERO_CHAR = '0';

		for( int i = 0 ; res.charAt(i) == NEGATIVE || res.charAt(i) == ZERO_CHAR; i++ ){
			if( res.charAt (i ) == ZERO_CHAR )
				res.deleteCharAt( i-- );
		}
		return res;
	}

	/* Debugging function  - Returns true if this object is equal to the primitive int passed*/
	public boolean equalsDebug(int num){
		try {
			int thisInt = Integer.parseInt( this.toString() );
			return thisInt == num;
		}catch ( NumberFormatException e ){
			System.out.println( "\'" + this.toString() + "\'" );
			return false;
		}
	}

	/* Returns true if this object and the compared object have equal sign*/
	private boolean oppositeSign(String relation) {
		return  relation.equals( Options.THIS_POS_OTHER_NEG.toString() ) ||
			relation.equals( Options.THIS_NEG_OTHER_POS.toString() );
	}

	/*Utility function for the minus algorithm*/
	private BigInt oppositeSigns(BigInt other ,String rel) {
		if( rel.equals( Options.THIS_NEG_OTHER_POS.toString() ) )
			return other.minus( makePositive( this ) );
		else {  // if( rel.equals( thisPosOtherNeg ))
			int res = this.compareTo( makePositive( other ) );
			switch ( res ) {
				case 1: 	return this.minus( makePositive( other ) );
				case - 1: return makeNegative( makePositive( other ).minus( this ) );
				default: return new BigInt( "0" );
			}
		}
	}

	/** Filling arrays a and b with the appreciate numbers in this object and object other*/
	private void fillArrays( int[] thisBigIntAsArray , int[] otherBigIntAsArray , BigInt other) {
		final int MISSING = -1;
		int thisSize = getSize() , otherSize = other.getSize();
		//checking for sign before the number - if so incrementing the size for the loop ahead
		if( this.numberAsString.indexOf( NEGATIVE ) != MISSING || this.numberAsString.indexOf( POSITIVE ) != MISSING )
			thisSize++;
		if(other.numberAsString.indexOf( NEGATIVE ) != MISSING ||  other.numberAsString.indexOf( POSITIVE ) != MISSING )
			otherSize++;

		//  filling the arrays
		for(int i = thisSize-1, j = 0 ; i >= 0 && j < thisBigIntAsArray.length ; i--, j++)
			thisBigIntAsArray[ j ] = Character.getNumericValue( this.numberAsString.charAt( i ) );

		for(int i = otherSize-1 , j = 0; i >= 0 && j < otherBigIntAsArray.length ; i--, j++)
			otherBigIntAsArray[ j ] = Character.getNumericValue( other.numberAsString.charAt( i ) );
	}

	/* Returns true if BigInt object is zero*/
	private boolean isZero( BigInt other ) {
		for( int i = 0 ; i < other.numberAsString.length() ; i++)
			if( other.numberAsString.charAt( i ) != '0')
				return false;
		return true;
	}
}
