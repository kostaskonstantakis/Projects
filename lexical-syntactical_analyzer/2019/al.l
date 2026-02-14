%{
  #include <stdio.h>
  #include <string.h>
  #include <stdlib.h>
  #include "parser.h" 
  #define YY_DECL int alpha_yylex(void* yylval) 
 

struct alpha_token_t {
  unsigned int     numline;
  unsigned int     numToken;
  char          *content;
  char          *type;
  struct alpha_token_t *alpha_yylex;

};

struct alpha_token_t *node=NULL; // creating an object to traverse the struct in the function. 

void Print(struct alpha_token_t *node) //print all tokens
{


printf("%d\t#%d\t“%s”\t%s\n",node->numline,node->numToken,node->content,node->type);
node=node->alpha_yylex; //this must act like the "next" node, probably




}

%}



IF              "if"
FOR  	        "for"
AND	        "and"
NOT 	        "not"	
ELSE	        "else"
WHILE 	        "while"
FUNCTION        "function"
RETURN 		"return"
BREAK           "break"
CONTINUE	"continue"
TRUE	 	"true"
FALSE    	"false"
LOCAL		"local"
NIL		"nil"
OR		"or"
	



ASSIGN		"="
PLUS		"+"
MINUS		"-"
MULTI		"*"
DIV		"/"
MOD		"%"
EQUAL		"=="
NOT_EQUAL	"!="
PLUS_PLUS	"++"
MINUS_MINUS     "--"
GREATER_THAN	">"
LESS_THAN	"<"
GREATER_EQUAL	">="
LESS_EQUAL	"<="



NUMBER		[0-9]+ 



REAL_NUMBER    	[0-9]{1,}"."[0-9]{1,} 

STRING		\"[^\n|\t|\\]*\" 
NESTED_STRING \"[.]*\/[/|*]\*\/\"

LEFT_BRACKET		\{
RIGHT_BRACKET		\}
LEFT_BRACKET2		\[
RIGHT_BRACKET2		\]
LEFT_PARENTHESIS	\(
RIGHT_PARENTHESIS	\)
SEMICOLON		\;
COMMA			\,
COLON			\:
DOUBLE_COLON		\:\: 
DOT			\.
DOUBLE_DOTS	        \.\. 


ID		       [a-zA-Z][a-zA-Z0-9_]* 

