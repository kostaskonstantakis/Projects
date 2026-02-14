#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "alpha.h"


void addToken(unsigned int line_number, unsigned int token_number, char* real_content, char* real_type, char* real_alpha_type){
	Token *tmp = head, *new_Token;
	new_Token = (Token*) malloc(sizeof(Token));
	new_Token -> numline = line_number;
	new_Token -> numToken = token_number;
	new_Token -> content = strdup(real_content);
	new_Token -> type = strdup(real_type);
	new_Token -> alpha_type = strdup(real_alpha_type);
	new_Token -> next = NULL;
	if(!tmp){
		head = new_Token;
	}
	else{
		while(tmp -> next != NULL){
			tmp = tmp -> next;
		}
		tmp -> next = new_Token;
	}
}


void printTokens(){
	Token *tmp = head;
	int i;
	char* t;
	for(i = 0; i < 81; i++){
		if(i == 41){
			printf("LEXICAL ANALYSIS");
		}
		printf("-");
	}
	printf("\n");
	while(tmp != NULL){
		if(!strcmp(tmp -> type, "STRING") || !strcmp(tmp -> type, "ID")) t = "char*";
		else if(!strcmp(tmp -> type, "INTCONST")) t = "integer";
		else if(!strcmp(tmp -> type, "REALCONST")) t = "real";
		else t = "enumerated";
		printf("%d: #%-15d %-15s %-15s %-15s %-15s\n", tmp -> numline, tmp -> numToken, tmp -> content, tmp -> type, tmp -> alpha_type, merge("<-", t));
		tmp = tmp -> next;
	}
}
