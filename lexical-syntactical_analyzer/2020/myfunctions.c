#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "myfunctions.h"
#include "alpha.h"

char* firstmerge(const char* s1, const char* s2){
	char* result = malloc(strlen(s1) + strlen(s2) + 5);
	strcpy(result, s1);
	strcat(result, s2);
	return result;
}

int find_new_lineno(char* string){
	size_t l = strlen(string);
       	size_t i;
       	int a =0;
       	for(i = 0; i < l; i++){
		if(string[i] == '\n') a++;
	}
	return a;
}

char* block_comment_lines(int first_line, int line){
	char* a = malloc(sizeof(4));
	char* b = malloc(sizeof(4));
	char* c = " - ";
	char* d = "\"";
	char* final = malloc(sizeof(20));
	sprintf(a, "%d", first_line);
	sprintf(b, "%d", line);
	a = firstmerge(d, a);
	final = firstmerge(a, c);
	final = firstmerge(final, b);
	final = firstmerge(final, d);
	return final;

}

int string_proccessing(char* text, int line, int token){
	int i = 0, j = 0;
	char *temp, *final;
	temp = malloc(strlen(text));
	final = malloc(strlen(text));
	temp = strdup(text);
	while(temp[i] != '\0'){
		if(temp[i] == '\\'){
			i++;
			switch (temp[i]){
				case 'a':
					final[j] = 'a';
					break;
				case 'e':
					final[j] = 'e';
					break;
				case 'f':
					final[j] = 'f';
					break;
				case 'r':
					final[j] = 'r';
					break;
				case 'n':
					final[j] = '\n';
					break;
				case 'b':
					final[j] = '\b';
					break;
				case 't':
					final[j] = '\t';
					break;
				case 'v':
					final[j] = 'v';
					break;
				case '\n':
					final[j] = '\b';
					break;
				case '\'':
					final[j] = '\'';
					break;
				case '\"':
					final[j] = '\"';
					break;
				case '\\':
					final[j] = '\\';
					break;
				case ' ':
					final[j] = ' ';
					break;
				default:
					final[j] = temp[i - 1];
					j++;
					final[j] = temp[i];
					fprintf(stderr,"Invalid escape character \\%c at line %d\n", final[j], line);
					return 0;
			}
			j++;
		}
		else{
			final[j] = temp[i];
			j++;
		}
		i++;
	}
	return 1;
	addToken(line, token, final, "STRING", "STRING");
}
