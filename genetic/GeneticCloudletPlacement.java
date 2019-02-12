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
		for(int i = 0; i < m; i++) {
			System.out.println(Arrays.toString(cloudlets[i]));
		}
		
		//variable holding assignment for devices
		int[] devices = new int[v];
		devices = deviceAssignments(P, E);
		
		System.out.println(Arrays.toString(devices));
		
		//replace this with while underThreshold()
		for(int i = 0; i < 5; i++) {
			
		}
		
		
		
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
