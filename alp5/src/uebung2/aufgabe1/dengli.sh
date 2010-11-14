#!/bin/sh
# usage: dengli.sh file1 file2    (needs execute permission!)

set -x

export CLASSPATH=../../../bin
package=uebung2.aufgabe1

mkfifo one two    
java $package.Filter "$1" one &
java $package.Filter "$2" two &
java $package.Merge one two
rm one two
