#include <cstdio>

/*
 * fib(0)   = fib(1) = 1
 * fib(i+2) = fib(i+1) + fib(i), i = {0, 1, 2}
 */

int fib(int i){
    if (i == 0 || i == 1)
        return 1;
}

int main()
{
    int x;

    printf("Integer X: ");
    scanf("%i", &x);

    printf("fib(%d) = %d\n", x, fib(x));

    return 0;
}
