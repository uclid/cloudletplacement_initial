import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

public class GeneticCloudletPlacement {

	private int estimate_optimal_cost = 0;
	private int final_cost = 0;
	private double final_latency = 0;
	private double final_coverage = 0;
	private int num_large = 0;
	private int num_medium = 0;
	private int num_small = 0;

	public GeneticCloudletPlacement(int num_large, int num_medium, int num_small) {
		// TODO Auto-generated constructor stub
		this.num_large = num_large;
		this.num_medium = num_medium;
		this.num_small = num_small;
	}

	/**
	 * @author Dixit Bhatta
	 * The method takes the decision variables, constraints,
	 * and solution arrays as arguments and displays the
	 * solution if there a feasible one.
	 * @param C set of cloudlets
	 * @param P set of candidate points
	 * @param V set of end devices
	 * @param cost placement cost matrix
	 * @param latency latency matrix
	 * @param threshold 
	 */
	public void geneticAlgorithm(ArrayList<Cloudlet> C, ArrayList<CandidatePoint> P,
			ArrayList<EndDevice> E, int[][] cost, int[][] latency, int assignment_size, double threshold) {
		
		int w = C.size();
		int n = P.size();
		int v = E.size();
		int m = assignment_size;
		HashMap<Cloudlet[], Double> cover_map = new HashMap<Cloudlet[], Double>();
		
		this.estimate_optimal_cost = estimateOptimal(C, E, cost);
		//System.out.println(estimate_optimal_cost);
		
		//variable holding assignments for the cloudlets
		Cloudlet[][] cloudlets = new Cloudlet[m][n];
		cloudlets = randomAssignments(C, n, m);
		//for(int i = 0; i < m; i++) {
			//System.out.println(Arrays.toString(cloudlets[i]));
		//}
		
		//variable holding assignment for devices
		int[] devices = new int[v];
		devices = deviceAssignments(P, E);
		
		//System.out.println(Arrays.toString(devices));
		
		//enclose this in while underThreshold()
		do {
			ArrayList<Cloudlet[]> B = new ArrayList<Cloudlet[]>();
			PriorityQueue<Cloudlet[]> pq = new PriorityQueue<Cloudlet[]>(m, new AssignmentsComparator(cost));
			for(int i = 0; i < m; i++) {
				pq.add(cloudlets[i]);
			}
			
			while(B.size() <= m) {
				Cloudlet[] c1 = new Cloudlet[n];
				Cloudlet[] c2 = new Cloudlet[n];
				Cloudlet[] a1 = new Cloudlet[n];
				Cloudlet[] a2 = new Cloudlet[n];
				
				if(!pq.isEmpty()) {
					c1 = pq.remove();
				}
				if(!pq.isEmpty()) {
					c2 = pq.remove();
				}
				//System.out.println(Arrays.toString(pq.remove()));
				
				
				//for crossover probability
				Random rand = new Random();
				int x = rand.nextInt(10);
				//System.out.println(x);
				
				/*System.out.println("Before Crossover");
				System.out.println("c1 " + Arrays.toString(c1) + "= " + fitness(c1, cost));
				System.out.println("c2 " + Arrays.toString(c2) + "= " + fitness(c2, cost));*/
				
				//crossover probability is 0.5 for now
				if(x >= 5) {
					//System.out.println("Crossover happend!");
					a1 = crossOver(c1.clone(),c2.clone())[0];
					a2 = crossOver(c1.clone(),c2.clone())[1];
				}
				else {
					a1 = c1.clone();
					a2 = c2.clone();
				}
	
				/*System.out.println("\nAfter Crossover, before mutation");
				System.out.println("a1 " + Arrays.toString(a1)+ "= " + fitness(a1, cost));
				System.out.println("a2 " + Arrays.toString(a2)+ "= " + fitness(a2, cost));*/
				
				a1 = mutate(a1);
				a2 = mutate(a2);
				
				/*System.out.println("\nAfter mutation");
				System.out.println("a1 " + Arrays.toString(a1)+ "= " + fitness(a1, cost));
				System.out.println("a2 " + Arrays.toString(a2)+ "= " + fitness(a2, cost));
				System.out.println("c1 " + Arrays.toString(c1) + "= " + fitness(c1, cost));
				System.out.println("c2 " + Arrays.toString(c2) + "= " + fitness(c2, cost));*/
				
				int c1_fit = fitness(c1, cost);
				int c2_fit = fitness(c2, cost);
				int a1_fit = fitness(a1, cost);
				int a2_fit = fitness(a2, cost);
				
				int fC = Math.min(c1_fit, c2_fit);
				int fA = Math.min(a1_fit, a2_fit);
				
				//System.out.println(fC + " " + fA);
				
				/*System.out.println("\nBefore coverage");
				System.out.println("a1 " + Arrays.toString(a1) + " = " + coverage(a1.clone(), devices, E, P));
				System.out.println("a2 " + Arrays.toString(a2) + " = " + coverage(a2.clone(), devices, E, P));
				System.out.println("c1 " + Arrays.toString(c1) + " = " + coverage(c1.clone(), devices, E, P));
				System.out.println("c2 " + Arrays.toString(c2) + " = " + coverage(c2.clone(), devices, E, P));*/
				
				double c1_cover = coverage(c1.clone(), devices, E, P);
				double c2_cover = coverage(c2.clone(), devices, E, P);
				double a1_cover = coverage(a1.clone(), devices, E, P);
				double a2_cover = coverage(a2.clone(), devices, E, P);
				cover_map.put(c1, c1_cover);
				cover_map.put(c2, c2_cover);
				cover_map.put(a1, a1_cover);
				cover_map.put(a2, a2_cover);
				//System.out.println("Here==" + cover_map.get(a1));
				
				double Vc = Math.min(c1_cover, c2_cover);
				double Va = Math.min(a1_cover, a2_cover);
				
				/*
				System.out.println("\nAfter coverage");
				System.out.println("a1 " + Arrays.toString(a1) + " = " + coverage(a1.clone(), devices, E, P));
				System.out.println("a2 " + Arrays.toString(a2) + " = " + coverage(a2.clone(), devices, E, P));
				System.out.println("c1 " + Arrays.toString(c1) + " = " + coverage(c1.clone(), devices, E, P));
				System.out.println("c2 " + Arrays.toString(c2) + " = " + coverage(c2.clone(), devices, E, P));
				System.out.println(Vc + " " + Va);
				*/
				
				ArrayList<Cloudlet[]> population = 	new ArrayList<Cloudlet[]>();
				population.add(c1);
				population.add(c2);
				population.add(a1);
				population.add(a2);
				
				//Cloudlet[] IF = fittest(population, cover_map, cost);
				//cover_map.put(IF, coverage(IF.clone(), devices, E, P));
				//System.out.println(Arrays.toString(IF));
				if((fA < fC) || ((fA == fC) && (Va >= Vc))) {
						//System.out.println(cover_map.get(a1a2[i]));
						if(cover_map.get(a1) >= threshold) {
							B.add(a1);
							cover_map.remove(c1);
						}
						else {
							cover_map.remove(c1);
							cover_map.remove(a1);
						}
						if(cover_map.get(a2) >= threshold) {
							B.add(a2);
							cover_map.remove(c2);
						}
						else {
							cover_map.remove(c2);
							cover_map.remove(a2);
						}
				}
				else {
					B.add(c1);
					B.add(c2);
					cover_map.remove(a1);
					cover_map.remove(a2);
				}
				//System.out.println(B.toString());
				Cloudlet[][] temp = new Cloudlet[B.size()][n];
				for(int i = 0; i<B.size(); i++) {
					temp[i] = B.get(i);
				}
				cloudlets = temp;
			}
			/*System.out.println("\nBest so far");
			for(int i = 0; i<B.size(); i++) {
				System.out.println(Arrays.toString(B.get(i)) + " " + fitness(B.get(i), cost) + " " + cover_map.get(B.get(i)));
			}*/
			
		} while(!underThreshold(cloudlets, cover_map, threshold));
		
		//System.out.println(Arrays.toString(devices));
		
		int[][] devices_new = new int[m][v];
		devices_new = maximizeCover(devices, cloudlets, E, P);
		//System.out.println(Arrays.toString(devices));
		
		int index = selectLeastLatency(devices_new, cloudlets, latency);
		this.final_cost = totalCost(cloudlets[index], cost);
		this.final_coverage = coverage(cloudlets[index].clone(), devices_new[index], E, P);
		System.out.println(index + ">" + Arrays.toString(cloudlets[index]) + " " + 
		this.final_cost + " " + this.final_coverage + "\n"
		+ index + ">" + Arrays.toString(devices_new[index]) + " " + this.final_latency);
		
	}

