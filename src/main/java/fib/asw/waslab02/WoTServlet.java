package fib.asw.waslab02;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet(urlPatterns = {"/tweets", "/tweets/*"})
public class WoTServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private TweetDAO tweetDAO;
	private String TWEETS_URI = "/waslab02/tweets/";

    public void init() {
    	tweetDAO = new TweetDAO((java.sql.Connection) this.getServletContext().getAttribute("connection"));
    }

    @Override
	// Implements GET http://localhost:8080/waslab02/tweets
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    	response.setContentType("application/json");
		response.setHeader("Cache-control", "no-cache");
		List<Tweet> tweets= tweetDAO.getAllTweets();
		JSONArray job = new JSONArray();
		for (Tweet t: tweets) {
			JSONObject jt = new JSONObject(t);
			jt.remove("class");
			job.put(jt);
		}
		response.getWriter().println(job.toString());

    }

    @Override
	// Implements POST http://localhost:8080/waslab02/tweets/:id/likes
	//        and POST http://localhost:8080/waslab02/tweets
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String uri = request.getRequestURI();
		int lastIndex = uri.lastIndexOf("/likes");
		if (lastIndex > -1) {  // uri ends with "/likes"
			// Implements POST http://localhost:8080/waslab02/tweets/:id/likes
			long id = Long.valueOf(uri.substring(TWEETS_URI.length(),lastIndex));		
			response.setContentType("text/plain");
			response.getWriter().println(tweetDAO.likeTweet(id));
		}
		else { 
			// Implements POST http://localhost:8080/waslab02/tweets
			int max_length_of_data = request.getContentLength();
			byte[] httpInData = new byte[max_length_of_data];
			ServletInputStream  httpIn  = request.getInputStream();
			httpIn.readLine(httpInData, 0, max_length_of_data);
			String body = new String(httpInData);
			/*      ^
		      The String variable body contains the sent (JSON) Data. 
		      Complete the implementation below.*/
			
		}
	}
    
    @Override
	// Implements DELETE http://localhost:8080/waslab02/tweets/:id
	public void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

		throw new ServletException("DELETE not yet implemented");
	}
}