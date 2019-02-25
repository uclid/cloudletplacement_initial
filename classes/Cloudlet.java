public class Cloudlet {
	public int id;
	public int processor;
	public int memory;
	public int storage;
	public int radius;
	
	/**
	 * @author Dixit Bhatta
	 * @param id identifier
	 * @param processor processing capacity
	 * @param memory memory capacity
	 * @param storage storage capacity
	 * @param radius coverage radius
	 */
	public Cloudlet(int id, int processor, int memory, int storage, int radius) {
		this.id = id;
		this.processor = processor;
		this.memory = memory;
		this.storage = storage;
		this.radius = radius;
	}
	
	public String toString() {
		String type = "";
		
		if(this.processor == 600) {
			type = "c1";
		}
		else if(this.processor == 400) {
			type = "c2";
		}
		else {
			type = "c3";
		}
		
		return type;
	}

}
