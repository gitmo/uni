#!/bin/sh
# usage: dengli.sh file1 file2    (needs execute permission!)

set -x

# Copies the resource file (english.txt, etc.) to bin/
ant clean init 
ant build

export CLASSPATH=../../../bin
package=uebung2.aufgabe1

echo "Aufgabe 1a)"
mkfifo one two    
java $package.Filter "$1" one &
java $package.Filter "$2" two &
java $package.Merge one two
rm one two

echo "Aufgabe 1b)"
java $package.Denglish localhost localhost test.txt test2.txt

# or: ant Denglish -Dargs='localhost localhost test.txt test2.txt'
