package be.peopleware.theopenmoviedb;

import java.util.List;

import org.jdom.Element;

import be.peopleware.theopenmoviedb.model.Actor;
import be.peopleware.theopenmoviedb.model.Director;
import be.peopleware.theopenmoviedb.model.Movie;

public class MovieParser {
	
	public static Movie parseMovie(Element element) {
		if (element.getChildren().size() == 0) {
			// Nothing found.
			return null;
		}
		Movie movie = new Movie();
		movie.setRating(Double.parseDouble(element.getChildText("rating")));
		movie.setPopularity(Integer.parseInt(element.getChildText("popularity")));
		movie.setName(element.getChildText("name"));
		movie.setImdbId(element.getChildText("imdb_id"));
		movie.setId(element.getChildText("id"));
		movie.setOverview(element.getChildText("overview"));
		Element images = element.getChild("images");
		if (images != null) {
			for (Element image : (List<Element>)images.getChildren("image")) {
				if ("poster".equals(image.getAttributeValue("type"))) {
					movie.getPosters().put(image.getAttributeValue("size"), image.getAttributeValue("url"));
				}
			}
		}
		Element people = element.getChild("cast");
		if (people != null) {
			for (Element person : (List<Element>)people.getChildren("person")) {
				Actor actor = parseActor(person);
				if (actor != null) {
					movie.getActors().add(actor);
				}
				Director director = parseDirector(person);
				if (director != null) {
					movie.getDirectors().add(director);
				}
			}
		}
		return movie;
	}
	
	private static Actor parseActor(Element person) {
		String name = person.getAttributeValue("name");
		if (name != null && "Actor".equals(person.getAttributeValue("job"))) {
			Actor actor = new Actor();
			actor.setName(name);
			return actor;
		} else {
			return null;
		}
	}

	private static Director parseDirector(Element person) {
		String name = person.getAttributeValue("name");
		if (name != null && "Director".equals(person.getAttributeValue("job"))) {
			Director director = new Director();
			director.setName(name);
			return director;
		} else {
			return null;
		}
	}

	
}
