#include <cstdio>

unsigned int fib(int n);

/*
 * fib(0)   = fib(1) = 1
 * fib(i+2) = fib(i+1) + fib(i), i = {0, 1, 2}
 */
unsigned int fib(int n)
{
    unsigned int n_0 = 1, n_1 = 1, n_2;
    for (int i = 3; i <= n; i++) {
        n_2 = n_0 + n_1;
        n_0 = n_1;
        n_1 = n_2;
    }
    return n_1;
}

int main()
{
    int x, res;

    do {
      printf("Non-negative Integer X: ");
      res = scanf("%i", &x);
      // flush input stream
      fpurge(stdin);
    } while (!res || x < 0);

    // Overflow at x > 47 with 32 bit uint's!
    printf("fib(%u) = %u\n", x, fib(x));

    return 0;
}
