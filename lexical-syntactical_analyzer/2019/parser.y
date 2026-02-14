%{
        #include <stdio.h>
        #include <stdlib.h> 
        #include <string.h>
	//#include "SymbolTableEntry.h" 
	int yyerror (char* yaccProvidedMessage);
	int yylex (void);
	int scope=0;
	extern int yylineno;
	extern char * yyval;
	extern char * yytext;
	extern FILE * yyin;
	extern FILE * yyout;
	
	

typedef struct Variable {
const char *name;
unsigned int scope;
unsigned int line;
} Variable;

typedef struct Function {
const char *name;
//List of arguments
unsigned int scope;
unsigned int line;
} Function;

enum SymbolType {
GLOBAL, LOCAL, FORMAL,
USERFUNC, LIBFUNC
};

typedef struct SymbolTableEntry {
bool isActive;
union {
Variable *varVal;
Function *funcVal;
} value;
enum SymbolType type;
} SymbolTableEntry;

/*The Symbol Table*/
typedef struct SymbolTable
{
	unsigned int size;
SymbolTableEntry node;
//SymbolTableEntry Array[1000];
	//SymbolTableEntry *SymbolArray[SIZE];
}SymbolTable;


void Hide(int scope,SymbolTableEntry *head){
	
}

void PrintTokensInScope(int scope,SymbolTableEntry *head)
{ 
}

SymbolTable *SymbolTable;

void Insert(const char *name,enum type, int scope, int line)
{
  if(type!=LIBFUNC //gotta use strcmp() here
&& (strcmp(name,"print")==0 ||  strcmp(name,"input")==0 
|| strcmp(name,"objectmemberkeys")==0 || strcmp(name,"objecttotalmembers")==0 || strcmp(name,"objectcopy")==0 
|| strcmp(name,"totalarguments")==0 || strcmp(name,"argument")==0 
|| strcmp(name,"typeof")==0 || strcmp(name,"strtonum")==0 || strcmp(name,"sqrt")==0 
|| strcmp(name,"cos")==0 || strcmp(name,"sin")==0 )) 
       printf("Severe error at line %d! You're trying to shadow a library function!\n",yylineno);

	   else {}
}

/* print of output*/
void PrintSymbolTable(SymbolTableEntry *head)
{
     
}
    

/* insert of token in symbol table */
SymbolTableEntry *InsertNode(SymbolTableEntry *head,SymbolTableEntry *node) // bool active,enum type,union value
{
     
}

SymbolTableEntry Lookup(SymbolTableEntry *head,SymbolTableEntry *node)
{ 
 
}



%}

%start program 
%token 	IF 
%token	ELSE
%token	WHILE
%token	FOR
%token	FUNCTION
%token	RETURN
%token	BREAK
%token	CONTINUE
%token	LOCAL
%token	TRUE
%token	FALSE
%token 	NIL
%token	NOT
%token	AND 
%token	OR

%token	ASSIGN
%token	PLUS
%token	MINUS
%token	MULTI
%token	DIV 
%token	MOD
%token	EQUAL
%token	NOT_EQUAL
%token	PLUS_PLUS
%token	MINUS_MINUS
%token	GREATER_THAN
%token	LESS_THAN
%token	GREATER_EQUAL
%token	LESS_EQUAL

%token  NUMBER
%token  REAL_NUMBER
%token  ID 
%token  STRING

%token	LEFT_BRACKET
%token	RIGHT_BRACKET
%token	LEFT_BRACKET2
%token	RIGHT_BRACKET2
%token	LEFT_PARENTHESIS
%token	RIGHT_PARENTHESIS
%token	SEMICOLON
%token	COMMA
%token	COLON
%token	DOUBLE_COLON
%token	DOT
%token	DOUBLE_DOTS

%token	LINE_COMMENT
%token 	MILTILINE_COMMENT
%token 	MILTILINE_COMMENT_END 
%token NESTED_COMMENT
%token NESTED_STRING


