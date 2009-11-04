package be.peopleware.theopenmoviedb;

import be.peopleware.theopenmoviedb.MovieService;
import be.peopleware.theopenmoviedb.model.Movie;


public class UtilTest {
	
	public static void main(String[] args) {
		String query = "the matrix";
		for (Movie movie : MovieService.searchForMovies(query)) {
			System.out.println(movie.getName() + " (" + movie.getImdbId() + ") : " + movie.getOverview());
		}
		
		String queryId = "24";
		Movie movie = MovieService.searchForMovie(queryId);
		System.out.println(movie.getName() + " (" + movie.getImdbId() + ") : " + movie.getOverview());
		System.out.println(movie.getActors());
		for(String s : TMDBProxy.getMainActors(queryId)) {
			System.out.println(s);
		}
	}

}
