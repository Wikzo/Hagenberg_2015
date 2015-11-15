// http://www.storminthecastle.com/2013/08/25/use-node-js-to-extract-data-from-the-web-for-fun-and-profit/

var http = require("http");

// Utility function that downloads a URL and invokes
// callback with the data.
function download(url, callback) {
    http.get(url, function(res) {
        var data = "";
        res.on('data', function (chunk) {
            data += chunk;
        });
        res.on("end", function() {
            callback(data);
        });
    }).on("error", function() {
        callback(null);
    });
}

var cheerio = require("cheerio");
var url = "http://www.echojs.com/";

download(url, function(data) {
    if (data) {
        // console.log(data);
        var $ = cheerio.load(data);
        $("article").each(function(i, e) {
            var link = $(e).find("h2>a");
            var poster = $(e).find("username").text();
            console.log(poster+": ["+link.html()+"]("+link.attr("href")+")");
        });
    }
});


/*
// SteamSpy API
// https://www.npmjs.com/package/steamspy

var SteamSpy = require('steamspy');
var client = new SteamSpy();


client.top100forever(function (err, response, data) {
    if ( !err ) {
        console.log(data);
    }
});*/