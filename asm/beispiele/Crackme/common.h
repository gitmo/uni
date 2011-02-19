/*
 *  common.h
 *  Crackme
 *
 *  Created by sebØrz on 2/19/11.
 *  Copyright 2011 __MyCompanyName__. All rights reserved.
 *
 */
#include <stdint.h>
#include <ctype.h>

void printHex (const char *str, uint8_t *p, size_t n);
void hex2data (const char *hex, uint8_t *data);
void parseNameFromArgs(char **nameref, int argc, const char * argv[]);
