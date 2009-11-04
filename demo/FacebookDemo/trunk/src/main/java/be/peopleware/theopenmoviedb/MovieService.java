package be.peopleware.theopenmoviedb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import be.peopleware.theopenmoviedb.model.Cast;
import be.peopleware.theopenmoviedb.model.Movie;

public class MovieService {
	
	private static final String API_KEY = "2f88cdad72e181fc6a6df8249608141b";
		
	private static final Map<String,List<Movie>> movieQueryCache = new ConcurrentHashMap<String, List<Movie>>();
	
	private static final Map<Integer, Movie> movieCache = new ConcurrentHashMap<Integer, Movie>();

	private static final Map<Integer, Cast> castCache = new ConcurrentHashMap<Integer, Cast>();
	
	private static final String API_BASE_URL = "http://api.themoviedb.org/2.1/";
	
	private static final String API_BASE_PATH = "/en/xml/";
	
	private enum OpenMovieDbMethod {
		MOVIE_SEARCH("Movie.search"),
		MOVIE_GETINFO("Movie.getInfo"),
		PERSON_GETINFO("Person.getInfo");

		private OpenMovieDbMethod(String urlPart) {
			this.urlPart = urlPart;
		}
		
		private String urlPart;
		private String getUrlPart() {
			return urlPart;
		}
	}
	
	private static String buildURL(String method, String query) throws UnsupportedEncodingException {
		return API_BASE_URL + method + API_BASE_PATH + API_KEY + "/" + URLEncoder.encode(query, "UTF-8");
	}
	
	public static Document requestDocument(OpenMovieDbMethod method, String query) {
		try {
			URL url = new URL(buildURL(method.getUrlPart(), query));
			SAXBuilder builder = new SAXBuilder();
			Document document = builder.build(url.toString());
			return document;
		} catch (MalformedURLException ex) {
			throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		} catch (JDOMException ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public static Movie searchForMovie(String id) {
		Movie result;
		
		if (movieCache.containsKey(Integer.parseInt(id))) {
			result = movieCache.get(Integer.parseInt(id));
		} else {
			Element root = requestDocument(OpenMovieDbMethod.MOVIE_GETINFO, id).getRootElement();
			Element movies = root.getChild("movies");
			if (movies != null && movies.getChildren().size() > 0) {
				Element movie = movies.getChild("movie");
				result = MovieParser.parseMovie(movie);
				if (result != null) {
					movieCache.put(Integer.parseInt(result.getId()), result);
				}
			} else {
				result = null;
			}
		}
		
		return result;
	}
	
	public static List<Movie> searchForMovies(String query) {
		List<Movie> result;
		
		if (movieQueryCache.containsKey(query)) {
			result = movieQueryCache.get(query);
		} else {
			result = new ArrayList<Movie>();
			Element root = requestDocument(OpenMovieDbMethod.MOVIE_SEARCH, query).getRootElement();
			Element movies = root.getChild("movies");
			for (Element element : (List<Element>)movies.getChildren("movie")) {
				Movie movie = MovieParser.parseMovie(element);
				if (movie != null) {
					result.add(movie);
				}
			}
			movieQueryCache.put(query, result);
		}
		return result;
	}
	
	public static Cast searchForPerson(String id) {
		Cast result;
		
		if (castCache.containsKey(Integer.parseInt(id))) {
			result = castCache.get(Integer.parseInt(id));
		} else {
			Element root = requestDocument(OpenMovieDbMethod.PERSON_GETINFO, id).getRootElement();
			Element people = root.getChild("people");
			if (people != null && people.getChildren().size() > 0) {
				Element person = people.getChild("person");
				result = MovieParser.parsePerson(person);
				if (result != null) {
					castCache.put(Integer.parseInt(result.getId()), result);
				}
			} else {
				result = null;
			}
		}
		
		return result;
	}

}
