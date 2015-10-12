var express = require("express"),
    app = require("express")(),
    http = require("http").Server(app);

var io = require('socket.io')(http);

	
http.listen(2500, function() {
    console.log("Connected to :2500");
});

app.use(express.static(__dirname));



io.on('connection', function (socket) {
  socket.emit('news', { hello: 'world' });
  socket.on('talktoserver', function (data) {
    console.log(data);
  });
  socket.on('disconnect', function () {
    io.emit('user disconnected');
  });
});





