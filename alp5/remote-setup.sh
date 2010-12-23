#!/bin/bash

# Change to the same directory this script is in
cd `dirname $0`

FU=$USER@lounge
REMDIR=git/uni/alp5

rsync -av $(pwd)/src $FU:$REMDIR && \
  ssh -q -o "BatchMode yes" $FU "cd $REMDIR; ant"

for h in nawab lounge andorra; do
  ssh -f -o "BatchMode yes" $USER@$h 'killall -u $USER java' >/dev/null 2>&1 &
done

