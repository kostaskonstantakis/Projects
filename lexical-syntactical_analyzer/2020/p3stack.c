#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "alpha.h"

// function used to determine whether the stack is empty

int isEmpty(struct StackNode* r){
	if(r != NULL) return 0;
	else return 1;
}


// function used to push an element into the stack

void push(struct StackNode** root, int offset){
	struct StackNode* newnode = (struct StackNode*)malloc(sizeof(struct StackNode));
	newnode -> offset = offset;
	newnode -> next = *root;
	(*root) = newnode;
}


/* function used to remove the top element from the stack and making sure
the stack isn't empty
*/

unsigned int pop_and_top(struct StackNode** root){
	if(!isEmpty((*root))){
		unsigned int off_set = (*root) -> offset;
		struct StackNode* temp = *root;
		*root = (*root) -> next;
		free(temp);
		return off_set;
	}
	else{
		fprintf(stderr, "Stack is empty!\n");
		return 1;
	}
}

