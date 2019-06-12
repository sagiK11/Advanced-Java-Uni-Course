import java.io.Serializable;

public class NotesAppDateFormat implements Serializable {
	private Integer day, month;
	private String year;

	public NotesAppDateFormat(Integer d , Integer m , String y) {
		day = d;
		month = m;
		year = y;
	}

	@Override
	public boolean equals(Object obj) {
		if( ! ( obj instanceof NotesAppDateFormat ) )
			return false;

		NotesAppDateFormat other = ( NotesAppDateFormat ) obj;
		return this.day.equals( other.day )
			&& this.month.equals( other.month )
			&& this.year.equals( other.year );
	}

	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + day.hashCode( );
		result = 31 * result + month.hashCode( );
		result = 31 * result + year.hashCode( );
		return result;
	}

	public String toString() {
		return day + "/" + month + "/" + year;
	}
}
