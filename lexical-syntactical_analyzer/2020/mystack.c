#include <limits.h>
#include <stdio.h>
#include <stdlib.h>
#include "alpha.h"

void pushphase2(struct SymtableStackNode** r, int l){
	struct SymtableStackNode* element = (struct SymtableStackNode*)malloc(sizeof(struct SymtableStackNode));
	element -> line = l;
	element -> next = *r;
	(*r) = element;
}

void popphase2(struct SymtableStackNode** r){
	if(r != NULL){
		struct SymtableStackNode* temp = *r;
		*r = (*r) -> next;
		free(temp);
	}
	else{
		fprintf(stderr, "The stack is empty(pop)!\n");
	}
}

void peekphase2(struct SymtableStackNode* r){
	if(r != NULL) printf("Comment block error at line: %d\n", r -> line);
	else fprintf(stderr, "The stack is empty(peek)!\n");
}

int isEmptyphase2(struct SymtableStackNode* r){
	if(r != NULL) return 0;
	else return 1;
}
