/*Marios Konstantinos Konstantakis A.M:3219
HY255 3rd assignment*/
#include <stdio.h>
#include <stdlib.h>
#include "symtable.h"
#include "symtablehash.c"
#include "symtablelist.c"

void main()
{
	SymTable_T symbol_table=SymTable_new();
	printf("Inserting (1,1)=%d\n",SymTable_put(symbol_table,"1",1));
	printf("Inserting (1,2)=%d\n",SymTable_put(symbol_table,"1",2));
	printf("Inserting (123,3)=%d\n",SymTable_put(symbol_table,"123",3));
	printf("Inserting (2,4)=%d\n",SymTable_put(symbol_table,"2",4));
	printf("Inserting (3,666)=%d\n",SymTable_put(symbol_table,"3",666));
	printf("Inserting (4,28)=%d\n",SymTable_put(symbol_table,"4",28));
	printf("Inserting (5,16)=%d\n",SymTable_put(symbol_table,"5",16));
	printf("Inserting (hy225,10)=%d\n",SymTable_put(symbol_table,"hy255",10));
	printf("Inserting (-2,shit)=%d\n",SymTable_put(symbol_table,-2,"shit"));
	printf("Inserting (0,0)=%d\n",SymTable_contains(symbol_table,0,0));
	printf("Inserting (11,32)=%d\n",SymTable_put(symbol_table,11,32));
   printf("Length of Symbol Table=%d\n",SymTable_getLength(symbol_table));
	printf("Contains (1,1) ? =%d\n",SymTable_contains(symbol_table,"1",1));
	printf("Contains (0,234) ? =%d", SymTable_contains(symbol_table,"0",234));
	printf("Inserting (Anestis,0)=%d\n",SymTable_put(symbol_table,"Anestis",0));
   printf("New length of Symbol Table=%d\n",SymTable_getLength(symbol_table));
	printf("Contains (32,bla bla bla) ? =%d",SymTable_contains(symbol_table,"32","bla bla bla "));
	printf("Contains (2,4) ? =%d",SymTable_contains(symbol_table,"2",4));

	
	SymTable_free(symbol_table);
	
}




