var express = require('express');
var app = express();
var http = require('http').Server(app);


var Datastore = require('nedb');
var db = new Datastore({ filename: __dirname + '/db/pathdb', autoload: true });


// TODO: server entry point files via express
app.use(express.static(__dirname));

app.get('/insert', function(req, res, next) {
    //id gets set outomatically !
    db.insert({ planet: "planet" + Math.random(), system: 'solar' });
    res.send('inserted data');
});

// looks for the system variable (e.g., "solar")
app.get('/find/:system', function(req, res, next) {
    console.log(req.params.system);
    db.find({ system: req.params.system }, function (err, docs) {
        if (!err) {
            //res.send(docs.length + "<br>" + docs);
            res.send(docs); // all objects
        }
    });
});

http.listen(2500, function(){
    console.log('listening on *:2500');
});
