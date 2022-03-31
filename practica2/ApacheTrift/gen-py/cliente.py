from calculadora import Calculadora
from os import system

import math

from thrift import Thrift
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

transport = TSocket.TSocket("localhost", 9090)
transport = TTransport.TBufferedTransport(transport)
protocol = TBinaryProtocol.TBinaryProtocol(transport)

client = Calculadora.Client(protocol)

transport.open()

numOpciones = 3
operadores = ['+', '-', 'x', '/']
tiposGrados = ['r', 'g']

def isInt(num):
    try:
        int(num)
        return True
    except ValueError:
        return False

def isFloat(num):
    try:
        float(num)
        return True
    except ValueError:
        return False

while True:
    system("clear")
    print("Calculadora")
    print("Seleccione una operacion:")
    print("1) Aritmeticas simples")
    print("2) Trigonometricas")
    print("3) Trigonometricas inversas")
    opcion = input("Opcion: ")

    if isInt(opcion):
        opcion = int(opcion)
        if opcion > 0 and opcion <= numOpciones:
            break

if opcion == 1:
    print("Operaciones aritmeticas simples:")
    print("Los operadores validos son: + - x /")
    print("Escriba la expresion separada por espacios somo se indica: <numero1> <operador> <numero2>")

    while True:
        expresion = input("Expresion: ")
        expresion = expresion.split()
        
        if len(expresion) == 3 and expresion[1] in operadores and isFloat(expresion[0]) and isFloat(expresion[2]):
            break
        else:

            if len(expresion) != 3:
                print("Numero de parametros incorrecto <numero1> <operador> <numero2>")
            
            else:
                if not isFloat(expresion[0]) or not isFloat(expresion[2]):
                    print("Los numeros introducidos no se reconocen")

                if expresion[1] not in operadores:
                    print("Operador insertado no válido")
    
    numero1 = float(expresion[0])
    operador = expresion[1]
    numero2 = float(expresion[2])

    if operador == '+':
        print("Resultado: " + str(client.sumar(numero1, numero2)))
    elif operador == '-':
        print("Resultado: " + str(client.restar(numero1, numero2)))
    elif operador == 'x':
        print("Resultado: " + str(client.multiplicar(numero1, numero2)))
    elif operador == '/':
        if numero2 == 0:
            print("La division entre 0 no esta definida")
        else:
            print("Resultado: " + str(client.dividir(numero1, numero2)))

elif opcion == 2:
    print("Operaciones trigonometricas")
    print("Formato separado por espacios: <numero> <unidad>")
    print("Utilize como unidad \'r\' para radianes y \'g\' para grados")
    
    while True:
        angulo = input("Angulo: ")
        angulo = angulo.split()

        if len(angulo) == 2 and angulo[1] in tiposGrados and isFloat(angulo[0]):
            break
        else:
            if len(angulo) != 2:
                print("Numero de parametros incorrecto <angulo> <unidad>")
            else:
                if angulo[1] not in tiposGrados:
                    print("La unidad introducida no es valida")
                if not isFloat(angulo[0]):
                    print("No se ha reconocido ningun numero")
    
    print(angulo)
    numero = float(angulo[0])
    unidad = angulo[1]

    if unidad == 'g':
        numero = math.radians(numero)

    print("Seno: " + str(client.seno(numero)))
    print("Coseno: " + str(client.coseno(numero)))
    print("Tangente: " + str(client.tangente(numero)))

elif opcion == 3:
    print("Operaciones trigonometricas inversas:")

    while True:
        numero = input("Introduzca un numero: ")
        if isFloat(numero):
            break
        else:
            print("No se ha reconocidad ningun numero")
    
    numero = float(numero)

    if numero >= -1 and numero <= 1:
        arcoseno = client.arcoseno(numero)
        arcocoseno = client.arcocoseno(numero)
        print("Arcoseno: " + str(arcoseno) + " radianes o " + str(math.degrees(arcoseno)) + " grados")
        print("Arcocoseno: " + str(arcocoseno) + " radianes o " + str(math.degrees(arcocoseno)) + " grados")
    else:
        print("Arcoseno: indefinido")
        print("Arcocoseno: indefinido")

    arcotangente = client.arcotangente(numero)
    print("Arcotangente: " + str(arcotangente) + " radianes o " + str(math.degrees(arcotangente)) + " grados")

transport.close()
