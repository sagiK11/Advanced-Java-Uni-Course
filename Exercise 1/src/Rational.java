public class Rational {
	private int numerator;
	private int denominator;

	public Rational(int numerator , int denominator) {
		if( denominator < 0 ) {
			this.denominator = 1;
			this.numerator = 0;
		} else if( denominator == 0 ) {
			this.denominator = 1;
			System.out.println( "Can not accept zero as denominator" );
		} else {
			this.numerator = numerator;
			this.denominator = denominator;
		}

	}

	public boolean greaterThan(Rational other) {
		return ( this.numerator * other.getDenominator( ) ) > ( this.denominator * other.getNumerator( ) );
	}

	public boolean equals(Rational other) {
		if( this.numerator == 0 )
			return other.getNumerator( ) == 0;
		if( other.numerator == 0 )
			return false;
		return ( this.numerator * other.getDenominator( ) ) == ( this.denominator * other.getNumerator( ) );
	}

	public Rational plus(Rational other) {
		Rational sum = new Rational(
			( ( this.numerator * other.getDenominator( ) ) + ( this.denominator * other.getNumerator( ) ) ) ,
			( this.denominator * other.getDenominator( ) ) );
		return sum.reduce( );
	}

	public Rational minus(Rational other) {
		Rational diff = new Rational(
			( ( this.numerator * other.getDenominator( ) ) - ( this.denominator * other.getNumerator( ) ) ) ,
			( this.denominator * other.getDenominator( ) ) );
		return diff.reduce( );
	}

	public Rational multiply(Rational other) {
		Rational product = new Rational(
			( this.numerator ) * ( other.getNumerator( ) ) ,
			( this.denominator * other.getDenominator( ) ) );
		return product.reduce( );
	}

	public int getNumerator() {
		return numerator;
	}

	public int getDenominator() {
		return denominator;
	}


	public Rational reduce() {
		int gcd = Math.abs( gcd( this.numerator , this.denominator ) );
		if( gcd == 0 )
			return new Rational( this.numerator , this.denominator );
		return new Rational( this.numerator / gcd , this.denominator / gcd );
	}

	private int gcd(int numerator , int denominator) {
		if( ( numerator == 0 && denominator == 0 ) || denominator == 1 ) {
			return 1;
		} else if( denominator == 0 || ( numerator == denominator ) )
			return numerator;
		return gcd( denominator , numerator % denominator );
	}

	public String toString() {
		return "(" + this.numerator + "/" + this.denominator + ")";
	}

}
