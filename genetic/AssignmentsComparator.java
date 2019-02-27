import java.util.ArrayList;
import java.util.Comparator;

public class AssignmentsComparator implements Comparator<Cloudlet[]> {
	public int[] devices;
	public ArrayList<Cloudlet> C;
	public ArrayList<CandidatePoint> P;
	public ArrayList<EndDevice> E;
	
	public AssignmentsComparator(int[] devices, ArrayList<Cloudlet> c, ArrayList<CandidatePoint> p, ArrayList<EndDevice> e) {
		// TODO Auto-generated constructor stub
		this.devices = devices;
		this.C = c;
		this.P = p;
		this.E = e;
	}

	@Override
	public int compare(Cloudlet[] a, Cloudlet[] b) {
		// TODO Auto-generated method stub
		if(placementCover(a) < placementCover(b)) {
			return -1;
		}
		else if(placementCover(a) > placementCover(b)) {
			return 1;
		}
		
		return 0;
	}

	private double placementCover(Cloudlet[] cloudlets) {
		// TODO Auto-generated method stub
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
	
	public double distance(int x1, int y1, int x2, int y2) {
		int y_diff = y2-y1;
		int x_diff = x2-x1;
		
		double x_sqr = Math.pow(x_diff, 2);
		double y_sqr = Math.pow(y_diff, 2);
		
		double dist = Math.sqrt(x_sqr + y_sqr);
		
		return dist;
	}
	
	/*private int placementCost(Cloudlet[] b) {
		// TODO Auto-generated method stub
		int total_cost = 0;
		
		for(int i = 0; i < b.length; i++) {
			if(b[i] != null) {
				total_cost += cost[b[i].id - 1][i];
			}
		}
		return total_cost;
	}*/
	
	/*@Override
	public int compare(Cloudlet[] a, Cloudlet[] b) {
		// TODO Auto-generated method stub
		if(leastZeroes(a) < leastZeroes(b)) {
			return -1;
		}
		else if(leastZeroes(a) > leastZeroes(b)) {
			return 1;
		}
		
		return 0;
	}

	private int leastZeroes(Cloudlet[] b) {
		// TODO Auto-generated method stub
		int zeroes = 0;
		
		for(int i = 0; i < b.length; i++) {
			if(b[i] == null) {
				zeroes++;
			}
		}
		return zeroes;
	}*/

}
