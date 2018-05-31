//MAIN PAGE:
//group by movies.id having movies.title LIKE letter% to find titles with starting letter from mainPage
//group by movies.id having genreNames (group_concat(distinct(genres.name)) LIKE %genre% to find genres containing genre from mainPage
//
//IN SEARCH FUNCTION:
//movies.title LIKE %title% find titles containing input title string
//movies.year LIKE %year% find year containing input year string
//movies.director LIKE %director% find director containing input director string
//group by movies.id having starNames LIKE %name% find input star name from list of star names for movie 


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
			List<String> queryList = new ArrayList<String>();
			List<String> queries = new ArrayList<String>();
			
			System.out.println("CONNECTED");
						
			String title = request.getParameter("title");
			String year = request.getParameter("year");
			String director = request.getParameter("director");
			String name = request.getParameter("starName");
			String genre = request.getParameter("genre");
			String letter = request.getParameter("letter");
			String order = request.getParameter("order");
			String limit = request.getParameter("limit");
			String offset = request.getParameter("offset");
			String query = "";
			
			
			boolean isLetter = false;
			
			if(!order.equals("null") && !order.isEmpty() && !genre.equals("null"))
			{
			String sortBy = "";
			if(order.equals("asc_title"))
				sortBy += "order by movies.title ASC ";
			else if(order.equals("desc_title"))
				sortBy += "order by movies.title DESC ";
			else if(order.equals("asc_rating"))
				sortBy += "order by rating ASC ";
			else if(order.equals("desc_rating"))
				sortBy += "order by rating DESC ";
			query += "select movies.id, movies.title, movies.year, movies.director, ";
			query += "group_concat(distinct(stars.name)) as starNames, group_concat(distinct(genres.name)) as genreNames, ratings.rating from movies ";
			query += "join stars_in_movies on movies.id = stars_in_movies.movieId ";
			query += "join stars on stars.id = starId ";
			query += "join ratings on movies.id = ratings.movieId ";
			query += "join genres_in_movies on movies.id = genres_in_movies.movieId ";
			query += "join genres on genres_in_movies.genreId = genres.id ";
			query += "group by movies.id having genreNames LIKE ? ";
			query += sortBy;
			query += "limit " + limit + " offset " + offset;
			queries.add(genre);
			}
			
			else if(!order.equals("null") && !order.isEmpty())
			{
			String sortBy = "";
			if(order.equals("asc_title"))
				sortBy += "order by movies.title ASC ";
			else if(order.equals("desc_title"))
				sortBy += "order by movies.title DESC ";
			else if(order.equals("asc_rating"))
				sortBy += "order by rating ASC ";
			else if(order.equals("desc_rating"))
				sortBy += "order by rating DESC ";
			query += "select movies.id, movies.title, movies.year, movies.director, ";
			query += "group_concat(distinct(stars.name)) as starNames, group_concat(distinct(genres.name)) as genreNames, ratings.rating from movies ";
			query += "join stars_in_movies on movies.id = stars_in_movies.movieId ";
			query += "join stars on stars.id = starId ";
			query += "join ratings on movies.id = ratings.movieId ";
			query += "join genres_in_movies on movies.id = genres_in_movies.movieId ";
			query += "join genres on genres_in_movies.genreId = genres.id ";
			query += "group by movies.id ";
			query += sortBy;
			query += "limit " + limit + " offset " + offset;
			}
			
			else if (!letter.equals("null") && !letter.isEmpty())
			{
			query += "select movies.id, movies.title, movies.year, movies.director, ";
			query += "group_concat(distinct(stars.name)) as starNames, group_concat(distinct(genres.name)) as genreNames, ratings.rating from movies ";
			query += "join stars_in_movies on movies.id = stars_in_movies.movieId ";
			query += "join stars on stars.id = starId ";
			query += "join ratings on movies.id = ratings.movieId ";
			query += "join genres_in_movies on movies.id = genres_in_movies.movieId ";
			query += "join genres on genres_in_movies.genreId = genres.id ";
			query += "group by movies.id having movies.title LIKE ? ";
			query += "limit " + limit + " offset " + offset;
			queries.add(letter);
			isLetter = true;
			}
			
			else if (!genre.equals("null") && !genre.isEmpty())
			{
			System.out.println("GENREEEE");
			System.out.println(genre);
			query += "select movies.id, movies.title, movies.year, movies.director, ";
			query += "group_concat(distinct(stars.name)) as starNames, group_concat(distinct(genres.name)) as genreNames, ratings.rating from movies ";
			query += "join stars_in_movies on movies.id = stars_in_movies.movieId ";
			query += "join stars on stars.id = starId ";
			query += "join ratings on movies.id = ratings.movieId ";
			query += "join genres_in_movies on movies.id = genres_in_movies.movieId ";
			query += "join genres on genres_in_movies.genreId = genres.id ";
			query += "group by movies.id having genreNames LIKE ? ";
			query += "limit " + limit + " offset " + offset;
			queries.add(genre);
			}
			
			else
			{

			
			query += "select movies.id, movies.title, movies.year, movies.director, ";
			query += "group_concat(distinct(stars.name)) as starNames, group_concat(distinct(genres.name)) as genreNames, ratings.rating from movies ";
			query += "join stars_in_movies on movies.id = stars_in_movies.movieId ";
			query += "join stars on stars.id = starId ";
			query += "join ratings on movies.id = ratings.movieId ";
			query += "join genres_in_movies on movies.id = genres_in_movies.movieId ";
			query += "join genres on genres_in_movies.genreId = genres.id where ";
			
//			if(!title.equals("null") && !title.isEmpty())
//			{
//				queryList.add("movies.title LIKE ?");
//				queries.add(title);
//				System.out.println(queryList);
//			}
			String[] splited = title.split("\\s+");
			String words = "";
			if(!title.equals("null") && !title.isEmpty()) {
				for(String str : splited) {
					words += "+" + str + "* ";
				}
				query += "MATCH (title) AGAINST ('" + words + "' IN BOOLEAN MODE) AND ";
			}
			
			
			if(!year.equals("null") && !year.isEmpty())
			{
				queryList.add("movies.year LIKE ?");
				queries.add(year);
				System.out.println(queryList);
			}
			
			if(!director.equals("null") && !director.isEmpty())
			{
				queryList.add("movies.director LIKE ?");
				queries.add(director);
				System.out.println(queryList);
			}
			
			if(!name.equals("null") || ! name.isEmpty())
			{
				queryList.add("stars.name LIKE ?");
				queries.add(name);
				System.out.println(queryList);
			}
			
			String queryAdd = String.join(" AND ", queryList);
			
			System.out.println(queryAdd);
			
			query += queryAdd;
			//query += " group by movies.id having starNames LIKE ? ";
			query += " group by movies.id ";
			//queries.add(name);
			query += "limit " + limit + " offset " + offset;
			}
		
			System.out.println(query);
			
			PreparedStatement preparedStatement = database.prepareStatement(query);
			if(isLetter)
				preparedStatement.setString(1, queries.get(0) + "%");
			else {
				for(int x = 0; x < queries.size(); x++)
				{
					preparedStatement.setString(x + 1, "%" +queries.get(x)+ "%");
				}
			}
			ResultSet rs = preparedStatement.executeQuery();
			System.out.println(preparedStatement);


			
			JsonArray jsonArray = new JsonArray();
			// Iterate through each row of rsl
			while (rs.next()) {

				String movieId = rs.getString("id");
				String movieTitle = rs.getString("title");
				String movieYear = rs.getString("year");
				String movieDirector = rs.getString("director");
				String genreNames = rs.getString("genreNames");
				String starNames = rs.getString("starNames");
				String rating = rs.getString("rating");

				// Create a JsonObject based on the data we retrieve from rs

				JsonObject jsonObject = new JsonObject();
				jsonObject.addProperty("movieId", movieId);
				jsonObject.addProperty("movieTitle", movieTitle);
				jsonObject.addProperty("movieYear", movieYear);
				jsonObject.addProperty("movieDirector", movieDirector);
				jsonObject.addProperty("genreNames", genreNames);
				jsonObject.addProperty("starNames", starNames);
				jsonObject.addProperty("rating", rating);
				//System.out.println("limit:" + limit);
				jsonObject.addProperty("limit", limit);
				jsonObject.addProperty("offset", offset);
				
				jsonArray.add(jsonObject);
				}
						
			    // write JSON string to output
			out.write(jsonArray.toString());
			    // set response status to 200 (OK)
			response.setStatus(200);

			rs.close();
			preparedStatement.close();
			database.close();
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
