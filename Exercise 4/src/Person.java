public class Person implements Comparable< Person > {   //---Beta Class----//
	private String name;
	private int id;
	private double height;

	public Person(int id , String name , double height) {
		this.id = id;
		this.name = name;
		this.height = height;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Person && this.height == ( ( Person ) obj ).height;
	}

	public int compareTo(Person other) {
		if( this.height == other.height )
			return 0;
		return this.height > other.height ? 1 : - 1;
	}

	@Override
	public String toString() {
		return "id: " + id + "\nname: " + name + "\nheight: " + height + "\n";
	}
}
