var app = require('express')();
var http = require('http').Server(app);
var url = require('url');
var path = require('path');
var express = require("express");

// same as: https://stackoverflow.com/questions/18905872/expressjs-where-express-static-dirname-point-to
// used to access files in director (e.g., http://localhost:3000/styles.css
app.use(express.static(__dirname));


app.get('/', function (req, res) {
    res.send('<h1>Hello world</h1>');
});

app.get('/upload/:anystring', function (req, res) {
    res.send('Hello, you typed: ' + req.params.anystring);

});


http.listen(3000, function () {
    console.log('listening on *:3000');
});
