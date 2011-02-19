#include <stdio.h>
#include <stdint.h>
#include <ctype.h>
#include <string.h>
#include <time.h>
#include <openssl/rsa.h>
#include <openssl/bn.h>
#include <openssl/err.h>
#include "common.h"

// #define DEBUG

// Default name, overwritten by args
char *name = "Dave";

// RSA key
char *modulus   = "eb9bf0b0f2565e3572e66b209bf6d643"; // Public modulus
char *exponent  = "3";                                // Public exponent

void initRSAPublicKey(RSA *rsa) {

    // Initialize with known key components
    if (!(BN_hex2bn(&rsa->n, modulus)   // public modulus
          && BN_hex2bn(&rsa->e, exponent)  // public exponent
          )) {
        printf("Error creating RSA key: bignum conversion failed!\n");
    }
}

int verify_msg(RSA *rsa, const char *d, size_t n, uint8_t *from) {

    // RSA modulus size in bytes. It can be used to determine how much memory
    // must be allocated for an RSA encrypted value.
    uint8_t flen = RSA_size(rsa);

    // Buffer for decrypted signature
    uint8_t to[flen];

    // Decrypt, returns the size of the recovered message digest.
    int size = RSA_public_decrypt(flen, from, to, rsa, RSA_PKCS1_PADDING);

    // Error
    if (size == -1) {
        printf("Wrong signature!\n");
        return 0;
    }

    // Compare to decrypted signature to name
    int result = (size == n) && (memcmp(name, to, n) == 0);

#ifdef DEBUG
    printf("RSA_public_decrypt returned:\n\t%d\n", size);
    printHex("Decrypted data:", to, size);
#endif

    to[size] = '\0';
    printf("Signature decrypts to:\n\t%s\n", to);

    return result;
}

int main (int argc, const char * argv[]) {

    // Take the cmdline arguments as name if any
    if (argc > 1)
        parseNameFromArgs(&name, argc, argv);

    // Create RSA key
    RSA *rsa = RSA_new();
    initRSAPublicKey(rsa);

    uint8_t signature[RSA_size(rsa)];       // Signature length depends on key, eg. 16 Bytes
    bzero(signature, sizeof signature);     // Zero all bytes

    char input[256];
    printf("\nHello %s!\nPlease enter your hex signature:\n>", name);
    if(!scanf("%127s", input))
        return EXIT_FAILURE;

    // Convert hex input string to binary data
    hex2data(input, signature);
    printHex("Input to data: ", signature, sizeof signature);

    puts("== Verify ==");

    // Verify signature for name
    if (verify_msg(rsa, name, strlen(name), signature)) {
        puts("You're cool!");
    } else {
        puts("Get lost!");
    }

    return EXIT_SUCCESS;
}
