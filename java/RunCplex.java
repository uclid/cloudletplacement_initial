import java.util.ArrayList;

public class RunCplex {
	
	public static void main(String[] args){
		
		//5 cloudlets as per our optimization example
		ArrayList<Cloudlet> cloudlets = new ArrayList<Cloudlet>();
		cloudlets.add(new Cloudlet(50, 50, 50, 1));
		cloudlets.add(new Cloudlet(100, 100, 100,2));
		cloudlets.add(new Cloudlet(100, 100, 100,2));
		cloudlets.add(new Cloudlet(100, 100, 100,2));
		cloudlets.add(new Cloudlet(200, 200, 200,3));
		
		//25 end devices as per our optimization example
		ArrayList<EndDevice> devices = new ArrayList<EndDevice>();
		devices.add(new EndDevice(10,10,10,2,0));
		devices.add(new EndDevice(20,20,20,2,0));
		devices.add(new EndDevice(10,10,10,3,0));
		devices.add(new EndDevice(20,20,20,3,0));
		devices.add(new EndDevice(10,10,10,3,0));
		devices.add(new EndDevice(10,10,10,0,1));
		devices.add(new EndDevice(20,20,20,1,1));
		devices.add(new EndDevice(10,10,10,2,1));
		devices.add(new EndDevice(20,20,20,2,1));
		devices.add(new EndDevice(20,20,20,2,1));
		devices.add(new EndDevice(10,10,10,4,1));
		devices.add(new EndDevice(20,20,20,0,2));
		devices.add(new EndDevice(10,10,10,1,2));
		devices.add(new EndDevice(20,20,20,1,2));
		devices.add(new EndDevice(10,10,10,1,2));
		devices.add(new EndDevice(10,10,10,2,2));
		devices.add(new EndDevice(20,20,20,3,2));
		devices.add(new EndDevice(10,10,10,3,2));
		devices.add(new EndDevice(20,20,20,0,3));
		devices.add(new EndDevice(20,20,20,0,3));
		devices.add(new EndDevice(10,10,10,0,3));
		devices.add(new EndDevice(20,20,20,0,3));
		devices.add(new EndDevice(10,10,10,1,3));
		devices.add(new EndDevice(20,20,20,4,3));
		devices.add(new EndDevice(10,10,10,4,3));
		
		//7 candidate points as per our optimization example
		ArrayList<CandidatePoint> points = new ArrayList<CandidatePoint>();
		points.add(new CandidatePoint(1,0));
		points.add(new CandidatePoint(3,0));
		points.add(new CandidatePoint(0,1));
		points.add(new CandidatePoint(3,1));
		points.add(new CandidatePoint(1,2));
		points.add(new CandidatePoint(2,2));
		points.add(new CandidatePoint(3,3));
		
		//Cost Matrix
		int[][] cost = {
			{ 1,  2,  1,  2,  2,  1,  1},
			{ 2,  3,  2,  3,  3,  2,  2},
			{ 2,  3,  2,  3,  3,  2,  2},
			{ 2,  3,  2,  3,  3,  2,  2},
			{ 5,  6,  5,  6,  6,  5,  5}
		}; 
		
		//Latency Matrix
		int[][] latency = {
           	{ 10,  10,  22,  14,  22,  20,  32},//l1
           	{ 10,  10,  22,  14,  22,  20,  32},
           			
           	{ 20,   0,  32,  10,  28,  22,  30},//l2
           	{ 20,   0,  32,  10,  28,  22,  30},
           	{ 20,   0,  32,  10,  28,  22,  30},
           			
           	{ 14,  32,   0,  30,  14,  22,  36},//l3
           			
           	{ 10,  14,  10,  20,  10,  14,  28},//l4
           			
           	{ 14,  14,  20,  10,  14,  10,  22},//l5
           	{ 14,  14,  20,  10,  14,  10,  22},
           	{ 14,  14,  20,  10,  14,  10,  22},
           			
           	{ 32,  14,  40,  10,  32,  22,  22},//l6
		               			
   			{ 22,  36,  10,  32,  10,  20,  32},//l7
   			
   			{ 20,  28,  14,  22,   0,  10,  22},//l8
   			{ 20,  28,  14,  22,   0,  10,  22},
   			{ 20,  28,  14,  22,   0,  10,  22},
   			
   			{ 22,  22,  12,  14,  10,   0,  14},//l9
   			
   			{ 28,  20,  32,  10,  20,  10,  10},//l10
   			{ 28,  20,  32,  10,  20,  10,  10},
   			
   			{ 32,  42,  20,  36,  14,  22,  30},//l11
   			{ 32,  42,  20,  36,  14,  22,  30},
   			{ 32,  42,  20,  36,  14,  22,  30},
   			{ 32,  42,  20,  36,  14,  22,  30},
   			
   			{ 30,  36,  22,  28,  10,  14,  20},//l12
   			
   			{ 45,  32,  45,  22,  32,  22,  10},//l13
   			{ 45,  32,  45,  22,  32,  22,  10}
		};
		
		
		CloudletPlacement place = new CloudletPlacement();
		place.cplexModel(cloudlets, points, devices, cost, latency);
		
	}

}
