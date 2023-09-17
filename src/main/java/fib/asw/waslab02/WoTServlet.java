package fib.asw.waslab02;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.security.*;

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
			
			JSONObject obj1 = new JSONObject(body);
			String autor = obj1.getString("author");
			String text = obj1.getString("text");
			Tweet tweet = tweetDAO.insertTweet(autor, text);
			JSONObject obj2 = new JSONObject(tweet);
			
			String token = hashId(tweet.getId().toString());
			//System.out.println("New token generated for ID = " + tweet.getId() + " is {" + token + "}");
			obj2.append("token", token);
			
			response.getWriter().println(obj2.toString());
		}
	}
    
    private String hashId(String id) {
    	String myHash = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(id.getBytes());
	        byte[] digest = md.digest();
	        myHash = new BigInteger(1, digest).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
          
    	return myHash;
    }
    
    @Override
	// Implements DELETE http://localhost:8080/waslab02/tweets/:id
	public void doDelete(HttpServletRequest req, HttpServletResponse resp)
			throws IOException, ServletException {

    	
    	

    	
    	String uri = req.getRequestURI();
    	int lastIndex = uri.lastIndexOf("/");
    	Long id = Long.valueOf(uri.substring(lastIndex+1));	
    	
    	String token = req.getHeader("Authorization");
    	
    	//System.out.print(token);
    	
    	String expectedToken = hashId(id.toString());
    	
    	if (!token.equals(expectedToken)) throw new ServletException("Token not recognised (ID = " + id + "). Expected " + expectedToken + " but actual " + token + "!!!");
    	
    	boolean dt =  tweetDAO.deleteTweet(id);
    	if (!dt) throw new ServletException();
    	
    	
	}
}