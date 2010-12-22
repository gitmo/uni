#!/bin/sh

#set -x         # show trace of every line
set -o errexit  # exit immediately on non-zero exit status

# Change to the same directory this script is in
cd `dirname $0`

# Asuming we're in src/uebung2/aufgabe1
export CLASSPATH=./../../../bin
package=uebung2.aufgabe1

# Check for arguments
if [ $# -ne 2 ]; then
  echo "Usage: `basename $0` file1 file2"
  exit 2
fi

# During the 'ant init' task (which build depends on) the resource
# files (english.txt, etc.) are copied to bin/. The build-task then 
# compiles all sources to bin/ as well.
ant -find build.xml

echo
echo "Exercise 1a)"
echo "============"
mkfifo one two    
java $package.Filter "$1" one &
java $package.Filter "$2" two &
java $package.Merge one two
rm one two

