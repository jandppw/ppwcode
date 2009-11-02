package be.peopleware.theopenmoviedb;

public class Actor {
	
	private String name;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return this.getClass().getName() + ": " + getName();
	}

}