%nonassoc GREATER_THAN GREATER_EQUAL LESS_THAN LESS_EQUAL EQUAL NOT_EQUAL
%left PLUS AND OR MULTI DIV MOD
%right NOT PLUS_PLUS MINUS_MINUS MINUS ASSIGN
%type <exprNode> lvalue


%left LEFT_PARENTHESIS RIGHT_PARENTHESIS
%left LEFT_BRACKET2 RIGHT_BRACKET2
%left DOT DOUBLE_DOTS
%define parse.error verbose

%union
{
	char* strVal;
	int intVal;
	double dbVal;
        SymbolTableEntry* exprNode;
}

%% 

program:  sttmnts {fprintf(yyout,"program -> sttmnts \n");}
	 
	  ;

sttmnts:  stmt sttmnts 	{fprintf(yyout,"statements -> stmt sttmnts\n");}
	  |  {fprintf(yyout,"statements -> empty \n");}
	  
	  ;

stmt: expr SEMICOLON {fprintf(yyout,"stmt -> expr; \n");}
      | ifstmt	{fprintf(yyout,"stmt -> ifstmt \n");}
      | whilestmt {fprintf(yyout,"stmt -> whilestmt  \n");}
      | forstmt	{fprintf(yyout,"stmt -> forstmt \n");}
      | returnstmt {fprintf(yyout,"stmt -> returnstmt \n");}
      | BREAK SEMICOLON	{fprintf(yyout,"stmt -> break; \n");} 
      | CONTINUE SEMICOLON {fprintf(yyout,"stmt -> continue; \n");} 
      | block	{fprintf(yyout,"stmt -> block \n");}
      | funcdef	{fprintf(yyout,"stmt -> funcdef \n");}	
      | SEMICOLON {fprintf(yyout,"stmt -> ; \n");}
     
      ;

expr: assignexpr {fprintf(yyout,"expr -> assignexpr; \n");}
      | expr PLUS expr {fprintf(yyout,"expr -> expr + expr; \n");}
      | expr MINUS expr {fprintf(yyout,"expr -> expr - expr; \n");}
      | expr AND expr {fprintf(yyout,"expr -> expr and expr; \n");}
      | expr OR expr {fprintf(yyout,"expr -> expr or expr; \n");}
      | expr MULTI expr {fprintf(yyout,"expr -> expr * expr; \n");}
      | expr DIV expr {fprintf(yyout,"expr -> expr / expr; \n");}
      | expr EQUAL expr {fprintf(yyout,"expr -> expr == expr; \n");}      
      | expr NOT_EQUAL expr {fprintf(yyout,"expr -> expr != expr; \n");}
      | expr GREATER_THAN expr {fprintf(yyout,"expr -> expr > expr; \n");}
      | expr LESS_THAN expr {fprintf(yyout,"expr ->  expr < expr \n");}
      | expr GREATER_EQUAL expr {fprintf(yyout,"expr -> expr >= expr  \n");}
      | expr LESS_EQUAL expr {fprintf(yyout,"expr -> expr <= expr  \n");}
      | term {fprintf(yyout,"expr -> term  \n");}
      
      ;

assignexpr: lvalue EQUAL expr {fprintf(yyout,"assignexpr -> lvalue=expr; \n");}

;



lvalue: ID {fprintf(yyout,"lvalue -> id; \n");}
	| LOCAL ID {fprintf(yyout,"lvalue ->local id; \n");}
	| DOUBLE_COLON ID {fprintf(yyout,"lvalue -> :: id; \n");}
	| member {fprintf(yyout,"lvalue -> member; \n");}

        ;

block: LEFT_BRACKET  RIGHT_BRACKET {fprintf(yyout,"block -> {}\n");} 
       | LEFT_BRACKET {scope++;} stmt RIGHT_BRACKET{fprintf(yyout,"block -> [stmt*]\n"); Hide(scope--,NULL);}
	
       ;
						

