; Hello World using nasm on OS X
;   nasm -f macho hello.asm && ld hello.o -o hello && ./hello
; This should also work on Linux:
;   nasm -f elf hello.asm && ld -melf_i386 -o hello -e start hello.o && ./hello

; Preprocessor macros
%define stdin   0           ; Standard file descriptors
%define stdout  1
%define stderr  2

%define SYS_exit    1       ; System calls
%define SYS_read    3
%define SYS_write   4

; segment for static data
section .data

message   db 'Hello, World!', 0Ah     ; Terminate with newline
length    equ $ - message             ; Length = Current Address - Previous Address

; code segment
section .text
  global start              ; Make the start function externally visible for the linker

kernel:                     ; Expects syscall number in eax register
    int     80h             ; Call kernel
    ret

start:                      ; entry symbol on OS X

    ; 1. Write "Hello, World!"

    ; Prepare the arguments for the system call to write
    ; BSD: C calling conventions
    push    length          ; size_t nbyte
    push    message         ; const void *buf
    push    stdout          ; int filedescriptor
 
    ; Linux: fastcall convention
    mov     edx, length
    mov     ecx, message
    mov     ebx, stdout

    mov     eax, SYS_write  ; Make the system call to write
    call    kernel
   
    ; Clean up the stack
    add     esp, 12         ; 3 args * 4 bytes each = 12 bytes

    ; 2. Exit the program

    ; Prepare the argument for the sys call to exit
    push    0               ; exit status returned to the operating system

    mov     eax, SYS_exit   ; Make the syscall to exit
    call    kernel
    ; No need to clean up the stack because already exited
