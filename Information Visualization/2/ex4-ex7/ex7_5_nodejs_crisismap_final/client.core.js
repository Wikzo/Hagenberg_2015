
/* Misc Propeties for the applicationn layer */

var panorama;
var directionsService;
var directionsDisplay;
var socket;
var coordinate = '';
var socket_id = '';
var time = '';
var allFlightPath = [];
var allMarkerStress = [];
var streetLineStatus = 0;
var markers = [];
var position_from = [],
    infowindow = [];
serverUserTime = 0;
createGroup = 0;
room_id = '';

var map;
var mapOverlays =[];
/* ---------------------------------------------------------------*/
/* initMap is executed on google.setOnLoadCallback                */
function initMap() {
	console.log("initmap");
	var viennaLng = 47.37639;
	var viennaLat = 15.09113;

	$("#coor_k").val(Math.random()*3.0 + viennaLng);   //Vienna Longitude 16.3738189
	$("#coor_B").val(Math.random()*3.0 + viennaLat); //Vienna Latitude 48.2081743
		
	//$("#coor_k").val(Math.random() + 12);
	//$("#coor_B").val(Math.random() + 107);
	var mapOptions = {
		//center: new google.maps.LatLng(12.2484861, 109.183363),
		center: new google.maps.LatLng(48, 16),
		zoom: 4
	};
	map = new google.maps.Map(document.getElementById("map"), mapOptions);
	panorama = map.getStreetView();
	directionsService = new google.maps.DirectionsService();
	directionsDisplay = new google.maps.DirectionsRenderer();
	
	google.maps.event.addListener(map, 'dblclick', function(event) {
		if (room_id != '') {
			socket.emit("event_room", room_id, "travel", [event.latLng.lat(), event.latLng.lng()]);
		} else {
			position_from = [coordinate];
			position_to = [event.latLng.lat(), event.latLng.lng()];
			travel(position_from, position_to);
		}
	});
	google.maps.event.addListener(map, 'bounds_changed', function() {
		if (room_id != '') {
			var mapview = {};
			center = {
				"lat": map.getCenter().lat(),//map.center.k,
				"lng": map.getCenter().lng() //map.center.B,
			}
			mapview.zoom = map.zoom;
			mapview.center = center;
			socket.emit("event_room", room_id, "bounds", mapview);
		}
	});
	google.maps.event.addListener(panorama, 'visible_changed', function() {
		streetview = {};
		if (panorama.getVisible()) {
			streetview.show = 1;
			streetview.getPano = panorama.getPano();
			streetview.getPov = panorama.getPov();
			streetview.getPosition = panorama.getPosition();
			streetview.getZoom = panorama.getZoom();
		} else {
			streetview.show = 0;
		}
		socket.emit("event_room", room_id, "streetview", streetview);
	});

	  
	google.maps.event.addListener(panorama, 'position_changed', function() {
		streetview_changed(panorama);
	});
	google.maps.event.addListener(panorama, 'pov_changed', function() {
		streetview_changed(panorama);
	});
	google.maps.event.addListener(panorama, 'zoom_changed', function() {
		streetview_changed(panorama);
	});
}	
/* ---------------------------------------------------------------*/
/* initMapDrawingManager is executed on google.setOnLoadCallback */
/* for drawing manager @see https://developers.google.com/maps/documentation/javascript/drawinglayer */
/* BEST EXAMPLE 
	for drawing polylines with marker 
		@see http://stackoverflow.com/questions/23893027/capture-google-maps-polyline-on-click-per-start-end 
		@see http://jsfiddle.net/8Xqaw/12/
	
*/
function initMapDrawingManager() {

/* Map Manager for Drawing */
  var drawingManager = new google.maps.drawing.DrawingManager({
    drawingMode: google.maps.drawing.OverlayType.MARKER,
    drawingControl: true,
    drawingControlOptions: {
      position: google.maps.ControlPosition.TOP_CENTER,
      drawingModes: [
        //google.maps.drawing.OverlayType.MARKER,
        //google.maps.drawing.OverlayType.CIRCLE,
        //google.maps.drawing.OverlayType.POLYGON,
        //google.maps.drawing.OverlayType.RECTANGLE,
		google.maps.drawing.OverlayType.POLYLINE
      ]
    },
    markerOptions: {
      icon: 'http://maps.google.com/mapfiles/ms/icons/blue.png'
    },
    circleOptions: {
      fillColor: '#ffff00',
      fillOpacity: 1,
      strokeWeight: 5,
      clickable: false,
      editable: true,
      zIndex: 1
    }
  });
  
  drawingManager.setMap(map);
  //
  drawingManager.setDrawingMode(null);
  /*document.getElementById('add-event-button').onclick = function() {
    drawingManager.setDrawingMode(google.maps.drawing.OverlayType.MARKER);
  }*/
  
  google.maps.event.addListener(map, 'click', function(event)
    {
	
        console.log("new click event" + event.latLng + drawingManager.getDrawingMode());
		//drawLine(event.latLng);
    });
  

    google.maps.event.addListener(drawingManager, 'circlecomplete', function(circle) {
	  var radius = circle.getRadius();
	  alert('ciclecomplete');
	});

	//event is called when drawing polyline is finished with choosing other drawing tool, or double click on map (ends the line)
	google.maps.event.addListener(drawingManager, 'overlaycomplete', function(event) {
	  
	  
	  if (event.type == google.maps.drawing.OverlayType.POLYLINE) {
		//this is not necessary, i just wanted to check again
		if (event.overlay instanceof google.maps.Polyline) {
		
			var tmpPathArray = [];
		
			console.log("yeah polyline");
			
			var polyLine = event.overlay;
			//remove line right after draw - because it gets redrawn with database anyway 
			
			
			
			var path = polyLine.getPath();  //instanceof MVCArray<LatLng>  polyLIne.getPath().getArray() returns reference to Array
			
			
			 for (var i = 0; i < path.getLength(); i++) { 
			  var latLng = path.getAt(i);  //instanceof google.maps.LatLng
			  //LatLngLiteral-Objects are understood by google.maps functions and can be used instead of LatLng-Objects e.g. {lat: -34, lng: 151} 
			  //can be used with JSON.parse() / JSON.stringify(), below see an appropriate json representation
			  var latLngLiteral = {lat: latLng.lat(), lng: latLng.lng()};
			  tmpPathArray.push(latLngLiteral);
			  
			  /*
			  var marker = new google.maps.Marker({
				position: latLng,
				map: map,
				title: 'Hello World!'
			  });
			  */
			 } 
			 
			 polyLine.setMap(null);
			
			//console.log("done" +  JSON.stringify(tmpPathArray));
			//console.log("original " +  JSON.stringify(polyLine.getPath().getArray()));
			
			
			socket.emit("mapsoverlay_added",{overlay: {type: "polyline", geo: tmpPathArray}});
			
			//var bounds = polyLine.getPath().getArray();
			//console.dir(bounds);
			//console.log(polyLine.strokeColor);
		} 
		
		//alert("finished polyline");
		
		drawingManager.setDrawingMode(null);
	  }
	  
	  
	  // http://stackoverflow.com/questions/11020407/google-maps-api-v3-drawing-manager
	  if (event.type == google.maps.drawing.OverlayType.MARKER) {
		//alert('new marker added');
		
		var newMarker = event.overlay;
        newMarker.content = "marker #" + markers.length;
        google.maps.event.addListener(newMarker, 'click', function() {
          infowindow.setContent(this.content);
          infowindow.open(map, this);
        });
        markers.push(newMarker);
		
		//auto pan to new marker 
		window.setTimeout(function() {
		  map.panTo(newMarker.getPosition());
		}, 3000);
		
		console.log("lat" + newMarker.getPosition().lat() + " long: " + newMarker.getPosition().lng());
	
	/*
		marker_stress = new google.maps.Marker({
			position: new google.maps.LatLng(data[data.length - 1].k, data[data.length - 1].B),
			icon: "http://icons.iconarchive.com/icons/fatcow/farm-fresh/32/hand-point-270-icon.png"
		});
	*/
	
	//console.log("lat".newMarker.getPosition().lat(). " long: ". newMarker.getPosition().lng());
	
	
	  }
	});
  

}

