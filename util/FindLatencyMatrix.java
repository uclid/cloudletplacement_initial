import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FindLatencyMatrix {
	
	public static void main (String[] args) {
		
		int[][] cand_points = new int[16][3];
    	
    	String csvFile = "data-files/east_harlem/east_points_20.csv";
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
        
      //device specifications have 5 fields
    	int[][] device_specs = new int[343][6];
    	
    	String csvFile2 = "data-files/east_harlem/east_device.csv";
        String line2 = "";
        String cvsSplitBy2 = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile2))) {
        	
        	int i = 0;
            while ((line2 = br.readLine()) != null) {

                // use comma as separator
                String[] item = line2.split(cvsSplitBy2);

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
        
        for(int i = 0; i < device_specs.length; i++) {
        	for(int j = 0; j < cand_points.length; j++) {
        		double dist = distance(device_specs[i][4], device_specs[i][5],cand_points[j][1],cand_points[j][2]);
        		int val = (int)Math.ceil(dist);
        		System.out.print(val);
        		if(j < cand_points.length-1) {
        			System.out.print(",");
        		}
        		
        	}
        	System.out.println();
        }
        
        
	}
	
	public static double distance(int x1, int y1, int x2, int y2) {
		int y_diff = y2-y1;
		int x_diff = x2-x1;
		
		double x_sqr = Math.pow(x_diff, 2);
		double y_sqr = Math.pow(y_diff, 2);
		
		double dist = Math.sqrt(x_sqr + y_sqr);
		
		return dist;
	}

}