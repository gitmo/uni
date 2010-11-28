#!/bin/sh
# IMPORTANT: Send error messages to Stderr (&2). Alls messages to Stdout
# will be interpreted by the calling programm as filtered words.

# Change to the same directory this script is in
cd `dirname $0`

# Setup class path: assume we're in */{src,bin}/uebung2/aufgabe1
export CLASSPATH=$(pwd)/../../../bin

# This setup specific path is for running from home and is not needed
# on the uni servers with a shared home; but doesn't hurt either.
# FIX HERE, if "Class not found" errors are thrown.
export CLASSPATH=git/uni/alp5/bin:$CLASSPATH

package=uebung2.aufgabe1

# Check for arguments
if [ $# -lt  2 ]; then
  echo "Do not call this directly. This is meant to be run from Java." >&2
  exit 2
fi

set -x
ssh -t "$1" java -cp $CLASSPATH $package.Filter \"$2\"

