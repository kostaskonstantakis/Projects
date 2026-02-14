all:
	bison -v --yacc --defines --output=parser.c parser.y  
	flex --outfile=al.c al.l
	gcc -g parser.c al.c st.c alpha.c quad.c p3stack.c mystack.c myfunctions.c -o parser 

clean:
	rm -rf al
	rm -rf al.c
	rm -rf parser
	rm -rf parser.c
	rm -rf parser.h

