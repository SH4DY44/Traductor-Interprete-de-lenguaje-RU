# EJEMPLO 3: Bucles y Contadores
# Demuestra: while, contadores, acumuladores, bucles anidados

imprime "=== CONTADOR SIMPLE ===";

# Contador básico del 1 al 5
contador = 1;
while (contador <= 5) {
    imprime "Conteo: ";
    imprime contador;
    contador = contador + 1;
}

imprime "";
imprime "=== TABLA DE MULTIPLICAR ===";

# Tabla de multiplicar del 3
numero = 3;
i = 1;
while (i <= 10) {
    resultado = numero * i;
    imprime numero;
    imprime " x ";
    imprime i;
    imprime " = ";
    imprime resultado;
    i = i + 1;
}

imprime "";
imprime "=== SUMA ACUMULATIVA ===";

# Suma de números del 1 al 10
suma_total = 0;
num = 1;
while (num <= 10) {
    suma_total = suma_total + num;
    log suma_total;
    num = num + 1;
}
imprime "Suma del 1 al 10: ";
imprime suma_total;

imprime "";
imprime "=== NÚMEROS PARES ===";

# Imprimir solo números pares del 2 al 20
actual = 2;
while (actual <= 20) {
    if (actual % 2 == 0) {
        imprime "Par: ";
        imprime actual;
    }
    actual = actual + 1;
}

imprime "";
imprime "=== FACTORIAL ===";

# Calcular factorial de 5
factorial_num = 5;
factorial = 1;
temp = factorial_num;

while (temp > 0) {
    factorial = factorial * temp;
    log factorial;
    temp = temp - 1;
}

imprime "Factorial de ";
imprime factorial_num;
imprime " es: ";
imprime factorial;

imprime "";
imprime "=== CUENTA REGRESIVA ===";

# Cuenta regresiva desde 10
regresiva = 10;
while (regresiva >= 0) {
    if (regresiva == 0) {
        imprime "¡DESPEGUE!";
    } else {
        imprime regresiva;
    }
    regresiva = regresiva - 1;
}