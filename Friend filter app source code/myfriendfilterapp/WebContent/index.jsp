<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    	               "http://www.w3.org/TR/html4/loose.dtd">

<html>
  <head>
    	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    	<title>GlassFish JSP Page</title>
  </head>
  <body>
    <h1>Friend Filter Page!</h1>
    
    <div id="fb-root"></div>
	<script>
 	 	// Additional JS functions here
  		window.fbAsyncInit = function() 
  		{
	   		FB.init({
	      	appId      : '447748791951781', // App ID
	      	channelUrl : 'http://apps.facebook.com/nasifmuslim/', // Channel File
	      	status     : true, // check login status
	      	cookie     : true, // enable cookies to allow the server to access the session
	      	xfbml      : true  // parse XFBML
    		});

    	// Additional init code here
    	    	
	   		FB.getLoginStatus(function(response)
	   		{
		   		 if (response.status === 'connected')
		   		 {
		   			//document.write('Hello World!');
		   			//document.write(response.userID);
		   			 // connected
		   		  } 
		   		 else if (response.status === 'not_authorized')
		   		 {
		   		    // not_authorized
		   			login();
		   		 } 
		   		 else 
		   		  {
		   		    // not_logged_in
		   			login();
		   		  }
	   		 });    

  		};
  		
  		function login()
  		{
  		    FB.login(function(response) 
  		    {
  		        if (response.authResponse) 
  		        {
  		            // connected
  		            //document.write(response.userID);
  		        	
  		        }
  		        else 
  		        {
  		            // cancelled
  		        }
  		    } , {scope: 'friends_likes'});
  		}
  			

  		// Load the SDK Asynchronously
  		(function(d)
  		{
     		var js, id = 'facebook-jssdk', ref = d.getElementsByTagName('script')[0];
     		if (d.getElementById(id)) {return;}
     		js = d.createElement('script'); js.id = id; js.async = true;
     		js.src = "//connect.facebook.net/en_US/all.js";
     		ref.parentNode.insertBefore(js, ref);
   		}(document));
	</script>
	
	
	<div id="fb-root"></div>
	<script>(function(d, s, id)
	{
	  var js, fjs = d.getElementsByTagName(s)[0];
	  if (d.getElementById(id)) return;
	  js = d.createElement(s); js.id = id;
	  js.src = "//connect.facebook.net/en_US/all.js#xfbml=1&appId=447748791951781";
	  fjs.parentNode.insertBefore(js, fjs);
	}(document, 'script', 'facebook-jssdk'));
	</script>
	
	<br><br>
	<a href="https://www.facebook.com/dialog/oauth?client_id=447748791951781&redirect_uri=http://localhost:8080/myfriendfilterapp/musicfilter?parameterId=null&scope=friends_likes">Friendlist filtered by your like music  </a>
  	<br><br>
  	<a href="https://www.facebook.com/dialog/oauth?client_id=447748791951781&redirect_uri=http://localhost:8080/myfriendfilterapp/musicfilter2?parameterId=null&scope=friends_likes">Friendlist filtered by friend's like music  </a>
  	<br><br>
  	<a href="https://www.facebook.com/dialog/oauth?client_id=447748791951781&redirect_uri=http://localhost:8080/myfriendfilterapp/musicfilter3&scope=friends_likes">Your like music popularity in chart </a>
	<br><br>
	<a href="https://www.facebook.com/dialog/oauth?client_id=447748791951781&redirect_uri=http://localhost:8080/myfriendfilterapp/musicfilter4&scope=friends_likes">Your friend's like music popularity in chart </a>
  	<br><br>
  	<a href="https://www.facebook.com/dialog/oauth?client_id=447748791951781&redirect_uri=http://localhost:8080/myfriendfilterapp/locationmap">Show friend's location in map </a>
  	<br><br><br>
  	<div class="fb-like" data-href="http://apps.facebook.com/nasifmuslim/" data-send="true" data-width="450" data-show-faces="true"></div>
  	<div class="fb-facepile" data-href="http://apps.facebook.com/nasifmuslim/" data-max-rows="1" data-width="300"></div>
  </body>
</html> 
