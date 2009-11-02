package be.peopleware.theopenmoviedb;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;

import be.peopleware.theopenmoviedb.model.Movie;

public class MovieService {
	
	private static final String API_KEY = "2f88cdad72e181fc6a6df8249608141b";
		
	private static final Map<String,List<Movie>> movieQueryCache = new ConcurrentHashMap<String, List<Movie>>();
	
	private static final Map<Integer, Movie> movieCache = new ConcurrentHashMap<Integer, Movie>();
	
	public static Document requestDocument(String query) {
		try {
			URL url = new URL("http://api.themoviedb.org/2.0/" + query + "&api_key=" + API_KEY);

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
		
			Element root = requestDocument("Movie.getInfo?id=" + id).getRootElement();
		
			Element totalResults = root.getChild("totalResults", Namespace.getNamespace("opensearch", "http://a9.com/-/spec/opensearch/1.1/"));
	
			if (Integer.parseInt(totalResults.getText()) > 0) {
			
				Element movieMatches = (Element)root.getChild("moviematches");
				Element element =  movieMatches.getChild("movie");
				result = MovieParser.parseMovie(element);
			
				movieCache.put(Integer.parseInt(result.getId()), result);
				
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
		
			Element root = requestDocument("Movie.search?title=" + query).getRootElement();
		
			Element totalResults = root.getChild("totalResults", Namespace.getNamespace("opensearch", "http://a9.com/-/spec/opensearch/1.1/"));
			
			if (Integer.parseInt(totalResults.getText()) > 0) {
				Element movieMatches = (Element)root.getChild("moviematches");
		
				for (Element element : (List<Element>)movieMatches.getChildren("movie")) {
					result.add(MovieParser.parseMovie(element));
				}
			}
			
			movieQueryCache.put(query, result);
		}
		return result;
	}


}
