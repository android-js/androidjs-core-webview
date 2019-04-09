var back = require('androidjs').back;
var fs = require('fs');
//var FileReader = require('FileReader');
var Buffer = require('Buffer');


back.on('save-File', function(name, blob){
    console.log('save-File called');
    var buffer = new Buffer(blob)
    fs.writeFileSync('/storage/emulated/0/' + name, buffer);

})


back.on('hello', function(){
    console.log('Hello World');
})
