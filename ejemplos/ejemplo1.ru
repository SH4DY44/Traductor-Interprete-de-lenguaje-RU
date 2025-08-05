# EJEMPLO 1: Calculadora Básica
# Demuestra: variables, aritmética, operadores, log e imprime

# Variables básicas
a = 10;
b = 5;
log a;
log b;

# Operaciones aritméticas básicas
suma = a + b;
resta = a - b;
multiplicacion = a * b;
division = a / b;
modulo = a % b;

imprime "=== CALCULADORA BÁSICA ===";
imprime "a = 10, b = 5";
imprime "";

imprime "Suma: ";
imprime suma;

imprime "Resta: ";
imprime resta;

imprime "Multiplicación: ";
imprime multiplicacion;

imprime "División: ";
imprime division;

imprime "Módulo: ";
imprime modulo;

# Potencias
potencia = a ^ 2;
imprime "10 al cuadrado: ";
imprime potencia;

# Operaciones combinadas
compleja = (a + b) * 2 - a / b;
imprime "Operación compleja (a+b)*2 - a/b: ";
imprime compleja;

# Números decimales
pi = 3.14159;
radio = 5.0;
area = pi * radio ^ 2;
imprime "Área del círculo (radio=5): ";
imprime area;