
var map;

/* ---------------------------------------------------------------*/
/* initMap is executed on google.setOnLoadCallback                */
function initMap() {
	var viennaLng = 47.37639;
	var viennaLat = 15.09113;
	$("#coor_k").val(Math.random()*3.0 + viennaLng);   //Vienna Longitude 16.3738189
	$("#coor_B").val(Math.random()*3.0 + viennaLat); //Vienna Latitude 48.2081743
		
	var mapOptions = {
		center: new google.maps.LatLng(48, 16),
		zoom: 4
	};
	map = new google.maps.Map(document.getElementById("map"), mapOptions);
}