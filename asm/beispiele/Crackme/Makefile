CC = gcc
CFLAGS = -m32
LFLAGS = -lcrypto
OBJS = common.o sign.o verify.o

all: crackme sign

crackme: ${OBJS}
	${CC} ${CFLAGS} common.c verify.c ${LFLAGS} -o crackme

sign: ${OBJS}
	${CC} ${CFLAGS} common.c sign.c ${LFLAGS} -o sign

gdb: crackme
	gdb -q crackme

clean:
	rm -f *.o sign crackme
