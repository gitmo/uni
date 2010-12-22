#!/bin/sh

exercise1a() {
  mkfifo one two    
  java $package.Filter "$1" one &
  java $package.Filter "$2" two &
  java $package.Merge one two
  rm one two
}

exercise1b() {
  java $package.Denglish "$@"
}

demo(){
  echo
  echo "Exercise 1a)"
  echo "============"
  exercise1a test1.txt test2.txt
 
  echo
  echo "Exercise 1b)"
  echo "============"
  echo 'IMPORTANT: If there are any "Class not found"'
  echo '           problems adapt0 CLASSPATH in remote.sh!'
  echo
  exercise1b $1@andorra.imp.fu-berlin.de $1@nawab.imp.fu-berlin.de \
             test1.txt test2.txt
}


# Change to the same directory this script is in
cd `dirname $0`

# Asuming we're in src/uebung2/aufgabe1
export CLASSPATH=./../../../bin
package=uebung2.aufgabe1

# During the 'ant init' task (which build depends on) the resource
# files (english.txt, etc.) are copied to bin/. The build-task then 
# compiles all sources to bin/ as well.
ant -find build.xml clean build

demo $USER

