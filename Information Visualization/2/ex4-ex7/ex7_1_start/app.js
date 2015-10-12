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
    getMapOverlays();
    getMapCluster();
	//TODO display map overlay
	//TODO display map clusters
	socket.on("mapsoverlay_added", function(overlay) {
        console.dir(overlay); // log all points
        db.insert(overlay); // insert to database

        getMapOverlays();
        getMapCluster();

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


function getMapCluster() {

    db.find({"overlay.type": "polyline" }, function (err, docs) {
        if (!err) {
            var pointList = [];
            //1.) get all points (lat long as normal array)
            for (var i = 0; i < docs.length; i++) {
                var geoCoords = docs[i].overlay.geo;
                for (var j = 0; j < geoCoords.length; j++) {
                    var latLng = geoCoords[j];
                    var point = [latLng.lat,latLng.lng];
                    pointList.push(point);
                }
            }

            var km = new kMeans({K: 6});
            km.cluster(pointList);
            while (km.step()) {
                km.findClosestCentroids();
                km.moveCentroids();
                if(km.hasConverged()) break;
            }
            var clusterWeights = [];
            for (var i = 0; i < km.clusters.length; i++) {
                clusterWeights[i] = km.clusters[i].length;
            }
            io.emit("mapsoverlay_getcluster",km.centroids,clusterWeights);
            console.log("cluster calculation done");
        }
    });
}
