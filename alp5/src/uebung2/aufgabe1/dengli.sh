#!/bin/sh
# Asuming we're in src/uebung2/aufgabe1
export CLASSPATH=../../../bin
package=uebung2.aufgabe1

# Change to the same directory this script is in
cd `dirname $0`

# Check for arguments
if [ $# -ne 2 ]; then
  echo "Usage: `basename $0` file1 file2"
  exit 2
fi

#set -x         # show trace of every line
set -o errexit  # exit immediately on non-zero exit status

# During the 'ant init' task (which build depends on) the resource
# files (english.txt, etc.) are copied to bin/. The build-task then 
# compiles all sources to bin/ as well.
ant build

echo
echo "Exercise 1a)"
echo "========+==="
mkfifo one two    
java $package.Filter "$1" one &
java $package.Filter "$2" two &
java $package.Merge one two
rm one two

echo
echo "Exercise 1b)"
echo "========+==="
java $package.Denglish localhost localhost "$1" "$2"

# There's also an ant target instead: 
#ant Denglish -Dargs="localhost localhost \"$1\" \"$2\""
