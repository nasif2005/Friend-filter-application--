<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" 
                                                  prefix="fn" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
	<title>Insert title here</title>
	
		<script type="text/javascript" src="http://www.google.com/jsapi"></script>
	    <script type="text/javascript">
	      google.load('visualization', '1', {packages: ['linechart']});
	    </script>
	    <script type="text/javascript">
	    var visualization;
	
	    function drawVisualization() 
	    {
	      // To see the data that this visualization uses, browse to
	      // http://spreadsheets.google.com/ccc?key=pCQbetd-CptGXxxQIG7VFIQ
	      var query = new google.visualization.Query('https://docs.google.com/spreadsheet/tq?key=0Aq4N8GK8re42dHlGMG0wM00tdE5PVjJZellXTEhFNEE&headers=-1');
	    
	      // Apply query language.
	      //query.setQuery('SELECT A');
	    
	      // Send the query with a callback function.
	      query.send(handleQueryResponse);
	    }
	    
	    function handleQueryResponse(response)
	    {
	      if (response.isError())
	      {
	        alert('Error in query: ' + response.getMessage() + ' ' + response.getDetailedMessage());
	        return;
	      }
	    
	      var data = response.getDataTable();
	      visualization = new google.visualization.PieChart(document.getElementById('visualization'));
	      visualization.draw(data, {legend: 'bottom'});
	    }
	    
	
	    google.setOnLoadCallback(drawVisualization);
	    </script>
    
</head>


<body>
 <h1>Music!</h1>
	
	 <h2>Your Like Music List!</h2>

		<c:forEach begin="0" end="${fn:length(mymusiclist1) - 1}" varStatus="loop"> 
		 <tr>
   		 	<td> <c:out value="${i}" /> </td>
   		 	<a href="https://www.facebook.com/dialog/oauth?client_id=447748791951781&redirect_uri=http://localhost:8080/myfriendfilterapp/musicfilter?parameterId=${mymusiclist_nowhitespace1[loop.index]}&scope=friends_likes">${mymusiclist1[loop.index]}</a>
   		 	<br>
   		 </tr>
  		</c:forEach>
  		
  		 
  		 <c:if test="${st != 'null'}">
  		  	<h3>Friend list after filter</h3>
  		 	<c:forEach items="${friendslist1}" var="user1">
		
				<c:forEach items="${friend_filter_list1}" var="user2">
				
				
      				<c:if test="${user1.getId()==user2}">
      					<tr>		
							<td><c:out value="${user1.getName()}" /></td>
							<td><img src='https://graph.facebook.com/<c:out value="${user1.getId()}"/>/picture' /></td>
							<br>
						</tr>
      				</c:if>
      		
				
				</c:forEach>
			
			</c:forEach>
 			
  		
			<br><br>
			You have <c:out value="${myfriendssize}" /> friends.
			<c:out value="${st}" /> matches with <c:out value="${myfriendssizeafterfilter}" /> friends.
			<br>
		
		 	<div id="visualization" style="height: 400px; width: 400px;"></div>
			
			<h4>YouTube Search results for <c:out value="${st}" /></h4>
			<c:forEach begin="0" end="5" varStatus="loop"> 
				<tr>		
					<td><img src='${youtube_image1[loop.index]}' /></td>
					<td><c:out value="${youtube_title1[loop.index]}" /></td>
					<td><a href=<c:out value="${youtube_link1[loop.index]}" />>Link</a></td>
					<td><c:out value="${youtube_rating1[loop.index]}" /></td>
					<br>	
				</tr>		
			 
			</c:forEach>
			
			
		    
		    
		</c:if>
		
		
</body>
<br><br><br><br><br><br>
<a href="http://localhost:8080/myfriendfilterapp/">Return to main page</a> 
</html>