/* ---------------------------------------------------------------*/
/* initSocketIo is executed on google.setOnLoadCallback           */
function initSocketIo() {
	socket = io();
//$(function() {	
	console.log("initsocketio");
	
    socket.on("invite_room", function(id, invite_room_id) {
        if (socket_id == id) {
            $("#invite_form").show();
        }
        $("#invite_form #no").click(function() {
            $("#invite_form").hide();
        });
        $("#invite_form #yes").click(function() {
            room_id = invite_room_id;
            socket.emit("status_invited_room", id, invite_room_id, 1);
            $("#invite_form").hide();
        });
    });

    
	
    socket.on("server_user", function(server_user) {
        if (serverUserTime == 0) {
            console.log("getserveruser");
			for (var i = 0; i < server_user.length; i++) {
                data_user = server_user[i];
                makeMarkerUser(data_user, server_user[i].id);
            }
            serverUserTime = 1;
        }
    });
	
    socket.on("user_connection", function(id) {
        //Đăng nhập vào hệ thống sẽ lấy được 1 ID từ phía server
        if (socket_id == '') {
            socket_id = id;
            //Làm việc khi lấy được ID
        }
    });
    $("#send_message").click(function() {
        data_message = {
            id: socket_id,
            message: $("#chat_message").val(),
            name: $("#user_name").val()
        };
        $("#chat_message").val("");
        socket.emit("message", data_message);
    });
    $("#button_login").click(function() {
        $("#login_panel").css({
            display: "none"
        });
        $("#world, .chat_area").css({
            display: "block"
        })
        var name = $("#user_name").val();
        var sex = $("#sex input:checked").prop("id");
        coordinate = [$("#coor_k").val(), $("#coor_B").val()];
        socket.emit("create_user", {
            id: socket_id,
            name: name,
            sex: sex,
            coordinate: coordinate
        });
    });
    //Lấy thông tin những người dùng trước
    //Tạo người dùng
    socket.on("create_user", function(create_user) {
        makeMarkerUser(create_user, create_user.id);
		console.log("socket on - create_user");
    });
    //Người dùng thoát ra
    socket.on("user_disconnect", function(id) {
        markers[$.trim(id)].setMap(null);
        markers[$.trim(id)] = undefined;
    })
    //Xử lý gửi chat message
    socket.on("message", function(data_message) {
        if (typeof(infowindow[data_message.id]) === 'undefined') {
            infowindow[data_message.id] = new google.maps.InfoWindow();
            infowindow[data_message.id].setContent("<b>" + data_message.name + "</b>: " + data_message.message);
            infowindow[data_message.id].open(map, markers[data_message.id]);
        } else {
            infowindow[data_message.id].setContent("<b>" + data_message.name + "</b>: " + data_message.message);
        }
        $("#message").html($("#message").html() + "<b>" + data_message.name + "</b>: " + data_message.message + "<br/>");
        if (time != '') {
            clearTimeout(time);
        }
        setTimeout(function() {
            /*if (typeof(infowindow[data_message.id]) !== undefined) {
                infowindow[data_message.id].close();
                delete infowindow[data_message.id];
            }*/
            infowindow[data_message.id].setContent("<b>" + data_message.name + "</b>");
        }, 5000);
    });
    socket.on("event_room", function(user_in_room, message_type, event_room) {
        if (message_type == "travel") {
            position_from = [];
            for (var i = 0; i < user_in_room.length; i++) {
                position_from.push([markers[user_in_room[i]].Ie.ka.x, markers[user_in_room[i]].Ie.ka.y]); //Hồi trước He giờ Ie, con mẹ Google Map
            }
            position_to = [event_room[0], event_room[1]];
            travel(position_from, position_to);
        } else if (message_type == "bounds") {
            map.setOptions({
                "zoom": event_room.zoom,
                "center": event_room.center
            });
        } else if (message_type == "streetview") {
            console.log(event_room);
            if (event_room.show == 1) {
                panorama.setVisible(true);
                panorama.setPano(event_room.setPano);
                panorama.setPov(event_room.getPov);
                panorama.setPosition({
                    "lat": event_room.getPosition.k,
                    "lng": event_room.getPosition.B
                });
                panorama.setZoom(event_room.getZoom);
            } else {
                panorama.setVisible(false);
            }
        }
    });

    $(".world").click(function() {
        $("#world").css({
            display: "block"
        });
        $("#room").css({
            display: "none"
        });
        return false;
    });
    $("#createroom").click(function(event) {
        if (createGroup == 0 && room_id == '') {
            room_id = Math.random().toString(36).substring(7);
            socket.emit("create_room", room_id);
            createGroup = 1;
            $("#room_message").html("Created new room<br/>");
        }
        $("#world").css({
            display: "none"
        });
        $("#room").css({
            display: "block"
        });
        return false;
    });
    $("#send_room_message").click(function(event) {
        var data_message = {
            message: $("#chat_room_message").val(),
            name: $("#user_name").val()
        }
        socket.emit("room_message", room_id, data_message);
        $("#chat_room_message").val("");
        console.log(room_id);
        //console.log(data_message);
    });
    socket.on("room_message", function(data_message) {
        $("#room_message").html($("#room_message").html() + "<b>" + data_message.name + "</b>: " + data_message.message + "<br/>");
    })
	
	
	socket.on("mapsoverlay_getall", function(docs) {
		
		//clear all before drawing again 
		for (var i = 0; i < mapOverlays.length; i++) {
			mapOverlays[i].setMap(null);
		}
		
		for (var i = 0; i < docs.length; i++) {
				//console.log("overlay " +i);
				if (docs[i].overlay.type === "polyline") {
					//console.dir(docs[i].overlay.geo.length + " " +docs[i].overlay.geo);
					var geoCoords = docs[i].overlay.geo;
					
					/* the geoCoods 
					var flightPlanCoordinates = [
						{lat: 37.772, lng: -122.214},
						{lat: 21.291, lng: -157.821},
						{lat: -18.142, lng: 178.431},
						{lat: -27.467, lng: 153.027}
					  ];
					  */
					//1.) draw line
					var polyLine = new google.maps.Polyline({
						path: geoCoords,
						geodesic: true,
						strokeColor: '#FF0000',
						strokeOpacity: 1.0,
						strokeWeight: 2
					  });					  
					polyLine.setMap(map);
					mapOverlays.push(polyLine);
					//2.) draw marker
					/*
					for (var j = 0; j < geoCoords.length; j++) {
						var latLng = geoCoords[j];
						
						var marker = new google.maps.Marker({
							position: latLng,
							map: map,
							title: 'addedfromdb',
							icon: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'
						});
					
					}
					*/
					
				}
		
			}
		});//end mapsoverlay_getall
		
		socket.on("mapsoverlay_getcluster", function(clusterCenters,clusterWeights) {
			
			for (var i = 0; i < clusterCenters.length;i++) {
				
				var latLng = {lat: clusterCenters[i][0], lng: clusterCenters[i][1]};
				
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
				
				var marker = new google.maps.Marker({
					position: latLng,
					map: map,
					title: 'current people: ' + clusterWeights[i],
					icon: 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'
				});
				mapOverlays.push(marker);
				
				
				
			}
		});
		
//});
}
/* ---------------------------------------------------------------*/

