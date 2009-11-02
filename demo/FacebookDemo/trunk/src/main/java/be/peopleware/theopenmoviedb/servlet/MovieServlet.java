package be.peopleware.theopenmoviedb.servlet;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import be.peopleware.theopenmoviedb.MovieService;
import be.peopleware.theopenmoviedb.model.Movie;

public class MovieServlet extends HttpServlet {
	
	private static final long serialVersionUID = 4442857337491447538L;

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json; charset=UTF-8");
		ServletOutputStream out = response.getOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(out, Charset.forName("UTF-8"));
		
		String queryId = request.getParameter("id");
		String queryName = request.getParameter("name");
		
		if (queryId != null && queryId.length() > 0) {
			handleIdRequest(writer, queryId);
		} else if (queryName != null && queryName.length() >= 3) {
			handleNameRequest(writer, queryName);
		} else {
			handleEmptyRequest(writer);
		}
		
		writer.close();
		
	}
	
	private void handleIdRequest(OutputStreamWriter writer, String queryId) throws IOException {

		Movie movie = MovieService.searchForMovie(queryId);
		if (movie != null) {
			JSONArray movieList = new JSONArray();
			movieList.put(new JSONObject().put("name", movie.getName())
					                      .put("movieId", movie.getId()));
			
			writer.write(createResult(movieList));
		} else {
			handleEmptyRequest(writer);
		}
		
	}
	
	private void handleNameRequest(OutputStreamWriter writer, String queryName) throws IOException {
		List<Movie> movies = MovieService.searchForMovies(queryName);
		
		JSONArray movieList = new JSONArray();
		for (Movie movie : movies) {
			movieList.put(new JSONObject().put("name", movie.getName())
					                      .put("movieId", movie.getId()));
		}
		
		writer.write(createResult(movieList));
	}
	
	private void handleEmptyRequest(OutputStreamWriter writer) throws IOException {
		writer.write(createResult(new JSONArray()));
	}
	
	private String createResult(JSONArray movieList) {
		JSONObject result = new JSONObject();
		result.put("identifier", "movieId");
		result.put("items", movieList);
		return result.toString();
	}

}
