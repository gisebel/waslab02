package fib.asw.waslab02;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TweetDAO {
	private Connection conn;

	public TweetDAO(Connection dbcon) {
		conn = dbcon;
	}

	public List<Tweet> getAllTweets() {
		List<Tweet> tweets = new ArrayList<Tweet>();
		try {
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM tweets ORDER BY twtime DESC");
			while (rs.next()) {
				Tweet tweet = new Tweet();
				tweet.setId(rs.getLong("twid"));
				tweet.setAuthor(rs.getString("twauthor"));
				tweet.setText(rs.getString("twtext"));
				tweet.setLikes(rs.getInt("twLIKES"));
				tweet.setDate(rs.getTimestamp("twTIME").getTime());
				tweets.add(tweet);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return tweets;
	}

	public int likeTweet(long id)
	{
		int output = -1;
		String update = "update tweets set twLIKES = twLIKES + 1 where twID = "+id;
		String query = "select twLIKES from tweets where twID = "+id;
		try {
			conn.setAutoCommit(false);
			try {
				Statement stmt = conn.createStatement();
				stmt.executeUpdate(update);
				ResultSet rs = stmt.executeQuery(query);
				rs.first();
				output = rs.getInt(1);
				rs.close();
				stmt.close();
				conn.commit();
			}
			catch (SQLException ex ) {
				System.err.println(ex.getStackTrace());
				conn.rollback();
			}
			conn.setAutoCommit(true);

		}
		catch (SQLException ex ) { System.err.println(ex.getStackTrace()); }
		return output;
	}

	public Tweet insertTweet(String author, String text)
	{
		Tweet result = null;
		if (text != null && !text.equals(""))
		{		
			if (author == null || author.equals("")) author ="Anonymous";
			String insert = "insert into tweets(twAUTHOR, twTEXT) values (?, ?)";
			try {
				PreparedStatement pst = conn.prepareStatement(insert, PreparedStatement.RETURN_GENERATED_KEYS);
				pst.setString(1, author);
				pst.setString(2, text);
				pst.executeUpdate();
				ResultSet rs = pst.getGeneratedKeys();
				if (rs != null && rs.next()) { 
					Statement stmt = conn.createStatement();
					String query = "select * from tweets where twID = " + rs.getLong(1);
					ResultSet rs2 = stmt.executeQuery(query);
					rs2.first();
					result = new Tweet();
					result.setDate(rs2.getTimestamp("twTIME").getTime());
					result.setAuthor(rs2.getString("twAUTHOR"));
					result.setText(rs2.getString("twTEXT"));
					result.setId(rs.getLong("twid"));
					result.setLikes(rs2.getInt("twLIKES"));
					rs2.close();
					stmt.close();
				}
				rs.close();
				pst.close();
			}
			catch (SQLException ex) {
				System.err.println(ex.getStackTrace());
			}
		}
		return result;
	}

	public boolean deleteTweet(long id)
	{
		int dts = 0;
		String delete = "delete from tweets where twID = "+id;
		try {
			Statement stmt = conn.createStatement();
			dts = stmt.executeUpdate(delete);
			stmt.close();
		}
		catch (SQLException ex) {
			System.err.println(ex.getStackTrace());
		}
		return dts > 0;
	}

}
