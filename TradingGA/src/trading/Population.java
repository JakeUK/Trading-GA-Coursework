package trading;

import java.util.ArrayList;

public class Population {

	public static int POP_SIZE = 100;
	public static double MUT_CHANCE = 0.2;
	public static double CROSS_CHANCE = 0.9;
	public static int TOURN_SIZE = 10;
	private Candidate[] pop = new Candidate[POP_SIZE];
	private double[] fitness = new double[POP_SIZE];
	
	public int bestIndex = -1;
	public int worstIndex = -1;
	
	/**
	 * Initialisation
	 * Random population
	 */
	public Population() {
		for(int i = 0; i < POP_SIZE; i++) {
			pop[i] = new Candidate();
		}
	}
	
	public void iterate() {
		evaluate();
		
		//==========================================
		ArrayList<Candidate> newPop = new ArrayList<>();
		while(newPop.size() != POP_SIZE) {
			if(Randomizer.r.nextDouble() < MUT_CHANCE || newPop.size() == POP_SIZE - 1) {
				int parent = tournamentSelect();
				newPop.add(Candidate.fromMutation(pop[parent]));
			}
			else if(Randomizer.r.nextDouble() < CROSS_CHANCE) {
				int mother = tournamentSelect();
				int father = tournamentSelect();
				Candidate[] cross = Candidate.fromCrossover(pop[mother], pop[father]);
				newPop.add(cross[0]);
				newPop.add(cross[1]);
			}else {
				int parent = tournamentSelect();
				newPop.add(Candidate.copyFromParent(pop[parent]));
			}
		}
		//==========================================
		pop = newPop.toArray(new Candidate[0]);
	}
	
	/**
	 * Roulette selection into tournament selection
	 * @return
	 */
	private int tournamentSelect() {
		int[] tourn = new int[TOURN_SIZE];
		for(int i = 0; i < TOURN_SIZE; i++) {
			
			tourn[i] = Randomizer.r.nextInt(POP_SIZE);
			//tourn[i] = rouletteSelect();
			
		}
		
		int fittest = -1;
		for(int c : tourn) {
			if(fittest == -1) {
				fittest = c;
			}else if(fitness[c] > fitness[fittest]) {
				fittest = c;
			}
		}
		
		return fittest;
	}
	
	/**
	 * Simple roulette selection
	 * @return
	 */
	private int rouletteSelect() {
		double totalFitness = 0;
		for(double d : fitness) {
			totalFitness += d;
		}
		
		double rolling = 0;
		double selection = totalFitness * Randomizer.r.nextDouble();
		for(int i = 0; i < POP_SIZE; i++) {
			rolling += fitness[i];
			if(selection < rolling) {
				return i;
			}
		}
		//Shouldn't get here
		System.out.println("roulette error");
		return -1;
	}
	
	private void evaluate() {
		for(int i = 0; i < POP_SIZE; i++) {
			fitness[i] = pop[i].getFitness();
			if(bestIndex != -1) {
				if(fitness[i] > fitness[bestIndex]) {
					bestIndex = i;
				}
			}else {
				bestIndex = i;
			}
			if(worstIndex != -1) {
				if(fitness[i] < fitness[worstIndex]) {
					worstIndex = i;
				}
			}else {
				worstIndex = i;
			}
		}
	}
	
	public String printStats() {
		String out = "Best : " + fitness[bestIndex] + "  Worst : " + fitness[worstIndex] + "\n";
		out += pop[bestIndex].getString() + "\n";
		return out;
	}
	
	public String endPrint() {
		String out = "";
		
		out += "Fitness of best : " + fitness[bestIndex];
		out += "\nConfig of best  : " + getBestWeights();
		
		return out;
	}
	
	public String getBestWeights() {
		return pop[bestIndex].getString();
	}
}