function makeMarkerUser(data_user, id) {
    if (data_user.sex == "male") {
        var icon_user = "http://icons.iconarchive.com/icons/hopstarter/face-avatars/24/Male-Face-D4-icon.png";
    } else {
        var icon_user = "http://icons.iconarchive.com/icons/hopstarter/face-avatars/24/Female-Face-FC-2-icon.png";
    }
    markers[id] = new google.maps.Marker({
        position: new google.maps.LatLng(data_user.coordinate[0], data_user.coordinate[1]),
        icon: icon_user
    });
    markers[id].setMap(map);
    markers[id].id = data_user.id;

    infowindow[id] = new google.maps.InfoWindow();
    infowindow[id].setContent("<b>" + data_user.name + "</b>");
    infowindow[id].open(map, markers[id]);
	
	console.log("makemarkeruser");
	
    google.maps.event.addListener(markers[id], 'dblclick', function(marker, id) {
        if (createGroup == 0) {
            alert("Please create group");
        } else {

            if (data_user.id != socket_id) {
                socket.emit("invite_room", data_user.id, room_id);
                alert("Sent invite message");
            } else {
                alert("You can't invite yourself");
            }

        }
    });
}

//------------end of init
function streetview_changed(panorama) {
        streetview = {};
        streetview.show = 1;
        streetview.getPano = panorama.getPano();
        streetview.getPov = panorama.getPov();
        streetview.getPosition = panorama.getPosition();
        streetview.getZoom = panorama.getZoom();
        socket.emit("event_room", room_id, "streetview", streetview);
    }


