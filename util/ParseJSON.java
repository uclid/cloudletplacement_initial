import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.json.*;

public class ParseJSON {
	
	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		// TODO Auto-generated method stub
		String content = new String(Files.readAllBytes(Paths.get("data-files/nycopendata/NYC_WiFi.json")), "UTF-8");
		
		JSONObject obj = new JSONObject(content);
		JSONArray array = obj.getJSONArray("data");
		
		int counter = 0;
		Set<String> myWiFi = new HashSet<String>();
		ArrayList<Double> myWiFiLat = new ArrayList<Double>();
		ArrayList<Double> myWiFiLon = new ArrayList<Double>();
		for(int i = 0; i < array.length(); i++) {
			String item = array.get(i).toString();
			//tokenize by splitting with commas except when enclosed by quotes
			String[] tokens = item.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
			if(item.contains("East Harlem")) {
				System.out.println(item);
				//System.out.println(tokens[14] + " " + tokens[16]);
				myWiFi.add(tokens[14] + " " + tokens[16]);
				counter++;
			}
		}
		System.out.println("Total " + counter);
		System.out.println("Total Set " + myWiFi.size());
		
		for(String s: myWiFi) {
			String[] xy = s.split(" ");
			double n = Double.parseDouble(xy[0].replaceAll("^\"|\"$", ""));
			double m = Double.parseDouble(xy[1].replaceAll("^\"|\"$", ""));
			myWiFiLat.add(n);
			myWiFiLon.add(m);
			//System.out.println(n + " " + m);
		}
		
		double max_x = Collections.max(myWiFiLon);
		double min_x = Collections.min(myWiFiLon);
		double max_y = Collections.max(myWiFiLat);
		double min_y = Collections.min(myWiFiLat);
		ArrayList<Double> x = new ArrayList<Double>();
		ArrayList<Double> y = new ArrayList<Double>();
		
		//find the range ratio of x to y
		System.out.println((max_x-min_x)/(max_y-min_y));
		
		//found it to be roughly 70:100 for Central Harlem
		//the smallest grid with as many at least 183 points
		//and this ratio is 14x20
		//found it 1:2 in East Harlem
		//the smallest grid with at least 81 points would be
		//7*14
		for(Double longitude:myWiFiLon) {
			x.add(7*(longitude - min_x)/(max_x - min_x));
		}
		
		for(Double latitude:myWiFiLat) {
			y.add(14*(latitude - min_y)/(max_y - min_y));
		}
		
		Set<String> final_coords = new HashSet<String>();
		for(int i = 0; i < myWiFiLon.size(); i++) {
			System.out.println((i+1) + " " + myWiFiLon.get(i) + " " + myWiFiLat.get(i));
			System.out.println((i+1) + " " + Math.round(x.get(i)) + " " + Math.round(y.get(i)));
			final_coords.add(Math.round(x.get(i)) + " " + Math.round(y.get(i)));
		}
		System.out.println(final_coords.size());
		
		
		//generate uninformly distributed indexes based on total points
		//divide counter to get the desired percentage of candidate points
		//candidate points are 10%, 15%, 20%
		int points = (int)Math.ceil(0.2*myWiFi.size());
		int xrange = 7;
		int yrange = 14;
		Random rand = new Random();
		System.out.println("------------------------------------");
		for(int i = 0; i < points; i++) {
			System.out.println(rand.nextInt(xrange+1));
		}
		System.out.println("------------------------------------");
		for(int i = 0; i < points; i++) {
			System.out.println(rand.nextInt(yrange+1));
		}
		
		//uniform random locations for n devices
		int n = 343;
		System.out.println("------------------------------------");
		for(int i = 0; i < n; i++) {
			System.out.println(rand.nextInt(xrange+1));
		}
		System.out.println("------------------------------------");
		for(int i = 0; i < n; i++) {
			System.out.println(rand.nextInt(yrange+1));
		}
		
		/*
		 * NSF Announces New York City as Testbed for New Wave of Mobile Technology
		 * https://engineering.columbia.edu/news/nsf-cosmos-testbed
		 * includes West Harlem, 5G
		 * */
		/*
		 * Population 2010
		 * NYC 8.194 million (2010)
		 * Central Harlem was at 115,000 (1.4%)
		 * West Harlem (Manhattanville and Hamilton Heights) was at 110,193 (1.34%)
		 * East Harlem was at 120,000 (1.46%)
		 * */
		
		/* Unique WiFi Hotspot locations
		 * Central Harlem 183
		 * West Harlem (Manhattanville and Hamilton Heights) 2 +  19 + 58 = 79
		 * East Harlem 81
		 * */
		
		/* LinkNYC Usage data
		 * https://data.cityofnewyork.us/resource/69wu-b929.json
		 * 2019, about 686,697 per week
		 * Based on approx. population percentage:
		 * Central Harlem ~ 9613 per week ~ 1373 per day
		 * West Harlem (Manhattanville and Hamilton Heights) ~ 9201 per week ~ 1314 per day
		 * East Harlem ~ 10,025 per week ~ 1432 per day
		 * */
		
		/* Num cloudlets = 1% of per day usage
		 * large = 20% of num cloudlets
		 * medium = 40% of num cloudlets
		 * small = 20% of num cloudlets
		 * Num devices = 25% of per day usage
		 * high demand = 50% of num devices
		 * low demand = 50% of num devices
		 * Cloud specs: 200, 400, 600 -> c3, c2, c1
		 * Device specs: 20, 10 -> high, low
		 * */
		
		/*Some refs
		 * https://www.cs.cmu.edu/~satya/docdir/satya-ieeepvc-cloudlets-2009.pdf
		 * */
		

	}

}
