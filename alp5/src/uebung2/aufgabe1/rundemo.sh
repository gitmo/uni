#!/bin/sh

SSHUSER=$USER

ant clean build
sh exercise1a.sh test1.txt test2.txt
sh exercise1b.sh $SSHUSER@andorra.imp.fu-berlin.de $SSHUSER@shanghai.imp.fu-berlin.de test1.txt test2.txt
