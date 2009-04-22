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

import be.peopleware.theopenmoviedb.Movie;
import be.peopleware.theopenmoviedb.Util;

public class MovieServlet extends HttpServlet {
	
	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json; charset=UTF-8");
		ServletOutputStream out = response.getOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(out, Charset.forName("UTF-8"));
		
		String queryId = request.getParameter("id");
		String queryTitle = request.getParameter("title");
		
		if (queryId != null && queryId.length() > 0) {
			handleIdRequest(writer, queryId);
		} else if (queryTitle != null && queryTitle.length() >= 3) {
			handleTitleRequest(writer, queryTitle);
		} else {
			handleEmptyRequest(writer);
		}
		
		writer.close();
		
	}
	
	private void handleIdRequest(OutputStreamWriter writer, String queryId) throws IOException {

		Movie movie = Util.searchForMovie(queryId);
		if (movie != null) {
			writer.write("{ identifier: 'movieId', items:[");
			writer.write("{title: \"" + movie.getTitle() + "\", movieId: \"" + movie.getId() + "\" }");
			writer.write("]}");
		} else {
			handleEmptyRequest(writer);
		}
		
	}
	
	private void handleTitleRequest(OutputStreamWriter writer, String queryTitle) throws IOException {
		
		List<Movie> movies = Util.searchForMovies(queryTitle);
		
		writer.write("{ identifier: 'movieId', items:[");
	
		for (int i = 0; i < movies.size(); i++) {
			writer.write("{title: \"" + movies.get(i).getTitle() + "\", movieId: \"" + movies.get(i).getId() + "\" }");
			if (i < movies.size() -1)
				writer.write(",");
		}
	
		writer.write("]}");
		
	}
	
	private void handleEmptyRequest(OutputStreamWriter writer) throws IOException {
		writer.write("{ identifier: 'movieId', items:[ ]}");
	}

}
