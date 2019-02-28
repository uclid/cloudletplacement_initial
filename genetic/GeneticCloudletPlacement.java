import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Random;

public class GeneticCloudletPlacement {

	private ArrayList<Cloudlet> C = null;
	private ArrayList<CandidatePoint> P = null;
	private ArrayList<EndDevice> E = null;
	private int[][] cost = {{}};
	private int[][] latency = {{}};
	private int estimate_optimal_cost = 0;
	private int min_needed_cloudlets = 0;
	private int final_cost = 0;
	private double final_latency = 0;
	private double final_coverage = 0;
	private int num_large = 0;
	private int num_medium = 0;
	private int num_small = 0;

	public GeneticCloudletPlacement(ArrayList<Cloudlet> cloudlets, ArrayList<CandidatePoint> points, 
			ArrayList<EndDevice> devices, int[][] cost, int[][] latency, int num_large, int num_medium, int num_small) {
		// TODO Auto-generated constructor stub
		this.C = cloudlets;
		this.P = points;
		this.E = devices;
		this.cost = cost;
		this.latency = latency;
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
	public void geneticAlgorithm(int assignment_size, double threshold) {
		
		int n = P.size();
		int v = E.size();
		int m = assignment_size;
		HashMap<Cloudlet[], Double> cover_map = new HashMap<Cloudlet[], Double>();
		HashMap<Cloudlet[], Integer> fitness_map = new HashMap<Cloudlet[], Integer>();
		
		CplexLPCloudletPlacement place = new CplexLPCloudletPlacement();
		int[] results = place.cplexModel(C, P, E, cost, latency);
		estimate_optimal_cost = results[0];
		min_needed_cloudlets = results[1];
		System.out.println("LP Optimal cost: " + estimate_optimal_cost + " Placed Cloudlets: " + min_needed_cloudlets);
		
		//variable holding assignments for the cloudlets
		Cloudlet[][] cloudlets = new Cloudlet[m][n];
		cloudlets = randomAssignments(n, m);
		//for(int i = 0; i < m; i++) {
			//System.out.println(Arrays.toString(cloudlets[i]));
		//}
		
		//variable holding assignment for devices
		int[] devices = new int[v];
		devices = deviceAssignments();
		//int null_counter = 0;
		CplexDeviceAssignments coverage = new CplexDeviceAssignments();
		int index = 0;
		CplexResults result = new CplexResults(false, 0, devices);
		
		do {
			//System.out.println(Arrays.toString(devices));
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
					else {
						c1 = oneRandomCloudlet();
					}
					if(!pq.isEmpty()) {
						c2 = pq.remove();
					}
					else {
						c2 = oneRandomCloudlet();
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
					
					int c1_fit = fitness(c1);
					int c2_fit = fitness(c2);
					int a1_fit = fitness(a1);
					int a2_fit = fitness(a2);
					
					int fC = Math.min(c1_fit, c2_fit);
					int fA = Math.min(a1_fit, a2_fit);
					
					//System.out.println(fC + " " + fA);
					
					/*System.out.println("\nBefore coverage");
					System.out.println("a1 " + Arrays.toString(a1) + " = " + coverage(a1.clone(), devices, E, P));
					System.out.println("a2 " + Arrays.toString(a2) + " = " + coverage(a2.clone(), devices, E, P));
					System.out.println("c1 " + Arrays.toString(c1) + " = " + coverage(c1.clone(), devices, E, P));
					System.out.println("c2 " + Arrays.toString(c2) + " = " + coverage(c2.clone(), devices, E, P));*/
					
					//System.out.println("1" + Arrays.toString(devices));
					double c1_cover = maxCoverage(devices, c1.clone());
					//System.out.println("2" + Arrays.toString(devices));
					double c2_cover = maxCoverage(devices, c2.clone());
					//System.out.println("3" + Arrays.toString(devices));
					double a1_cover = maxCoverage(devices, a1.clone());
					double a2_cover = maxCoverage(devices, a2.clone());
					//cover_map.put(c1, c1_cover);
					//cover_map.put(c2, c2_cover);
					//cover_map.put(a1, a1_cover);
					//cover_map.put(a2, a2_cover);
					//System.out.println("Here==" + cover_map.get(a1));
					/*if(fitness(c1) == 38) {
						null_counter++;
						if(null_counter > 3)
							System.exit(0);
					}*/
					
					double Vc = Math.min(c1_cover, c2_cover);
					double Va = Math.min(a1_cover, a2_cover);
					
					
					/*System.out.println("\nAfter coverage");
					System.out.println("a1 " + Arrays.toString(a1) + " = " + (fitness(a1) + this.estimate_optimal_cost) + " " + maxCover(devices, a1.clone()));
					System.out.println("a2 " + Arrays.toString(a2) + " = " + (fitness(a2) + this.estimate_optimal_cost) + " " + maxCover(devices, a2.clone()));
					System.out.println("c1 " + Arrays.toString(c1) + " = " + (fitness(c1) + this.estimate_optimal_cost) + " " + maxCover(devices, c1.clone()));
					System.out.println("c2 " + Arrays.toString(c2) + " = " + (fitness(c2) + this.estimate_optimal_cost) + " " + maxCover(devices, c2.clone()));
					System.out.println(Vc + " " + Va);*/
					
					if((fA < fC) || ((fA == fC) && (Va >= Vc))) {
							//System.out.println(cover_map.get(a1a2[i]));
							if(a1_cover >= threshold) {
								B.add(a1);
								cover_map.put(a1, a1_cover);
								fitness_map.put(a1, a1_fit);
							}
							if(a2_cover >= threshold) {
								B.add(a2);
								cover_map.put(a2, a2_cover);
								fitness_map.put(a2, a2_fit);
							}
					}
					else {
						if(c1_cover >= threshold) {
							B.add(c1);
							cover_map.put(c1, c1_cover);
							fitness_map.put(c1, c1_fit);
						}
						if(c2_cover >= threshold) {
							B.add(c2);
							cover_map.put(c2, c2_cover);
							fitness_map.put(c2, c2_fit);
						}
					}
				}
				//System.out.println(B.toString());
				Cloudlet[][] temp = new Cloudlet[B.size()][n];
				for(int i = 0; i<B.size(); i++) {
					temp[i] = B.get(i);
				}
				cloudlets = temp;
				/*System.out.println("\nBest so far");
				for(int i = 0; i<B.size(); i++) {
					System.out.println(Arrays.toString(B.get(i)) + " " + fitness(B.get(i), cost) + " " + cover_map.get(B.get(i)));
				}*/
				
			} while(!underThreshold(cloudlets, cover_map, threshold));
			
			index = selectLeastCost(cloudlets);
			result = coverage.cplexModel(C, P, E, cloudlets[index], latency);
			final_latency = result.objective;
			
		}while(!result.solved);
		System.out.println("\n"+ index + ">" + Arrays.toString(cloudlets[index]) + " " + final_cost + " " + final_latency);
		System.out.println(Arrays.toString(result.device_array));
		
	}
	
