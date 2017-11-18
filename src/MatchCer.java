import java.util.ArrayList;

/**This class represents a matchup ceremony.  The matches field contains
 * the matches involved and the beams field contains the number of perfect matches
 * in the given set.*/
public class MatchCer {
	
	//This is the list of matches in the ceremony.
	private ArrayList<Match> matchupList;
	
	//This is the total number of perfect matches in the matchupList.
	private int beams;
	
	/**Getters and setters for the match and result.  Both should be set
	 * by the constructor and never changed, but setters are included regardless.*/
	//This is a getter method for the matchupList.
	public ArrayList<Match> getMatchupList() {
		return matchupList;
	}
	
	//This is a setter method for the matchupList.
	public void setMatchupList(ArrayList<Match> matchupList) {
		this.matchupList = matchupList;
	}
	
	//This is a getter method for the number of perfect matches.
	public int getBeams() {
		return beams;
	}
	
	//This is a setter method for the number of perfect matches.
	public void setBeams(int beams) {
		this.beams = beams;
	}
	
	//This is a constructor for the class.  It sets the matchupList and the beams.
	public MatchCer(ArrayList<Match> matchupList, int beams) {
		this.matchupList = matchupList;
		this.beams = beams;
	}
}
