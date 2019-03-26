var http = require('http');
var leftPad = require('left-pad');
var fs = require('fs')
var versions_server = http.createServer( (request, response) => {
    fs.readFile(process.argv[2], function(err,data){
        if(err) console.log(err);
        response.writeHead(200, { 'Content-Type': 'text/html' });
        response.end(data);
    })
//  response.end('Versions: ' + JSON.stringify(process.versions) + ' left-pad: ' + leftPad(42, 5, '0'));
});
versions_server.listen(3000);
var io = require('socket.io')(versions_server);

io.on('connection', function (socket) {
  console.log('A user connected');
  socket.emit('news', { hello: 'world' });
  socket.on('my other event', function (data) {
    console.log(data);
  });
  socket.on('foo', function (data){
    console.log('foo', data);
  });
  socket.on('helloFromJava', function(message){
    console.log(message);
  })
  socket.emit('getClientPath');
  socket.on('resClientPath', function(message){
    console.log(message);
//    fs.writeFileSync(message + '/hello.txt', 'Hello App');
    fs.readFile(message + '/hello.txt', 'utf-8' ,function(err, data){
        if(err) console.log(err);
        console.log(data);
    })
  })
});