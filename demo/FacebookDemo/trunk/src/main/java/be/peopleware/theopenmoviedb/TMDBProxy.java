package be.peopleware.theopenmoviedb;

import java.util.ArrayList;
import java.util.List;

import be.peopleware.theopenmoviedb.model.Cast;
import be.peopleware.theopenmoviedb.model.Movie;


public class TMDBProxy {
	
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
				Cast actor = movie.getActors().get(i);
				result.add(processCast(actor));
			}
		} 
		return result;
	}
	
	public static List<String> getDirectors(String id) {
		Movie movie = MovieService.searchForMovie(id);
		List<String> result = new ArrayList<String>();
		if (movie != null) {
			for (int i=0; i < 5 && i < movie.getDirectors().size(); i++) {
				Cast director = movie.getDirectors().get(i);
				result.add(processCast(director));
			}
		} 
		return result;
	}
	
	private static String processCast(Cast person) {
		if (person.getBirthday() != null && person.getBirthplace() != null && !person.getBirthday().equals("") && !person.getBirthplace().equals("")) {
			String birthYear = person.getBirthday().substring(0, 4);
			return person.getName() + " (" + birthYear + ", " + person.getBirthplace() + ")";
		} else {
			return person.getName();
		}
	}
	
}
