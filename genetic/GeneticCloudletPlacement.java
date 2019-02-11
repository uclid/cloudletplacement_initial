import java.util.ArrayList;
import java.util.PriorityQueue;

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
			ArrayList<EndDevice> E, int[][] cost, int[][] latency) {
		
		int w = C.size();
		int n = P.size();
		int v = E.size();
		
		//variable holding assignments for the cloudlets
		int[][] y = new int[w][n];
		y = randomAssignments(w, n);
		
		//variable holding assignment for devices
		int[][] a = new int[v][n];
		a = deviceAssignments(v, n);
		
		while(!underThreshold(C, P, E)) {
			PriorityQueue<int[]> pq = new PriorityQueue(w);
			
		}
		
	}

	private boolean underThreshold(ArrayList<Cloudlet> c, ArrayList<CandidatePoint> p, ArrayList<EndDevice> e) {
		// TODO Auto-generated method stub
		return false;
	}

	private int[][] deviceAssignments(int v, int n) {
		// TODO Auto-generated method stub
		return null;
	}

	private int[][] randomAssignments(int w, int n) {
		// TODO Auto-generated method stub
		return null;
	}

}
