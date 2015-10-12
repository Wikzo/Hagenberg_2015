function route(handle, pathname, response, postData) {
    console.log("Router about to route for: " + pathname);

    if (typeof handle[pathname] === 'function') {
        handle[pathname](response, postData);
    }
    else {
        console.log("No request handler found for '" + pathname + "'");

        // https://nodejs.org/api/http.html#http_response_writehead_statuscode_statusmessage_headers
        response.writeHead(200, {"Content-Type": "text/plain"});
        response.write("404 Not Found.");
        response.end();
    }
}

exports.route = route;