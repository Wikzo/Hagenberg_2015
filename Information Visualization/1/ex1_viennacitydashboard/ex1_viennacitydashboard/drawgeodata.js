function drawgeodata(url, labelId ) {

	//var markerArray = [];
	//alert("hello");
	//console.log("hello");
	
	$.ajax({
		url: url,
		datatype: "JSON"
	}).done(function(data) {
	
			var markerArray = [['Long','Lat','Name']];
			console.dir(data);
			
			$.each(data.features, function() {
				var lat = this.geometry.coordinates[0];
				var lng = this.geometry.coordinates[1];
				var labelName = this.properties[labelId];
				//console.log(labelName + " lat: " + lat + " long " + lng);
				
				var markerItem = [lng,lat,labelName];
				markerArray.push(markerItem);
				
			});
			
			var dataTable = google.visualization.arrayToDataTable(markerArray);
			//document.getElementById('map_div')
			var map = new google.visualization.Map($('#map_div')[0]);  
			map.draw(dataTable,{showTip:true});
			
			
			
			
		}); 
	

}