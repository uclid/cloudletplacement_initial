public class EndDevice {
	public int processor;
	public int memory;
	public int storage;
	public int xlocation;
	public int ylocation;
	
	/**
	 * @author Dixit Bhatta
	 * @param processor processing demand
	 * @param memory memory demand
	 * @param storage storage demand
	 * @param xlocation x-coordinate in the grid
	 * @param ylocation y-coordinate in the grid
	 */
	public EndDevice(int processor, int memory, int storage, int xlocation, int ylocation) {
		this.processor = processor;
		this.memory = memory;
		this.storage = storage;
		this.xlocation = xlocation;
		this.ylocation = ylocation;
	}

}