ifstmt: IF LEFT_PARENTHESIS expr RIGHT_PARENTHESIS stmt {fprintf(yyout,"ifstmt -> if (expr) stmt\n");}
	| IF LEFT_PARENTHESIS expr RIGHT_PARENTHESIS stmt ELSE stmt  {fprintf(yyout,"ifstmt -> if (expr) stmt else stmt\n");}
	
	;
		
whilestmt: WHILE LEFT_PARENTHESIS expr RIGHT_PARENTHESIS stmt	{fprintf(yyout,"whilestmt -> while (expr) stmt\n");}
	
	   ;
			
forstmt: FOR LEFT_PARENTHESIS elist SEMICOLON expr SEMICOLON elist RIGHT_PARENTHESIS stmt {fprintf(yyout,"forstmt ==> for(elist; expr; elist) stmt\n");}

         ;
		
returnstmt: RETURN expr  SEMICOLON	{fprintf(yyout,"returnstmt -> return expr ;\n");}
	    | RETURN SEMICOLON	{fprintf(yyout,"returnstmt -> return ;\n");}
	
	    ;


funcdef: FUNCTION LEFT_PARENTHESIS idlist RIGHT_PARENTHESIS block {fprintf(yyout,"funcdef -> function (idlist) {} ;\n"); scope++}

         ;

const: NIL {fprintf(yyout,"const -> nil ;\n");}
       | TRUE {fprintf(yyout,"const -> true ;\n");}
       | FALSE {fprintf(yyout,"const -> false ;\n");}
       | STRING {fprintf(yyout,"const -> string ;\n");}
       | NUMBER {fprintf(yyout,"const -> number ;\n");}
	
       ;


term:   LEFT_PARENTHESIS expr RIGHT_PARENTHESIS {fprintf(yyout,"term -> (expr) ;\n");}
   	| MINUS expr {fprintf(yyout,"term -> -expr ;\n");}
	| NOT expr {fprintf(yyout,"term -> not expr ;\n");}
 	| PLUS_PLUS lvalue {fprintf(yyout,"term -> ++expr ;\n");}
	| lvalue PLUS_PLUS {fprintf(yyout,"term -> expr++ ;\n");}
	| MINUS_MINUS lvalue {fprintf(yyout,"term -> --expr ;\n");}
	| lvalue MINUS_MINUS {fprintf(yyout,"term -> expr-- ;\n");}
	| primary {fprintf(yyout,"term -> primary ;\n");}
	
	;

primary: lvalue {fprintf(yyout,"primary -> lvalue ;\n");}
	 | call {fprintf(yyout,"primary -> call ;\n");}
	 | objectdef {fprintf(yyout,"primary -> objectdef ;\n");}
	 | LEFT_PARENTHESIS funcdef RIGHT_PARENTHESIS {fprintf(yyout,"primary -> ( funcdef ) ;\n");}
	 | const {fprintf(yyout,"primary -> const ;\n");}
	 
	 ;

member: lvalue DOT ID {fprintf(yyout,"member -> lvalue.id ;\n");}
	| lvalue LEFT_BRACKET2 expr RIGHT_BRACKET2 {fprintf(yyout,"member -> lvalue[expr] ;\n");}
	| call DOT ID {fprintf(yyout,"member -> call.id ;\n");}
	| call LEFT_BRACKET2 expr RIGHT_BRACKET2 {fprintf(yyout,"member -> call[expr] ;\n");}
	
	;

call:   call LEFT_PARENTHESIS elist RIGHT_PARENTHESIS {fprintf(yyout,"call -> call (elist) ;\n");}
	| lvalue callsuffix {fprintf(yyout,"call -> lvalue callsuffix ;\n");}
	| LEFT_PARENTHESIS funcdef RIGHT_PARENTHESIS  LEFT_PARENTHESIS elist RIGHT_PARENTHESIS {fprintf(yyout,"call -> (funcdef) (elist);\n");}
	
	;


