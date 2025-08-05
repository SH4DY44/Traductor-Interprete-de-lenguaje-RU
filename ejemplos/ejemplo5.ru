# EJEMPLO COMPLETO: Estadísticas de una "lista" manual
# Demuestra: asignaciones, if/else, while, operaciones, lógica, log, imprime

# Inicialización de variables
suma = 0;
conteo = 0;
promedio = 0;
maximo = -9999;
minimo = 9999;
i = 0;

# Simulación de una lista con variables individuales
x1 = 23;
x2 = 15;
x3 = 9;
x4 = 42;
x5 = 31;

# Bucle que recorre los 5 elementos
while (i < 5) {
    actual = 0;

    if (i == 0) {
        actual = x1;
    } else if (i == 1) {
        actual = x2;
    } else if (i == 2) {
        actual = x3;
    } else if (i == 3) {
        actual = x4;
    } else {
        actual = x5;
    }

    log actual;

    suma = suma + actual;
    conteo = conteo + 1;

    if (actual > maximo) {
        maximo = actual;
    }

    if (actual < minimo) {
        minimo = actual;
    }

    i = i + 1;
}

# Calcular promedio si conteo > 0
if (conteo != 0) {
    promedio = suma / conteo;
} else {
    promedio = 0;
}

# Mostrar resultados
imprime "=== ESTADÍSTICAS DE VALORES ===";
imprime "Cantidad: ";
imprime conteo;

imprime "Suma total: ";
imprime suma;

imprime "Promedio: ";
imprime promedio;

imprime "Máximo: ";
imprime maximo;

imprime "Mínimo: ";
imprime minimo;

# Validación lógica final
esEstable = promedio >= 20 && promedio <= 35;
imprime "¿Promedio dentro del rango 20–35? ";
imprime esEstable;
