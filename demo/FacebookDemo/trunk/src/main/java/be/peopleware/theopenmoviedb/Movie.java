package be.peopleware.theopenmoviedb;

import java.util.HashMap;
import java.util.Map;

public class Movie {
	
	private double score;
	private int popularity;
	private String title;
	
	private String imdbId;
	private String id;
	private String shortOverview;
	
	private Map<String, String> posters = new HashMap<String, String>();
	
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public int getPopularity() {
		return popularity;
	}
	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImdbId() {
		return imdbId;
	}
	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}
	public String getShortOverview() {
		return shortOverview;
	}
	public void setShortOverview(String shortOverview) {
		this.shortOverview = shortOverview;
	}
	public Map<String, String> getPosters() {
		return posters;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}

}
