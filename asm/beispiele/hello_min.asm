; Preprocessor macros
%define stdout  1

%define SYS_exit    1       ; System calls
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

    push    length          ; size_t nbyte
    push    message         ; const void *buf
    push    stdout          ; int filedescriptor
    mov     eax, SYS_write  ; Make the system call to write
    call    kernel
   
    add     esp, 12         ; 3 args * 4 bytes each = 12 bytes

    push    0               ; exit status returned to the operating system
    mov     eax, SYS_exit   ; Make the syscall to exit
    call    kernel
