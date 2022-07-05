package trading;

public class Main {

	public static void main(String[] args) {
		Stocks.filename = "data.csv";
		
		int iterations = 100;
		
		Population p = new Population();
		for(int i = 1; i <= iterations; i++) {
			p.iterate();
			//System.out.println(i + " | " + p.printStats());
		}
		
		System.out.println(p.endPrint());
	}

}
