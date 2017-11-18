
/**This class represents a match between one guy and one girl.  Fields are pretty
* self explanatory.*/
public class Match {
	
	//This is the guy in the match.
	private Person guy;
	
	//This is the girl in the match.
	private Person girl;
	
	/**Getters and setters for the guy and girl in the match.  Both should be set
	 * by the constructor and never changed, but setters are included regardless.*/
	//This is a getter method for the guy in the match.
	public Person getGuy() {
		return guy;
	}
	
	//This is a setter method for the guy in the match.
	public void setGuy(Person guy) {
		this.guy = guy;
	}
	
	//This is a getter method for the girl in the match.
	public Person getGirl() {
		return girl;
	}
	
	//This is a setter method for the girl in the match.
	public void setGirl(Person girl) {
		this.girl = girl;
	}
	
	//This is a constructor for the class.  It sets the guy and girl in the match.
	public Match(Person guy, Person girl) {
		this.guy = guy;
		this.girl = girl;
	}
}
