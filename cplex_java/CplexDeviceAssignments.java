import java.util.ArrayList;
import java.util.Arrays;

import ilog.concert.*;
import ilog.cplex.*;

public class CplexDeviceAssignments {
	
	/**
	 * @author Dixit Bhatta
	 * The method takes the decision variables, constraints,
	 * and solution arrays as arguments and displays the
	 * solution if there a feasible one.
	 * @param C set of cloudlets
	 * @param P set of candidate points
	 * @param V set of end devices
	 * @param cost placement cost matrix
	 * @param latency latency matrix
	 */
	public CplexResults cplexModel(ArrayList<Cloudlet> C, ArrayList<CandidatePoint> P , 
			ArrayList<EndDevice> E, Cloudlet[] cloudlets, int[][] latency) {
		
		int w = C.size();
		int n = P.size();
		int v = E.size();
		boolean isSolved = false;
		double objValue = 0;
		int[] devices_array = new int[v];
		
		try {
			//new model object
			IloCplex model = new IloCplex();
			//System.out.println(Arrays.toString(cloudlets));
			//the decision variable y_{jk}
			IloIntVar[][] y = new IloIntVar[w][n];
			//specifying range for the decision variable, 0 or 1
			for(int i = 0; i < w; i++) {
				for(int k = 0; k < n; k++) {
					y[i][k] = model.boolVar();
					y[i][k].setLB(0);
					y[i][k].setUB(0);
				}
			}
			
			for(int j = 0; j < n; j++) {
					if(cloudlets[j] != null) {
						//System.out.println(cloudlets[j] + " " + (cloudlets[j].id-1) + " " + j);
						y[cloudlets[j].id-1][j].setLB(1);
						y[cloudlets[j].id-1][j].setUB(1);
					}
			}
			
			//the decision variable a_{ik}
			IloIntVar[][] a = new IloIntVar[v][n];
			//specifying range for the decision variable, 0 or 1
			for(int i = 0; i < v; i++) {
				for(int k = 0; k < n; k++) {
					a[i][k] = model.boolVar();
				}
			}
			
			//latency minimization
			
			IloLinearNumExpr latency_obj = model.linearNumExpr();
			for(int i = 0; i < v; i++) {
				for(int k = 0; k < n; k++) {
					latency_obj.addTerm(a[i][k], latency[i][k]);
				}
			}
			
			//minimize the objective function
			//model.addMinimize(cost_obj);
			model.addMinimize(latency_obj);
			
			/*Constraint 2: Each end device must be within coverage
			 * range of some cloudlet.*/
			for(int i = 0; i < v; i++) {
				for(int k = 0; k < n; k++) {
					double dist = distance(E.get(i).xlocation, E.get(i).ylocation, P.get(k).xlocation, P.get(k).ylocation);
					IloLinearNumExpr radius = model.linearNumExpr();
					for(int j = 0; j < w; j++) {
						radius.addTerm(y[j][k], C.get(j).radius);
					}
					IloLinearNumExpr covered = model.linearNumExpr();
					covered.addTerm(a[i][k], dist);
					model.addLe(covered, radius);
				}
			}
			
			
			/*Constraint 3: Sum of memory demand of served end 
			 * devices should be less than or equal to serving cloudlet.*/
			for(int k = 0; k < n; k++) {
				IloLinearNumExpr devices_mem = model.linearNumExpr();
				for(int i = 0; i < v; i++) {
					devices_mem.addTerm(a[i][k], E.get(i).memory);
				}
				IloLinearNumExpr cloudlet_mem = model.linearNumExpr();
				for(int j = 0; j < w; j++) {
					cloudlet_mem.addTerm(y[j][k], C.get(j).memory);
				}
				model.addLe(devices_mem, cloudlet_mem);
			}
			
			/*Constraint 4: Sum of storage demand of served end 
			 * devices should be less than or equal to serving cloudlet.*/
			for(int k = 0; k < n; k++) {
				IloLinearNumExpr devices_stor = model.linearNumExpr();
				for(int i = 0; i < v; i++) {
					devices_stor.addTerm(a[i][k], E.get(i).storage);
				}
				IloLinearNumExpr cloudlet_stor = model.linearNumExpr();
				for(int j = 0; j < w; j++) {
					cloudlet_stor.addTerm(y[j][k], C.get(j).storage);
				}
				model.addLe(devices_stor, cloudlet_stor);
			}

			/*Constraint 5: Sum of processing demand of served end devices
			 * should be less than or equal to serving cloudlet.*/
			for(int k = 0; k < n; k++) {
				IloLinearNumExpr devices_proc = model.linearNumExpr();
				for(int i = 0; i < v; i++) {
					devices_proc.addTerm(a[i][k], E.get(i).processor);
				}
				IloLinearNumExpr cloudlet_proc = model.linearNumExpr();
				for(int j = 0; j < w; j++) {
					cloudlet_proc.addTerm(y[j][k], C.get(j).processor);
				}
				model.addLe(devices_proc, cloudlet_proc);
			}
			
			/*Constraint 6: An end device can be served from a candidate 
			 * point only if there is at least one cloudlet placed there.*/
			for(int i = 0; i < v; i++) {
				for(int k = 0; k < n; k++) {
					IloLinearNumExpr cloudlet_placed = model.linearNumExpr();
					for(int j = 0; j < w; j++) {
						cloudlet_placed.addTerm(y[j][k], 1);
					}
					model.addLe(a[i][k], cloudlet_placed);
				}
			}
			
			/*Constraint 9: All end devices must be served, each 
			 * from exactly one candidate point.*/
			for(int i = 0; i < v; i++) {
				IloLinearNumExpr point = model.linearNumExpr();
				for(int k=0; k < n; k++) {
					point.addTerm(a[i][k], 1);
				}
				model.addEq(point, 1);
			}
			
			/*
			 * Now towards solving the model
			 * */
			isSolved = model.solve();
			if(isSolved) {
				objValue = model.getObjValue();
				System.out.println("\nObjective value is: " + objValue);
				//System.out.print("\nCloudlet Assignments\n");
				/*for(int j = 0; j < w; j++) {
					for(int k=0; k < n; k++) {
						if(model.getValue(y[j][k]) == 1)
							System.out.print(" y[" + j + "][" + k + "] = " + model.getValue(y[j][k]));
					}
					//System.out.println("\n");
				}*/
				//System.out.print("\nDevice Assignments\n");
				//System.out.print("[");
				for(int i = 0; i < v; i++) {
					for(int k=0; k < n; k++) {
						if(model.getValue(a[i][k]) == 1) {
							devices_array[i] = k;
							//System.out.print(k + ",");
						}
					}
					//System.out.println("\n");
				}
				//System.out.print("]");
				
			}
			
		
		}
		catch(IloException e) {
			System.out.println(e.getMessage());
		}
		
		//System.out.println(Arrays.toString(devices_array));
		CplexResults c = new CplexResults(isSolved, objValue, devices_array);
		
		return c;
	}
	
	public double distance(int x1, int y1, int x2, int y2) {
		int y_diff = y2-y1;
		int x_diff = x2-x1;
		
		double x_sqr = Math.pow(x_diff, 2);
		double y_sqr = Math.pow(y_diff, 2);
		
		double dist = Math.sqrt(x_sqr + y_sqr);
		
		return dist;
	}
}