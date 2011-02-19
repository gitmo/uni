/*
 *  genrsa.h
 *  Crackme
 *
 */

#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <ctype.h>
#include <openssl/pem.h>
#include <openssl/rsa.h>
#include <openssl/bn.h>

// PEM pubkey file
char *fname   = "my_private.pem";

int initRSAKey(RSA *rsa, const char *modulus, const char *exponent, const char *p, const char *q) {

    if(modulus[1] == 'x')
        modulus += 2;

    // Initialize with known key components
    if (!(BN_hex2bn(&rsa->n, modulus)   // public modulus
          && BN_hex2bn(&rsa->e, exponent)  // public exponent
          && BN_dec2bn(&rsa->p, p)         // secret prime factor
          && BN_dec2bn(&rsa->q, q)         // secret prime factor
          )) {
        printf("Error creating RSA key: bignum conversion failed!\n");
    }

	int ok = -1;

    // BIGNUM context for bignum operations
    BN_CTX *ctx = BN_CTX_new();
	if (ctx == NULL) goto err;

	BN_CTX_start(ctx);
	BIGNUM *r0 = BN_CTX_get(ctx);
	BIGNUM *r1 = BN_CTX_get(ctx);
	BIGNUM *r2 = BN_CTX_get(ctx);
	if (r2 == NULL) goto err;

    /* We need the (other) RSA components non-NULL */
	if(!rsa->d && ((rsa->d=BN_new()) == NULL)) goto err;
	if(!rsa->dmp1 && ((rsa->dmp1=BN_new()) == NULL)) goto err;
	if(!rsa->dmq1 && ((rsa->dmq1=BN_new()) == NULL)) goto err;
	if(!rsa->iqmp && ((rsa->iqmp=BN_new()) == NULL)) goto err;

	/* calculate d */
	if (!BN_sub(r1,rsa->p,BN_value_one())) goto err;	/* p-1 */
	if (!BN_sub(r2,rsa->q,BN_value_one())) goto err;	/* q-1 */
	if (!BN_mul(r0,r1,r2,ctx)) goto err;	/* (p-1)(q-1) */
	if (!BN_mod_inverse(rsa->d,rsa->e,r0,ctx)) goto err;	/* d */

    /* calculate d mod (p-1) */
	if (!BN_mod(rsa->dmp1,rsa->d,r1,ctx)) goto err;

	/* calculate d mod (q-1) */
	if (!BN_mod(rsa->dmq1,rsa->d,r2,ctx)) goto err;

	/* calculate inverse of q mod p */
	if (!BN_mod_inverse(rsa->iqmp,rsa->q,rsa->p,ctx)) goto err;

	ok = 1;
err:
	if (ok == -1)
    {
        printf("Error creating RSA key: bignum operation failed!\n");
    }
	if (ctx != NULL)
    {
		BN_CTX_end(ctx);
		BN_CTX_free(ctx);
    }

	return ok;
}

void writeRSAtoPEM (RSA *rsa, const char *fname) {
    FILE *fp;

    // Open file for writing
    if (!(fp = fopen(fname, "w"))) {
        perror(fname);
        exit(1);
    }

    // Write RSA key in PEM format
    if (!PEM_write_RSAPrivateKey(fp, rsa, NULL, NULL, 0, 0, NULL)) {
        printf("Error writing PEM file!\n");
    }

    fclose(fp);
}

int main (int argc, const char * argv[]) {

    // Take the cmdline arguments as name if any
    if (argc < 5) {
        printf("Usage: %s modulus exponent prime1 prime2\n", argv[0]);
        exit(2);
    }

    // Create RSA key
    RSA *rsa = RSA_new();
    initRSAKey(rsa, argv[1], argv[2], argv[3], argv[4]);

    writeRSAtoPEM(rsa, fname);

    printf("\nEverything went better than expected.\nKey is in %s.\n\n", fname);

    return EXIT_SUCCESS;
}
