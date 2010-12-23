#!/bin/bash
#
# Demo of the RMI master/worker pattern:
# Mandelbrot fractal computation
#
# Usage:
#   $ USER=joe ./run.sh 20 100 andorra nawab lounge

# For VPN connections set the local vpn endpoint
# so that the workers will find the master's objects
if netstat -rn | grep ppp0 >/dev/null; then
  IP=`netstat -rn | grep ppp0 | grep UH | tr -s ' ' | cut -f 2 -d ' '`  
  VPNPROP=-Djava.rmi.server.hostname=$IP
fi

# Change to the same directory this script is in
cd `dirname $0`

# Setup class path: assume we're in */{src,bin}/uebungX/aufgabeX
export CLASSPATH=$(pwd)/../../../bin

# This setup specific path is for running from home and is not needed
# on the uni servers with a shared home; but doesn't hurt either.
# FIX HERE, if "Class not found" errors are thrown.
export CLASSPATH=git/uni/alp5/bin:$CLASSPATH

# To build project:
ant -find build.xml

PACKAGE=uebung4.aufgabe1

N=$1; shift
for host in $*; do
  ssh -q -o "BatchMode yes" "$host" "java -cp $CLASSPATH $PACKAGE.WorkerImpl" &
  PIDS="$! $PIDS"
done

java $VPNPROP uebung4.aufgabe2.MandelMaster $N $* || \
  { if [ $? -gt 128 ]; then
      echo " Master: Killed. Cleaning hanging java processed"
      for host in $*; do
        ssh -f -o "BatchMode yes" $USER@$host 'killall -u $USER java' >/dev/null 2>&1
      done
    fi; }

# Wait for the workers ssh's to terminate.
while ps -p $PIDS >/dev/null 2>&1; do echo -n "wait for it... "; sleep 1; done; echo

