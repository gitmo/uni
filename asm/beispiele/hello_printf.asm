; Hello World using nasm on OS X
;   nasm -f macho hello_printf.asm && gcc hello_printf.o -o hello -m32 && ./hello

; code segment
section .text

extern _printf                  ; OS X uses _ for symbol prefix
extern _exit
global _main

_main:
    push    ebp
    mov     ebp, esp
    sub     esp, 8               ; OS X: reallign stack to 16 bytes

    lea     eax, [rel message]
    mov     [esp], eax
  	call    _printf
 
    mov     [esp], dword 0       ; exit status returned to the operating system
    call    _exit
    leave
    ret

; segment for static data
section .data

message   db 'Hello, World from printf!', 0Ah, 0  ; Terminate with newline
length    equ $ - message             ; Length = Current Address - Previous Address
