package red.orange;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.geonames.Toponym;
import org.geonames.ToponymSearchCriteria;
import org.geonames.ToponymSearchResult;
import org.geonames.WebService;

import au.com.bytecode.opencsv.CSVReader;

import com.google.gdata.client.ClientLoginAccountType;
import com.google.gdata.client.GoogleService;
import com.google.gdata.client.Service.GDataRequest;
import com.google.gdata.client.Service.GDataRequest.RequestType;
import com.google.gdata.util.AuthenticationException;
import com.google.gdata.util.ContentType;
import com.google.gdata.util.ServiceException;
import com.restfb.Connection;
import com.restfb.DefaultFacebookClient;
import com.restfb.FacebookClient;
import com.restfb.Parameter;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.User;

/**
 * Servlet implementation class locationmap
 */
@WebServlet("/locationmap")
public class locationmap extends HttpServlet 
{
	
	private static final String SERVICE_URL = "https://www.google.com/fusiontables/api/query";
	private GoogleService service;
	
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public locationmap() 
    {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		response.getWriter().println("**");
		try
		{
			// get access token code
			String code = request.getParameter("code");
			String my_access_token = "";
			String authURL = "https://graph.facebook.com/oauth/access_token?client_id=447748791951781&&redirect_uri=http://localhost:8080/myfriendfilterapp/locationmap&client_secret=35b28bee7543d230e7ec804382db97f9&code=" + code;
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
			response.getWriter().println("User location: " + user.getLocation());// print all the user info
				
			//NamedFacebookType mylocation =  user.getLocation();
			//response.getWriter().println(mylocation.getName());	
			
			/*create connection to the facebook friends*/
			Connection<User> myfriends = facebookClient.fetchConnection("me/friends", User.class,Parameter.with("fields", "favorite_teams,location,favorite_athletes,name"));
			
			/*get all friends list*/
			List<com.restfb.types.User> myfriends_list = myfriends.getData();
			List<String> friend_id_list = new ArrayList<String>();
			List<String> friend_name_list = new ArrayList<String>();
			List<String> friend_location_list = new ArrayList<String>();
			
			for (User friend : myfriends_list)
			{
				try
				{
					NamedFacebookType myfriends_location =  friend.getLocation();
					
					
					
					String p2 = new String();
					p2=p2+myfriends_location.getName();
					response.getWriter().println(p2);
					//response.getWriter().println(friend.getName());
					friend_location_list.add(p2);			
					friend_id_list.add(friend.getId());
					friend_name_list.add(friend.getName());
				}
				
				catch(Exception e)
				{
					response.getWriter().println("Cannot read the information2: "+e);
				}				
							
			}
			
			//for(int i=0;i<friend_location_list.size();i++)
			//	response.getWriter().println(friend_id_list.get(i)+" "+friend_name_list.get(i)+" "+friend_location_list.get(i)+"**");
			
			/***************************************/
			String NAME = "nasif50000@gmail.com";
			String PASSWORD = "appleorange";
			
			// table id
			String tableId="117A9zovcVEiC-o0-ANXFqFR4HdKSk-S5GSzYg3A";
			boolean useEncId = true;
			QueryResults results; 
			locationmap example =  new locationmap(NAME, PASSWORD);
			// convert place name to log/lat
			WebService.setUserName("nasif2005");
			ToponymSearchCriteria searchCriteria = new ToponymSearchCriteria();
			// name
			String name = new String();
			
			// location
			String place_name = "Dhaka, Bangladesh";
			String place_log_lat = new String();
			
			// photo
			String facebook_id = "1635518586";
			String facebook_photolink = new String();
			
			//System.out.println("--- delete all rows into the table ---"); 
			results = example.run("DELETE FROM " + tableId ,useEncId);

			
			for(int i=0;i<friend_location_list.size();i++)
			{
				response.getWriter().println(friend_id_list.get(i)+" "+friend_name_list.get(i)+" "+friend_location_list.get(i)+"**");
			
				name = friend_name_list.get(i);
				facebook_id = friend_id_list.get(i);
				place_name = friend_location_list.get(i);
				facebook_photolink = "https://graph.facebook.com/"+facebook_id+"/picture";
				
				searchCriteria.setQ(place_name);//
				ToponymSearchResult searchResult = WebService.search(searchCriteria);
				int counter = 0 ;
				for (Toponym toponym : searchResult.getToponyms()) 
				{
				     System.out.println(toponym.getLatitude()+" "+ toponym.getLongitude());
				     place_log_lat = toponym.getLatitude()+" "+ toponym.getLongitude();
				     counter++;
				     if (counter>0)
				    	 break;
				}
				
				
				
				//System.out.println("--- Insert data into the table ---");
				results = example.run("INSERT INTO " + tableId + " (Name, Location, Photo) VALUES ('"+name+"','"+place_log_lat+"','"+facebook_photolink+"')",useEncId);
			    

			}


			RequestDispatcher rd = request.getRequestDispatcher("showmap.jsp");
			rd.forward(request, response); 
		 
			
		}
		catch(Exception e)
		{
			response.getWriter().println("Cannot read the information1: "+e);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		// TODO Auto-generated method stub
	}
	
	public locationmap(String email, String password) throws AuthenticationException
 	{
 		service = new GoogleService("fusiontables", "fusiontables.ApiExample");
 		service.setUserCredentials(email, password, ClientLoginAccountType.GOOGLE);
 	}

 	// Result of a Fusion Table query.	
 	private static class QueryResults 
 	{
 		final List<String> columnNames;
 		final List<String[]> rows;

	     public QueryResults(List<String> columnNames, List<String[]> rows)
	     {
	    	 this.columnNames = columnNames;
	    	 this.rows = rows;
	     }
     
 	}
 	
 	private QueryResults getResults(GDataRequest request)throws IOException
 	{
		 InputStreamReader inputStreamReader = new InputStreamReader(request.getResponseStream());
	     BufferedReader bufferedStreamReader = new BufferedReader(inputStreamReader);
	     CSVReader reader = new CSVReader(bufferedStreamReader);
	     
	     // The first line is the column names, and the remaining lines are the rows.
	     List<String[]> csvLines = reader.readAll();
	     List<String> columns = Arrays.asList(csvLines.get(0));
	     List<String[]> rows = csvLines.subList(1, csvLines.size());
	     QueryResults results = new QueryResults(columns, rows);
	     return results;
 	}
 	
 	//Returns the results of running a Fusion Tables SQL query
 	 	public QueryResults run(String query, boolean isUsingEncId) throws IOException, ServiceException 
 	 	{

 	 	   System.out.println(query+"*");
 	 		
 	 	   String lowercaseQuery = query.toLowerCase();
 		   String encodedQuery = URLEncoder.encode(query, "UTF-8");
 		
 		   GDataRequest request;
 		   // If the query is a select, describe, or show query, run a GET request.
 		   if (lowercaseQuery.startsWith("select") || lowercaseQuery.startsWith("describe") ||lowercaseQuery.startsWith("show")) 
 		   {
 			   URL url = new URL(SERVICE_URL + "?sql=" + encodedQuery + "&encid=" + isUsingEncId);
 			   request = service.getRequestFactory().getRequest(RequestType.QUERY, url, ContentType.TEXT_PLAIN);
 		   } 
 		   else 
 		   {
 		     // Otherwise, run a POST request.
 			   URL url = new URL(SERVICE_URL + "?encid=" + isUsingEncId);
 			   request = service.getRequestFactory().getRequest(RequestType.INSERT, url, new ContentType("application/x-www-form-urlencoded"));
 			   OutputStreamWriter writer = new OutputStreamWriter(request.getRequestStream());
 			   writer.append("sql=" + encodedQuery);
 			   writer.flush();
 		   }
 		
 		   request.execute();

 		   return getResults(request);
 	 	}


}
