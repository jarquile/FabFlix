import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


@WebServlet("/MovieListServlet")
public class MovieListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@Resource(name = "jdbc/moviedb")
	private DataSource dataSource;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json"); // Response mime type
		
		try {
			Connection database = dataSource.getConnection();
			PrintWriter out = response.getWriter();
			
			System.out.println("CONNECTED");
						
			String title = request.getParameter("title");
			String year = request.getParameter("year");
			String director = request.getParameter("director");
			String name = request.getParameter("starName");
			
			List<String> queryList = new ArrayList<String>();

			
			String query = "select * from movies join stars_in_movies on movies.id = stars_in_movies.movieId join stars on stars.id = starId where ";
			
			if(title != null || !title.isEmpty())
			{
				queryList.add("movies.title LIKE '%" + title + "%'");
				System.out.println(queryList);
			}
			
			if(year != null || !year.isEmpty())
			{
				queryList.add("movies.year LIKE '%" + year + "%'");
				System.out.println(queryList);
			}
			
			if(director != null || !director.isEmpty())
			{
				queryList.add("movies.director LIKE '%" + director + "%'");
				System.out.println(queryList);
			}
			
			if(name != null || ! name.isEmpty())
			{
				queryList.add("stars.name LIKE '%" + name + "%'");
				System.out.println(queryList);
			}
			
			String queryAdd = String.join(" AND ", queryList);
			
			System.out.println(queryAdd);
			
			query += queryAdd;
			
			System.out.println(query);
						
			Statement statement = database.createStatement();
			
			ResultSet rs = statement.executeQuery(query);	

			JsonArray jsonArray = new JsonArray();

			// Iterate through each row of rs
			while (rs.next()) {

				String starId = rs.getString("starId");
				String starName = rs.getString("name");
				String starDob = rs.getString("birthYear");

				String movieId = rs.getString("movieId");
				String movieTitle = rs.getString("title");
				String movieYear = rs.getString("year");
				String movieDirector = rs.getString("director");

				// Create a JsonObject based on the data we retrieve from rs

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("star_id", starId);
				jsonObject.addProperty("star_name", starName);
				jsonObject.addProperty("star_dob", starDob);
				jsonObject.addProperty("movie_id", movieId);
				jsonObject.addProperty("movie_title", movieTitle);
				jsonObject.addProperty("movie_year", movieYear);
				jsonObject.addProperty("movie_director", movieDirector);
				
				jsonArray.add(jsonObject);
				}
						
			    // write JSON string to output
			out.write(jsonArray.toString());
			    // set response status to 200 (OK)
			response.setStatus(200);

			rs.close();
			statement.close();
			database.close();
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}