	private int selectLeastLatency(int[][] devices_new, Cloudlet[][] cloudlets, int[][] latency) {
		// TODO Auto-generated method stub
		int min_latency_index = 0;
		int min_latency = Integer.MAX_VALUE;
		for(int i = 0; i < cloudlets.length; i++) {
			int sum_latency = totalLatency(devices_new[i], cloudlets[i], latency);
			//System.out.println();
			if(sum_latency < min_latency) {
				min_latency = sum_latency;
				this.final_latency = min_latency;
				//System.out.println(min_dist);
				min_latency_index = i;
			}
		}
		return min_latency_index;
	}
	
	private int totalLatency(int[] devices, Cloudlet[] cloudlets, int[][] latency) {
		// TODO Auto-generated method stub
		int sum_latency = 0;
		for(int j = 0; j < devices.length; j++) {
			int point_index = devices[j];
			if(cloudlets[point_index] != null) {
				sum_latency += latency[j][point_index];
				//System.out.print(latency[j][point_index] + " ");
			}
		}
		return sum_latency;
	}

	private int[][] maximizeCover(int[] devices, Cloudlet[][] cloudlets, ArrayList<EndDevice> E,
			ArrayList<CandidatePoint> P) {
		// TODO Auto-generated method stub
		int[][] devices_new = new int[cloudlets.length][E.size()];
		
		for(int i = 0; i < cloudlets.length; i++) {
			devices_new[i] = devices.clone();
			//System.out.println("->" + Arrays.toString(devices_new[i]));
			int[] processor = {0,0,0,0,0,0,0};
			int[] memory = {0,0,0,0,0,0,0};
			int[] storage = {0,0,0,0,0,0,0};
			
			//copy of the cloudlet specifications so that
			//they do get reset for next coverage maximization
			for(int j = 0; j < cloudlets[i].length; j++) {
				if(cloudlets[i][j] != null) {
					processor[j] = cloudlets[i][j].processor;
					memory[j] = cloudlets[i][j].memory;
					storage[j] = cloudlets[i][j].storage;
				}
			}
			
			for(int j = 0; j < E.size(); j++) {
				int index = devices_new[i][j];
				if(cloudlets[i][index] == null) {
					double min_dist = Double.MAX_VALUE;
					int min_dist_index = index;
					for(int k = 0; k < cloudlets[i].length; k++) {
						if(cloudlets[i][k] != null && inRangeAndCapacity(k, processor, memory, storage, cloudlets[i][k], E.get(j), P)) {
							double d = distance(P.get(k).xlocation, P.get(k).ylocation,
									E.get(j).xlocation, E.get(j).ylocation);
							if(d < min_dist) {
								min_dist = d;
								min_dist_index = k;
							}
							processor[k] -= E.get(i).processor;
							memory[k] -= E.get(i).memory;
							storage[k] -= E.get(i).storage;
						}
					}
					devices_new[i][j] = min_dist_index;
				}
			}
			//System.out.println(">-" + Arrays.toString(devices_new[i]));
		}
		return devices_new;
	}

