package be.peopleware.theopenmoviedb.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Movie {
	
	private double rating;
	private int popularity;
	private String name;
	
	private String imdbId;
	private String id;
	private String overview;
	
	private Map<String, String> posters = new HashMap<String, String>();
	
	private List<Cast> actors = new ArrayList<Cast>();
	private List<Cast> directors = new ArrayList<Cast>();
	
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public int getPopularity() {
		return popularity;
	}
	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getImdbId() {
		return imdbId;
	}
	public void setImdbId(String imdbId) {
		this.imdbId = imdbId;
	}
	public String getOverview() {
		return overview;
	}
	public void setOverview(String overview) {
		this.overview = overview;
	}
	public Map<String, String> getPosters() {
		return posters;
	}
	public List<Cast> getActors() {
		return actors;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getId() {
		return id;
	}
	public void setDirectors(List<Cast> directors) {
		this.directors = directors;
	}
	public List<Cast> getDirectors() {
		return directors;
	}

}
