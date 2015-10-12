var map;
var mapOverlays = [];


/* ---------------------------------------------------------------*/
/* initMap is executed on google.setOnLoadCallback                */
function initMap() {
    var viennaLng = 47.37639;
    var viennaLat = 15.09113;
    $("#coor_k").val(Math.random() * 3.0 + viennaLng);   //Vienna Longitude 16.3738189
    $("#coor_B").val(Math.random() * 3.0 + viennaLat); //Vienna Latitude 48.2081743

    var mapOptions = {
        center: new google.maps.LatLng(48, 16),
        zoom: 4
    };
    //map = new google.maps.Map(document.getElementById("map"), mapOptions); // old
    map = new google.maps.Map($('#map')[0], mapOptions); // using jQuery
}

/* ---------------------------------------------------------------*/
/* initMapDrawingManager is executed on google.setOnLoadCallback */
function initMapDrawingManager() {

    console.log("init drawing manager");

    var drawingManager = new google.maps.drawing.DrawingManager({
        drawingMode: google.maps.drawing.OverlayType.MARKER,
        drawingControl: true,
        drawingControlOptions: {
            position: google.maps.ControlPosition.TOP_LEFT,
            drawingModes: [
                //google.maps.drawing.OverlayType.MARKER,
                //google.maps.drawing.OverlayType.CIRCLE,
                //google.maps.drawing.OverlayType.POLYGON,
                //google.maps.drawing.OverlayType.RECTANGLE,
                google.maps.drawing.OverlayType.POLYLINE
            ]
        }

    });
    drawingManager.setMap(map);
    drawingManager.setDrawingMode(null);

    //Overlay - POLYLINE - drawnEvent
    google.maps.event.addListener(drawingManager, 'overlaycomplete', function (event) {
        console.log("Polylines completed");

        var polyLine = event.overlay;
        var path = polyLine.getPath();

        var tmpPathArray = [];

        // manually getting longitude and latitude data
        for (var i = 0; i < path.getLength(); i++) {
            var point = path.getAt(i);

            var latLngLiteral = {lat: point.lat(), lng: point.lng()};
            //console.log(latLngLiteral);

            tmpPathArray.push(latLngLiteral);

            //console.log(point.lat() + ", " + point.lng());
        }

        polyLine.setMap(null); // remove lines after drawing
        drawingManager.setDrawingMode(null); // set to moving mode instead of drawing

        socket.emit("mapsoverlay_added",
            {
                overlay: {
                    type: "polyline",
                    geo: tmpPathArray
                }
            }); // end socket

    }); // end overlay complete event
} // end initMapDrawingManager

function initSocketIo() {
    console.log("Initialized socket io");
    socket = io();

    socket.on("mapsoverlay_getAll", function (docs) {
        console.log("docs found");

        // THIS --> getMapOverlays(); // redo

        // delete all exisiting lines
        for (var i = 0; i < mapOverlays.length; i++) {
            mapOverlays[i].setMap(null);
        }

        mapOverlays = [];

        for (var i = 0; i < docs.length; i++) {
            var geoCoords = docs[i].overlay.geo;
            //console.log(geoCoords);

            var polyLine = new google.maps.Polyline(
                {
                    path: geoCoords,
                    geodesic: true,
                    strokeColor: '#FF0000',
                    strokeOpacity: 1.0,
                    strokeWeight: 2
                }
            );
            polyLine.setMap(map);

            mapOverlays.push(polyLine); // prevent re-drawing all old lines
        }

    });

    socket.on("mapsoverlay_getcluster", function(clusterCenters,clusterWeights) {

        for (var i = 0; i < clusterCenters.length;i++) {
            var latLng = {lat: clusterCenters[i][0], lng: clusterCenters[i][1]};

            //1.) draw circle
            var cityCircle = new google.maps.Circle({
                strokeColor: '#0000FF',
                strokeOpacity: 0.3,
                strokeWeight: 2,
                fillColor: '#0000FF',
                fillOpacity: 0.2,
                map: map,
                center: latLng,
                radius: Math.sqrt(clusterWeights[i]) * 80000  //approx 200.000 is a good value for display
            });
            mapOverlays.push(cityCircle);
            //2.) draw marker
            var marker = new google.maps.Marker({
                position: latLng,
                map: map,
                title: 'current people: ' + clusterWeights[i],
                icon: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'
            });
            mapOverlays.push(marker);
        }
    });

}
