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


public class Util {
	
	private static final String API_KEY = "2f88cdad72e181fc6a6df8249608141b";
	
	private static final Map<String,List<Movie>> movieQueryCache = new ConcurrentHashMap<String, List<Movie>>();
	
	private static final Map<Integer, Movie> movieCache = new ConcurrentHashMap<Integer, Movie>();
	
	private static Document requestDocument(String query) {
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
				result = parseMovie(element);
			
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
					result.add(parseMovie(element));
				}
			}
			
			movieQueryCache.put(query, result);
		}
		return result;
	}
	
	private static Movie parseMovie(Element element) {
		Movie movie = new Movie();
		movie.setScore(Double.parseDouble(element.getChildText("score")));
		movie.setPopularity(Integer.parseInt(element.getChildText("popularity")));
		movie.setTitle(element.getChildText("title"));
		movie.setImdbId(element.getChildText("imdb"));
		movie.setId(element.getChildText("id"));
		movie.setShortOverview(element.getChildText("short_overview"));
		for (Element poster : (List<Element>)element.getChildren("poster")) {
			movie.getPosters().put(poster.getAttributeValue("size"), poster.getText());
		}
		Element people = element.getChild("people");
		if (people != null) {
			for (Element person : (List<Element>)people.getChildren("person")) {
				Element name = person.getChild("name");
				if (name != null && "actor".equals(person.getAttributeValue("job"))) {
					movie.getActors().add(name.getText());
				}
			}
		}
		return movie;
	}
	
	public static String getPosterThumb(String id) {
		Movie movie = searchForMovie(id);
		if (movie != null) {
			return movie.getPosters().get("thumb");
		} else {
			return null;
		}
	}
	
	public static String getShortOverview(String id) {
		Movie movie = searchForMovie(id);
		if (movie != null) {
			return movie.getShortOverview();
		} else {
			return null;
		}
	}
	
	public static String[] getMainActors(String id) {
		Movie movie = searchForMovie(id);
		if (movie != null) {
			return movie.getActors().toArray(new String[] {});
		} else {
			return new String[] {};
		}
	}
	
	public static void main(String[] args) {
		String query = "the matrix";
		for (Movie movie : searchForMovies(query)) {
			System.out.println(movie.getTitle() + " (" + movie.getImdbId() + ") : " + movie.getShortOverview());
		}
		
		String queryId = "24";
		Movie movie = searchForMovie(queryId);
		System.out.println(movie.getTitle() + " (" + movie.getImdbId() + ") : " + movie.getShortOverview());
		System.out.println(movie.getActors());
		for(String s : getMainActors(queryId)) {
			System.out.println(s);
		}
	}

}
