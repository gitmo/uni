#!/bin/sh
# Important: Send error messages to Stderr (&2). Alls messages to Stdout
#   will be interpreted by the calling programm as filtered words.

# Change to the same directory this script is in
cd `dirname $0`

package=uebung2.aufgabe1

# Setup class path: assume we're in */{src,bin}/uebung2/aufgabe1
echo "IMPORTANT: If there are any "Class not found" problems fix CLASSPATH in remote.sh"! >&2
export CLASSPATH=$(pwd)/../../../bin

# This setup specific path is for running from home and is not needed
# on the uni servers with a shared home; but doesn't hurt either.
export CLASSPATH=git/uni/alp5/bin:$CLASSPATH


# Check for arguments
if [ $# -lt  2 ]; then
  echo "Do not call this directly. This is meant to be run from Java." >&2
  exit 2
fi

set -x
ssh -f "$1" java -cp $CLASSPATH $package.Filter \"$2\"

