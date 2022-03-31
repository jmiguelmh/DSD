import glob
import sys
import math
import numpy as np

from calculadora import Calculadora

from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol
from thrift.server import TServer

import logging

logging.basicConfig(level=logging.DEBUG)


class CalculadoraHandler:
    def __init__(self):
        self.log = {}

    def sumar(self, a, b):
        return a + b

    def restar(self, a, b):
        return a - b
    
    def multiplicar(self, a, b):
        return a * b

    def dividir(self, a, b):
        return a / b
    
    def seno(self, a):
        return math.sin(a)
    
    def coseno(self, a):
        return math.cos(a)
    
    def tangente(self, a):
        return math.tan(a)
    
    def arcoseno(self, a):
        return math.asin(a)
    
    def arcocoseno(self, a):
        return math.acos(a)
    
    def arcotangente(self, a):
        return math.atan(a)

if __name__ == "__main__":
    handler = CalculadoraHandler()
    processor = Calculadora.Processor(handler)
    transport = TSocket.TServerSocket(host="127.0.0.1", port=9090)
    tfactory = TTransport.TBufferedTransportFactory()
    pfactory = TBinaryProtocol.TBinaryProtocolFactory()

    server = TServer.TSimpleServer(processor, transport, tfactory, pfactory)

    print("iniciando servidor...")
    server.serve()
    print("fin")
