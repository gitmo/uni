CC = gcc
CFLAGS = -m32
LFLAGS = -lcrypto
OBJS = common.o sign.o verify.o genrsa.o

all: crackme sign genrsa

crackme: ${OBJS}
	${CC} ${CFLAGS} common.c verify.c ${LFLAGS} -o crackme

sign: ${OBJS}
	${CC} ${CFLAGS} common.c sign.c ${LFLAGS} -o sign

genrsa: ${OBJS}
	${CC} ${CFLAGS} genrsa.c ${LFLAGS} -o genrsa

gdb: crackme
	gdb -q crackme

clean:
	rm -f *.o sign crackme genrsa
