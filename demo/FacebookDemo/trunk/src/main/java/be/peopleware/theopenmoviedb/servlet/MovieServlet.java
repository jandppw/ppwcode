package be.peopleware.theopenmoviedb.servlet;

import java.io.IOException;
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
		
		String queryId = request.getParameter("id");
		String queryTitle = request.getParameter("title");
		
		if (queryId != null && queryId.length() > 0) {
			handleIdRequest(out, queryId);
		} else if (queryTitle != null && queryTitle.length() >= 3) {
			handleTitleRequest(out, queryTitle);
		} else {
			handleEmptyRequest(out);
		}
		
		out.close();
		
	}
	
	private void handleIdRequest(ServletOutputStream out, String queryId) throws IOException {
		Movie movie = Util.searchForMovie(queryId);
		if (movie != null) {
			out.println("{ identifier: 'movieId', items:[");
			out.print("{title: \"" + movie.getTitle() + "\", movieId: \"" + movie.getId() + "\" }");
			out.println("]}");
		} else {
			handleEmptyRequest(out);
		}
		
	}
	
	private void handleTitleRequest(ServletOutputStream out, String queryTitle) throws IOException {
		
		List<Movie> movies = Util.searchForMovies(queryTitle);
		
		out.println("{ identifier: 'movieId', items:[");
	
		for (int i = 0; i < movies.size(); i++) {
			out.print("{title: \"" + movies.get(i).getTitle() + "\", movieId: \"" + movies.get(i).getId() + "\" }");
			if (i < movies.size() -1)
				out.println(",");
		}
	
		out.println("]}");
		
	}
	
	private void handleEmptyRequest(ServletOutputStream out) throws IOException {
		out.println("{ identifier: 'movieId', items:[ ]}");
	}

}
