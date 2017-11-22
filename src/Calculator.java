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
	
	//This is essentially a table of counts that will keep track of how many full match
	//pairings that pass all data each couple is present in.  Each element is a guy pid and
	//each element of those elements is a girl pid.  For example, if a truth booth
	//says guy 4 and girl 7 are a perfect match, then matchFrac.get(4).get(7) should equal
	//perfCount after running genPerfCount.  If a truth booth say guy 3 and girl 0 are a no
	//match, then matchFrac.get(3).get(0) should be 0.
	public ArrayList<ArrayList<Double>> matchFrac;
	
	public Calculator(int n, ArrayList<TruthBooth> truthBooths, ArrayList<MatchCer> matchCers) {
		this.n = n;
		perfCount = 0;
		this.truthBooths = truthBooths;
		this.matchCers = matchCers;
		genCount = 0;
		facts = genFacts(n);
		matchFrac = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> temp;
		for (int i = 0; i < n; i++) {
			temp = new ArrayList<Double>(n);
			for (int j = 0; j < n; j++) {
				temp.add(0.0);
			}
			matchFrac.add(temp);
		}
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
				for (int i = 0; i < n; i++) {
					Double temp = matchFrac.get(i).get(curFMP.get(i));
					matchFrac.get(i).set(curFMP.get(i), temp + 1); 
				}
			}
		}
		for (int j = 0; j < n; j++) {
			for(int k = 0; k < n; k++) {
				Double num = matchFrac.get(j).get(k);
				matchFrac.get(j).set(k, num/perfCount);
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
		Person guy4 = new Person("Joe", 4);
		Person guy5 = new Person("Michael", 5);
		Person guy6 = new Person("E$", 6);
		Person guy7 = new Person("Clinton", 7);
		Person guy8 = new Person("Shad", 8);
		Person guy9 = new Person("Tyler", 9);
		Person guy10 = new Person("Dmitry", 10);
		Person girl0 = new Person("Alivia", 0);
		Person girl1 = new Person("Geles", 1);
		Person girl2 = new Person("Nurys", 2);
		Person girl3 = new Person("Alexis", 3);
		Person girl4 = new Person("Zoe", 4);
		Person girl5 = new Person("Keyana", 5);
		Person girl6 = new Person("Jada", 6);
		Person girl7 = new Person("Uche", 7);
		Person girl8 = new Person("Audrey", 8);
		Person girl9 = new Person("Nicole", 9);
		Person girl10 = new Person("Diandra", 10);
		Match match1 = new Match(guy1, girl1);
		Match match2 = new Match(guy2, girl2);
		Match match3 = new Match(guy3, girl3);
		Match match4 = new Match(guy3, girl0);
		Match match5 = new Match(guy6, girl5);
		Match match6 = new Match(guy7, girl7);
		Match match7 = new Match(guy10, girl9);
		TruthBooth tb1 = new TruthBooth(match1, false);
		TruthBooth tb2 = new TruthBooth(match2, false);
		TruthBooth tb3 = new TruthBooth(match3, false);
		TruthBooth tb4 = new TruthBooth(match4, false);
		TruthBooth tb5 = new TruthBooth(match5, false);
		TruthBooth tb6 = new TruthBooth(match6, false);
		TruthBooth tb7 = new TruthBooth(match7, false);
		//System.out.println(calculatorTest.genCurFMP(0));
		//System.out.println(calculatorTest.checkTB(calculatorTest.genCurFMP(0), tb0));
		//System.out.println(calculatorTest.checkTB(calculatorTest.genCurFMP(0), tb1));
		//System.out.println(calculatorTest.checkTB(calculatorTest.genCurFMP(0), tb2));
		//System.out.println(calculatorTest.checkTB(calculatorTest.genCurFMP(0), tb3));
		ArrayList<TruthBooth> tbs = new ArrayList<TruthBooth>();
		tbs.add(tb1);
		tbs.add(tb2);
		tbs.add(tb3);
		tbs.add(tb4);
		tbs.add(tb5);
		tbs.add(tb6);
		tbs.add(tb7);
		
		//Week 1 Matchup Ceremony
		Match mu10 = new Match(guy0, girl0);
		Match mu11 = new Match(guy1, girl1);
		Match mu12 = new Match(guy2, girl2);
		Match mu13 = new Match(guy3, girl3);
		Match mu14 = new Match(guy4, girl4);
		Match mu15 = new Match(guy5, girl5);
		Match mu16 = new Match(guy6, girl6);
		Match mu17 = new Match(guy7, girl7);
		Match mu18 = new Match(guy8, girl8);
		Match mu19 = new Match(guy9, girl9);
		Match mu110 = new Match(guy10, girl10);
		ArrayList<Match> matchlist1 = new ArrayList<Match>();
		matchlist1.add(mu10);
		matchlist1.add(mu11);
		matchlist1.add(mu12);
		matchlist1.add(mu13);
		matchlist1.add(mu14);
		matchlist1.add(mu15);
		matchlist1.add(mu16);
		matchlist1.add(mu17);
		matchlist1.add(mu18);
		matchlist1.add(mu19);
		matchlist1.add(mu110);
		MatchCer muc1 = new MatchCer(matchlist1, 3);
		
		//Week 2 Matchup Ceremony
		Match mu20 = new Match(guy3, girl3);
		Match mu21 = new Match(guy2, girl2);
		Match mu22 = new Match(guy9, girl4);
		Match mu23 = new Match(guy5, girl5);
		Match mu24 = new Match(guy6, girl6);
		Match mu25 = new Match(guy7, girl7);
		Match mu26 = new Match(guy10, girl9);
		Match mu27 = new Match(guy1, girl10);
		Match mu28 = new Match(guy4, girl8);
		Match mu29 = new Match(guy8, girl1);
		Match mu210 = new Match(guy0, girl0);
		ArrayList<Match> matchlist2 = new ArrayList<Match>();
		matchlist2.add(mu20);
		matchlist2.add(mu21);
		matchlist2.add(mu22);
		matchlist2.add(mu23);
		matchlist2.add(mu24);
		matchlist2.add(mu25);
		matchlist2.add(mu26);
		matchlist2.add(mu27);
		matchlist2.add(mu28);
		matchlist2.add(mu29);
		matchlist2.add(mu210);
		MatchCer muc2 = new MatchCer(matchlist2, 1);
		
		//Week 3 Matchup Ceremony
		Match mu30 = new Match(guy2, girl1);
		Match mu31 = new Match(guy5, girl8);
		Match mu32 = new Match(guy4, girl4);
		Match mu33 = new Match(guy0, girl0);
		Match mu34 = new Match(guy9, girl9);
		Match mu35 = new Match(guy10, girl2);
		Match mu36 = new Match(guy8, girl5);
		Match mu37 = new Match(guy6, girl3);
		Match mu38 = new Match(guy7, girl7);
		Match mu39 = new Match(guy1, girl6);
		Match mu310 = new Match(guy3, girl10);
		ArrayList<Match> matchlist3 = new ArrayList<Match>();
		matchlist3.add(mu30);
		matchlist3.add(mu31);
		matchlist3.add(mu32);
		matchlist3.add(mu33);
		matchlist3.add(mu34);
		matchlist3.add(mu35);
		matchlist3.add(mu36);
		matchlist3.add(mu37);
		matchlist3.add(mu38);
		matchlist3.add(mu39);
		matchlist3.add(mu310);
		MatchCer muc3 = new MatchCer(matchlist3, 2);
				
		//Week 4 Matchup Ceremony
		Match mu40 = new Match(guy10, girl3);
		Match mu41 = new Match(guy0, girl10);
		Match mu42 = new Match(guy4, girl4);
		Match mu43 = new Match(guy7, girl7);
		Match mu44 = new Match(guy8, girl8);
		Match mu45 = new Match(guy1, girl5);
		Match mu46 = new Match(guy6, girl9);
		Match mu47 = new Match(guy5, girl1);
		Match mu48 = new Match(guy3, girl2);
		Match mu49 = new Match(guy2, girl0);
		Match mu410 = new Match(guy9, girl6);
		ArrayList<Match> matchlist4 = new ArrayList<Match>();
		matchlist4.add(mu40);
		matchlist4.add(mu41);
		matchlist4.add(mu42);
		matchlist4.add(mu43);
		matchlist4.add(mu44);
		matchlist4.add(mu45);
		matchlist4.add(mu46);
		matchlist4.add(mu47);
		matchlist4.add(mu48);
		matchlist4.add(mu49);
		matchlist4.add(mu410);
		MatchCer muc4 = new MatchCer(matchlist4, 3);
		
		//Week 5 Matchup Ceremony
		Match mu50 = new Match(guy1, girl9);
		Match mu51 = new Match(guy2, girl10);
		Match mu52 = new Match(guy0, girl0);
		Match mu53 = new Match(guy4, girl4);
		Match mu54 = new Match(guy8, girl8);
		Match mu55 = new Match(guy9, girl5);
		Match mu56 = new Match(guy6, girl1);
		Match mu57 = new Match(guy3, girl3);
		Match mu58 = new Match(guy7, girl6);
		Match mu59 = new Match(guy10, girl7);
		Match mu510 = new Match(guy5, girl2);
		ArrayList<Match> matchlist5 = new ArrayList<Match>();
		matchlist5.add(mu50);
		matchlist5.add(mu51);
		matchlist5.add(mu52);
		matchlist5.add(mu53);
		matchlist5.add(mu54);
		matchlist5.add(mu55);
		matchlist5.add(mu56);
		matchlist5.add(mu57);
		matchlist5.add(mu58);
		matchlist5.add(mu59);
		matchlist5.add(mu510);
		MatchCer muc5 = new MatchCer(matchlist5, 1);
		
		//Week 6 Matchup Ceremony
		Match mu60 = new Match(guy1, girl5);
		Match mu61 = new Match(guy4, girl3);
		Match mu62 = new Match(guy5, girl7);
		Match mu63 = new Match(guy7, girl1);
		Match mu64 = new Match(guy3, girl4);
		Match mu65 = new Match(guy8, girl8);
		Match mu66 = new Match(guy0, girl2);
		Match mu67 = new Match(guy10, girl10);
		Match mu68 = new Match(guy2, girl0);
		Match mu69 = new Match(guy9, girl9);
		Match mu610 = new Match(guy6, girl6);
		ArrayList<Match> matchlist6 = new ArrayList<Match>();
		matchlist6.add(mu60);
		matchlist6.add(mu61);
		matchlist6.add(mu62);
		matchlist6.add(mu63);
		matchlist6.add(mu64);
		matchlist6.add(mu65);
		matchlist6.add(mu66);
		matchlist6.add(mu67);
		matchlist6.add(mu68);
		matchlist6.add(mu69);
		matchlist6.add(mu610);
		MatchCer muc6 = new MatchCer(matchlist6, 4);
		
		//Week 7 Matchup Ceremony
		Match mu70 = new Match(guy0, girl2);
		Match mu71 = new Match(guy1, girl5);
		Match mu72 = new Match(guy5, girl8);
		Match mu73 = new Match(guy8, girl0);
		Match mu74 = new Match(guy3, girl6);
		Match mu75 = new Match(guy10, girl10);
		Match mu76 = new Match(guy4, girl7);
		Match mu77 = new Match(guy9, girl9);
		Match mu78 = new Match(guy6, girl4);
		Match mu79 = new Match(guy7, girl1);
		Match mu710 = new Match(guy2, girl3);
		ArrayList<Match> matchlist7 = new ArrayList<Match>();
		matchlist7.add(mu70);
		matchlist7.add(mu71);
		matchlist7.add(mu72);
		matchlist7.add(mu73);
		matchlist7.add(mu74);
		matchlist7.add(mu75);
		matchlist7.add(mu76);
		matchlist7.add(mu77);
		matchlist7.add(mu78);
		matchlist7.add(mu79);
		matchlist7.add(mu710);
		MatchCer muc7 = new MatchCer(matchlist7, 5);
		
		
		
		
		ArrayList<MatchCer> mucs = new ArrayList<MatchCer>();
		mucs.add(muc1);
		mucs.add(muc2);
		mucs.add(muc3);
		mucs.add(muc4);
		mucs.add(muc5);
		mucs.add(muc6);
		mucs.add(muc7);
		//System.out.println(calculatorTest.checkTBAll(calculatorTest.genCurFMP(0), tbs));
		//int i = 0;
		//while(!calculatorTest.checkTBAll(calculatorTest.genCurFMP(i), tbs)) {
		//	i++;
		//}
		//System.out.println(i);
		//System.out.println(calculatorTest.genCurFMP(i));
		//System.out.println(calculatorTest.genCurFMP(367919));
		Calculator calculatorTest = new Calculator(11, tbs, mucs); 
		calculatorTest.genPerfCount();
		System.out.println(calculatorTest.perfCount);
		//calculatorTest.matchFrac.get(0).set(1, 3);
		System.out.println(calculatorTest.matchFrac);
	}

}
