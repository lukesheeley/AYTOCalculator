import java.util.ArrayList;

/**This class implements all of the computational functionality of the program.  It
 * generates full match pairings and checks whether they meet the restrictions imposed
 * by the truth booths and matchup ceremonies.*/
public class Calculator {
	
	//The total number of full match pairings that fit the data.
	private int perfCount;
	
	//The current full match pairing that we need to test against the data.
	//curFMP[i] is the number of the girl paired with the guy with number i.
	//For example, curFMP[4] is the pid of the girl paired with the guy with pid 4.
	private ArrayList<int> curFMP;
	
	//This is the list of truth booths that we have.
	private ArrayList<TruthBooth> truthBooths;
	
	//This is the list of matchup ceremonies that we have.
	private ArrayList<MatchCer> matchCers;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
