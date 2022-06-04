var http = require("http");
var url = require("url");

function calcular(operacion, val1, val2) {
	if (operacion=="sumar") return val1+val2;
	else if (operacion == "restar") return val1-val2;
	else if (operacion == "producto") return val1*val2;
	else if (operacion == "dividir") return val1/val2;
	else return "Error: Parametros no validos";
}

var httpServer = http.createServer(
	function(request, response) {
		var uri = url.parse(request.url).pathname;
		var output = "";
		while (uri.indexOf('/') == 0) uri = uri.slice(1);
		var params = uri.split("/");
		if (params.length >= 3) {
			var val1 = parseFloat(params[1]);
			var val2 = parseFloat(params[2]);
			var result = calcular(params[0], val1, val2);
			output = result.toString();
		}
		else output = "Error: El numero de parametros no es valido";
        response.writeHead(200, {"Content-Type": "text/plain", "access-control-allow-origin": "*"});
		response.write(output);
		response.end();
	}
);
httpServer.listen(8080);
console.log("Servicio HTTP iniciado");