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
import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.extensions.Rating;
import com.google.gdata.data.media.mediarss.MediaKeywords;
import com.google.gdata.data.media.mediarss.MediaPlayer;
import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.spreadsheet.ListEntry;
import com.google.gdata.data.spreadsheet.ListFeed;
import com.google.gdata.data.spreadsheet.SpreadsheetEntry;
import com.google.gdata.data.spreadsheet.SpreadsheetFeed;
import com.google.gdata.data.spreadsheet.WorksheetEntry;
import com.google.gdata.data.spreadsheet.WorksheetFeed;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.data.youtube.YtPublicationState;
import com.google.gdata.data.youtube.YtStatistics;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.json.JsonObject;
import com.restfb.types.User;

/********************************/
/*Filtered by User's music list**/
/********************************/

/**
 * Servlet implementation class musicfilter
 */
@WebServlet("/musicfilter")
public class musicfilter extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public musicfilter()
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
			// get parameter from hyper-link
			String parameterId = request.getParameter("parameterId");
			response.getWriter().println(parameterId+"**");
			// get access token
			String code = request.getParameter("code");
			String my_access_token = "";
			String authURL = "https://graph.facebook.com/oauth/access_token?client_id=447748791951781&redirect_uri=http://localhost:8080/myfriendfilterapp/musicfilter?parameterId="+parameterId+"&client_secret=35b28bee7543d230e7ec804382db97f9&code=" + code;		
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
			ArrayList<String>  mymusiclist_nowhitespace = new java.util.ArrayList<String>();
			
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
			
		   for(int i = 0; i <mymusiclist.size() ; i++)// remove white space in names
			   mymusiclist_nowhitespace.add(mymusiclist.get( i ).replaceAll("\\s",""));
		    	//response.getWriter().println( "$"+mymusiclist.get( i ) );
		    
		    
		    /*create connection to the facebook friends*/
			Connection<User> myfriends = facebookClient.fetchConnection("me/friends", User.class,Parameter.with("fields", "favorite_teams,location,favorite_athletes,name"));
			//response.getWriter().println("Count of my friends: " + myfriends.getData().size());
			
			/*get all friends list*/
			List<com.restfb.types.User> myfriends_list = myfriends.getData();
			List<String> friend_filter_list = new ArrayList<String>();
			
			for (User friend : myfriends_list)
			{
				try{
				response.getWriter().println(friend.getId());
				String query2 = "SELECT uid, name, music FROM user WHERE uid=" + friend.getId();
				List<JsonObject> queryResults2 = facebookClient.executeQuery(query2, JsonObject.class);
				//response.getWriter().println(queryResults2.get(0).getString("music"));
			
				ArrayList<String>  myfriendmusiclist = new java.util.ArrayList<String>();
				ArrayList<String>  myfriendmusiclist_nowhitespace = new java.util.ArrayList<String>();
				
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
				
				for(int i = 0; i <myfriendmusiclist.size() ; i++)// remove white space in names
					myfriendmusiclist_nowhitespace.add(myfriendmusiclist.get( i ).replaceAll("\\s",""));

				/* filter process*/
				for(int j = 0; j < myfriendmusiclist_nowhitespace.size() ; j++)
				{
					//response.getWriter().println(parameterId+"::"+myfriendmusiclist_nowhitespace.get(j)+"++"); 
					if( parameterId.matches( myfriendmusiclist_nowhitespace.get(j) )== true )//check
					 {
						 friend_filter_list.add(friend.getId());
						 response.getWriter().println( " ****** " + friend.getId() );
					
					 }
				}
				
				}
				catch(Exception e)
				{
					response.getWriter().println("Cannot read the information2: "+e);
				}
			}
			
		
			response.getWriter().println("hello worldio"+friend_filter_list.size());
			 /*printout for friend list after filter*/
			for (User friend2 : myfriends_list)
			{
				for(String i :friend_filter_list)
				{
					if (i.matches(friend2.getId())==true)
					{
						response.getWriter().println( " 123 " + friend2.getId()+ friend2.getName());
					}
				}
					
			}
					
			// to find corresponding whitespace name
			int temp=-1;
			for(int j = 0; j < mymusiclist_nowhitespace.size() ; j++)
			{
				if(parameterId.matches(mymusiclist_nowhitespace.get(j)))
				{
					temp = j;
				}
			}
			
			/////// get info from youtube ////////
			String USERNAME = "nasif50000@gmail.com";
			String PASSWORD = "appleorange";
			String developer_key = "AI39si5AvW9MLT0k9MUf9hCrYhCs3lN04_vQOJ2UU1OH-T-va1lAZbSP6vSyb6rF0uVbWZP9wjvwgxkCFJcPbQfhkBAOaUQ49A";
			String clientID = "684348800476.apps.googleusercontent.com";
			
			YouTubeService service = new YouTubeService(clientID, developer_key);	
			YouTubeQuery query2 = new YouTubeQuery(new URL("http://gdata.youtube.com/feeds/api/videos"));

			// to store youtube info
			List<String> youtube_title = new ArrayList<String>();
			List<String> youtube_link = new ArrayList<String>();
			List<String> youtube_rating = new ArrayList<String>();
			List<String> youtube_image = new ArrayList<String>();
			
			// parameterId is not NULL
			if(temp!=-1)
			{
				query2.setFullTextQuery(mymusiclist.get(temp));// query Lady Gaga		
			
				VideoFeed videoFeed = service.query(query2, VideoFeed.class);
				
				int i=0;// for counter
				for( VideoEntry videoEntry : videoFeed.getEntries() ) 
				{
					boolean detailed = true;
					response.getWriter().println("*Title: " + videoEntry.getTitle().getPlainText());
					youtube_title.add("Title: " + videoEntry.getTitle().getPlainText());
					
					if(videoEntry.isDraft()) 
					{
						response.getWriter().println("Video is not live");
						YtPublicationState pubState = videoEntry.getPublicationState();
						
						if(pubState.getState() == YtPublicationState.State.PROCESSING)
						{
							response.getWriter().println("Video is still being processed.");
						}
						else if(pubState.getState() == YtPublicationState.State.REJECTED)
						{
							response.getWriter().println("Video has been rejected because: ");
							response.getWriter().println(pubState.getDescription());
							response.getWriter().println("For help visit: ");
							response.getWriter().println(pubState.getHelpUrl());
						}
						else if(pubState.getState() == YtPublicationState.State.FAILED)
						{
							response.getWriter().println("Video failed uploading because: ");
							response.getWriter().println(pubState.getDescription());
							response.getWriter().println("For help visit: ");
							response.getWriter().println(pubState.getHelpUrl());
						}
					}

					if(videoEntry.getEditLink() != null)
					{
						response.getWriter().println("Video is editable by current user.");
					}

					if(detailed==true)
					{
						YouTubeMediaGroup mediaGroup = videoEntry.getMediaGroup();

						MediaPlayer mediaPlayer = mediaGroup.getPlayer();
						response.getWriter().println("*Web Player URL: " + mediaPlayer.getUrl());
						youtube_link.add(mediaPlayer.getUrl()); // youtube
						MediaKeywords keywords = mediaGroup.getKeywords();

						Rating rating = videoEntry.getRating();
					   
						if(rating != null) 
						{
							response.getWriter().println("*Average rating: " + rating.getAverage());
							youtube_rating.add("Rating: " + rating.getAverage());// youtube
						}

						YtStatistics stats = videoEntry.getStatistics();
					   
						response.getWriter().println();

						response.getWriter().println("\tThumbnails:");
						int p=0;// for counter
						for(MediaThumbnail mediaThumbnail : mediaGroup.getThumbnails())
						{
								response.getWriter().println("\t\t*Thumbnail URL: " + mediaThumbnail.getUrl());
								youtube_image.add(mediaThumbnail.getUrl());// youtube
								response.getWriter().println();
							
								p++;	// get 1 print 
								if(p > 0)
									break;
						}

						
					}//end if loop
					
					i++;				// get 6 print out
					if(i>5)
						break;
				
				
				}//end for loop
						
			}// end of if loop
			
			
			/////// to draw graph from google chart ////////
			//connect with Google Spreadsheets
			SpreadsheetService service2 = new SpreadsheetService("MySpreadsheetIntegration-v3");
			service2.setUserCredentials(USERNAME, PASSWORD);
			
			// Define the URL to request.  This should never change.
			URL SPREADSHEET_FEED_URL = new URL("https://spreadsheets.google.com/feeds/spreadsheets/private/full");
			   
			// Make a request to the API and get all spreadsheets.
			SpreadsheetFeed feed = service2.getFeed(SPREADSHEET_FEED_URL, SpreadsheetFeed.class);
			List<SpreadsheetEntry> spreadsheets = feed.getEntries();
			
			// Choose the first spreadsheet 
		    SpreadsheetEntry spreadsheet = spreadsheets.get(0);
		    response.getWriter().println(spreadsheet.getTitle().getPlainText()+"$$");

			// Get the first worksheet of the first spreadsheet.
		    WorksheetFeed worksheetFeed = service2.getFeed( spreadsheet.getWorksheetFeedUrl(), WorksheetFeed.class);
		    List<WorksheetEntry> worksheets = worksheetFeed.getEntries();
		    WorksheetEntry worksheet = worksheets.get(0);
		    
			// Fetch the list feed of the worksheet.
		    URL listFeedUrl = worksheet.getListFeedUrl();
		    ListFeed listFeed = service2.getFeed(listFeedUrl, ListFeed.class);
		    	
		    	    
		    if (temp != -1)
		    {
		    	 // Choose first row
				ListEntry row = listFeed.getEntries().get(0);
				
				String z1 = Integer.toString( friend_filter_list.size() );
		    	String z2 = Integer.toString( myfriends_list.size()-friend_filter_list.size() );
		    	// Update first row's data
		    	row.getCustomElements().setValueLocal("Music", mymusiclist.get(temp));
		    	row.getCustomElements().setValueLocal("Number", z1);
		    
				// Choose second row
		    	ListEntry row1 = listFeed.getEntries().get(1);
				// Update second row's data
		    	row1.getCustomElements().setValueLocal("Music", "Other");
		    	row1.getCustomElements().setValueLocal("Number", z2);
		    
		    	// Save the rows using the API.
		    	row.update();
		    	row1.update();
		    }
			
			/*****to show friend list in jsp page******/
			
			if (temp == -1)
				request.setAttribute("st",parameterId);
			else
				request.setAttribute("st",mymusiclist.get(temp));// Lady Gaga
			
			request.setAttribute("mymusiclist1",mymusiclist);
			
			request.setAttribute("mymusiclist_nowhitespace1", mymusiclist_nowhitespace);
			
			///////////////////	show in second load /////////////
			
			request.setAttribute("friendslist1",myfriends_list);
			
			request.setAttribute("friend_filter_list1",friend_filter_list);
			
			request.setAttribute("myfriendssize",myfriends_list.size());
			
			request.setAttribute("myfriendssizeafterfilter",friend_filter_list.size());
			
			request.setAttribute("youtube_title1",youtube_title);
			
			request.setAttribute("youtube_link1",youtube_link);
			
			request.setAttribute("youtube_rating1",youtube_rating);
			
			request.setAttribute("youtube_image1",youtube_image);
			
			RequestDispatcher rd = request.getRequestDispatcher("showfriendlist.jsp");
			rd.forward(request, response); 
			
	        
		}// end of try
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

}
