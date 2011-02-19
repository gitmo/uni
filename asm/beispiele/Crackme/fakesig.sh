#!/bin/bash -x
openssl genrsa -out fakekey.pem 128
read

echo -n 'Dave' \
    | openssl rsautl -inkey fakekey.pem -sign \
    | openssl rsautl -inkey fakekey.pem -verify -raw -hexdump

rm fakekey.pem
