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
		
		String query = request.getParameter("title");
		if (query != null && query.length() >= 3) {
			
			List<Movie> movies = Util.searchForMovies(query);
		
			out.println("{ identifier: 'id', items:[");
		
			for (int i = 0; i < movies.size(); i++) {
				out.print("{title: \"" + movies.get(i).getTitle() + "\", id: \"" + movies.get(i).getId() + "\" }");
				if (i < movies.size() -1)
					out.println(",");
			}
		
			out.println("]}");
			
		} else {
			out.println("{ identifier: 'id', items:[ ]}");
		}
		
		out.close();
		
	}

}
