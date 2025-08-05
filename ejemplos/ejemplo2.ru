#  para detectar un numero primo
num = 11;
esPrimo = true;
i = 2;

while (i < num) {
    if (num % i == 0) {
        esPrimo = false;
    }
    i = i + 1;
}

if (esPrimo) {
    imprime num;
    imprime "es primo";
} else {
    imprime num;
    imprime "no es primo";
}
