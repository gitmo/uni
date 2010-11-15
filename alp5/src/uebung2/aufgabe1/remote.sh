#!/bin/sh
# Important: Send error messages to Stderr (&2). Alls messages to Stdout
#   will be interpreted by the calling programm as filtered words.

# Change to the same directory this script is in
cd `dirname $0`

# Classpath: if there are any "Class not found" problems fix here!
export CLASSPATH=$(pwd)/../../../bin:git/uni/alp5/bin
package=uebung2.aufgabe1

# Check for arguments
if [ $# -lt  2 ]; then
  echo "Do not call this directly. This is meant to be run from Java." >&2
  exit 2
fi

set -x
ssh -f "$1" java -cp $CLASSPATH $package.Filter \"$2\"

