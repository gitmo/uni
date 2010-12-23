#!/bin/bash
#
# Demo of the RMI master/worker pattern:
# Mandelbrot fractal computation
#
# Usage:
#   $ ./run.sh 20 100 user@andorra user@nawab user@lounge

# Change to the same directory this script is in
cd `dirname $0`

# For VPN connections we need to set the local vpn endpoint
# so that the workers will find the master's objects
if netstat -rn | grep ppp0 >/dev/null; then
  if [[ -z $VPNPROP ]]; then
    # Try to autodetect local IP address
    IP=`netstat -rn | grep ppp0 | grep UH | tr -s ' ' | cut -f 2 -d ' '`
    # If valid address, set Java sytem property for RMI
    if [[ $IP =~ ^[0-9]+\.[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
      echo "DETECTED VPN: Setting 'java.rmi.server.hostname'"
      echo "to the local endpoint's address $IP"
      echo "================================================"
      VPNPROP=-Djava.rmi.server.hostname=$IP
    else
      echo "There seems to be a VPN running but I could not detect the"
      echo "IP address of its local endpoint."
      echo "Please set it manually first, e.g.:"
      echo "  $ export VPNPROP=-Djava.rmi.server.hostname=impXXXXXX.vpn.mi.fu-berlin.de"
      exit 1
    fi
  fi
fi

# Setup class path: assume we're in */{src,bin}/uebungX/aufgabeX
export CLASSPATH=$(pwd)/../../../bin

# This setup specific path is for running from home and is not needed
# on the uni servers with a shared home; but doesn't hurt either.
# FIX HERE, if "Class not found" errors are thrown.
export CLASSPATH=git/uni/alp5/bin:$CLASSPATH

# To build project:
ant -q -find build.xml
echo "================================================"

PACKAGE=uebung4.aufgabe1

MASTERARGS="$1 $2"; shift 2

for host in $*; do
  ssh -q -o "BatchMode yes" $host "java -cp $CLASSPATH $PACKAGE.WorkerImpl" &
  PIDS="$! $PIDS"
done

java $VPNPROP uebung4.aufgabe2.MandelMaster $MASTERARGS $* || \
  { if [ $? -gt 128 ]; then
      echo " Master: Killed. Cleaning hanging java processed"
      for host in $*; do
        ssh -f -o "BatchMode yes" $host 'killall -u $USER java' >/dev/null 2>&1
      done
    fi; }

# Wait for the workers ssh's to terminate.
while ps -p $PIDS >/dev/null 2>&1; do echo -n "wait for it... "; sleep 1; done; echo

