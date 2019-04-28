import java.util.ArrayList;

public class TestGenetic {
	
	public static void main(String[] args){
		
		//5 cloudlets as per our optimization example
		int num_cloudlets = 14;
		//number of large cloudlets
		int num_large = 0;
		//number of medium cloudlets
		int num_medium = 0;
		//nuumber of small cloudelts
		int num_small = 0;
		//25 end devices as per our optimization example
		int num_devices = 343;
		//7 candidate points as per our optimization example
		int num_candidates = 16;
		//number of assignments in the algorithm seed set
		int assignment_size = 10;
		//threshold value for coverage, 95% for now
		double threshold = 0.95;
		
		ReadCSV reader = new ReadCSV();
		int[][] cloudlet_specs = reader.getCloudlets(num_cloudlets);
		int[][] device_specs = reader.getDevices(num_devices);
		int[][] cand_points = reader.getPoints(num_candidates);
		
		
		ArrayList<Cloudlet> cloudlets = new ArrayList<Cloudlet>();
		for(int i = 0; i < cloudlet_specs.length; i++) {
			cloudlets.add(new Cloudlet(cloudlet_specs[i][0], cloudlet_specs[i][1], 
					cloudlet_specs[i][2], cloudlet_specs[i][3], cloudlet_specs[i][4]));
			if(cloudlet_specs[i][1] == 650) {
				num_large++;
			}
			else if(cloudlet_specs[i][1] == 450) {
				num_medium++;
			}
			else {
				num_small++;
			}
		}
		
		ArrayList<EndDevice> devices = new ArrayList<EndDevice>();
		for(int i = 0; i < device_specs.length; i++) {
			devices.add(new EndDevice(device_specs[i][0], device_specs[i][1],
					device_specs[i][2], device_specs[i][3], device_specs[i][4], device_specs[i][5]));
		}
		
		ArrayList<CandidatePoint> points = new ArrayList<CandidatePoint>();
		for(int i = 0; i < cand_points.length; i++) {
			points.add(new CandidatePoint(cand_points[i][0], cand_points[i][1], cand_points[i][2]));
		}
		
		//Cost Matrix
		int[][] cost = reader.getCosts(num_cloudlets, num_candidates);
		
		//Latency Matrix
		int[][] latency = reader.getLatencies(num_devices, num_candidates);
		
		//System.out.println(num_large + " " + num_medium + " " + num_small);
		int[] costs = new int[100];
		double[] latencies = new double[100];
		long[] times = new long[100];
		
		for(int i = 0; i < 100; i++) {
			long startTime = System.nanoTime();
			GeneticCloudletPlacement place = new GeneticCloudletPlacement(cloudlets, points, devices, cost, latency,
					num_large, num_medium, num_small);
			
			place.geneticAlgorithm(assignment_size, threshold);
			long endTime = System.nanoTime();
			
			long duration = (endTime - startTime)/1000000;
			costs[i] = place.getCost();
			latencies[i] = place.getLatency();
			times[i] = duration;
			
		}
		
		//System.out.println("\nTime = " + duration + " ms");
		System.out.println("Costs");
		for(int i = 0; i < 100; i++) {
			System.out.println(costs[i]);
		}
		System.out.println("Latencies");
		for(int i = 0; i < 100; i++) {
			System.out.println(latencies[i]);
		}
		System.out.println("Times");
		for(int i = 0; i < 100; i++) {
			System.out.println(times[i]);
		}
		
		
	}

}