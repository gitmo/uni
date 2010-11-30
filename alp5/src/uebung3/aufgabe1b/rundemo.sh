#!/bin/sh
#
# Demo of the RMI system
#
# Invocation:
#
#   $ ant clean build   # on remote file system
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

echo
echo "Setting up the remote filter"
echo "============================"
for host in $*; do
  ssh -qf "$USER@$host$SUFFIX" "killall -u $USER java 2>/dev/null; \
    java -cp "$CLASSPATH" $package.FilterRemote $PORT"
done
sleep 5

echo
echo "Starting ForeignRMI (Ctrl-C to quit!)"
echo "====================================="
ssh -qt "$USER@$alpha$SUFFIX" java -cp "$CLASSPATH" $package.ForeignRMI $FILE $PORT $*

echo
echo "==================================="
echo "Cleaning java processes on hosts..."
for host in $*; do
  ssh -qf "$USER@$host$SUFFIX" 'killall -u $USER java'
done
echo done

