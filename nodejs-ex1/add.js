
// think of exports as this
// var exports = module.exports;

function add(a,b) {
    return a + b;
}

function subtract(a,b) {
    return a - b;
}


// module.exports = { 
//     add, 
//     subtract 
// };

module.exports.add = add;
module.exports.subtract = subtract;