LINE_COMMENT		\/\/ 
MULTILINE_COMMENT	\/\* 
MULTILINE_COMMENT_END   \*\/
NESTED_COMMENT \/[/|*]*\['*\']*\" 



%option noyywrap
%option yylineno

%%


{STRING}          {fprintf(stderr,"Recognized string: %s\n",yytext); yylval.strVal=strdup(yytext); return STRING;}
{NUMBER}	   {fprintf(stderr,"Recognized number: %s\n",yytext); yylval.intVal=atoi(yytext); return NUMBER;}
{REAL_NUMBER}       {fprintf(stderr,"Recognized real number: %s\n",yytext); yylval.dbVal=atoi(yytext); return REAL_NUMBER;}
{ASSIGN}		{fprintf(stderr,"Recognized assign: %s\n",yytext); yylval.strVal=strdup(yytext); return ASSIGN;}
{PLUS}		     {fprintf(stderr,"Recognized plus: %s\n",yytext); yylval.strVal=strdup(yytext); return PLUS;}
{MINUS}		 {fprintf(stderr,"Recognized minus: %s\n",yytext); yylval.strVal=strdup(yytext); return MINUS;}
{MULTI}		{fprintf(stderr,"Recognized multi: %s\n",yytext); yylval.strVal=strdup(yytext); return MULTI;}
{DIV}		{fprintf(stderr,"Recognized div: %s\n",yytext); yylval.strVal=strdup(yytext); return DIV;}
{MOD}		{fprintf(stderr,"Recognized mod: %s\n",yytext); yylval.strVal=strdup(yytext); return MOD;}
{EQUAL}		{fprintf(stderr,"Recognized equal token: %s\n",yytext); yylval.strVal=strdup(yytext); return EQUAL;}
{NOT_EQUAL}	{fprintf(stderr,"Recognized not equal token: %s\n",yytext); yylval.strVal=strdup(yytext); return NOT_EQUAL;}
{PLUS_PLUS}	{fprintf(stderr,"Recognized plus plus: %s\n",yytext); yylval.strVal=strdup(yytext); return PLUS_PLUS;}
{MINUS_MINUS}    {fprintf(stderr,"Recognized minus minus: %s\n",yytext); yylval.strVal=strdup(yytext); return MINUS_MINUS;} 
{GREATER_THAN}	{fprintf(stderr,"Recognized greater than token: %s\n",yytext); yylval.strVal=strdup(yytext); return GREATER_THAN;}
{LESS_THAN}	{fprintf(stderr,"Recognized less than token: %s\n",yytext); yylval.strVal=strdup(yytext); return LESS_THAN;}
{GREATER_EQUAL}	 {fprintf(stderr,"Recognized greater equal token: %s\n",yytext); yylval.strVal=strdup(yytext); return GREATER_EQUAL;}
{LESS_EQUAL}	{fprintf(stderr,"Recognized less equal token: %s\n",yytext); yylval.strVal=strdup(yytext); return LESS_EQUAL; }
{IF}             {fprintf(stderr,"Recognized if token: %s\n",yytext); yylval.strVal=strdup(yytext); return IF;} 
{FOR}  	        {fprintf(stderr,"Recognized for token: %s\n",yytext); yylval.strVal=strdup(yytext); return FOR;}
{AND}	      {fprintf(stderr,"Recognized and token: %s\n",yytext); yylval.strVal=strdup(yytext); return AND;}
{NOT} 	        {fprintf(stderr,"Recognized not token: %s\n",yytext); yylval.strVal=strdup(yytext); return NOT;}
{ELSE}	        {fprintf(stderr,"Recognized else token: %s\n",yytext); yylval.strVal=strdup(yytext); return ELSE;}
{WHILE} 	{fprintf(stderr,"Recognized while: %s\n",yytext); yylval.strVal=strdup(yytext); return WHILE;}        
{FUNCTION}      {fprintf(stderr,"Recognized function: %s\n",yytext); yylval.strVal=strdup(yytext); return FUNCTION;}  
{RETURN} 	{fprintf(stderr,"Recognized return: %s\n",yytext); yylval.strVal=strdup(yytext); return RETURN;}	
{BREAK}         {fprintf(stderr,"Recognized break: %s\n",yytext); yylval.strVal=strdup(yytext); return BREAK;}  
{CONTINUE}	{fprintf(stderr,"Recognized continue: %s\n",yytext); yylval.strVal=strdup(yytext); return CONTINUE;}
{TRUE}	 	{fprintf(stderr,"Recognized true: %s\n",yytext); yylval.strVal=strdup(yytext); return TRUE;}
{FALSE}    	{fprintf(stderr,"Recognized false: %s\n",yytext); yylval.strVal=strdup(yytext); return FALSE;}
{LOCAL}		{fprintf(stderr,"Recognized local token: %s\n",yytext); yylval.strVal=strdup(yytext); return LOCAL;}
{NIL}		{fprintf(stderr,"Recognized nil token: %s\n",yytext); yylval.strVal=strdup(yytext); return NIL;}
{OR}		{fprintf(stderr,"Recognized or: %s\n",yytext); yylval.strVal=strdup(yytext); return OR;}
{LEFT_BRACKET}		{fprintf(stderr,"Recognized left bracket: %s\n",yytext); yylval.strVal=strdup(yytext); return LEFT_BRACKET;}
{RIGHT_BRACKET}		{fprintf(stderr,"Recognized right bracket: %s\n",yytext); yylval.strVal=strdup(yytext); return RIGHT_BRACKET;}
{LEFT_BRACKET2}		{fprintf(stderr,"Recognized left different type of bracket: %s\n",yytext); yylval.strVal=strdup(yytext); return LEFT_BRACKET2;}
{RIGHT_BRACKET2}	{fprintf(stderr,"Recognized right different type of bracket: %s\n",yytext); yylval.strVal=strdup(yytext); return RIGHT_BRACKET2;}
{LEFT_PARENTHESIS}	{fprintf(stderr,"Recognized left parenthesis: %s\n",yytext); yylval.strVal=strdup(yytext); return LEFT_PARENTHESIS;}
{RIGHT_PARENTHESIS}	{fprintf(stderr,"Recognized right parenthesis: %s\n",yytext); yylval.strVal=strdup(yytext); return RIGHT_PARENTHESIS;}
{SEMICOLON}		{fprintf(stderr,"Recognized semicolon: %s\n",yytext); yylval.strVal=strdup(yytext); return SEMICOLON;}
{COMMA}			{fprintf(stderr,"Recognized comma: %s\n",yytext); yylval.strVal=strdup(yytext); return COMMA;}
{COLON}			{fprintf(stderr,"Recognized colon: %s\n",yytext); yylval.strVal=strdup(yytext); return COLON;}
{DOUBLE_COLON}		{fprintf(stderr,"Recognized double colon: %s\n",yytext); yylval.strVal=strdup(yytext); return DOUBLE_COLON;}
{DOT}			{fprintf(stderr,"Recognized dot: %s\n",yytext); yylval.strVal=strdup(yytext); return DOT; }
{DOUBLE_DOTS}	        {fprintf(stderr,"Recognized double dot: %s\n",yytext); yylval.strVal=strdup(yytext); return DOUBLE_DOTS;}
{MULTILINE_COMMENT_END} {fprintf(stderr,"Recognized multiline comment ending: %s\n",yytext); yylval.strVal=strdup(yytext); return MULTILINE_COMMENT_END; }
{MULTILINE_COMMENT} {fprintf(stderr,"Recognized multiline comment: %s\n",yytext); yylval.strVal=strdup(yytext); return MULTILINE_COMMENT;}
{LINE_COMMENT} {fprintf(stderr,"Recognized line comment: %s\n",yytext); yylval.strVal=strdup(yytext); return LINE_COMMENT; }
{ID} {fprintf(stderr,"Recognized id: %s\n",yytext);  yylval.strVal=strdup(yytext); return ID;}
{NESTED_STRING} {fprintf(stderr,"Recognized string: %s\n",yytext); yylval.strVal=strdup(yytext); return NESTED_STRING; }
{NESTED_COMMENT} {fprintf(stderr,"Recognized multiline comment: %s\n",yytext); yylval.strVal=strdup(yytext); return NESTED_COMMENT; }



%%

alpha_yylex(NULL);