funcdef: FUNCTION LEFT_BRACKET RIGHT_BRACKET LEFT_PARENTHESIS idlist RIGHT_PARENTHESIS block {fprintf(yyout,"funcdef -> function (idlist) {};\n"); scope++;}
	 | FUNCTION ID LEFT_BRACKET RIGHT_BRACKET LEFT_PARENTHESIS idlist RIGHT_PARENTHESIS block {fprintf(yyout,"funcdef -> function [id] (idlist) {};\n"); scope++;}
	
	 ;


callsuffix: normcall {fprintf(yyout,"callsuffix -> normcall;\n");}
	    | methodcall {fprintf(yyout,"callsuffix -> methodcall;\n");}
	
	    ;

normcall: LEFT_PARENTHESIS elist RIGHT_PARENTHESIS {fprintf(yyout,"normcall -> (elist);\n");} 

;

methodcall: DOUBLE_DOTS ID LEFT_PARENTHESIS elist RIGHT_PARENTHESIS {fprintf(yyout,"methodcall -> .. id (elist);\n");} 
;
/* equivalent to lvalue.id(lvalue, elist) */


elist:  expr {fprintf(yyout,"elist -> expr;\n");} 
	| expressions  {fprintf(yyout,"elist -> expressions ;\n");} 
	
        ; 

expressions: expressions expr {fprintf(yyout,"expressions -> expressions expr;\n");} 
	     | expr {fprintf(yyout,"elist -> expr;\n");} 
	     
	     ; 
 
objectdef: LEFT_BRACKET2 elist RIGHT_BRACKET2  {fprintf(yyout,"objectdef -> [elist];\n");} 
	   | LEFT_BRACKET2 indexed RIGHT_BRACKET2 {fprintf(yyout,"objectdef -> [indexed];\n");}  
	   
	   ;

indexedelem: LEFT_BRACKET expr COLON expr RIGHT_BRACKET {fprintf(yyout,"indexedelem -> {expr : expr};\n");} 
;
indexedelems: indexedelems indexedelem {fprintf(yyout,"indexedelems -> [indexedelems]*;\n");} 
	      | indexedelem {fprintf(yyout,"indexedelems -> indexedelem;\n");} 
	      
              ;

indexed:  indexedelems {fprintf(yyout,"indexed -> [indexedelem]*;\n");} 
	 
	 ;

ids: ids ID {fprintf(yyout,"ids -> [id]*;\n");} 
	| ID {fprintf(yyout,"ids -> id;\n");} 
	;

idlist: ids {fprintf(yyout,"idlist -> idlist;\n");} 
	;







%%

int yyerror (char* yaccProvidedMessage)
{
	fprintf(stderr, "%s: at line %d, before token: '%s'\n", yaccProvidedMessage, yylineno, yytext);
}


int main(int argc, char **argv) 
{
SymbolTable symboltable;

if(argc>1)
{
 if(!(yyin=fopen(argv[1],"r")))
   {
     fprintf(stderr,"Cannot read file: %s\n",argv[1]);
     return 1;
   }
}
else {
 SymbolTableEntry *tmp=symboltable.node; 
/*
Insert("print",LIBFUNC,0,0);
Insert("input",LIBFUNC,0,0);
Insert("objecttotalmembers",LIBFUNC,0,0);
Insert("objectcopy",LIBFUNC,0,0);
Insert("objectmemberkeys",LIBFUNC,0,0);
Insert("totalarguments",LIBFUNC,0,0);
Insert("argument",LIBFUNC,0,0);
Insert("typeof",LIBFUNC,0,0);
Insert("strtonum",LIBFUNC,0,0);
Insert("sqrt",LIBFUNC,0,0);
Insert("cos",LIBFUNC,0,0);
Insert("sin",LIBFUNC,0,0);

*/

Insert("input",USERFUNC,0,0);
Insert("typeof",LOCAL,0,0);
}
yyparse();
return 0;
}
