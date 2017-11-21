import java.util.ArrayList;

/**This class implements all of the computational functionality of the program.  It
 * generates full match pairings and checks whether they meet the restrictions imposed
 * by the truth booths and matchup ceremonies.*/
public class Calculator {
	
	//This is the number of couples in a full match pairing, aka how many guys there are
	//and how many girls there are.
	private int n;
	
	//The total number of full match pairings that fit the data.
	private int perfCount;
	
	//The current full match pairing that we need to test against the data.
	//curFMP[i] is the number of the girl paired with the guy with number i.
	//For example, curFMP[4] is the pid of the girl paired with the guy with pid 4.
	private ArrayList<Integer> curFMP;
	
	//This is the list of truth booths that we have.
	private ArrayList<TruthBooth> truthBooths;
	
	//This is the list of matchup ceremonies that we have.
	private ArrayList<MatchCer> matchCers;
	
	//This is the total number of full match pairings that we've tried.
	//Used to generate the full match pairings.  Goes from 0 to n!-1.
	private int genCount;
	
	//This is a list of factorials from 1 to n-1, used to generate the full match pairings.
	private ArrayList<Integer> facts;
	
	public Calculator(int n, ArrayList<TruthBooth> truthBooths, ArrayList<MatchCer> matchCers) {
		this.n = n;
		perfCount = 0;
		this.truthBooths = truthBooths;
		this.matchCers = matchCers;
		genCount = 0;
		facts = genFacts(n);
	}
	
	//This is a helper function that calculates the factorial of a given integer.
	public int getFactorial(int x) {
		int fact = 1;
		while (x > 1) {
			fact *= x;
			x--;
		}
		return fact;
	}
	
	//This is a helper function that will generate the facts field.
	public ArrayList<Integer> genFacts(int x) {
		ArrayList<Integer> returnList = new ArrayList<Integer>(x);
		for (int i = 0; i < x; i++) {
			returnList.add(getFactorial(i));
		}
		return returnList;
	}
	
	//This is a helper function that will generate the curFMP field.
	//It generates a unique full matchup pairing for every value of
	//genCount from 0 to n!-1.
	public ArrayList<Integer> genCurFMP(int num) {
		ArrayList<Integer> returnList = new ArrayList<Integer>(this.n);
		int rem = num;
		ArrayList<Integer> options = new ArrayList<Integer>(this.n);
		for (int j = 0; j < n; j++) {
			options.add(j);
		}
		for (int i = 0; i < n; i++) {
			//System.out.println(rem);
			returnList.add(options.get(rem/this.facts.get(this.facts.size() - i - 1)));
			options.remove(rem/this.facts.get(this.facts.size() - i - 1));
			rem = rem % this.facts.get(this.facts.size() - i - 1);
		}
		//System.out.println(options);
		//System.out.println(returnList);
		this.genCount++;
		return returnList;
	}
	
	/**I realized that testing my genCurFMP function the way I was imagining would
	 * actually take quuuuuuiiiiiiiite a long time so I'm going to run the math and
	 * see if I can rigorously prove to myself the the lists it generates are the n!
	 * unique lists that are expected.  Order of the lists does not matter for the
	 * purpose of the program, just that it generates all n! different lists successfully.
	 * 
	//this is a helper function designed to test the genCurFMP method.
	//genCurFMP should, as genCount goes from 0 to n!-1, generate
	//n! UNIQUE lists representing all possible matchings.
	public boolean testGenCurFMP() {
		ArrayList<ArrayList<Integer>> testList = new ArrayList<ArrayList<Integer>>();
		int factmin1 = this.facts.get(this.facts.size() - 1);
		int factmin2 = this.facts.get(this.facts.size() - 2);
		int factmin3 = this.facts.get(this.facts.size() - 3);
		for (int j = 0; j < this.n; j++) {
			for (int k = 0; k < this.n - 1; k++) {
				for (int s = 0; s < this.n - 2; s++) {
					for (int i = j * factmin1 + k * factmin2 + s * factmin3; i < factmin1 * j + factmin2 * k + factmin3 * (s + 1); i++) {
						ArrayList<Integer> newItem = genCurFMP(i);
						if (newItem.get(0) != j) {
							return false;
						}
						if (testList.contains(newItem)) {
							return false;
						}
						testList.add(newItem);	
					}
					testList.clear();
					System.out.println("running " + j + " " + k + " " + s);
				}
			}
		}
		System.out.println(testList.size());
		return true;
	}
	*/
	
	//This method tests a given full match pairing against the results of a truth booth.
	//Returns true if the full match pairing passes the truth booth, false if it
	//contradicts it.	
	public boolean checkTB(ArrayList<Integer> fmp, TruthBooth tb) {
		if (tb.getResult()) {
			if (tb.getMatch().getGirl().getPid() != fmp.get(tb.getMatch().getGuy().getPid())) {
				return false;
			}
		}
		else {
			if (tb.getMatch().getGirl().getPid() == fmp.get(tb.getMatch().getGuy().getPid())) {
				return false;
			}
		}
		return true;
	}
	
