var express = require("express"),
    app = require("express")(),
    http = require("http").Server(app),
    io = require("socket.io")(http),
    util = require("util"),
    fs = require("fs");

var datastore = require('nedb');
var kMeans = require('kmeans-js');

//chatmap application
var server_user = [];
var clients = []; 
var group_leader = [];

//database
var db = new datastore({ filename: __dirname + '/db/pathdb', autoload: true });

app.use(express.static(__dirname));

http.listen(2500, function() {
    console.log("Connected to :2500");
});

io.sockets.on("connection", function(socket) {
    io.emit("user_connection", socket.id);
	//send server users
    io.emit("server_user", server_user);
	//send map overlays
	getMapOverlays();
	getMapCluster();
	
    socket.on("create_user", function(data_user) {
        server_user.push(data_user);
        io.emit("create_user", data_user); 
		
    });
	
    socket.on("message", function(data_message) {
        io.emit("message", data_message);
    })
    socket.on("disconnect", function() {
        var i = 0;
        for (var i = 0; i < server_user.length; i++) {
            if (server_user[i].id == socket.id) {
                server_user.splice(i, 1); //Xóa dữ liệu người dùng
            }
        }
        io.emit("user_disconnect", socket.id);
        //fs.writeFile('socket.txt', util.inspect(socket, false, null));
    });
    //Tạo nhóm mới
    socket.on("create_room", function(room_id) {
        io.sockets.connected[socket.id].join(room_id);
        group_leader[room_id] = socket.id;
    });
    socket.on("invite_room", function(id, room_id) {
        io.sockets.connected[id].emit("invite_room", id, room_id);
    });
    socket.on("status_invited_room", function(id, room_id, status) {
        if (status == 1) {
            io.sockets.connected[id].join(room_id);
        }
    });
    socket.on("event_room", function(room_id, message_type, event_room) {
        if (group_leader[room_id] == socket.id) {
            if (message_type == "travel") {
                socket.in(room_id).emit("event_room", getUserRoom(room_id), message_type, event_room);
                io.sockets.connected[socket.id].emit("event_room", getUserRoom(room_id), message_type, event_room);
                console.log("Da chi duong");
            } else if (message_type == "bounds" || message_type == "streetview") {
                socket.in(room_id).emit("event_room", '', message_type, event_room);
            }
        }
    });
    socket.on("room_message", function(room_id, data_message) {
        socket.in(room_id).emit("room_message", data_message);
        io.sockets.connected[socket.id].emit("room_message", data_message);
    })
	
	socket.on("mapsoverlay_added", function(overlay) {
        //io.emit("message", data_message);
		console.log("overlay " + overlay);
		
		//id gets set outomatically ! 
		db.insert(overlay);
			
		getMapOverlays();
		getMapCluster();
		
    })
	
	
});


//this returns all users that are in the room ? TODO _yak
//his is an associative array with keys that are socket ids. In our case, we wanted to know the number //of clients in a room, so we did Object.keys(io.nsps[yourNamespace].adapter.rooms[roomName]).length
function getUserRoom(room_id) {
    var user = [];
    for (var key in io.sockets.adapter.rooms[room_id]) {
        if (io.sockets.adapter.rooms[room_id][key] == true) {
            user.push(key);
        }
    }
    return user;
}

function getMapOverlays() {
		/* DB Format
		{"overlay":{
			"type":"polyline",												//overlaytype
			"geo":[{"lat":48.378145469762444,"lng":15.3094482421875},..]}, 	//contains array with points
			"_id":"pvqjZYjL06tRb9lE"} 										//this gets added automatically
		*/	
		//db.find{} this finds all documents, If no document is found, docs is equal to []
		db.find({"overlay.type": "polyline" }, function (err, docs) {
		   if (err) {
				  // console.log('nedb returned' + require('util').inspect(err)); 	  
		  }else {
			console.log('found ' + docs.length);
			for (var i = 0; i < docs.length; i++) {
				console.log("overlay " +i);
				console.dir(docs[i].overlay);
			 }
			io.emit("mapsoverlay_getall", docs);
			//console.dir(docs);	

			}
		});
		
	
}

function getMapCluster() {

		db.find({"overlay.type": "polyline" }, function (err, docs) {
		 if (!err) {
			var pointIdx = 0;
			var pointList = [];
			for (var i = 0; i < docs.length; i++) {
				//console.log("overlay " +i);
				if (docs[i].overlay.type === "polyline") {
					//console.dir(docs[i].overlay.geo.length + " " +docs[i].overlay.geo);
					var geoCoords = docs[i].overlay.geo;
					
					for (var j = 0; j < geoCoords.length; j++) {
						var latLng = geoCoords[j];
						var point = [latLng.lat,latLng.lng];
						pointList.push(point);
						pointIdx++;
					}
				}
			}
			
			//console.log("pointsfound" + pointIdx);
			//console.dir(pointList);
		 
		 
			
			//var data = [[1, 2, 3], [2,3,5], [2,10,5] , [2,3,5], [2,3,5], [2,3,5], [2,3,5], [69, 10, 25]];

			var km = new kMeans({
				K: 6
			});

			km.cluster(pointList);
			while (km.step()) {
				km.findClosestCentroids();
				km.moveCentroids();

				console.log(km.centroids);

				if(km.hasConverged()) break;
			}
			
			
			var clusterWeights = [];
			for (var i = 0; i < km.clusters.length; i++) {
				
				clusterWeights[i] = km.clusters[i].length;
			}
			
			

			//console.log('Finished in:', km.currentIteration, ' iterations');
			//console.log('centroids',km.centroids);
			//console.log('clusters',km.clusters);

			io.emit("mapsoverlay_getcluster",km.centroids,clusterWeights);			
			console.log("cluster calculation done");
		}
	});




}