import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Random;

public class GeneticCloudletPlacement {

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
	 */
	public void geneticAlgorithm(ArrayList<Cloudlet> C, ArrayList<CandidatePoint> P,
			ArrayList<EndDevice> E, int[][] cost, int[][] latency, int assignment_size) {
		
		int w = C.size();
		int n = P.size();
		int v = E.size();
		int m = assignment_size;
		
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
		
			ArrayList<Cloudlet[]> B = null;
			PriorityQueue<Cloudlet[]> pq = new PriorityQueue<Cloudlet[]>(m, new AssignmentsComparator(cost));
			for(int i = 0; i < m; i++) {
				pq.add(cloudlets[i]);
			}
			
			Cloudlet[] c1 = pq.remove();
			Cloudlet[] c2 = pq.remove();
			//System.out.println(Arrays.toString(pq.remove()));
			
			Cloudlet[][] a1a2 = new Cloudlet[m][2];
			
			//for crossover probability
			Random rand = new Random();
			int x = rand.nextInt(10);
			//System.out.println(x);
			
			System.out.println("Before Crossover");
			System.out.println("c1 " + Arrays.toString(c1) + "= " + fitness(c1, cost));
			System.out.println("c2 " + Arrays.toString(c2) + "= " + fitness(c2, cost));
			
			//crossover probability is 0.5 for now
			if(x >= 5) {
				System.out.println("Crossover happend!");
				a1a2 = crossOver(c1.clone(),c2.clone());
			}
			else {
				a1a2[0] = c1.clone();
				a1a2[1] = c2.clone();
			}

			System.out.println("\nAfter Crossover, before mutation");
			System.out.println("a1 " + Arrays.toString(a1a2[0])+ "= " + fitness(a1a2[0], cost));
			System.out.println("a2 " + Arrays.toString(a1a2[1])+ "= " + fitness(a1a2[1], cost));
			
			a1a2[0] = mutate(a1a2[0]);
			a1a2[1] = mutate(a1a2[1]);
			
			System.out.println("\nAfter mutation");
			System.out.println("a1 " + Arrays.toString(a1a2[0])+ "= " + fitness(a1a2[0], cost));
			System.out.println("a2 " + Arrays.toString(a1a2[1])+ "= " + fitness(a1a2[1], cost));
			System.out.println("c1 " + Arrays.toString(c1) + "= " + fitness(c1, cost));
			System.out.println("c2 " + Arrays.toString(c2) + "= " + fitness(c2, cost));
			
			int fC = Math.min(fitness(c1, cost), fitness(c2, cost));
			int fA = Math.min(fitness(a1a2[0], cost), fitness(a1a2[1], cost));
			
			System.out.println(fC + " " + fA);
			
			System.out.println("\nBefore coverage");
			System.out.println("a1 " + Arrays.toString(a1a2[0]) + " = " + coverage(a1a2[0].clone(), devices, E, P));
			System.out.println("a2 " + Arrays.toString(a1a2[1]) + " = " + coverage(a1a2[1].clone(), devices, E, P));
			System.out.println("c1 " + Arrays.toString(c1) + " = " + coverage(c1.clone(), devices, E, P));
			System.out.println("c2 " + Arrays.toString(c2) + " = " + coverage(c2.clone(), devices, E, P));
			
			double Vc = Math.min(coverage(c1.clone(), devices, E, P), coverage(c2.clone(), devices, E, P));
			double Va = Math.min(coverage(a1a2[0].clone(), devices, E, P), coverage(a1a2[1].clone(), devices, E, P));
			
			
			System.out.println("\nAfter coverage");
			System.out.println("a1 " + Arrays.toString(a1a2[0]) + " = " + coverage(a1a2[0].clone(), devices, E, P));
			System.out.println("a2 " + Arrays.toString(a1a2[1]) + " = " + coverage(a1a2[1].clone(), devices, E, P));
			System.out.println("c1 " + Arrays.toString(c1) + " = " + coverage(c1.clone(), devices, E, P));
			System.out.println("c2 " + Arrays.toString(c2) + " = " + coverage(c2.clone(), devices, E, P));
			System.out.println(Vc + " " + Va);
			
			
			
	}

	private double coverage(Cloudlet[] c1, int[] devices, ArrayList<EndDevice> E, ArrayList<CandidatePoint> P) {
		// TODO Auto-generated method stub
		double coverage = 0;
		int[] processor = {0,0,0,0,0,0,0};
		int[] memory = {0,0,0,0,0,0,0};
		int[] storage = {0,0,0,0,0,0,0};
		
		for(int i = 0; i < c1.length; i++) {
			if(c1[i] != null) {
				processor[i] = c1[i].processor;
				memory[i] = c1[i].memory;
				storage[i] = c1[i].storage;
			}
		}
		
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
		return total_cost;
	}

	private Cloudlet[] mutate(Cloudlet[] A) {
		// TODO Auto-generated method stub
		Cloudlet[] mutated = A;
		
		for(int i = 0; i < A.length; i++) {
			Random rand = new Random();
			int x = rand.nextInt(10);
			int y = rand.nextInt(4);
			
			//mutation probability is 0.1
			if(x < 1) {
				System.out.println("Mutation happend!");
				//System.out.println(x);
				//System.out.println(y);
				if(y == 0) {
					mutated[i] = null;
				}
				else if(y == 1) {
					//large cloudlet has id 5
					mutated[i] = new Cloudlet(5, 200,200,200,3);
				}
				else if(y == 2) {
					//medium cloudlet has id 2, 3 or 4
					mutated[i] = new Cloudlet(4, 100,100,100,2);
				}
				else{
					//small cloudlet has id 1
					mutated[i] = new Cloudlet(1, 50,50,50,1);
				}
			}
			
		}
		
		return mutated;
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
			for(Cloudlet c: C) {
				Random rand = new Random();
				int x = rand.nextInt(n);
				
				cloudlets[m-1][x] = c;	
			}
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

}