function travel(from, to) {
    for (var i = 0; i < Math.max(allFlightPath.length, allMarkerStress.length); i++) {
        if (typeof(allFlightPath[i]) !== undefined) {
            allFlightPath[i].setMap(null);
        }
        if (typeof(allMarkerStress[i]) !== undefined) {
            allMarkerStress[i].setMap(null);
        }
    }
    allFlightPath = [];
    allMarkerStress = [];
    for (var i = 0; i < from.length; i++) {
        var request = {
            origin: new google.maps.LatLng(from[i][0], from[i][1]),
            destination: new google.maps.LatLng(to[0], to[1]), //lat, lng
            travelMode: google.maps.TravelMode["WALKING"]
        };
        directionsService.route(request, function(response, status) {
            var flightPath = '',
                marker_stress = '';
            if (status == google.maps.DirectionsStatus.OK) {
                data = response.routes[0].overview_path;
                color = "#ff0000";
                opacity = 1;

                flightPath = new google.maps.Polyline({
                    path: data,
                    geodesic: true,
                    strokeColor: color,
                    strokeOpacity: opacity,
                    strokeWeight: 2,
                    map: map
                });
                flightPath.setMap(map);
                marker_stress = new google.maps.Marker({
                    position: new google.maps.LatLng(data[data.length - 1].k, data[data.length - 1].B),
                    icon: "http://icons.iconarchive.com/icons/fatcow/farm-fresh/32/hand-point-270-icon.png"
                });
                marker_stress.setMap(map);
                allFlightPath.push(flightPath);
                allMarkerStress.push(marker_stress);
            }
        });
    }
}