	private boolean underThreshold(Cloudlet[][] cloudlets, HashMap<Cloudlet[], Double> cover_map, double threshold) {
		// TODO Auto-generated method stub
		for(Cloudlet[] c: cloudlets) {
			if(cover_map.get(c) < threshold) {
				return false;
			}
		}
		return true;
	}

	private int estimateOptimal(ArrayList<Cloudlet> C, ArrayList<EndDevice> E, int[][] cost) {
		// TODO Auto-generated method stub
		int total_proc_demand = 0;
		int estimate_optimal_cost = 0;
		
		for(EndDevice e: E) {
			total_proc_demand += e.processor;
		}
		
		int counter = C.size()-1;

		while(counter >= 0) {
			if(total_proc_demand >= C.get(counter).processor) {
				//System.out.println(total_proc_demand + " " + C.get(counter).processor + " " + cost[C.get(counter).id - 1][0]);
				total_proc_demand -= C.get(counter).processor;
				int min = Integer.MAX_VALUE;
				for (int element : cost[C.get(counter).id - 1]) {
				    min = Math.min(min, element);
				}
				//System.out.println(min);
				estimate_optimal_cost += min;
			}
			else if(total_proc_demand > 0) {
				//System.out.println(total_proc_demand + " " + C.get(counter).processor + " " + cost[C.get(counter).id - 1][0]);
				total_proc_demand -= C.get(counter).processor;
				int min = Integer.MAX_VALUE;
				for (int element : cost[C.get(counter).id - 1]) {
				    min = Math.min(min, element);
				}
				//System.out.println(min);
				estimate_optimal_cost += min;
			}
			counter--;
		}
		
		return estimate_optimal_cost;
	}

