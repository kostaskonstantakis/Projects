%{	
	#include <stdio.h>
	#include <string.h>
	#include <stdlib.h>
	#include <string.h>
	#include "alpha.h"
	#include "parser.h"

	int tokencount = 0;



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
}
%}

%option noyywrap
%option yylineno  


IF				"if"
ELSE				"else"
WHILE				"while"
FOR				"for"
FUNCTION			"function"
RETURN				"return"
BREAK				"break"
CONTINUE			"continue"
TRUE				"true"
FALSE				"false"
AND				"and"
NOT				"not"
OR				"or"
NIL				"nil"
LOCAL				"local"

ASSIGN				"="
PLUS				"+"
MINUS				"-"
MULTIPLY			"*"
DIVISION			"/"
MOD				"%"
EQUAL				"=="
NOT_EQUAL			"!="
PLUS_PLUS			"++"
MINUS_MINUS			"--"
LESS				"<"
GREATER				">"
LESS_EQUAL			"<="
MORE_EQUAL			">="

LEFT_CURLY_BRACKET		"{"
RIGHT_CURLY_BRACKET		"}"
LEFT_SQUARE_BRACKET		"["
RIGHT_SQUARE_BRACKET		"]"
LEFT_PARENTHESIS		"("
RIGHT_PARENTHESIS		")"
SEMICOLON			";"
COMMA				","
COLON				":"
COLON_COLON			"::"
FULL_STOP			"."
FULL_STOP_FULL_STOP		".."

BLOCK_COMMENT			"/*"
LINE_COMMENT			"//".*
INTEGER				[0-9]*
REALCONST			[0-9]*\.[0-9](e|E)?[0-9]*
STRING				\"(\\.|[^"\\])*\"
ID				[a-zA-Z][a-zA-z_0-9]*

WHITESPACES			[ \r\n\t\v]
OTHER				.



%%

{IF}				{return IF;} 

{ELSE}				{return ELSE;}

{WHILE}				{return WHILE;}

{FOR}				{return FOR;}

{FUNCTION}			{return FUNCTION;}

{RETURN}			{return RETURN;}

{BREAK}				{return BREAK;}

{CONTINUE}			{return CONTINUE;}

{TRUE}				{return TRUE;}

{FALSE}				{return FALSE;}

{AND}				{return AND;}

{NOT}				{return NOT;}

{OR}				{return OR;}

{NIL}				{return NIL;}

{LOCAL}				{return LOCAL;}

{BLOCK_COMMENT}			{tokencount++;
				int count;
				count = yylineno;
				char next;
				push(&root, yylineno);
				next = input();
				while(next != '\0' && (isEmpty(root) == 0)){
					if(next == '/'){
						if((next = input()) == '\0') break;
						if(next == '*'){
							push(&root, yylineno);
						}
						if(next != '/') next = input();
					}
					else if(next == '*'){
						if((next = input()) == '\0') break;
						if(next == '/'){
							pop(&root);
						}
						if(next != '*' && (isEmpty(root) == 0)) next = input();
					}
					else{
						next = input();
					}
				}
				if((isEmpty(root) == 0))  peek(root);
				//else addToken(yylineno, tokencount, block_comment_lines(count, yylineno), "COMMENT", "BLOCK_COMMENT");
				}

{ASSIGN}			{return ASSIGN;}

{PLUS}				{return PLUS;}

{MINUS}				{return MINUS;}

{MULTIPLY}			{return MULTIPLY;}

{DIVISION}			{return DIVISION;}

{MOD}				{return MOD;}

{EQUAL}				{return EQUAL;}

{NOT_EQUAL}                     {return NOT_EQUAL;}

{PLUS_PLUS}                     {return PLUS_PLUS;}

{MINUS_MINUS}                   {return MINUS_MINUS;}

{LESS}				{return LESS;}

{GREATER}			{return GREATER;}

{LESS_EQUAL}                    {return LESS_EQUAL;}

{MORE_EQUAL}                    {return MORE_EQUAL;}

{INTEGER}			{return INTEGER;}

{REALCONST}			{return REALCONST;}

{STRING}			{tokencount++;
				int s = string_proccessing(yytext, yylineno, tokencount);
				if(s == 1) return STRING;}

{LEFT_CURLY_BRACKET}		{return LEFT_CURLY_BRACKET;}

{RIGHT_CURLY_BRACKET}		{return RIGHT_CURLY_BRACKET;}

{LEFT_SQUARE_BRACKET}		{return LEFT_SQUARE_BRACKET;}

{RIGHT_SQUARE_BRACKET}		{return RIGHT_SQUARE_BRACKET;}

{LEFT_PARENTHESIS}		{return LEFT_PARENTHESIS;}

{RIGHT_PARENTHESIS}		{return RIGHT_PARENTHESIS;}

{SEMICOLON}			{return SEMICOLON;}

{COMMA}				{return COMMA;}

{COLON}				{return COLON;}

{COLON_COLON}			{return COLON_COLON;}

{FULL_STOP}			{return FULL_STOP;}
	
{FULL_STOP_FULL_STOP}		{return FULL_STOP_FULL_STOP;}

{ID}				{yylval.stringVal =strdup(yytext);
				return ID;}

{WHITESPACES}			{}

{LINE_COMMENT}			{}


{OTHER}				{fprintf(stderr, "Does not match with any rule (line: %d): %s!\n",yylineno, yytext);}

%%
