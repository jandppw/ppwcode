package be.peopleware.theopenmoviedb;

import java.util.List;

import org.jdom.Element;

import be.peopleware.theopenmoviedb.model.Actor;
import be.peopleware.theopenmoviedb.model.Movie;

public class MovieParser {
	
	public static Movie parseMovie(Element element) {
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
				Actor actor = parseActor(person);
				if (actor != null) {
					movie.getActors().add(actor);
				}
			}
		}
		return movie;
	}
	
	private static Actor parseActor(Element person) {
		Element name = person.getChild("name");
		if (name != null && "actor".equals(person.getAttributeValue("job"))) {
			Actor actor = new Actor();
			actor.setName(name.getText());
			return actor;
		} else {
			return null;
		}
	}

}
