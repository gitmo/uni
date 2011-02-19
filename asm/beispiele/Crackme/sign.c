/*
 *  sign.c
 *  Crackme
 *
 *  Created by sebØrz on 2/19/11.
 *  Copyright 2011 __MyCompanyName__. All rights reserved.
 *
 */

#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <openssl/rsa.h>
#include <openssl/bn.h>
#include "common.h"
#include "sign.h"

// RSA key
char *modulus = "eb9bf0b0f2565e3572e66b209bf6d643"; // Public modulus
char *exponent  = "3";                              // Public exponent
char *d = "9d12a075f6e43ecd0522320c1f0522db";       // Private exponent

void initRSAPrivateKey(RSA *rsa) {

    // Initialize with known key components
    if (!(BN_hex2bn(&rsa->n, modulus)   // public modulus
          && BN_hex2bn(&rsa->e, exponent)  // public exponent
          && BN_hex2bn(&rsa->d, d)         // private exponent
          )) {
        printf("Error creating RSA key: bignum conversion failed!\n");
    }
}

int sign_msg(RSA *rsa, const char *d, size_t n, uint8_t *to) {

    int size = RSA_private_encrypt(n, (unsigned char *) d, to, rsa, RSA_PKCS1_PADDING);
    if (size != RSA_size(rsa)) {
        printf("\tSigning failed (signature size is %d)!\n", size);
    } else {
        printHex("RSA signature:", to, size);
    }

    return size;
}

int main (int argc, const char * argv[]) {

    char *name = "Dave";    // default name

    // Take the cmdline arguments as name if any
    if (argc > 1)
        parseNameFromArgs(&name, argc, argv);

    // Create RSA key
    RSA *rsa = RSA_new();
    initRSAPrivateKey(rsa);

    uint8_t signature[RSA_size(rsa)];   // Signature length, eg. 16 Bytes

    puts("== Signing ==");

    printf("Name:\n\t'%s'\n", name);
    sign_msg(rsa, name, strlen(name), signature);

    printf("Everything went better than expected.\n");
    return EXIT_SUCCESS;
}
