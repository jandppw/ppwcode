package be.peopleware.theopenmoviedb;

import java.util.ArrayList;
import java.util.List;

import be.peopleware.theopenmoviedb.model.Movie;


public class Util {
	
	public static Movie getMovie(String id) {
		return MovieService.searchForMovie(id);
	}
	
	public static List<Movie> getMovies(String title) {
		return MovieService.searchForMovies(title);
	}
	
	public static String getPosterThumb(String id) {
		Movie movie = MovieService.searchForMovie(id);
		if (movie != null && movie.getPosters().get("thumb") != null) {
			return movie.getPosters().get("thumb");
		} else {
			return "image/blank.gif";
		}
	}
	
	public static String getShortOverview(String id) {
		Movie movie = MovieService.searchForMovie(id);
		if (movie != null) {
			return movie.getShortOverview();
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
	
	public static void main(String[] args) {
		String query = "the matrix";
		for (Movie movie : MovieService.searchForMovies(query)) {
			System.out.println(movie.getTitle() + " (" + movie.getImdbId() + ") : " + movie.getShortOverview());
		}
		
		String queryId = "24";
		Movie movie = MovieService.searchForMovie(queryId);
		System.out.println(movie.getTitle() + " (" + movie.getImdbId() + ") : " + movie.getShortOverview());
		System.out.println(movie.getActors());
		for(String s : getMainActors(queryId)) {
			System.out.println(s);
		}
	}

}
