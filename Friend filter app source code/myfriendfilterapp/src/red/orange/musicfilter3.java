package red.orange;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gdata.client.spreadsheet.SpreadsheetService;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.json.JsonObject;
import com.restfb.types.User;

/***********************************************/
/*Your music match with your friends in chart**/
/*********************************************/
/**
 * Servlet implementation class musicfilter3
 */
@WebServlet("/musicfilter3")
public class musicfilter3 extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public musicfilter3() 
    {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try{
			String code = request.getParameter("code");
			String my_access_token = "";
			String authURL = "https://graph.facebook.com/oauth/access_token?client_id=447748791951781&redirect_uri=http://localhost:8080/myfriendfilterapp/musicfilter3"+"&client_secret=35b28bee7543d230e7ec804382db97f9&code=" + code;		
			URL url = new URL(authURL);
			
			ByteArrayOutputStream bytearray = new ByteArrayOutputStream();
			InputStream is = url.openStream();

			int k;
			
			while ((k = is.read()) != -1) 
			{
				bytearray.write(k);
			}
			
			String s1 = new String(bytearray.toByteArray());
			//response.getWriter().println("My application access token: " + s1);
			
			
			String[] s2 = s1.split("&");
				
			for (String temp : s2) 
			{
				String[] s3 = temp.split("=");
				if (s3[0].equals("access_token")) 
				{
					my_access_token = s3[1];
				}
			} 
			
			//response.getWriter().println("My application access token: " + my_access_token);// print my_access_token
			
			
			/*create facebook client*/
			FacebookClient facebookClient = new DefaultFacebookClient(my_access_token);
			User user = facebookClient.fetchObject("me", User.class);
					

			String query = "SELECT uid, name, music FROM user WHERE uid="+user.getId();
			List<JsonObject> queryResults = facebookClient.executeQuery(query, JsonObject.class);
			//response.getWriter().println(queryResults.get(0).getString("music"));

			ArrayList<String>  mymusiclist = new java.util.ArrayList<String>();
			ArrayList<Integer>  mymusiclist_popularity= new java.util.ArrayList<Integer>();
			
			String p1 = new String();
			String p2 = new String();

			StringTokenizer st = new StringTokenizer(queryResults.get(0).getString("music"),",");
			mymusiclist.add((String) st.nextElement());//get the first element
			
			while(st.hasMoreTokens())
			{
				p1=(String) st.nextElement();
				p2=stringconvert(p1);
				mymusiclist.add(p2);//get rest of the element
				//response.getWriter().println("**");
			}
			
			for(String i : mymusiclist)// my music list print
				response.getWriter().println(i);
			
			 /*create connection to the facebook friends*/
			Connection<User> myfriends = facebookClient.fetchConnection("me/friends", User.class,Parameter.with("fields", "favorite_teams,location,favorite_athletes,name"));
			//get all friends list
			List<com.restfb.types.User> myfriends_list = myfriends.getData();
			// get all the friends music list
			ArrayList<String>  myfriendmusiclist = new java.util.ArrayList<String>();
			
			for (User friend : myfriends_list)
			{
				try
				{
					response.getWriter().println(friend.getId());
					String query2 = "SELECT uid, name, music FROM user WHERE uid=" + friend.getId();
					List<JsonObject> queryResults2 = facebookClient.executeQuery(query2, JsonObject.class);
					
					
					String p3 = new String();
					String p4 = new String();
					StringTokenizer st2 = new StringTokenizer(queryResults2.get(0).getString("music"),",");
					
					myfriendmusiclist.add((String) st2.nextElement());//get the first element
					
					while(st2.hasMoreTokens())
					{
						p3=(String) st2.nextElement();
						p4=stringconvert(p3);
						myfriendmusiclist.add(p4);//get rest of the element
					}
				}
				catch(Exception e)
				{
					response.getWriter().println("Cannot read the information2: "+e);
				}
			}// end for loop
			
			for(String i : myfriendmusiclist)// my friend's music list print
				response.getWriter().println(i+"*");
			
			for(String i : mymusiclist)
			{
				int counter = 0;
				response.getWriter().println(i+"*");
				for(String j : myfriendmusiclist)
				{
					if(i.matches(j)==true)
					{
						counter++;
					}
				}
				mymusiclist_popularity.add(counter);
			}
			
			for(int i : mymusiclist_popularity)// my music's popularity print
				response.getWriter().println(i+"$");
			
			/////// to draw graph from google chart ////////
			String USERNAME = "nasif50000@gmail.com";
			String PASSWORD = "appleorange";
			String developer_key = "AI39si5AvW9MLT0k9MUf9hCrYhCs3lN04_vQOJ2UU1OH-T-va1lAZbSP6vSyb6rF0uVbWZP9wjvwgxkCFJcPbQfhkBAOaUQ49A";
			String clientID = "684348800476.apps.googleusercontent.com";
		    
			// Application code here
			SpreadsheetService service = new SpreadsheetService("MySpreadsheetIntegration-v3");
			service.setUserCredentials(USERNAME, PASSWORD);
			  
			// Define the URL to request.  This should never change.
			URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");

			// Make a request to the API and get all spreadsheets.
			SpreadsheetFeed feed = service.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
			List<SpreadsheetEntry> spreadsheets = feed.getEntries();
			   
			// choose first spreadsheet
			SpreadsheetEntry spreadsheet = spreadsheets.get(0);
			response.getWriter().println(spreadsheet.getTitle().getPlainText()+"$$");

			// Get the first worksheet of the first spreadsheet
			WorksheetFeed worksheetFeed = service.getFeed( spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
			List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
			WorksheetEntry worksheet = worksheets.get(1);
			    
			// Fetch the list feed of the worksheet.
			URL listFeedUrl = worksheet.getListFeedUrl();
			ListFeed listFeed = service.getFeed(listFeedUrl, ListFeed.class);
			    
			for (ListEntry row : listFeed.getEntries()) 
			{
			    row.delete();// to delete the row
				response.getWriter().println("row delete");
			}
			
			// Create a local representation of the new row.
			ListEntry row = new ListEntry();
		    for(int i=0; i< mymusiclist.size();i++)
		    {
		    	String z1 = Integer.toString( mymusiclist_popularity.get(i) );
		    	
		    	row.getCustomElements().setValueLocal("Music", mymusiclist.get(i));
			    row.getCustomElements().setValueLocal("Number", z1);
			   
			    row = service.insert(listFeedUrl, row);
		    }
		    
		 
		    RequestDispatcher rd = request.getRequestDispatcher("showchart.jsp");
			rd.forward(request, response); 
	
		}
		catch(Exception e)
		{
			response.getWriter().println("Cannot read the information1: "+e);
			response.getWriter().println("Please check your 'music' in Facebook account");
		}	
			
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		// TODO Auto-generated method stub
	}
	
	String stringconvert(String p1)
	{
		String s3=new String();// storage string
		
		char[] Chararray = p1.toCharArray();//convert string to char array
		
		char temp;
		
		for(int i=0;i<p1.length()-1;i++)//re-model the string
		{
			temp = Chararray[i];
			Chararray[i] = Chararray[i+1];
		}
		
		for(int i=0;i<Chararray.length-1;i++)// convert char array to string
		{
			//System.out.println(Chararray[i]+"*");
			s3=s3+String.valueOf(Chararray[i]);
		}
		
	/*	for(char temp2: Chararray2)// print info
		{
			System.out.println(temp2);
		}*/
		
		//System.out.println(s3.length());//print string length
		
		return s3;
		
	}
	
	boolean repeatcheck(ArrayList<String> mylist, String s)
	{
		for(String q : mylist)
		{
			if(q.matches(s)==true)
				return false;
		}
		
		return true;	
		
	}
	
	
	
	
	}
	



