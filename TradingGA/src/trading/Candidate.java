package trading;

public class Candidate {
	public static final int NO_OF_WEIGHTS = 4;
	protected double[] weights;
	protected int[] max = new int[2]; // 0 = maxBuy, 1 = maxSell
	
	/**
	 * Randomized values
	 */
	public Candidate() {
		weights = new double[NO_OF_WEIGHTS];
		for(int i = 0; i < NO_OF_WEIGHTS; i++) {
			double val = Randomizer.r.nextInt(1001);
			weights[i] = val/1000;
		}
		max[0] = Randomizer.r.nextInt(10) + 1;
		max[1] = Randomizer.r.nextInt(10) + 1;
	}
	
	public static Candidate copyFromParent(Candidate parent) {
		Candidate nCand = new Candidate();
		nCand.weights = parent.weights.clone();
		nCand.max = parent.max.clone();
		return nCand;
	}
	
	public static Candidate fromMutation(Candidate parent) {
		Candidate nCand = copyFromParent(parent);
		int selected = Randomizer.r.nextInt(NO_OF_WEIGHTS + 2);
		
		// Choose a single weight or maximum to mutate
		if(selected < NO_OF_WEIGHTS) {
			double sizeOfChange = ((double) Randomizer.r.nextInt(50))/1000;
			double newVal;
			
			//chose a direction
			if(Randomizer.r.nextBoolean()) {
				newVal = nCand.weights[selected] + sizeOfChange;
			}else {
				newVal = nCand.weights[selected] - sizeOfChange;
			}
			
			if(newVal > 1) newVal = 1;
			if(newVal < 0) newVal = 0;
			
			nCand.weights[selected] = newVal;
		}else{
			boolean dir = Randomizer.r.nextBoolean();
			int maxSel = selected - NO_OF_WEIGHTS;
			if(!(nCand.max[maxSel] == 1) && !dir) {
				nCand.max[maxSel]--;
			}else if(dir){
				nCand.max[maxSel]++;
			}	
		}
		return nCand;
	}
	
	/**
	 * 1 point crossover
	 * @param mother
	 * @param father
	 * @return
	 */
	
	public static Candidate[] fromCrossover(Candidate mother, Candidate father) {
		Candidate mClone = copyFromParent(mother);
		Candidate fClone = copyFromParent(father);
		
		int rotate = Randomizer.r.nextInt(NO_OF_WEIGHTS - 2);
		for(int i = 0; i < NO_OF_WEIGHTS; i++) {
			if(i <= rotate) {
				mClone.weights[i] = father.weights[i];
			}else {
				fClone.weights[i] = mother.weights[i];
			}
		}
		
		return new Candidate[] {mClone, fClone};
	}
	
	public double getFitness() {
		return Stocks.runCandidate(weights, max);
	}
	
	public String getString() {
		String out = "";
		for(int i = 0; i < weights.length; i++) {
			out += weights[i] + " ";
		}
		out += " | ";
		out += "Max Buy : " + max[0];
		out += " Max Sell : " + max[1];
		return out;
	}
	
}
