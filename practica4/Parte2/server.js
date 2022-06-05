var http = require("http");
var url = require("url");
var fs = require("fs");
var path = require("path");
var socketio = require("socket.io");
var MongoClient = require('mongodb').MongoClient;
var MongoServer = require('mongodb').Server;
var mimeTypes = { "html": "text/html", "jpeg": "image/jpeg", "jpg": "image/jpeg", "png": "image/png", "js": "text/javascript", "css": "text/css", "swf": "application/x-shockwave-flash"};



var httpServer = http.createServer(
	function(request, response) {
		var uri = url.parse(request.url).pathname;
		if (uri=="/") uri = "/cliente.html";
		var fname = path.join(process.cwd(), uri);
		fs.exists(fname, function(exists) {
			if (exists) {
				fs.readFile(fname, function(err, data){
					if (!err) {
						var extension = path.extname(fname).split(".")[1];
						var mimeType = mimeTypes[extension];
						response.writeHead(200, mimeType);
						response.write(data);
						response.end();
					}
					else {
						response.writeHead(200, {"Content-Type": "text/plain"});
						response.write('Error de lectura en el fichero: '+uri);
						response.end();
					}
				});
			}
			else{
				console.log("Peticion invalida: "+uri);
				response.writeHead(200, {"Content-Type": "text/plain"});
				response.write('404 Not Found\n');
				response.end();
			}
		});
	}
);


var estado_persiana = 'cerrada';
var estado_aa = 'encendido';



MongoClient.connect("mongodb://localhost:27017/", { useUnifiedTopology: true }, function(err, db) {

	httpServer.listen(8080); 
 
	var io = socketio(httpServer); 

	var dbo = db.db("P4");

	//dbo.collection("sensores").deleteMany({});  //borramos los datos de la colección para que no haya error


	dbo.createCollection("sensores", function(err, collection){
		io.sockets.on('connection',function(client) {

	    	client.on('poner', function(data) {
				collection.insertOne(data, {safe:true}, function(err, result) {});
				
				io.sockets.emit('actualizar', 
					"temperatura: " + data.temperatura + 
					", luminosidad: " + data.luminosidad + 
					", fecha: "+ data.time );
					

				io.sockets.emit('actualizar_temp', data.temperatura);
				io.sockets.emit('actualizar_lum', data.luminosidad)
			});

	    	client.on('obtener', function () {
				collection.find().sort({_id:-1}).limit(1).forEach(function(result){
					client.emit('actualizar', 
						"temperatura: " + result.temperatura + 
						", luminosidad: " + result.luminosidad + 
						", fecha: "+ result.time );

				});
			});
			
			
			client.on('obtener_temp', function () {
				collection.find().sort({_id:-1}).limit(1).forEach(function(result){
					client.emit('actualizar_temp',result.temperatura);
				});
			});

			client.on('obtener_lum', function () {
				collection.find().sort({_id:-1}).limit(1).forEach(function(result){
					client.emit('actualizar_lum',result.luminosidad);
				});
			});

			
			// Cambia el estado de la persiana
			client.on('cambiar_estado_persiana', function(){
				if (estado_persiana == 'abierta')
					estado_persiana = 'cerrada';
				else
					estado_persiana = 'abierta';

				io.sockets.emit('actualizar_estado_persiana',estado_persiana);
				console.log("servidor: "+estado_persiana);
			});

			// Cambia el estado del aire
			client.on('cambiar_estado_aa', function(){
				if (estado_aa == 'encendido')
					estado_aa = 'apagado';
				else
					estado_aa = 'encendido';

				io.sockets.emit('actualizar_estado_aa',estado_aa);
			});

			client.on('obtener_estado_persiana', function(){
				client.emit('obt_estado_persiana', estado_persiana);
			});

			client.on('obtener_estado_aa', function(){
				client.emit('obt_estado_aa', estado_aa);
			});

			client.on('cerrar_persiana', function(){
				estado_persiana = 'cerrada';
				io.sockets.emit('actualizar_estado_persiana', estado_persiana);
			});

			// Los dos siguientes eventos se producen cuando se supera un umbral de luminosidad/temperatura
			client.on('alerta_lum',function(data){
				io.sockets.emit('actualizar_advertencia_lum',data);
			});

			client.on('alerta_temp',function(data){
				io.sockets.emit('actualizar_advertencia_temp',data);
			});
	    });
    });
});

console.log("Servicio domótico iniciado");

// var http = require("http");
// var url = require("url");
// var fs = require("fs");
// var path = require("path");
// var socketio = require("socket.io");
// var MongoClient = require('mongodb').MongoClient;
// var MongoServer = require('mongodb').Server;
// var mimeTypes = { "html": "text/html", "jpeg": "image/jpeg", "jpg": "image/jpeg", "png": "image/png", "js": "text/javascript", "css": "text/css", "swf": "application/x-shockwave-flash"};

