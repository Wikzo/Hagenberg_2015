var http = require("http");
var url = require('url');

function start(route) {
    function onRequest(request, response) {

        var timeStamp = new Date().toString();

        var pathName = url.parse(request.url).pathname;
        console.log("*Server* request for " + pathName + " received [" + timeStamp + "]");

        route(pathName);

        // https://nodejs.org/api/http.html#http_response_writehead_statuscode_statusmessage_headers
        response.writeHead(200, {"Content-Type": "text/plain"});
        response.write("Hello World");
        response.end();
    }

    http.createServer(onRequest).listen(8888);
    console.log("Server has started.");
}

exports.start = start;