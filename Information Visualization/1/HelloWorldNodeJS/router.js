function route(handle, pathname) {
    console.log("Router about to route for: " + pathname);

    if (typeof handle[pathname] === 'function') {
        handle[pathname]();
        return handle[pathname]();
    }
    else {
        console.log("No request handler found for '" + pathname + "'");
        return "404 Not found";
    }
}

exports.route = route;