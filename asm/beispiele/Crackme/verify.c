/*
 *  verify.c
 *  Crackme
 *
 */

#include <stdio.h>
#include <stdint.h>
#include <ctype.h>
#include <string.h>
#include <openssl/rsa.h>
#include <openssl/bn.h>
#include "common.h"

//#define DEBUG

int verify_msg(const char *modulus, const char *exponent, const char *name, size_t n, uint8_t *from) {

    int ok = 0;

    puts("\n== Begin Verify ==");

    // Create RSA public key
    RSA *rsa = RSA_new();

    // Initialize with public key components
    if (! (BN_hex2bn(&rsa->n, modulus) && BN_hex2bn(&rsa->e, exponent)) ) {
        printf("Error creating RSA key: bignum conversion failed!\n");
        return EXIT_FAILURE;
    }

    // RSA modulus size in bytes. It can be used to determine how much memory
    // must be allocated for an RSA encrypted value.
    uint8_t flen = RSA_size(rsa);

    // Buffer for decrypted signature
    uint8_t to[flen];

    // Decrypt, returns the size of the recovered message digest.
    int size = RSA_public_decrypt(flen, from, to, rsa, RSA_PKCS1_PADDING);

    #ifdef DEBUG
    // Error
    if (size == -1) {
        printf("Wrong signature!\n");
    } else {
        printf("RSA_public_decrypt returned:\n\t%d\n", size);
        printHex("Decrypted data:", to, size);
        to[size] = '\0';
        printf("Signature decrypts to:\n\t%s\n", to);
    }
    #endif

    // Compare to decrypted signature to name
    ok = (size == n) && (memcmp(name, to, n) == 0);

    puts("== End Verify ==\n");

    return ok;
}


int main (int argc, const char * argv[]) {

    // Default name, overwritten by args
    char *name = "Dave";

    // RSA key
    // Take the cmdline arguments as name if any
    if (argc > 1)
        parseNameFromArgs(&name, argc, argv);

    // RSA public key components
    char *modulus   = "eb9bf0b0f2565e3572e66b209bf6d643"; // Public modulus
    char *exponent  = "3";                                // Public exponent
    size_t size     = strlen(modulus)/2;                  // Number of bytes of modulus

    // Byte array for signature
    uint8_t signature[ size ];  // Signature length depends on key, eg. 16 Bytes
    bzero(signature, size);     // Zero all bytes

    // Get signature as hex string from stdin
    char input[256];

    printf("\nHello %s! Please enter your hex signature:\n> ", name);
    if (! scanf("%127[^\n]", input) )
        return EXIT_FAILURE;

    // Convert hex input string to binary data
    hex2data(input, signature);

    #ifdef DEBUG
        printHex("Input to data: ", signature, sizeof signature);
    #endif

    // Verify signature for name
    if (verify_msg(modulus, exponent, name, strlen(name), signature)) {
        puts("You're cool!");
    } else {
        puts("Get lost!");
    }

    return EXIT_SUCCESS;
}
