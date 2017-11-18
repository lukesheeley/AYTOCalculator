
/**This class represents a person on the show.
* Each person has a name and a number.  The name is for readability and easy reference.
* The number for the men references which order they appear in each match-up ceremony listing.
* The number for both men and women will be used in calculations.*/
public class Person {
	
	//This is the name of a person.
	private String name;
	
	//This is the person's number.
	private int pid;
	
	/**Getters and setters for a person's name and pid.  Name and pid should be set
	 * by the constructor and never changed, but setters are included regardless.*/
	//This is a getter method for a person's name.
	public String getName() {
		return name;
	}
	
	//This is a setter method for a person's name.
	public void setName(String name) {
		this.name = name;
	}
	
	//This is a getter method for a person's number.
	public int getPid() {
		return pid;
	}
	
	//This is a setter method for a person's pid.
	public void setPid(int pid) {
		this.pid = pid;
	}
	
	//This is a constructor for the class.  It sets name and pid for a new person.
	public Person(String name, int pid) {
		this.name = name;
		this.pid = pid;
	}
}
