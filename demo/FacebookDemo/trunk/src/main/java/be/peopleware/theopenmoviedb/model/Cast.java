package be.peopleware.theopenmoviedb.model;

public class Cast {
	
	private String id;
	private String name;
	private String birthday;
	private String birthplace;
	
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	
	public String getBirthday() {
		return birthday;
	}
	
	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}
	
	public String getBirthplace() {
		return birthplace;
	}
	
	public String toString() {
		return this.getClass().getName() + ": " + getName();
	}
	
}
