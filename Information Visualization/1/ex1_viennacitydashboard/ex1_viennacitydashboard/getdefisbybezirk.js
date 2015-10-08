function getdefisbybezirk(url) {
	
	
	var districtCount = 23;
	
	$.ajax({
		url: url,
		datatype: "JSON"
	}).done(function(data) {
			
			var districtDefiArray = [];
			//just for initialization
			for (var i = 0; i <= districtCount; i++) {
				districtDefiArray[i] = 0;
			}
		
			//count districts
			$.each(data.features, function() {
				var districtIdx = parseInt(this.properties['BEZIRK']);
				districtDefiArray[districtIdx]++;
			});
			
			var labelArray = [['DistrictNr','DefiCount']];
			for (var i = 0; i < districtDefiArray.length; i++) {
		
				labelArray.push(["Bezirk"+i,districtDefiArray[i]]);
			}
			console.log(labelArray);
			
			var tableData = google.visualization.arrayToDataTable(labelArray);
			var pieChart = new google.visualization.PieChart($('#map_div')[0]);
			pieChart.draw(tableData,{title: 'Cool Chart'});
			
			var tableChart = new google.visualization.Table($('#table_div')[0]);
			tableChart.draw(tableData);
					
		}); 
		
		
	
	
	
	
	
	
	
	
	
	/*
	// this.properties['BEZIRK'] // district number 1- 24
	//
	
	//var districtCount = 23;
	//var districtIdx = parseInt(this.properties['BEZIRK']);
	
	
	var districtArray = [['Bezirk','Defi Count']];
	districtArray.push(['bez 1',10]);
	districtArray.push(['bez 2',30]);
	districtArray.push(['bez 3',66]);
	//TODO fill district array with data 
	
	//------------------- Draw Pie Chart 
	var tableData = google.visualization.arrayToDataTable(districtArray);
	var chart = new google.visualization.PieChart($('#map_div')[0]);
	chart.draw(tableData,{title: 'Pie Chart of Defis by Bezirk'});

	//------------------- Draw Data Table
	
	var table = new google.visualization.Table($('#table_div')[0]);
	table.draw(tableData,{showRowNumbers: false});
	*/
	
}