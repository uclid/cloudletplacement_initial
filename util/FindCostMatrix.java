import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FindCostMatrix {
	
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
        
        int[][] cloudlet_specs = new int[14][5];
    	
    	String csvFile2 = "data-files/east_harlem/east_cloudlet.csv";
        String line2 = "";
        String cvsSplitBy2 = ",";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile2))) {
        	
        	int i = 0;
            while ((line2 = br.readLine()) != null) {

                // use comma as separator
                String[] item = line2.split(cvsSplitBy2);

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
        
        for(int i = 0; i < cloudlet_specs.length; i++) {
        	for(int j = 0; j < cand_points.length; j++) {
        		int x = 0;
        		if(cloudlet_specs[i][1] == 250) {
        			x = 1;
        		}
        		else if(cloudlet_specs[i][1] == 450) {
        			x = 2;
        		}
        		else {
        			x = 3;
        		}
        		int y = 0;
        		if(cand_points[j][0] == 1 || cand_points[j][0] == 5 || cand_points[j][0] == 8 || cand_points[j][0] == 14 || 
        				cand_points[j][0] == 9 || cand_points[j][0] == 12 || cand_points[j][0] == 13 
        				|| cand_points[j][0] == 19 || cand_points[j][0] == 21 || cand_points[j][0] == 23
        				|| cand_points[j][0] == 27 || cand_points[j][0] == 31) {
        			y = 2;
        		}
        		else if(cand_points[j][0] == 7 || cand_points[j][0] == 6 || cand_points[j][0] == 3 || cand_points[j][0] == 20 || 
        				cand_points[j][0] == 11 || cand_points[j][0] == 15 || cand_points[j][0] == 34
        				|| cand_points[j][0] == 17 || cand_points[j][0] == 33) {
        			y = 3;
        		}
        		else {
        			y = 1;
        		}
        		System.out.print(x+y);
        		if(j < cand_points.length-1) {
        			System.out.print(",");
        		}
        		
        	}
        	System.out.println();
        }
        
        
	}

}
