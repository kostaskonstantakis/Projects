# -----------------------------------
# Program x86asm.s
# Compute and print the sum 0+...+N
# -----------------------------------

	.section .data	# 
N:	.long 1000	    # 
S:	.long 0		    # 
Msg:	.ascii "The sum from 0 to %d is %d\n\0"


	.section .text	# part of memory where all the instructions are
.globl main
main:	
	pushl %ebp	    #  Βάζει τον stack pointer της καλούμενης συνάρτησης στην σοίβα
	movl %esp, %ebp	# Βάζει τον τωρινό stack pointer για να καλέσει την συνάρτηση

 	# initialize
    movl N, %ebx	# %ebx=N (καταχωρητή γενικού σκοπού)
	

 	# compute sum
L1:
	addl %ebx, S	# s+=%ebx=s+N
	decl %ebx       # N--;
	cmpl $0, %ebx   # compares 0 to %ebx
	jng  L2	    	# goto l2
    movl $L1, %eax	# Bάλε την διεύθυνση της L1 συνάρτησης στον γενικό καταχωρητή %eax
    jmp *%eax   	# goto διεύθυνση που περιέχεται στον καταχωρητή %eax,S

L2:
	# print result
	pushl S	    	# βάλε το αποτέλεσμα στο S,την στοίβα
	pushl N	        # -''- N,πάλι στην στοίβα
	pushl $Msg  	# -''- στον καταχωρητή που έχει το Msg
	call printf 	# τύπωσε
	popl %eax   	# %eax="The sum from 0 to %d is %d\n\0" (αυτό είναι το περιεχόμενο του)
	popl %eax   	# %eax=S
	popl %eax   	# %eax=N

	# exit
	movl $0, %eax  	# %eax=0; 
    leave	    	#Κάνει τον stack pointer ίσο με την αρχική τιμή του
 	ret             #επέστρεψε
