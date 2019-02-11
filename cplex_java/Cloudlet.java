public class Cloudlet {
	
	public int processor;
	public int memory;
	public int storage;
	public int radius;
	
	/**
	 * @author Dixit Bhatta
	 * @param processor processing capacity
	 * @param memory memory capacity
	 * @param storage storage capacity
	 * @param radius coverage radius
	 */
	public Cloudlet(int processor, int memory, int storage, int radius) {
		this.processor = processor;
		this.memory = memory;
		this.storage = storage;
		this.radius = radius;
	}

}
