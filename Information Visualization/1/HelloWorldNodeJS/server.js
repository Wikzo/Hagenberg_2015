var http = require("http");
var url = require('url');

function start(route, handle) {
    function onRequest(request, response) {

        var timeStamp = new Date().toString();

        var postData = "";
        var pathname = url.parse(request.url).pathname;
        console.log("Server request for " + pathname + " received [" + timeStamp + "]");

        request.setEncoding("utf8");

        // called when a new chunk of data was received
        request.addListener("data", function (postDataChunk) {
            postData += postDataChunk;
            console.log("Received POST data chunk '" +
                postDataChunk + "'.");
        });

        // called when all chunks of data have been received
        request.addListener("end", function () {
            route(handle, pathname, response, postData);
        });
    }

    http.createServer(onRequest).listen(8888);
    console.log("Server has started.");
}

exports.start = start;