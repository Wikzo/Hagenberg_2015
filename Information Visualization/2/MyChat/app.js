var app = require('express')();
var http = require('http').Server(app);
var express = require("express");
var io = require('socket.io')(http);


// same as: https://stackoverflow.com/questions/18905872/expressjs-where-express-static-dirname-point-to
// used to access files in director (e.g., http://localhost:3000/styles.css
app.use(express.static(__dirname));


// routing
app.get('/', function (req, res) {
    res.send('<h1>Hello world</h1>');
});

app.get('/upload/:anystring', function (req, res) {
    res.send('Hello, you typed: ' + req.params.anystring);

});


// io chat
io.on('connection', function (socket) {
    socket.emit('news', { hello: 'world' });
    socket.on('talktoserver', function (data) {
        console.log(data);
    });
    socket.on('disconnect', function () {
        io.emit('user disconnected');
    });
});

http.listen(2500, function() {
    console.log("Connected to :2500");
});
