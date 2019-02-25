import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

public class ReadCSV {
    
    public int[][] getCloudlets(int num_cloudlets) {
    	//cloudlet specifications have 4 fields
    	int[][] cloudlet_specs = new int[num_cloudlets][5];
    	
    	String csvFile = "data-files/central_harlem/central_cloudlet.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	
        	int i = 0;
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] item = line.split(cvsSplitBy);

                cloudlet_specs[i][0] = Integer.parseInt(item[0]);
                cloudlet_specs[i][1] = Integer.parseInt(item[1]);
                cloudlet_specs[i][2] = Integer.parseInt(item[2]);
                cloudlet_specs[i][3] = Integer.parseInt(item[3]);
                cloudlet_specs[i][4] = Integer.parseInt(item[4]);
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	return cloudlet_specs;
    }
    
    public int[][] getDevices(int num_devices) {
    	//device specifications have 5 fields
    	int[][] device_specs = new int[num_devices][6];
    	
    	String csvFile = "data-files/central_harlem/central_device.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	
        	int i = 0;
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] item = line.split(cvsSplitBy);

                device_specs[i][0] = Integer.parseInt(item[0]);
                device_specs[i][1] = Integer.parseInt(item[1]);
                device_specs[i][2] = Integer.parseInt(item[2]);
                device_specs[i][3] = Integer.parseInt(item[3]);
                device_specs[i][4] = Integer.parseInt(item[4]);
                device_specs[i][5] = Integer.parseInt(item[5]);
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	return device_specs;
    }
    
    public int[][] getPoints(int num_candidates) {
    	//device specifications have 5 fields
    	int[][] cand_points = new int[num_candidates][3];
    	
    	String csvFile = "data-files/central_harlem/central_points_10.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	
        	int i = 0;
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] item = line.split(cvsSplitBy);

                cand_points[i][0] = Integer.parseInt(item[0]);
                cand_points[i][1] = Integer.parseInt(item[1]);
                cand_points[i][2] = Integer.parseInt(item[2]);
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	return cand_points;
    }
    
    public int[][] getCosts(int num_cloudlets, int num_candidates) {
    	//device specifications have 5 fields
    	int[][] costs = new int[num_cloudlets][num_candidates];
    	
    	String csvFile = "data-files/central_harlem/central_costs_10.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	
        	int i = 0;
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] item = line.split(cvsSplitBy);
                for(int j = 0; j < num_candidates; j++) {
                    costs[i][j] = Integer.parseInt(item[j]);
                }
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	return costs;
    }

    public int[][] getLatencies(int num_devices, int num_candidates) {
    	//device specifications have 5 fields
    	int[][] latency = new int[num_devices][num_candidates];
    	
    	String csvFile = "data-files/central_harlem/central_latencies_10.csv";
        String line = "";
        String cvsSplitBy = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
        	
        	int i = 0;
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] item = line.split(cvsSplitBy);
                for(int j = 0; j < num_candidates; j++) {
                    latency[i][j] = Integer.parseInt(item[j]);
                }
                i++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    	
    	return latency;
    }
}
