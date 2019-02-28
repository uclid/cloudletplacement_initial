
public class CplexResults {
	public boolean solved;
	public double objective;
	public int[] device_array;
	
	public CplexResults(boolean solved, double objective, int[] device_array) {
		this.solved = solved;
		this.objective = objective;
		this.device_array = device_array;
	}

}
