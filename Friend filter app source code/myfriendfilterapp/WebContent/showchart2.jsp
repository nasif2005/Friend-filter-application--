<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
<h1>Friend Like Music's Popularity</h1>

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
	      var query = new google.visualization.Query('https://docs.google.com/spreadsheet/tq?range=A%3AB&key=0Aq4N8GK8re42dHlGMG0wM00tdE5PVjJZellXTEhFNEE&gid=2&headers=-1');
	    
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
	      visualization.draw(data ,{legend: 'bottom'});
	      
	    }
	    
	
	    google.setOnLoadCallback(drawVisualization);
	   </script>
</head>

<body>
	
	<div id="visualization" style="height: 400px; width: 400px;"></div>
</body>
<br><br><br>
<a href="http://localhost:8080/myfriendfilterapp/">Return to main page</a> 
</html>