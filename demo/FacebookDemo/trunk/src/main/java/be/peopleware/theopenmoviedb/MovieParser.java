package be.peopleware.theopenmoviedb;

import java.util.List;

import org.jdom.Element;

import be.peopleware.theopenmoviedb.model.Cast;
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
				String actorId = parseActorId(person);
				if (actorId != null) {
					Cast cast = MovieService.searchForPerson(actorId);
					movie.getActors().add(cast);
				}
				String directorId = parseDirectorId(person);
				if (directorId != null) {
					Cast cast = MovieService.searchForPerson(directorId);
					movie.getDirectors().add(cast);
				}
			}
		}
		return movie;
	}
	
	private static String parseActorId(Element person) {
		String id = person.getAttributeValue("id");
		if (id != null && "Actor".equals(person.getAttributeValue("job"))) {
			return id;
		} else {
			return null;
		}
	}

	private static String parseDirectorId(Element person) {
		String id = person.getAttributeValue("id");
		if (id != null && "Director".equals(person.getAttributeValue("job"))) {
			return id;
		} else {
			return null;
		}
	}
	
	public static Cast parsePerson(Element person) {
		String id = person.getChildText("id");
		String name = person.getChildText("name");
		String birthday = person.getChildText("birthday");
		String birthplace = person.getChildText("birthplace");
		Cast result = new Cast();
		result.setId(id);
		result.setName(name);
		result.setBirthday(birthday);
		result.setBirthplace(birthplace);
		return result;
	}

	
}