	/*private Cloudlet[] fittest(ArrayList<Cloudlet[]> population, HashMap<Cloudlet[], Double> cover_map, int[][] cost) {
		// TODO Auto-generated method stub
		Cloudlet[] best = population.get(0);
		
		for(int i = 1; i < population.size(); i++) {
			if(fitness(population.get(i), cost) < fitness(best, cost) ) {
				best = population.get(i);
			}
			else if(fitness(population.get(i), cost) == fitness(best, cost)) {
				if(cover_map.get(population.get(i)) > cover_map.get(best)) {
					best = population.get(i);
				}
			}
		}
		
		return best;
	}*/

	private double coverage(Cloudlet[] c1, int[] devices, ArrayList<EndDevice> E, ArrayList<CandidatePoint> P) {
		// TODO Auto-generated method stub
		double coverage = 0;
		int[] processor = {0,0,0,0,0,0,0};
		int[] memory = {0,0,0,0,0,0,0};
		int[] storage = {0,0,0,0,0,0,0};
		
		//copy of the cloudlet specifications so that
		//they do get reset for next coverage calculation
		for(int i = 0; i < c1.length; i++) {
			if(c1[i] != null) {
				processor[i] = c1[i].processor;
				memory[i] = c1[i].memory;
				storage[i] = c1[i].storage;
			}
		}
		
		//calculate coverage
		for(int i = 0; i < devices.length; i++) {
			int point = devices[i];
			if(c1[point] != null) {
				if(inRangeAndCapacity(point, processor, storage, memory, c1[point], E.get(i), P)) {
					coverage++;
					//System.out.println(processor[point] + " - " + c1[point].processor);
					processor[point] -= E.get(i).processor;
					memory[point] -= E.get(i).memory;
					storage[point] -= E.get(i).storage;
				}
			}
		}
		
		return coverage/E.size();
	}
	
