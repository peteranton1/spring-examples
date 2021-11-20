const operatorsObj = require("./add.js");


function greet(name) {
    console.log("Hello there, " + name + "!");
}

greet("Fred");

var a = 1;
var b = 2; 

var aplusb = operatorsObj.add(a, b);
console.log("add(" + a + ", " + b + ") = " + aplusb);

var aminusb = operatorsObj.subtract(a, b);
console.log("subtract(" + a + ", " + b + ") = " + aminusb);