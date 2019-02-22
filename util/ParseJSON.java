import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;

import org.json.*;

public class ParseJSON {
	
	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		// TODO Auto-generated method stub
		String content = new String(Files.readAllBytes(Paths.get("data-files/nycopendata/NYC_WiFi.json")), "UTF-8");
		
		JSONObject obj = new JSONObject(content);
		JSONArray array = obj.getJSONArray("data");
		
		int counter = 0;
		for(int i = 0; i < array.length(); i++) {
			String item = array.get(i).toString();
			//tokenize by splitting with commas except when enclosed by quotes
			String[] tokens = item.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
			if(item.contains("Central Harlem")) {
				System.out.println(item);
				System.out.println(tokens[14] + " " + tokens[16]);
				counter++;
			}
		}
		System.out.println("Total " + counter);
		
		//generate uninformly distributed indexes based on total points
		//divide counter to get the desired percentage of candidate points
		//candidate points are 20%, 30%, 40%, 50%
		Random rand = new Random();
		for(int i = 0; i < counter/5; i++) {
			System.out.println(rand.nextInt(counter));
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
		
		/* LinkNYC Usage data
		 * https://data.cityofnewyork.us/resource/69wu-b929.json
		 * 2019, about 686,697 per week
		 * Based on approx. population percentage:
		 * Central Harlem ~ 9613 per week ~ 1373 per day
		 * West Harlem (Manhattanville and Hamilton Heights) ~ 9201 per week ~ 1314 per day
		 * East Harlem ~ 10,025 per week ~ 1432 per day
		 * */
		
		/* Num cloudlets = 15% of total points
		 * large = 20% of total cloudlets
		 * medium = 40% of total cloudlets
		 * small = 20% of total cloudlets
		 * Num devices = 125% of total points
		 * high demand = 55% of total devices
		 * low demand = 45% of total devices 
		 * */
		

	}

}
