// http://www.nodebeginner.org/
/*
function execute(someFunction, value) {
    someFunction(value);
}

execute(function (word) {
    console.log(word)
}, "Hejsa");

execute(function (word) {
    console.log(word);
}, "hejsa2");
*/

// more examples ...
function databaseSimulation(callback, waitTime) {
    console.log("beginning big database work...");

    setTimeout(callback, waitTime);
    console.log("database work has begun.");

}

function databaseDone() {
    console.log("database work done!");
}

databaseSimulation(databaseDone,  5000);
console.log("Hey, I am still here!")
