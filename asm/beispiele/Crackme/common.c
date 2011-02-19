/*
 *  common.c
 *  Crackme
 *
 *  Created by sebØrz on 2/19/11.
 *  Copyright 2011 __MyCompanyName__. All rights reserved.
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <strings.h>
#include "common.h"


void printHex (const char *str, uint8_t *p, size_t n) {
    int i;
    printf("%s\n\t", str);
    if (0 < n && n < 256)
        for (i = 0; i < n; i++)
            printf("%02x", p[i]);
    printf("\n");
}

void hex2data (const char *hex, uint8_t *data) {
    int i, pos = 0;
    for (i = 0; i < strlen(hex); i++) {
        char d = hex[i] - '0';
        if (!(0 <= d && d < 10))
            d = toupper(hex[i]) - 'A' + 10;

        if (0 <= d && d < 16) {
            if ((pos+1) % 2)    // Higher digit
                data[pos/2] = 16*d;
            else
                data[pos/2] += d;
            pos++;
        }
    }
}

void parseNameFromArgs(char **nameref, int argc, const char * argv[]) {
    char *name = *nameref;
    size_t i, j, sz = 1, n = 0;
    for (i = 1; i < argc; i++) {    // Determine size.
        sz += strlen(argv[i]) + 1;      // Account for each space
    }
    name = malloc(sz);
    for (i = 1; i < argc; i++) {
        // Concatenate strings char-by-char, convert to lowercase
        for (j = 0; argv[i][j]; j++)
            name[n++] = tolower(argv[i][j]);
        name[n++] = ' ';
    }

    name[n-1] = '\0';
    *nameref=name;
}
