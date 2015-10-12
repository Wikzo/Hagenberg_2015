var express = require("express"),
    app = require("express")(),
    http = require("http").Server(app),
    io = require("socket.io")(http),
    util = require("util"),
    fs = require("fs");

	var datastore = require('nedb');
	var kMeans = require('kmeans-js');

//database
var db = new datastore({ filename: __dirname + '/db/pathdb', autoload: true });

app.use(express.static(__dirname));

http.listen(2500, function() {
    console.log("Server started at :2500");
});

io.sockets.on("connection", function(socket) {
	console.log("client connected");	
	//TODO display map overlay
	//TODO display map clusters
	socket.on("mapsoverlay_added", function(overlay) {
        console.dir(overlay); // log all points
        db.insert(overlay); // insert to database

        getMapOverlays();

		//TODO display map clusters (same as above)
    });
	socket.on("disconnect", function() {
		console.log("client disconnected");
    });
});

function getMapOverlays()
{
    db.find({"overlay.type":"polyline"}, function(err,docs)
    {
        if(!err)
        {
            //console.log("no errors!");

            io.emit("mapsoverlay_getAll", docs);
        }
    });
}