	private boolean inRangeAndCapacity(int point, int[] processor, int[] storage, int[] memory, Cloudlet c1, EndDevice endDevice,
			ArrayList<CandidatePoint> P) {
		// TODO Auto-generated method stub
		double d = distance(P.get(point).xlocation, P.get(point).ylocation,
				endDevice.xlocation, endDevice.ylocation);
		if(d <= c1.radius ){
			if(endDevice.processor <= processor[point]) {
				if(endDevice.memory <= memory[point]) {
					if(endDevice.storage <= storage[point]){
						return true;
					}
				}
			}
		}
		return false;
	}

	private int fitness(Cloudlet[] b, int[][] cost) {
		// TODO Auto-generated method stub
		int total_cost = 0;
		
		for(int i = 0; i < b.length; i++) {
			if(b[i] != null) {
				total_cost += cost[b[i].id - 1][i];
			}
		}
		
		int dist_from_total = Math.abs(total_cost - this.estimate_optimal_cost);
		
		return dist_from_total;
	}
	
	/*private int selectLeastCost(Cloudlet[][] cloudlets, int[][] cost) {
		// TODO Auto-generated method stub
		int min_cost = Integer.MAX_VALUE;
		int min_cost_index = 0;
		
		for(int i = 0; i < cloudlets.length; i++) {
			int sum_cost = totalCost(cloudlets[i], cost);
			if(sum_cost < min_cost) {
				min_cost = sum_cost;
				this.final_cost = min_cost;
				min_cost_index = i;
			}
		}
		
		return min_cost_index;
	}*/
	
	private int totalCost(Cloudlet[] b, int[][] cost) {
		// TODO Auto-generated method stub
		int total_cost = 0;
		
		for(int i = 0; i < b.length; i++) {
			if(b[i] != null) {
				total_cost += cost[b[i].id - 1][i];
			}
		}
		
		return total_cost;
	}

