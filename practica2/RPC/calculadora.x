/* Archivo calculadora.x */

program CALCULADORA {
    version CALCULADORA_ARITMETICA {
        float SUMAR (float a, float b) = 1;
        float RESTAR (float a,float b) = 2;
        float MULTIPLICAR (float a, float b) = 3;
        float DIVIDIR (float a, float b) = 4;
    } = 1;

    version CALCULADORA_TRIGONOMETRICA {
        float SENO (float a) = 5;
        float COSENO (float a) = 6;
        float TANGENTE (float a) = 7;
    } = 2;

    version CALCULADORA_TRIGONOMETRICA_INVERSA {
         float ARCOSENO (float a) = 8;
        float ARCOCOSENO (float a) = 9;
        float ARCOTANGENTE (float a) = 10; 
    } = 3;
} = 0x20000001;