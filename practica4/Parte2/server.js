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
		if (uri=="/") uri = "/client.html";
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

var estado_persiana = 'abierta';
var estado_ac = 'activo';
var estado_luz = 'encendida';

MongoClient.connect("mongodb://localhost:27017/datos_sensores", function(err, db) {

    var dbo = db.db("practica4");
	httpServer.listen(8080);
	var io = socketio(httpServer);
	
	dbo.createCollection("sensores", function(err, collection){
		io.sockets.on('connection',function(client) {
			
			// funcion que se ejecuta cuando se reciben datos nuevos de los sensores
	    	client.on('datos_actualizados', function(data) {
				io.sockets.emit('actualizar', 
					"temperatura: " + data.temperatura + 
					", luminosidad: " + data.luminosidad + 
					", humedad: " + data.humedad +
					", fecha: "+ data.time );

				io.sockets.emit('actualizar_temp', data.temperatura);
				io.sockets.emit('actualizar_lum', data.luminosidad);
				io.sockets.emit('actualizar_hum', data.humedad)
			});

	    	// funcion que se ejecuta cuando el cliente pide los ultimos datos disponibles
	    	client.on('obtener', function () {
				dbo.collection("sensores").find().sort({_id:-1}).limit(1).forEach(function(result){
					client.emit('actualizar', 
						"temperatura: " + result.temperatura + 
						", luminosidad: " + result.luminosidad + 
						", humedad: " + result.humedad +
						", fecha: "+ result.time );

				});
			});

	    	// funcion que se ejectua cuando el cliente solo pide la temperatura
			client.on('obtener_temp', function () {
				dbo.collection("sensores").find().sort({_id:-1}).limit(1).forEach(function(result){
					client.emit('actualizar_temp',result.temperatura);
				});
			});

			// funcion que se ejectua cuando el cliente solo pide la luminosidad
			client.on('obtener_lum', function () {
				dbo.collection("sensores").find().sort({_id:-1}).limit(1).forEach(function(result){
					client.emit('actualizar_lum',result.luminosidad);
				});
			});

			// funcion que se ejectua cuando el cliente solo pide la humedad
			client.on('obtener_hum', function () {
				dbo.collection("sensores").find().sort({_id:-1}).limit(1).forEach(function(result){
					client.emit('actualizar_hum',result.humedad);
				});
			});

			// funcion que abre/cierra la persiana cuando lo solicita el cliente
			client.on('cambiar_estado_persiana', function(){
				if (estado_persiana == 'abierta')
					estado_persiana = 'cerrada';
				else
					estado_persiana = 'abierta';

				// Se le indica al cliente que ha cambiado el estado de la persiana
				io.sockets.emit('actualizar_estado_persiana',estado_persiana);
				console.log("Servidor: la persiana está " + estado_persiana);
			});

			// funcion que enciende/apaga el aire acondicionado cuando lo solicita el cliente
			client.on('cambiar_estado_ac', function(){
				if (estado_ac == 'activo')
					estado_ac = 'apagado';
				else
					estado_ac = 'activo';

				// Se le indica al cliente que ha cambiado el estado del aire acondicionado
				io.sockets.emit('actualizar_estado_ac',estado_ac);
				console.log("Servidor: el aire acondicionado está " + estado_ac);
			});

			// funcion que se ejecuta cuando el cliente enciende/apaga la luz
			client.on('cambiar_estado_luz', function(){
				if (estado_luz == 'encendida')
					estado_luz = 'apagada';
				else
					estado_luz = 'encendida';

				// Se le indica al cliente que ha cambiado el estado de la luz
				io.sockets.emit('actualizar_estado_luz',estado_luz);
				console.log("Servidor: la luz está " + estado_luz);
			});

			client.on('obtener_estado_persiana', function(){
				client.emit('obt_estado_persiana', estado_persiana);
			});

			client.on('obtener_estado_luz', function(){
				client.emit('obt_estado_luz', estado_luz);
			});

			client.on('obtener_estado_ac', function(){
				client.emit('obt_estado_ac', estado_ac);
			});

			client.on('cerrar_persiana', function(){
				estado_persiana = 'cerrada';
				io.sockets.emit('actualizar_estado_persiana', estado_persiana);
			});

			// Los dos siguientes eventos se producen cuando se supera un umbral de luminosidad/temperatura
			client.on('alerta_lum',function(data){
				io.sockets.emit('actualizar_advertencia_lum',data);
                console.log("Advertencia: luminosidad");
			});
            
			client.on('alerta_temp',function(data){
                io.sockets.emit('actualizar_advertencia_temp',data);
                console.log("Advertencia: temperatura");
			});

			client.on('pedir_temperatura', function() {
				var weather = require('weather-js');
				weather.find({search: 'Granada, ES', degreeType: 'C'}, function(err, result) {
					if(err)
						console.log(err);

					var temperatura = result[0].current.temperature;
					// console.log(temperatura);
					io.sockets.emit('enviar_temperatura', temperatura);
				  });
			});
	    });
    });
});

console.log("Servicio MongoDB iniciado");