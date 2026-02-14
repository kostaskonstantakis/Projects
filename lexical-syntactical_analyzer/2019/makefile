all:
	
	bison --yacc --defines --output=parser.c parser.y 
	flex --outfile=al.c al.l
	gcc -o al al.c parser.c 
           

clean:
	rm -rf *.out
	rm -rf *~
	rm -rf *.c
	rm -rf *.output
	rm -rf *.o