	private Cloudlet[] mutate(Cloudlet[] A) {
		// TODO Auto-generated method stub
		Cloudlet[] mutated = A;
		//System.out.println("Mutate0 " + Arrays.toString(mutated));
		for(int i = 0; i < A.length; i++) {
			Random rand = new Random();
			int x = rand.nextInt(10);
			int y = rand.nextInt(4);
			
			//mutation probability is 0.1
			if(x < 1) {
				//System.out.println("Mutation happend!");
				//System.out.println(x);
				//System.out.println(y);
				if(y == 0) {
					mutated[i] = null;

				}
				else if(y == 1) {
					mutated[i] = new Cloudlet((this.num_large+this.num_medium+this.num_small), 200,200,200,3);
				}
				else if(y == 2) {
					mutated[i] = new Cloudlet((this.num_medium+this.num_small), 100,100,100,2);
				}
				else{
					//small cloudlet has id 1
					mutated[i] = new Cloudlet(this.num_small, 50,50,50,1);
				}
			}
			
		}
		
		/*correct the cloudlet numbers if they 
		became greater than available cloudlets 
		it's possible after both crossover and mutation*/
		int diff_small= cloudCount(mutated,"c3") - num_large;
		int diff_medium= cloudCount(mutated,"c2") - num_medium;
		int diff_large = cloudCount(mutated,"c1") - num_large;
		/*if(cloudCount(mutated,"c3") == 0 && cloudCount(mutated,"c2") ==0 && cloudCount(mutated,"c1") == 0) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		//System.out.println("Mutate1 " + Arrays.toString(mutated));
		if(diff_small > 0) {
			for(int i = 0, j = 0; i < diff_small; j++) {
					if(mutated[j] != null && mutated[j].toString().equals("c3")) {
						mutated[j] = null;
						i++;
					}
			}
		}
		if(diff_medium > 0) {
			for(int i = 0, j = 0; i < diff_medium; j++) {
				if(mutated[j] != null && mutated[j].toString().equals("c2")) {
					mutated[j] = null;
					i++;
				}
			}
		}
		if(diff_large > 0) {
			for(int i = 0, j = 0; i < diff_large; j++) {
				if(mutated[j] != null && mutated[j].toString().equals("c1")) {
					mutated[j] = null;
					i++;
				}
			}
		}
		
		//System.out.println("Mutate2 " + Arrays.toString(A));
		
		return mutated;
	}

	private int cloudCount(Cloudlet[] A, String string) {
		// TODO Auto-generated method stub
		int count = 0;
		
		for(Cloudlet a: A) {
			if(a != null && a.toString().equals(string)) {
				count++;
			}
		}
		
		return count;
	}

	private Cloudlet[][] crossOver(Cloudlet[] c1, Cloudlet[] c2) {
		// TODO Auto-generated method stub
		Cloudlet[][] crossed = new Cloudlet[c1.length][2];
		int mid_point = (int)Math.round(Math.ceil(c1.length/2.0));
		
		for(int i = mid_point; i < c1.length; i++) {
			Cloudlet buffer = c1[i];
			c1[i] = c2[i];
			c2[i] = buffer;
		}
		
		crossed[0] = c1;
		crossed[1] = c2;
		
		return crossed;
	}

	private int[] deviceAssignments(ArrayList<CandidatePoint> P, ArrayList<EndDevice> E) {
		int n = P.size();
		int v = E.size();
		
		int[] devices = new int[v];
		for(int i = 0; i < v; i++) {
			double min_dist = Double.MAX_VALUE;
			int min_dist_index = 0;
			for(int j = 0; j < n; j++) {
				double d = distance(E.get(i).xlocation, E.get(i).ylocation, P.get(j).xlocation, P.get(j).ylocation);
				if(d < min_dist) {
					min_dist = d;
					min_dist_index = j;
				}
				devices[i] = min_dist_index;
			}
		}
		return devices;
	}

	private Cloudlet[][] randomAssignments(ArrayList<Cloudlet> C, int n, int m) {
		// TODO Auto-generated method stub
		Cloudlet[][] cloudlets = new Cloudlet[m][n];
		
		while(m > 0) {
			ArrayList<Integer> indexes = new ArrayList<Integer>();
			for(int i = 0; i < n; i++) {
				indexes.add(i);
			}
			for(Cloudlet c: C) {
				Random rand = new Random();
				int x = rand.nextInt(indexes.size());
				//System.out.println(indexes.size() + " " + c.id);
				cloudlets[m-1][indexes.get(x)] = c;
				indexes.remove(x);
			}
			//System.out.println(Arrays.toString(cloudlets[m-1]));
			m -= 1;
		}
		
		return cloudlets;
	}
	
	/*private Cloudlet[][] randomAssignments(ArrayList<Cloudlet> C, int n, int m) {
		// TODO Auto-generated method stub
		Cloudlet[][] cloudlets = new Cloudlet[m][n];
		
		while(m > 0) {
			ArrayList<Integer> indexes = new ArrayList<Integer>();
			for(int i = 0; i < n; i++) {
				indexes.add(i);
			}
			for(int j = C.size()-1; j >= 2; j--) {
				Random rand = new Random();
				int x = rand.nextInt(indexes.size());
				//System.out.println(indexes.size() + " " + c.id);
				cloudlets[m-1][indexes.get(x)] = C.get(j);
				indexes.remove(x);
			}
			//System.out.println(Arrays.toString(cloudlets[m-1]));
			m -= 1;
		}
		
		return cloudlets;
	}*/
	
	public double distance(int x1, int y1, int x2, int y2) {
		int y_diff = y2-y1;
		int x_diff = x2-x1;
		
		double x_sqr = Math.pow(x_diff, 2);
		double y_sqr = Math.pow(y_diff, 2);
		
		double dist = Math.sqrt(x_sqr + y_sqr);
		
		return dist;
	}
	
	public int getCost() {
		return this.final_cost;
	}
	
	public double getLatency() {
		return this.final_latency;
	}
	
	public double getCoverage() {
		return this.final_coverage;
	}


}
