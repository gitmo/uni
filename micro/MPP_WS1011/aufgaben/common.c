#include "common.h"

// Wartet c millisekunden
void wait_ms(unsigned int c) {
    for(; c > 0; c--)
        wait(100);    // Wartet 100 * 10 mikrosekunde
}