// var httpServer = http.createServer(
// 	function(request, response) {
// 		var uri = url.parse(request.url).pathname;
// 		if (uri=="/") uri = "/client.html";
// 		var fname = path.join(process.cwd(), uri);
// 		fs.exists(fname, function(exists) {
// 			if (exists) {
// 				fs.readFile(fname, function(err, data){
// 					if (!err) {
// 						var extension = path.extname(fname).split(".")[1];
// 						var mimeType = mimeTypes[extension];
// 						response.writeHead(200, mimeType);
// 						response.write(data);
// 						response.end();
// 					}
// 					else {
// 						response.writeHead(200, {"Content-Type": "text/plain"});
// 						response.write('Error de lectura en el fichero: '+uri);
// 						response.end();
// 					}
// 				});
// 			}
// 			else{
// 				console.log("Peticion invalida: "+uri);
// 				response.writeHead(200, {"Content-Type": "text/plain"});
// 				response.write('404 Not Found\n');
// 				response.end();
// 			}
// 		});
// 	}
// );

// var estadoPersiana = 'abierta';
// var estadoAC = 'apagado';

// MongoClient.connect("mongodb://localhost:27017/", function(err, db) {
//     if(!err){
// 		console.log("Conectado a Base de Datos");
// 	}

//     var dbo = db.db("practica4");
// 	httpServer.listen(8080);
// 	var io = socketio(httpServer);

//     dbo.createCollection("sensores", function(err, collection) {
//         if(!err){
// 			console.log("Colección creada en Mongo: " + collection.collectionName);
// 		}

//         io.sockets.on('connection', function(client) {
// 			client.on('poner', function(data) {
//                 collection.insertOne(data, {safe:true}, function(err, result) {
//                     if(!err){
//                         console.log("Elemento insertado en la colección");
//                     }
//                 });
                
//                 var str = "luminosidad: " + data.luminosidad + ", temperatura: " + data.temperatura + ", fecha: " + data.time;
//                 io.sockets.emit('actualizar', str);
//                 io.sockets.emit('actualizar_luminosidad', data.luminosidad);
//                 io.sockets.emit('actualizar_temperatura', data.temperatura);
//             });
            
//             client.on('obtener', function() {
//                 collection.find().sort({_id:-1}).limit(1).forEach(function(result) {
//                     var str = "luminosidad: " + data.luminosidad + ", temperatura: " + data.temperatura + ", fecha: " + data.time;
//                     client.emit('actualizar', str);
//                 });
//             });

//             client.on('obtener_luminosidad', function() {
//                 collection.find().sort({_id:-1}).limit(1).forEach(function(result) {
//                     client.emit('actualizar_luminosidad', result.luminosidad);
//                 });
//             });

//             client.on('obtener_temperatura', function() {
//                 collection.find().sort({_id:-1}).limit(1).forEach(function(result) {
//                     client.emit('actualizar_temperatura', result.temperatura);
//                 });
//             });

//             client.on('cambiar_estado_persiana', function() {
//                 if(estadoPersiana == 'abierta')
//                     estadoPersiana = 'cerrada';
//                 else
//                     estadoPersiana = 'abierta';
                
//                 io.sockets.emit('actualizar_estado_persiana', estadoPersiana);
//                 console.log("Servidor: el estado de la persiana es " + estadoPersiana);
//             });

//             client.on('cambiar_estado_a/c', function() {
//                 if(estadoAC == 'apagado')
//                     estadoAC = 'encendido';
//                 else
//                     estadoAC = 'apagado';
                
//                 io.sockets.emit('actualizar_estado_a/c', estadoAC);
//                 console.log("Servidor: el estado del A/C es " + estadoAC);
//             });

//             client.on('obtener_estado_persiana', function() {
//                 client.emit('obt_estado_persiana', estadoPersiana);
//             });

//             client.on('obtener_estado_a/c', function() {
//                 client.emit('obt_estado_a/c', estadoAC);
//             });

//             client.on('cerrar_persiana', function() {
//                 estadoPersiana = 'cerrada';
//                 io.sockets.emit('actualizar_estado_persiana', estadoPersiana);
//             });

//             client.on('alerta_luminosidad', function() {
//                 io.sockets.emit('actualizar_advertencia_luminosidad', estadoPersiana);
//             });

//             client.on('alerta_temperatura', function() {
//                 io.sockets.emit('actualizar_advertencia_temperatura', estadoPersiana);
//             });
// 		})
//     });
// });