n = 5;
factorial = 1;

while (n > 1) {
    factorial = factorial * n;
    n = n - 1;
}

imprime "Resultado del factorial:";
imprime factorial;
