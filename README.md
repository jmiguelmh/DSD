# Desarrollo de Sistemas Distribuidos
Este repositorio contiene la prácticas de la asignatura Desarrollo de Sistemas Distribuidos que pertenece a la rama de Ingeniería del Software de la Universidad de Granada.

## Práctica 1 - SSH
Esta práctica consiste en una introducción a SSH. Los principales objetivos de la práctica son:
- Entender el funcionamiento de SSH.
- Realizar una conexión SSH entre dos ordenadores del aula.
- Realizar una conexión SSH sin contraseña (ssh-keygen).

## Práctica 2 - Sun RPC y Apache Thrift
### Introdución
El desarrollo de la práctica consiste en la creación de una calculadora distribuida, en la que existe un cliente y un servidor. El cliente solicita al usuario, de forma interactiva mediante un sistema de menús, que operaciones desea realizar (aritméticas, trigonométricas o trigonométricas inversas). A continuación, el programa cliente envía los datos y la operación al servidor que se encargará de realizar los calculos necesarios y devolver el resultado de la operación al cliente. Finalmente el cliente recibe el resultado y lo muestra por pantalla al usuario.

### Sun RPC
#### Compilación
Primero es necesario comprobar si el servicio rpcbind está activo:
*systemctl status rpcbind*
En el caso de que no lo esté se puede lanzar con:\n
*systemctl start rpcbind*

A continuación, se generan todos los códigos fuente de C necesarios a partir del fichero calculadora.x (fichero donde se han definido todas la funciones que admite la calculadora):<br />
*rpcgen -NCa calculadora.x*

Se deben sustituir los archivos calculadora_client.c y calculadora_server.c ya que los que genera por defecto rpcgen son plantilla vacías.
También es necesario modificar el archivo Makefile.calculadora, en concreto hay que añadirle el argumento -lm en LDLIBS ya que se utilizan funciones de la librería math.h.

Finalmente compilamos los archivos mediante el Makefile:<br />
*make -f Makefile.calculadora*

#### Ejecución
Para ejecutar la calculadora primero es necesario lanzar el servidor (recomiendo que sea en segundo plano, sino en otra terminal):<br />
*./calculadora_server &*

Después ejecutamos el cliente que require como parámetros la dirección IP de donde se encuentra el servidor, si éste se encuentra en la misma máquina se puede utilizar localhost:<br />
*./calculadora_client localhost*

La calculadora está implementa con un sistema de menús que permite al usuario navegar a través de ellos y seleccionar la operación que desea ejecutar

### Apache Thrift
#### Compilación
Primero es necesario tener instalado el compilador, ya dependiento de que distribución se utilice se utilizará un gestor de paquetes u otro. En mi caso al ser Ubuntu sería:<br />
*sudo apt install thrift-compiler*

Es necesario instalar Python y thrift en los paquetes de python:<br />
*sudo apt install python*<br />
*pip install thrift*

Para generar los archivos Python necesarios a partir del archivo calculadora.thrift (fichero en el que se han definido las funciones de la calculadora) se una el comando:<br />
*thrift -gen py calculadora.thrift*

El último paso es mover los archivos cliente.py y servidor.py dentro del directorio gen-py.

#### Ejecución
Para ejecutar la calculadora primero es necesario lanzar el servidor (recomiendo que sea en segundo plano, sino en otra terminal):<br />
*python3 servidor.py &*

Después ejecutamos el cliente:<br />
*python3 cliente.py localhost*

La calculadora está implementa con un sistema de menús que permite al usuario navegar a través de ellos y seleccionar la operación que desea ejecutar

## Práctica 3 - RMI
### Introducción
El objetivo de esta práctica es apreder a utilizar las invocaciones a procedimiento remoto que implementa Java RMI. La práctica consiste en dos partes.

### Parte 1
Esta parte consiste en ejecutar varios ejemplos aportados por el profesor para lograr un mayor entendimiento de Java RMI.
El primer ejemplo es un programa en el que el cliente envía un identificador y el servidor lo devuelve. Además si este identificador el servidor se espera 5 segundos antes de reenviarlo
El segundo ejemplo es parecido al primero, pero en este se lanzan varias hebras que mandan cada una un identificador. Este ejemplo trata de mostrar el entrelazamiento de las ejecuciones de cada hebra debido a la concurrencia que se produce entre estas.
El tercer ejemplo es un programa en el que hay dos servidores, uno réplica de otro. También se muestra como registrar un servicio en rmiregistry mediante código de Java.

#### Compilación y ejecución de los ejemplos
El primer paso es compilar todos archivos Java:\n
*javac \*.java*

### Parte 2