	//This method tests a given full match pairing against the results of a list of truth
	//booths.  Returns true if the full match pairing passes all truth booths and false if
	//it contradicts any.
	public boolean checkTBAll(ArrayList<Integer> fmp, ArrayList<TruthBooth> tbs) {
		boolean result = true;
		for (TruthBooth tb : tbs) {
			result = result && checkTB(fmp, tb);
		}
		return result;
	}
	
	//This method tests a given full match pairing against the results of a matchup ceremony.
	//Returns true if the full match pairing passes the matchup ceremony, false if it
	//contradicts it.
	public boolean checkMC(ArrayList<Integer> fmp, MatchCer ceremony) {
		int count = 0;
		for(int i = 0; i < fmp.size(); i++) {
			for(Match m : ceremony.getMatchupList()) {
				if (m.getGuy().getPid() == i && m.getGirl().getPid() == fmp.get(i)) {
					count++;
				}
			}
		}
		if(count == ceremony.getBeams()) {
			return true;
		}
		else {
			return false;
		}
	}
	
	//This method tests a given full match pairing against the results of a list of
	//matchup ceremonies.  Returns true if the full match pairing passes all matchup
	//ceremonies and false if it contradicts any.
	public boolean checkMCAll(ArrayList<Integer> fmp, ArrayList<MatchCer> ceremonies) {
		boolean result = true;
		for (MatchCer cer : ceremonies) {
			result = result && checkMC(fmp, cer);
		}
		return result;
	}
	
	//This method tests all possible full match pairings against both the given truth
	//booths and the given matchup ceremonies.  If an individual full match pairing
	//passes both, perfCount is incremented.
	public void genPerfCount() {
		while(genCount < facts.get(facts.size() - 1) * n) {
			curFMP = genCurFMP(genCount);
			if (checkTBAll(curFMP, truthBooths) && checkMCAll(curFMP, matchCers)) {
				perfCount++;
			}
		}
	}
	
	
	public static void main(String[] args) {
		ArrayList<TruthBooth> truthBoothsTest = new ArrayList<TruthBooth>();
		ArrayList<MatchCer> matchCersTest = new ArrayList<MatchCer>();
		//Calculator calculatorTest = new Calculator(11, truthBoothsTest, matchCersTest); 
		//System.out.println(calculatorTest.facts);
		//System.out.println(calculatorTest.genCount);
		//calculatorTest.genCurFMP(39916799);
		//System.out.println(calculatorTest.genCount);
		//for (int i = 0; i < 39916800; i++) {
		//	calculatorTest.genCurFMP(i);
		//}
		//System.out.println("Done.");
		//calculatorTest.testGenCurFMP();
		Person guy0 = new Person("Kareem", 0);
		Person guy1 = new Person("Anthony", 1);
		Person guy2 = new Person("Malcolm", 2);
		Person guy3 = new Person("Keith", 3);
		Person girl0 = new Person("Alivia", 0);
		Person girl1 = new Person("Geles", 1);
		Person girl2 = new Person("Nurys", 2);
		Person girl3 = new Person("Alexis", 3);
		Match match0 = new Match(guy0, girl0);
		Match match1 = new Match(guy1, girl1);
		Match match2 = new Match(guy2, girl2);
		Match match3 = new Match(guy3, girl3);
		TruthBooth tb0 = new TruthBooth(match0, true);
		TruthBooth tb1 = new TruthBooth(match1, false);
		TruthBooth tb2 = new TruthBooth(match2, false);
		TruthBooth tb3 = new TruthBooth(match3, false);
		//System.out.println(calculatorTest.genCurFMP(0));
		//System.out.println(calculatorTest.checkTB(calculatorTest.genCurFMP(0), tb0));
		//System.out.println(calculatorTest.checkTB(calculatorTest.genCurFMP(0), tb1));
		//System.out.println(calculatorTest.checkTB(calculatorTest.genCurFMP(0), tb2));
		//System.out.println(calculatorTest.checkTB(calculatorTest.genCurFMP(0), tb3));
		ArrayList<TruthBooth> tbs = new ArrayList<TruthBooth>();
		tbs.add(tb0);
		tbs.add(tb1);
		tbs.add(tb2);
		tbs.add(tb3);
		//System.out.println(calculatorTest.checkTBAll(calculatorTest.genCurFMP(0), tbs));
		//int i = 0;
		//while(!calculatorTest.checkTBAll(calculatorTest.genCurFMP(i), tbs)) {
		//	i++;
		//}
		//System.out.println(i);
		//System.out.println(calculatorTest.genCurFMP(i));
		//System.out.println(calculatorTest.genCurFMP(367919));
		Calculator calculatorTest = new Calculator(11, tbs, matchCersTest); 
		calculatorTest.genPerfCount();
		System.out.println(calculatorTest.perfCount);
	}

}
