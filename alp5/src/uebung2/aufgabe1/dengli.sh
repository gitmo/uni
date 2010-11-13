# usage: dengli.sh $1 $2    (needs execute permission!)

package=uebung2.aufgabe1

mkfifo one two    
java $package.Filter "$1" one &
java $package.Filter "$2" two &
java $package.Merge one two
rm one two
