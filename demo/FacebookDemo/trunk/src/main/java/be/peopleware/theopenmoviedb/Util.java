package be.peopleware.theopenmoviedb;

import java.util.ArrayList;
import java.util.List;

import be.peopleware.theopenmoviedb.model.Movie;


public class Util {
	
	public static String getPosterThumb(String id) {
		Movie movie = MovieService.searchForMovie(id);
		if (movie != null && movie.getPosters().get("thumb") != null) {
			return movie.getPosters().get("thumb");
		} else {
			return "image/blank.gif";
		}
	}
	
	public static String getOverview(String id) {
		Movie movie = MovieService.searchForMovie(id);
		if (movie != null) {
			return movie.getOverview();
		} else {
			return null;
		}
	}
	
	public static List<String> getMainActors(String id) {
		Movie movie = MovieService.searchForMovie(id);
		List<String> result = new ArrayList<String>();
		if (movie != null) {
			for (int i=0; i < 5 && i < movie.getActors().size(); i++) {
				result.add(movie.getActors().get(i).getName());
			}
		} 
		return result;
	}
	
	public static List<String> getDirectors(String id) {
		Movie movie = MovieService.searchForMovie(id);
		List<String> result = new ArrayList<String>();
		if (movie != null) {
			for (int i=0; i < 5 && i < movie.getDirectors().size(); i++) {
				result.add(movie.getDirectors().get(i).getName());
			}
		} 
		return result;
	}
	
}
