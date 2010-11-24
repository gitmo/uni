        .data                        # Data section
        
msg:    .ascii "Hello, world.\n"     # The string to print.
        len = . - msg                # The length of the string.

		.text
		.globl	start
start:                               # Entry point.
         pushl   $len                # Arg 3 to write: length of string.
         pushl   $msg                # Arg 2: pointer to string.
         pushl   $1                  # Arg 1: file descriptor.
         movl    $4, %eax            # Write.
         call    do_syscall
         addl    $12, %esp           # Clean stack.

         pushl   $0                  # Exit status.
         movl    $1, %eax            # Exit.
         call    do_syscall

do_syscall:
         int     $0x80               # Call kernel.
         ret

