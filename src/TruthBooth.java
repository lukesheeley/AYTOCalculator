
/**This class represents the results of a truth booth.  The match field contains
* the match involved and the result field contains a boolean representing the outcome.
* A result of false means the couple is a no match and a result of true means
* the couple is a perfect match.*/
public class TruthBooth {
	
	//This is the guy and girl that went into the truth booth.
	private Match match;
	
	//This is the result of the truth booth.
	private boolean result;
	
	/**Getters and setters for the match and result.  Both should be set
	 * by the constructor and never changed, but setters are included regardless.*/
	//This is a getter method for the couple that went into the truth booth.
	public Match getMatch() {
		return match;
	}
	
	//This is a setter method for the couple that went into the truth booth.
	public void setMatch(Match match) {
		this.match = match;
	}
	
	//This is a getter method for the result of the truth booth.
	public boolean getResult() {
		return result;
	}
	
	//This is a setter method for the result of the truth booth.
	public void setResult(boolean result) {
		this.result = result;
	}
	
	//This is a constructor for the class.  It sets the couple and result for the truth booth.
	public TruthBooth(Match match, boolean result) {
		this.match = match;
		this.result = result;
	}
}
