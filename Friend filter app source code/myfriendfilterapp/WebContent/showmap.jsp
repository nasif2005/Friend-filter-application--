<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:v="urn:schemas-microsoft-com:vml">
  <head>
    <meta http-equiv="content-type" content="text/html; charset=utf-8"/>
    <title>Google Maps V3 API Sample</title>
    <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=false"></script>
    <script type="text/javascript">

      function initialize()
      {
        var mapDiv = document.getElementById('map-canvas');
       
        var map = new google.maps.Map(mapDiv, {
          center: new google.maps.LatLng(37.4419, -122.1419),
          zoom: 3,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        });
        
        
        var layer = new google.maps.FusionTablesLayer({
        query: {
          select: 'myfusiontabletest',
          from: '117A9zovcVEiC-o0-ANXFqFR4HdKSk-S5GSzYg3A'
        },
      });
      layer.setMap(map);
        
        
        
        
        
        
      }

      google.maps.event.addDomListener(window, 'load', initialize);
    </script>
    
  </head>
  
  <body style="font-family: Arial; border: 0 none;">
    <div id="map-canvas" style="width: 700px; height: 500px"></div>
  </body>
</html>