	private Cloudlet[] oneRandomCloudlet() {
		// TODO Auto-generated method stub
		Cloudlet[] cloudlets = new Cloudlet[P.size()];
		
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		for(int i = 0; i < P.size(); i++) {
			indexes.add(i);
		}
		for(int j = C.size()-1; j >= (C.size() - min_needed_cloudlets); j--) {
			Random rand = new Random();
			int x = rand.nextInt(indexes.size());
			//System.out.println(indexes.size() + " " + c.id);
			cloudlets[indexes.get(x)] = C.get(j);
			indexes.remove(x);
		}
		return cloudlets;
	}
	
	private int totalLatency(int[] devices, Cloudlet[] cloudlets) {
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

	private boolean underThreshold(Cloudlet[][] cloudlets, HashMap<Cloudlet[], Double> cover_map, double threshold) {
		// TODO Auto-generated method stub
		for(Cloudlet[] c: cloudlets) {
			if(cover_map.get(c) < threshold) {
				return false;
			}
		}
		return true;
	}
		
	private double maxCoverage(int[] devices, Cloudlet[] cloudlets) {
		// TODO Auto-generated method stub
		int[] devices_new = devices.clone();
		double covered = 0;
		
		//System.out.println("->" + Arrays.toString(devices_new[i]));
		int[] processor = new int[cloudlets.length];
		int[] memory = new int[cloudlets.length];
		int[] storage = new int[cloudlets.length];
		
		//copy of the cloudlet specifications so that
		//they do get reset for next coverage maximization
		for(int j = 0; j < cloudlets.length; j++) {
			if(cloudlets[j] != null) {
				processor[j] = cloudlets[j].processor;
				memory[j] = cloudlets[j].memory;
				storage[j] = cloudlets[j].storage;
			}
		}
		
		for(int j = 0; j < E.size(); j++) {
			int index = devices_new[j];
			//System.out.println(index + " " +cloudlets[index]);
			if(cloudlets[index] == null) {
				double min_dist = Double.MAX_VALUE;
				int min_dist_index = index;
				for(int k = 0; k < cloudlets.length; k++) {
					if(cloudlets[k] != null && inRangeAndCapacity(k, processor, memory, storage, cloudlets[k], E.get(j))) {
						double d = distance(P.get(k).xlocation, P.get(k).ylocation,
								E.get(j).xlocation, E.get(j).ylocation);
						if(d < min_dist) {
							min_dist = d;
							min_dist_index = k;
						}
					}
				}
				//System.out.println(devices_new[j] + " " + min_dist_index);
				devices_new[j] = min_dist_index;
				if(cloudlets[min_dist_index] != null) {
					//System.out.println("There");
					covered++;
					processor[min_dist_index] -= E.get(j).processor;
					memory[min_dist_index] -= E.get(j).memory;
					storage[min_dist_index] -= E.get(j).storage;
				}
			}
			else {
				//System.out.println("Here");
				if(inRangeAndCapacity(index, processor, storage, memory, cloudlets[index], E.get(j))) {
					covered++;
					//System.out.println(processor[point] + " - " + cloudlets[index].processor);
					processor[index] -= E.get(j).processor;
					memory[index] -= E.get(j).memory;
					storage[index] -= E.get(j).storage;
				}
			}
			//System.out.println("Covered: " + covered/E.size());
			//System.out.println(">-" + Arrays.toString(devices_new[i]));
		}
		return covered/E.size();
	}
	
	private boolean inRangeAndCapacity(int point, int[] processor, int[] storage, int[] memory, Cloudlet c1, EndDevice endDevice) {
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

	private int fitness(Cloudlet[] b) {
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
	
	private int selectLeastCost(Cloudlet[][] cloudlets) {
		// TODO Auto-generated method stub
		int min_cost = Integer.MAX_VALUE;
		int min_cost_index = 0;
		
		for(int i = 0; i < cloudlets.length; i++) {
			int sum_cost = totalCost(cloudlets[i]);
			if(sum_cost < min_cost) {
				min_cost = sum_cost;
				this.final_cost = min_cost;
				min_cost_index = i;
			}
		}
		
		return min_cost_index;
	}
	
	private int totalCost(Cloudlet[] b) {
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
					mutated[i] = new Cloudlet((this.num_large+this.num_medium+this.num_small), 650,650,650,9);
				}
				else if(y == 2) {
					mutated[i] = new Cloudlet((this.num_medium+this.num_small), 450,450,450,6);
				}
				else{
					//small cloudlet has id 1
					mutated[i] = new Cloudlet(this.num_small, 250,250,250,3);
				}
			}
			
		}
		
		mutated = validate(mutated);
		//System.out.println("Mutate2 " + Arrays.toString(A));
		
		return mutated;
	}
	
