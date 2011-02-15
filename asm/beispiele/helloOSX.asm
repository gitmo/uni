; Hello World using nasm on OS X
;
; nasm -f macho helloOSX.asm && ld helloOSX.o -o hello && ./hello

%define stdin   0
%define stdout  1
%define stderr  2

%define SYS_nosys   0
%define SYS_exit    1
%define SYS_fork    2
%define SYS_read    3
%define SYS_write   4

BITS 32

; static data
section .data

hello   db 'Hello, World!', 0x0a
hlen    equ $-hello

; code
section .text

global  start                   ; make the main function externally visible

start:
; 1 print "hello, world"

    ; 1a prepare the arguments for the system call to write
    push    dword hlen          ; length
    push    dword hello         ; message
    push    dword stdout        ; file descriptor value

    ; 1b make the system call to write
    mov     eax, SYS_write
    sub     esp, 4              ; OS X (and BSD) system calls needs "extra space" on stack
    int     80h

    ; 1c clean up the stack
   ;add esp, 16                 ; 3 args * 4 bytes/arg + 4 bytes extra space = 16 bytes

; 2 exit the program

    ; 2a prepare the argument for the sys call to exit
    push    dword 0             ; exit status returned to the operating system

    ; 2b make the call to sys call to exit
    mov     eax, SYS_exit
    sub     esp, 4              ; OS X (and BSD) system calls needs "extra space" on stack
    int     80h

    ; 2c no need to clean up the stack because no code here would executed: already exited
