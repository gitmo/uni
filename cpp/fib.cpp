#include <cstdio>

/*
 * fib(0)   = fib(1) = 1
 * fib(i+2) = fib(i+1) + fib(i), i = {0, 1, 2}
 */

int fib(int n)
{
    int n_0 = 1, n_1 = 1;
    for (int i = 3; i <= n; i++) {
        int n_2 = n_0 + n_1;
        n_0 = n_1;
        n_1 = n_2;
    }
    return n_1;
}
int main()
{
    int x;

    do {
      printf("Integer X (>=0): ");
      scanf("%i", &x);
    } while (x < 0);

    printf("fib(%d) = %d\n", x, fib(x));

    return 0;
}