	private Cloudlet[] validate(Cloudlet[] C) {
		/*correct the cloudlet numbers if they 
		became greater than available cloudlets 
		it's possible after both crossover and mutation*/
		int diff_small= cloudCount(C,"c3") - num_large;
		int diff_medium= cloudCount(C,"c2") - num_medium;
		int diff_large = cloudCount(C,"c1") - num_large;
		/*if(cloudCount(C,"c3") == 0 && cloudCount(C,"c2") ==0 && cloudCount(C,"c1") == 0) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		//System.out.println("Mutate1 " + Arrays.toString(C));
		Random rand = new Random();
		if(diff_small > 0) {
			ArrayList<Integer> indexes = new ArrayList<Integer>();
			for(int j = 0; j < C.length; j++) {
					if(C[j] != null && C[j].toString().equals("c3")) {
						indexes.add(j);
					}
			}
			for(int i = 0; i < diff_small; i++) {
				int remove = rand.nextInt(indexes.size());
				C[indexes.get(remove)] = null;
				indexes.remove(remove);
			}
		}
		if(diff_medium > 0) {
			ArrayList<Integer> indexes = new ArrayList<Integer>();
			for(int j = 0; j < C.length; j++) {
					if(C[j] != null && C[j].toString().equals("c2")) {
						indexes.add(j);
					}
			}
			for(int i = 0; i < diff_medium; i++) {
				int remove = rand.nextInt(indexes.size());
				C[indexes.get(remove)] = null;
				indexes.remove(remove);
			}
		}
		if(diff_large > 0) {
			ArrayList<Integer> indexes = new ArrayList<Integer>();
			for(int j = 0; j < C.length; j++) {
					if(C[j] != null && C[j].toString().equals("c1")) {
						indexes.add(j);
					}
			}
			for(int i = 0; i < diff_large; i++) {
				int remove = rand.nextInt(indexes.size());
				C[indexes.get(remove)] = null;
				indexes.remove(remove);
			}
		}
		
		return C;
		
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
		
		crossed[0] = validate(c1.clone());
		crossed[1] = validate(c2.clone());
		
		return crossed;
	}

	private int[] deviceAssignments() {
		
		int[] devices = new int[E.size()];
		for(int i = 0; i < E.size(); i++) {
			double min_dist = Double.MAX_VALUE;
			int min_dist_index = 0;
			for(int j = 0; j < P.size(); j++) {
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
	
	private Cloudlet[][] randomAssignments(int n, int m) {
		// TODO Auto-generated method stub
		Cloudlet[][] cloudlets = new Cloudlet[m][n];
		
		while(m > 0) {
			ArrayList<Integer> indexes = new ArrayList<Integer>();
			for(int i = 0; i < n; i++) {
				indexes.add(i);
			}
			for(int j = C.size()-1; j >= (C.size() - min_needed_cloudlets); j--) {
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
	}
	
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
