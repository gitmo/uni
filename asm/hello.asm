;
;
;

%include	'system.inc'

; static data
section	.data

hello	db  'Hello, World!', 0Ah
hbytes	equ	$ - hello

; code
section	.text

global	start

start:
        push	dword hbytes
        push	dword hello
        push	dword stdout
        sys.write

        push	dword 0
        sys.exit

