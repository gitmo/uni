#!/bin/sh
#
# Demo of the RMI system
#
# Invocation:
#
#   $ USER="xxxx" ./rundemo.sh Blatt3.txt andorra nawab peking shanghai
#

PORT=61337
SUFFIX=".imp.fu-berlin.de"

# Change to the same directory this script is in
cd `dirname $0`

# Setup class path: assume we're in */{src,bin}/uebung2/aufgabe1
export CLASSPATH=$(pwd)/../../../bin

# This setup specific path is for running from home and is not needed
# on the uni servers with a shared home; but doesn't hurt either.
# FIX HERE, if "Class not found" errors are thrown.
export CLASSPATH=git/uni/alp5/bin:$CLASSPATH

package=uebung3.aufgabe1b

# Check for arguments
if [ $# -lt 3 ]; then
  echo "Usage:  $(basename "$0") TxtFile ForeignHost FilterHost [FilterHost ...]" >&2
  exit 2
fi

FILE=$1
shift
alpha=$1
shift

set -x
for host in $*; do
  ssh -t "$USER@$host$SUFFIX" java -cp "$CLASSPATH" $package.FilterRemote $PORT &
  PIDS="$PIDS $!"
  sleep 1
done

ssh -t "$USER@$alpha$SUFFIX" java -cp "$CLASSPATH" $package.ForeignRMI $FILE $PORT $*
kill $PIDS
