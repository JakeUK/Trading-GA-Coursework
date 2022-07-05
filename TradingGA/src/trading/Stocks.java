package trading;

import java.io.File;
import java.util.Scanner;

public class Stocks {
	
	private static boolean read = false;
	private static double[][] data;
	private static int dataHeight;
	private static int dataWidth = 5;
	public static String filename;
	
	public static double runCandidate(double[] weights, int[] max) {
		read();
		double budget = 3000.0;
		int stocks = 0;
		
		//Starts at a point where all indicators have values
		for(int i = 28; i < dataHeight; i++) {
			//For each price
			
			
			double buy = 0.0;
			double sell = 0.0;
			double hold = 0.0;
			
			//Calculate weighted decision
			for(int d = 0; d < weights.length; d++) {
				if(data[i][d+1] == 0.0) {
					hold += weights[d];
				}
				else if(data[i][d+1] == 1.0) {
					buy += weights[d];
				}
				else if(data[i][d+1] == 2.0) {
					sell += weights[d];
				}
			}
			
			//Buying/Selling algorithm
			double price = data[i][0];
			int maxBuy = max[0];
			int maxSell = max[1];
			
			if(buy > sell && buy > hold) {
				//Buy (all)
				int toBuy = (int) (budget / price);
				if(toBuy > maxBuy) {
					toBuy = maxBuy;
				}
				//System.out.println(toBuy);
				stocks += toBuy;
				budget -= toBuy * price;
				
			}else if(sell > buy && sell > hold) {
				//Sell (all)
				int toSell = stocks;
				if(toSell > maxSell) {
					toSell = maxSell;
				}
				//System.out.println(toSell);
				budget += price * toSell;
				stocks -= toSell;
				
			}else {
				//Do nothing (if there is no clear winner or if hold wins)
			}
			
		}
		
		//Sell all remaining stocks at ending price
		budget += stocks * data[dataHeight-1][0];
		
		return budget;
		
	}
	
	private static void read() {
		if(!read) {
			try {
				File file = new File(filename);
				
				Scanner reader = new Scanner(file);
				//Ignore top line
				reader.nextLine();
				dataHeight = 0;
				//=========Getting width and height of file
				while(reader.hasNextLine()) {
					reader.nextLine();
					dataHeight++;
				}
				reader.close();
				//=========================================
				data = new double[dataHeight][dataWidth];
				
				reader = new Scanner(file);
				reader.nextLine();
				int i = 0;
				while(reader.hasNextLine()) {
					String[] raw = reader.nextLine().split(",");
					data[i][0] = Double.parseDouble(raw[0]);
					data[i][1] = Double.parseDouble(raw[6]);
					data[i][2] = Double.parseDouble(raw[7]);
					data[i][3] = Double.parseDouble(raw[8]);
					data[i][4] = Double.parseDouble(raw[9]);
					i++;
				}
				
				read = true;
				reader.close();
			}catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
}
