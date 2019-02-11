import java.util.ArrayList;

public class RunCplex {
	
	public static void main(String[] args){
		
		//5 cloudlets as per our optimization example
		int num_cloudlets = 5;
		//25 end devices as per our optimization example
		int num_devices = 25;
		//7 candidate points as per our optimization example
		int num_candidates = 7;
		
		ReadCSV reader = new ReadCSV();
		int[][] cloudlet_specs = reader.getCloudlets(num_cloudlets);
		int[][] device_specs = reader.getDevices(num_devices);
		int[][] cand_points = reader.getPoints(num_candidates);
		
		
		ArrayList<Cloudlet> cloudlets = new ArrayList<Cloudlet>();
		for(int i = 0; i < cloudlet_specs.length; i++) {
			cloudlets.add(new Cloudlet(i, cloudlet_specs[i][0], cloudlet_specs[i][1], 
					cloudlet_specs[i][2], cloudlet_specs[i][3]));
		}
		
		ArrayList<EndDevice> devices = new ArrayList<EndDevice>();
		for(int i = 0; i < device_specs.length; i++) {
			devices.add(new EndDevice(i, device_specs[i][0], device_specs[i][1],
					device_specs[i][2], device_specs[i][3], device_specs[i][4]));
		}
		
		ArrayList<CandidatePoint> points = new ArrayList<CandidatePoint>();
		for(int i = 0; i < cand_points.length; i++) {
			points.add(new CandidatePoint(i, cand_points[i][0], cand_points[i][1]));
		}
		
		//Cost Matrix
		int[][] cost = reader.getCosts(num_cloudlets, num_candidates);
		
		//Latency Matrix
		int[][] latency = reader.getLatencies(num_devices, num_candidates);
		
		//timing the CPLEX run
		long startTime = System.nanoTime();
		CplexCloudletPlacement place = new CplexCloudletPlacement();
		place.cplexModel(cloudlets, points, devices, cost, latency);
		long endTime = System.nanoTime();
		
		long duration = (endTime - startTime)/1000000;
		
		System.out.println("Time = " + duration + " ms");
		
	}

}
