#!/bin/bash -x
msieve -q 0xeb9bf0b0f2565e3572e66b209bf6d643
read
./genrsa
read
./genrsa eb9bf0b0f2565e3572e66b209bf6d643 3 17565485750407864799 17829183392364625949
read
openssl rsa -in my_private.pem -text
read
echo -n 'Dave' \
    | openssl rsautl -inkey my_private.pem -sign -hexdump
rm my_private.pem
