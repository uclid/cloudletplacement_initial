import java.util.Arrays;
import java.util.Comparator;

public class AssignmentsComparator implements Comparator<Cloudlet[]> {
	public int[][] cost;
	
	public AssignmentsComparator(int[][] cost) {
		// TODO Auto-generated constructor stub
		this.cost = cost;
	}

	@Override
	public int compare(Cloudlet[] a, Cloudlet[] b) {
		// TODO Auto-generated method stub
		if(placement_cost(a) < placement_cost(b)) {
			return -1;
		}
		else if(placement_cost(a) > placement_cost(b)) {
			return 1;
		}
		
		return 0;
	}

	private int placement_cost(Cloudlet[] b) {
		// TODO Auto-generated method stub
		int total_cost = 0;
		
		for(int i = 0; i < b.length; i++) {
			if(b[i] != null) {
				total_cost += cost[b[i].id - 1][i];
			}
		}
		return total_cost;
	}

}
