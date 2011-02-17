; Hello World using nasm on OS X
;
; nasm -f macho hello.asm && ld hello.o -o hello && ./hello

; preprocessor macros
%define stdin   0
%define stdout  1
%define stderr  2

%define SYS_nosys   0
%define SYS_exit    1
%define SYS_fork    2
%define SYS_read    3
%define SYS_write   4

; instruct nasm to assemble for 32-bit
BITS 32

; code segment
section .text

global  start                   ; make the main function externally visible

; By default, the FreeBSD kernel uses the C calling convention. Further, although
; the kernel is accessed using int 80h, it is assumed the program will call a
; function that issues int 80h, rather than issuing int 80h directly.
; -- http://www.int80h.org/bsdasm/
kernel:
    int     80h                 ; Call kernel
    ret

start:
    ; 1 print "hello, world"

    ; 1a prepare the arguments for the system call to write
    push    dword length        ; length
    push    dword message       ; string
    push    dword stdout        ; file descriptor value

    ; 1b make the system call to write
    mov     ax, SYS_write
    call    kernel
    
    ; 1c clean up the stack
    add     esp, 12             ; 3 args * 4 bytes/arg = 12 bytes

    ; 2 exit the program

    ; 2a prepare the argument for the sys call to exit
    push    dword 0             ; exit status returned to the operating system

    ; 2b make the call to sys call to exit
    mov     eax, SYS_exit
    call    kernel
    ; 2c no need to clean up the stack because no code here would executed: already exited


; static data segment
section .data

message   db 'Hello, World!', 0x0a
length  equ $-hello

