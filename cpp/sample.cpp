#include <cstdio>

int main()
{
    int dm;
    double euro;
    
    printf("Bitte ganze DM eingeben: ");
    scanf("%i", & dm);

    euro = dm / 1.95583;

    printf("%i DM sind genau %f Euro.\n", dm, euro);

    return 0;
}
