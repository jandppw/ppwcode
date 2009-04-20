package be.peopleware.theopenmoviedb;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;


public class Util {
	
	private static final String API_KEY = "2f88cdad72e181fc6a6df8249608141b";
	
	private static Document requestDocument(String query) {
		try {
			URL url = new URL("http://api.themoviedb.org/2.0/Movie.search?title=" + query + "&api_key=" + API_KEY);

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
	
	public static List<Movie> searchForMovies(String query) {
		List<Movie> result = new ArrayList<Movie>();
		
		Element root = requestDocument(query).getRootElement();
		
		Element totalResults = root.getChild("totalResults", Namespace.getNamespace("opensearch", "http://a9.com/-/spec/opensearch/1.1/"));
		
		if (Integer.parseInt(totalResults.getText()) > 0) {
			Element movieMatches = (Element)root.getChild("moviematches");
		
			for (Element element : (List<Element>)movieMatches.getChildren("movie")) {
				Movie movie = new Movie();
				movie.setScore(Double.parseDouble(element.getChildText("score")));
				movie.setPopularity(Integer.parseInt(element.getChildText("popularity")));
				movie.setTitle(element.getChildText("title"));
				movie.setImdbId(element.getChildText("imdb"));
				movie.setShortOverview(element.getChildText("short_overview"));
				for (Element poster : (List<Element>)element.getChildren("poster")) {
					movie.getPosters().put(poster.getAttributeValue("size"), poster.getText());
				}
				result.add(movie);
			}
		}
		return result;
	}
	
	public static String getPosterThumb(String query) {
		List<Movie> movies = searchForMovies(query);
		if (movies.size() > 0) {
			return movies.get(0).getPosters().get("thumb");
		} else {
			return null;
		}
	}
	
	public static void main(String[] args) {
		String query = "the matrix";
		for (Movie movie : searchForMovies(query)) {
			System.out.println(movie.getTitle() + " (" + movie.getImdbId() + ") : " + movie.getShortOverview());
		}
	}

}
