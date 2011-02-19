	.cstring
LC0:
	.ascii "Hello World!\0"
	.text
.globl _main
_main:
	pushl	%ebp
	movl	%esp, %ebp
	pushl	%ebx
	subl	$20, %esp
	call	L3
"L00000000001$pb":
L3:
	popl	%ebx
	leal	LC0-"L00000000001$pb"(%ebx), %eax
	movl	%eax, (%esp)
	call	_printf
	addl	$20, %esp
	popl	%ebx
	leave
	ret
	.subsections_via_symbols
