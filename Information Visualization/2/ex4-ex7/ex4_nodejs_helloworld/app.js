var express = require("express"),
    app = require("express")(),
    http = require("http").Server(app);

http.listen(2500, function() {
    console.log("Connected to :2500");
});

//app.use(express.static(__dirname));


console.log("test");


app.get("/", function(req, res) {
			res.send('hello world');
    	//res.sendfile(__dirname + "/index.html");
		console.log("test2");
		
});


