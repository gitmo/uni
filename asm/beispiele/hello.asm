; Hello World using nasm on OS X
;
; nasm -f macho hello.asm && ld hello.o -o hello && ./hello

; Preprocessor macros
%define stdin   0               ; Standard file descriptors
%define stdout  1
%define stderr  2

%define SYS_exit    1           ; System calls
%define SYS_read    3
%define SYS_write   4

BITS 32                         ; Instruct nasm to assemble for 32-bit

global start                    ; Make the start function externally visible for the linker

; code segment
section .text

kernel:                         ; Expects syscall number in eax register
    int     80h                 ; Call kernel
    ret

start:
    ; 1. print "hello, world"

    ; Prepare the arguments for the system call to write (C calling conventions)
    push    dword length        ; size_t nbyte
    push    dword message       ; const void *buf
    push    dword stdout        ; int filedescriptor

    mov     eax, SYS_write      ; Make the system call to write
    call    kernel
    
    ; Clean up the stack
    add     esp, 12             ; 3 args * 4 bytes each = 12 bytes

    ; 2. exit the program

    ; Prepare the argument for the sys call to exit
    push    dword 0             ; exit status returned to the operating system

    mov     eax, SYS_exit       ; Make the syscall to exit
    call    kernel
    ; No need to clean up the stack because already exited

; segment for static data
section .data

message   db 'Hello, World!', 0Ah   ; Terminate with newline
length    equ $ - message           ; Length = Current Address - Previous Address

