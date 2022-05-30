# Desarrollo de Sistemas Distribuidos
Este repositorio contiene la prácticas de la asignatura Desarrollo de Sistemas Distribuidos que pertenece a la rama de Ingeniería del Software de la Universidad de Granada.

## Práctica 1 - SSH
Esta práctica consiste en una introducción a SSH. Los principales objetivos de la práctica son:
- Entender el funcionamiento de SSH.
- Realizar una conexión SSH entre dos ordenadores del aula.
- Realizar una conexión SSH sin contraseña (ssh-keygen).

## Práctica 2 - Sun RPC y Apache Thrift
### Sun RPC
#### Introdución
El desarrollo de la práctica consiste en la creación de una calculadora distribuida, en la que existe un cliente y un servidor. El cliente solicita al usuario, de forma interactiva mediante un sistema de menús, que operaciones desea realizar (aritméticas, trigonométricas o trigonométricas inversas). A continuación, el programa cliente envía los datos y la operación al servidor que se encargará de realizar los calculos necesarios y devolver el resultado de la operación al cliente. Finalmente el cliente recibe el resultado y lo muestra por pantalla al usuario.

#### Compilación
Primero es necesario comprobar si el servicio rpcbind está activo:
*systemctl status rpcbind*
En el caso de que no lo esté se puede lanzar con:
*systemctl start rpcbind*

A continuación, se generan todos los códigos fuente de C necesarios a partir del fichero calculadora.x (fichero donde se han definido todas la funciones que admite la calculadora):
*rpcgen -NCa calculadora.x*

Se deben sustituir los archivos calculadora_client.c y calculadora_server.c ya que los que genera por defecto rpcgen son plantilla vacías.
También es necesario modificar el archivo Makefile.calculadora, en concreto hay que añadirle el argumento -lm en LDLIBS ya que se utilizan funciones de la librería math.h.

Finalmente compilamos los archivos mediante el Makefile:
*make -f Makefile.calculadora*

#### Ejecución
Para ejecutar la calculadora primero es necesario lanzar el servidor (recomiendo que sea en segundo plano, sino en otra terminal):
*./calculadora_server &*

Después ejecutamos el cliente que require como parámetros la dirección IP de donde se encuentra el servidor, si éste se encuentra en la misma máquina se puede utilizar localhost:
*./calculadora_client localhost*

La calculadora está implementa con un sistema de menús que permite al usuario navegar a través de ellos y seleccionar la operación que desea ejecutar

## Práctica 3 - RMI
El objetivo de esta práctica es apreder a utilizar las invocaciones a procedimiento remoto que implementa Java RMI.
