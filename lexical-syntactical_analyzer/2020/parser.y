%{
    #include <stdio.h>
    #include <stdlib.h> 
    #include <string.h>
	#include "alpha.h"

	int yyerror (char* yaccProvidedMessage);
	int yylex (void);
	
	int scope = 0, lookup_result, is_func, temp = 10, loopcounter = 0, breakFlag = 0, continueFlag = 0;
	char *function_name;
	char my_func_name[10];
	extern int yylineno;
	extern char * yytext;
	extern FILE * yyin;
	extern FILE * yyout;
	extern SymbolTableEntry *ScopeList[MAX_SCOPE_SIZE];
	extern SymbolTableEntry *SymbolTable[HASH_TABLE_SIZE];
	struct StackNode* scopeoffsetstack, loopcounterstack;

	// pointer ston enumerated typo twn const	
//type* constT = (type*)malloc(sizeof(type));
//constT = create_type(constT,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,NULL,const,NULL);
	// pointer ston typo twn expression
//type* expressionT = (type*)malloc(sizeof(type));
//expressionT = create_type(expressionT,NULL,NULL,NULL,expr,NULL,NULL,NULL,NULL,NUL$
//type* statementT = (type*)malloc(sizeof(type));
//statementT = create_type(statementT,NULL,stmt,NULL,NULL,NULL,NULL,NULL,NULL,NULL,$



%}


%union
{
	expr expressionnode;
	iopcode opcodenode;
	quad quadnode;
	incompleteList incompletelistnode;
	call_t callnode;
	stmt stmtnode;
	symbol symbolnode;
	type typenode;
}


%start		program 

%type <expressionnode> expr lvalue member primary assignexpr call term objectdef const stmts stmt break continue M N 
%type <expressionnode> callsuffix normcall methodcall elist expr_comma indexed indexedelem indexedelem_comma block funcname funcdef funcprefix funcargs funcbody idlist ID_comma ifprefix elseprefix
%type <expressionnode> ifstmt loopstmt whilestmt whilestart whilecond forprefix forstmt returnstmt
%type <symbolnode> SEMICOLON
%type <typenode> AND

%token		<stringVal> ID 
%token		<intVal> INTEGER
%token		<realVal> REALCONST
%token		<stringVal> STRING


%token		IF 
%token		ELSE
%token		WHILE
%token		FOR
%token		FUNCTION
%token		RETURN
%token		BREAK
%token		CONTINUE
%token		AND
%token		NOT 
%token		OR
%token		LOCAL
%token		<TRUE> TRUE
%token		<FALSE> FALSE
%token		<NIL> NIL

%token		ASSIGN
%token		PLUS
%token		MINUS
%token		MULTIPLY
%token		DIVISION 
%token		MOD
%token		EQUAL
%token		NOT_EQUAL
%token		PLUS_PLUS
%token		MINUS_MINUS
%token		GREATER
%token		LESS
%token		MORE_EQUAL
%token		LESS_EQUAL

%token		LEFT_CURLY_BRACKET
%token		RIGHT_CURLY_BRACKET
%token		LEFT_SQUARE_BRACKET
%token		RIGHT_SQUARE_BRACKET
%token		LEFT_PARENTHESIS
%token		RIGHT_PARENTHESIS
%token		SEMICOLON
%token		COMMA
%token		COLON
%token		COLON_COLON
%token		FULL_STOP
%token		FULL_STOP_FULL_STOP

%right		ASSIGN
%left 		OR
%left		AND
%nonassoc	EQUAL NOT_EQUAL GREATER MORE_EQUAL LESS LESS_EQUAL	
%left		PLUS MINUS MULTIPLY DIVISION MOD	
%right		NOT PLUS_PLUS MINUS_MINUS UMINUS
%left		FULL_STOP_FULL_STOP FULL_STOP LEFT_SQUARE_BRACKET RIGHT_SQUARE_BRACKET LEFT_PARENTHESIS RIGHT_PARENTHESIS





%%
 
program: stmts	 
	;


stmts: stmt SEMICOLON stmts
	|{$$ = NULL;}
	;


stmt: expr SEMICOLON  
	| ifstmt 
	| whilestmt 
	| forstmt 
	| returnstmt 
	| break 
	| continue 
	| block 
	| funcdef 
	| SEMICOLON 
	;

break : BREAK SEMICOLON{ 
		if(loopcounter > 0){
			breakFlag++;
			$$ = (expr*)malloc(sizeof(struct expr));
			$$ -> contlist = makelist(nextQuad());
			$$ -> breaklist = NULL;
			emit(jump, NULL, NULL, NULL, 0 , yylineno);
		}
		else fprintf(stderr, "Break is not in loop!\n");
	}
	;
	
continue: CONTINUE SEMICOLON{ 
		if(loopcounter > 0){
			continueFlag++;
			$$ = (expr*)malloc(sizeof(expr));
			$$ -> contlist = makelist(nextQuad());
			$$ -> breaklist = NULL;
			emit(jump, NULL, NULL, NULL, 0 , yylineno);
		}
		else fprintf(stderr, "Break is not in loop!\n");
	}
	;
	
expr: assignexpr{
		$$ = $1;
	}
	| expr PLUS expr{
		if($1 -> type == boolexpr_e || $1 -> type == constbool_e) fprintf(stderr, "Error, arithmetic operator(+) used on boolean expresion!\n");
		expr* temp_expr;
		temp_expr = newexpr(arithmexpr_e);
		temp_expr -> sym = newtemp(); 											// new_temp()
		emit(add, $1, $3, temp_expr, -1, yylineno);
		$$ = temp_expr;
	}
	| expr MINUS expr{
		if($1 -> type == boolexpr_e || $1 -> type == constbool_e) fprintf(stderr, "Error, arithmetic operator(-) used on boolean expresion!\n");expr* temp_expr;
		temp_expr = newexpr(arithmexpr_e);
		temp_expr -> sym = newtemp(); 											// new_temp()
		emit(sub, $1, $3, temp_expr, -1, yylineno);
		$$ = temp_expr;
	}
	| expr AND{
		if($1 -> type != boolexpr_e){
			emit(if_eq, $1, newexpr_constbool(1), NULL, -1, yylineno);
			emit(JUMP, NULL, NULL, NULL, -1, yylineno);
			$1 -> truelist = makelist(nextQuad());
			$1 -> falselist = makelist(nextQuad() + 1);
		}
	} M expr{
		if($1 -> type != boolexpr_e){
			emit(if_eq, $5, newexpr_constbool(1), NULL, -1, yylineno);
			emit(JUMP, NULL, NULL, NULL, -1, yylineno);
			$1 -> truelist = makelist(nextQuad());
			$1 -> falselist = makelist(nextQuad() + 1);
		}
		backpatch($1 -> truelist, $4);
		expr* temp_expr;
		temp_expr = newexpr(boolexpr_e);
		temp_expr -> sym = newtemp(); 											// new_temp()
		$$ = temp_expr;
		$$ -> truelist = $5 -> truelist;
		$$ -> falselist = merge($1 -> falselist, $2 -> falselist);
		//flag = 1
	}
	| expr OR{
		if($1 -> type != boolexpr_e){
			emit(if_eq, $1, newexpr_constbool(1), NULL, -1, yylineno);
			emit(JUMP, NULL, NULL, NULL, -1, yylineno);
			$1 -> truelist = makelist(nextQuad());
			$1 -> falselist = makelist(nextQuad() + 1);
		}
	} M expr{
		if($1 -> type != boolexpr_e){
			emit(if_eq, $5, newexpr_constbool(1), NULL, -1, yylineno);
			emit(JUMP, NULL, NULL, NULL, -1, yylineno);
			$1 -> truelist = makelist(nextQuad());
			$1 -> falselist = makelist(nextQuad() + 1);
		}
		backpatch($1 -> falselist, $4);
		expr* temp_expr;
		temp_expr = newexpr(boolexpr_e);
		temp_expr -> sym = newtemp(); 											// new_temp()
		$$ = temp_expr;
		$$ -> truelist = merge($1 -> truelist, $5 -> truelist);
		$$ -> falselist = $5 -> falselist;
		//flag = 1
	}
	| expr MULTIPLY expr{
		if($1 -> type == boolexpr_e || $1 -> type == constbool_e) fprintf(stderr, "Error, arithmetic operator(*) used on boolean expresion!\n");
		expr* temp_expr;
		temp_expr = newexpr(arithmexpr_e);
		temp_expr -> sym = newtemp(); 											// new_temp()
		emit(mul, $1, $3, temp_expr, -1, yylineno);
		$$ = temp_expr;
	}
	| expr DIVISION expr{
		if($1 -> type == boolexpr_e || $1 -> type == constbool_e) fprintf(stderr, "Error, arithmetic operator(/) used on boolean expresion!\n");
		expr* temp_expr;
		temp_expr = newexpr(arithmexpr_e);
		temp_expr -> sym = newtemp(); 											// new_temp()
		emit(div, $1, $3, temp_expr, -1, yylineno);
		$$ = p_expr;
	}
	| expr MOD expr{
		if($1 -> type == boolexpr_e || $1 -> type == constbool_e) fprintf(stderr, "Error, arithmetic operator(%) used on boolean expresion!\n");
		expr* temp_expr;
		temp_expr = newexpr(arithmexpr_e);
		temp_expr -> sym = newtemp(); 											// new_temp()
		emit(mod, $1, $3, temp_expr, -1, yylineno);
		$$ = temp_expr;
	}
	| expr EQUAL expr{
		if($1 -> type == boolexpr_e || $1 -> type == constbool_e) fprintf(stderr, "Error, comparison operator(==) used on boolean expresion!\n");
		expr* temp_expr;
		temp_expr = newexpr(boolexpr_e);
		temp_expr -> sym = newtemp();
		emit(if_greater, $1, $3, NULL, -1, yylineno);
		emit(jump, NULL, NULL, NULL, -1, yylineno);
		$$ = temp_expr;
		$$ -> truelist = makelist(nextQuad());
		$$ -> falselist = makelist(nextQuad() + 1);
	}
	| expr NOT_EQUAL expr{
		if($1 -> type == boolexpr_e || $1 -> type == constbool_e) fprintf(stderr, "Error, comparison operator(!=) used on boolean expresion!\n");
		expr* temp_expr;
		temp_expr = newexpr(boolexpr_e);
		temp_expr -> sym = newtemp();
		emit(if_greater, $1, $3, NULL, -1, yylineno);
		emit(jump, NULL, NULL, NULL, -1, yylineno);
		$$ = temp_expr;
		$$ -> truelist = makelist(nextQuad());
		$$ -> falselist = makelist(nextQuad() + 1);
	}
	| expr GREATER expr{
		if($1 -> type == boolexpr_e || $1 -> type == constbool_e) fprintf(stderr, "Error, comparison operator(>) used on boolean expresion!\n");
		expr* temp_expr;
		temp_expr = newexpr(boolexpr_e);
		temp_expr -> sym = newtemp();
		emit(if_greater, $1, $3, NULL, -1, yylineno);
		emit(jump, NULL, NULL, NULL, -1, yylineno);
		$$ = temp_expr;
		$$ -> truelist = makelist(nextQuad());
		$$ -> falselist = makelist(nextQuad() + 1);
	}
	| expr LESS expr{
		if($1 -> type == boolexpr_e || $1 -> type == constbool_e) fprintf(stderr, "Error, comparison operator(<) used on boolean expresion!\n");
		expr* temp_expr;
		temp_expr = newexpr(boolexpr_e);
		temp_expr -> sym = newtemp();
		emit(if_greater, $1, $3, NULL, -1, yylineno);
		emit(jump, NULL, NULL, NULL, -1, yylineno);
		$$ = temp_expr;
		$$ -> truelist = makelist(nextQuad());
		$$ -> falselist = makelist(nextQuad() + 1);
	}
	| expr MORE_EQUAL expr{
		if($1 -> type == boolexpr_e || $1 -> type == constbool_e) fprintf(stderr, "Error, comparison operator(>=) used on boolean expresion!\n");
		expr* temp_expr;
		temp_expr = newexpr(boolexpr_e);
		temp_expr -> sym = newtemp();
		emit(if_greater, $1, $3, NULL, -1, yylineno);
		emit(jump, NULL, NULL, NULL, -1, yylineno);
		$$ = temp_expr;
		$$ -> truelist = makelist(nextQuad());
		$$ -> falselist = makelist(nextQuad() + 1);
	}
	| expr LESS_EQUAL expr{
		if($1 -> type == boolexpr_e || $1 -> type == constbool_e) fprintf(stderr, "Error, comparison operator(<=) used on boolean expresion!\n");
		expr* temp_expr;
		temp_expr = newexpr(boolexpr_e);
		temp_expr -> sym = newtemp();
		emit(if_greater, $1, $3, NULL, -1, yylineno);
		emit(jump, NULL, NULL, NULL, -1, yylineno);
		$$ = temp_expr;
		$$ -> truelist = makelist(nextQuad());
		$$ -> falselist = makelist(nextQuad() + 1);
	}
	| term	{$$ = $1;}
	;

M:	{$$ = nextQuad();}
	;

N:	{$$ = nextQuad(); emit(jump,NULL,NULL,NULL,-1,yylineno);	
	}
	;

term: LEFT_PARENTHESIS expr RIGHT_PARENTHESIS{$$ = $2;} 
	| 	MINUS expr %prec UMINUS{
			checkuminus($2);
			expr* temp_expr;
			temp_expr = newexpr(arithmexpr_e);
			temp_expr -> sym = newtemp();
			$$ = temp_expr;
			emit(uminus, $2, NULL, $$ -1, yylineno);
		}
	|	NOT expr{
			expr* temp_expr;
			temp_expr = newexpr(boolexpr_e);
			temp_expr -> sym = newtemp();
			$$ = temp_expr;
			emit(not, $2, NULL, $$, -1, yylineno);
			$$->truelist = $2->falselist;
			$$->falselist = $2->truelist;
		}
	|	PLUS_PLUS lvalue{
			if(is_func == 1){
				fprintf(stderr, "%s is function, (++) is illegal action!\n", function_name);
				exit(1);
			}
			if($2 -> type == tableitem_e){
				$$ = emit_iftableintem($2);
				emit(add, $$, newexpr_constint(1), $$, -1, yylineno);
				emit(tablesetelem, $$, $2 -> index, -1, yylineno); //
			}
			else{
				expr* temp_expr;
				temp_expr = newexpr(arithexpr_e);
				temp_expr -> sym = newtemp();
				emit(add, $2, newexpr_constint(1), $$, -1, yylineno);
				$$ = temp_expr;
				emit(assign, $2, NULL, $$, -1, yylineno);
			}
		}
	| 	lvalue PLUS_PLUS{
			if(is_func == 1){
					fprintf(stderr, "%s is function, (++) is illegal action!\n", function_name);
					exit(1);
				}
			expr* temp_expr;
			temp_expr = newexpr(assignexpr_e);
			temp_expr -> sym = newtemp();
			if($1 -> type == tableintem_e){
				expr* val = emit_iftableitem($1);
				emit(assign, val, NULL, temp_expr, -1, yylineno);
				emit(add, value, newexpr_constint(1), val, -1, yylineno);
				emit(tablesetelem, $1 -> index, val, $1, -1, yylineno);
			}
			else{
				emit(assign, $1, NULL, temp_expr, -1, yylineno);
				emit(add, $1, newexpr_constint(1), $1, -1, yylineno);
			}
			$$ = $1; //
		}
	| MINUS_MINUS lvalue {
		if(is_func == 1){
			fprintf(stderr, "%s is function, (--) is illegal action!\n", function_name);
			exit(1);
		}
		if($2 -> type == tableitem_e){
			$$ = emit_iftableintem($2);
			emit(sub, $$, newexpr_constint(1), $$, -1, yylineno);
			emit(tablesetelem, $$, $2 -> index, -1, yylineno); //
		}
		else{
			expr* temp_expr;
			temp_expr = newexpr(arithexpr_e);
			temp_expr -> sym = newtemp();
			emit(sub, $2, newexpr_constint(1), $$, -1, yylineno);
			$$ = temp_expr;
			emit(assign, $2, NULL, $$, -1, yylineno);
		}
		}
	| lvalue MINUS_MINUS {
		if(is_func == 1){
			fprintf(stderr, "%s is function, (--) is illegal action!\n", function_name);
			exit(1);
		}
		expr* temp_expr;
		temp_expr = newexpr(assignexpr_e);
		temp_expr -> sym = newtemp();
		if($1 -> type == tableintem_e){
			expr* val = emit_iftableitem($1);
			emit(assign, val, NULL, temp_expr, -1, yylineno);
			emit(sub, value, newexpr_constint(1), val, -1, yylineno);
			emit(tablesetelem, $1 -> index, val, $1, -1, yylineno);
		}
		else{
			emit(assign, $1, NULL, temp_expr, -1, yylineno);
			emit(sub, $1, newexpr_constint(1), $1, -1, yylineno);
		}
			$$ = $1;
		}
	| primary{$$ = $1;}
	;
	
	
assignexpr: lvalue {if(is_func == 1){ fprintf(stderr, "%s is function, assignment is illegal action!\n", function_name);
						is_func = 0;}
			}
			ASSIGN expr
	;


primary: lvalue{$$ = emit_iftableintem($1);}
	| call{$$ = $1;} //
	| objectdef{$$ = $1;}
	| LEFT_PARENTHESIS funcdef RIGHT_PARENTHESIS{
		$$ = newexpr(programfunc_e);
	 	$$ -> sym = $2 -> sym;
	}
	| const{$$ = $1;}
	;
	
const: NIL
	| TRUE 
	| FALSE 
	| STRING
	| REALCONST 
	| INTEGER
	;
	 
lvalue: ID {
		lookup_result = lookup_entry($1, GLOBAL, scope);
		SymbolTableEntry* entry;
		if(lookup_result == 2){
			entry = new_entry($1, GLOBAL, scope, yylineno, var_s, currscopespace(), currscopeoffset());
			insert(new_entry(entry); //search in scope 0, find nothing, insert in current scope (0)
		}
		else if(lookup_result == 3){
			entry = new_entry($1, LOCL, scope, yylineno, var_s, currscopespace(), currscopeoffset());
			insert(entry); //find an undeclared variable, insert in current scope
		}	
		is_func = is_function($1, scope);
		if(is_func) function_name = $1;
		inccurrscopeoffset();
		//+++?
		$$ = lvalue_expr(entry);
	}
	| LOCAL ID {
		lookup_result = lookup_entry($2, LOCL, scope);
		SymbolTableEntry* entry;
		if(lookup_result == 2){
			entry = new_entry($2, GLOBAL, 0, yylineno, var_s, currscopespace(), currscopeoffset());
			insert(entry); //find an a local variable in global scope, we ignore the local an insert it as global
		}
		else if(lookup_result == 3){
			entry = new_entry($2, LOCL, scope, yylineno, var_s, currscopespace(), currscopeoffset());
			insert(entry); //find a local variable, insert
		}
		is_func = is_function($2, scope);
		if(is_func){
			function_name = $2;
			fprintf(stderr, "Warning, %s is function!\n",);	
		}
		$$ = lvalue_expr(entry);
	}
	| COLON_COLON ID {
		lookup_result = lookup_entry($2, GLOBAL, scope); // (::) global reference, check if is in global scope
		is_func = is_function($2, scope);
		if(is_func) function_name = $2;
		//?????
	}
	| member{$$ = $1;}
	;


member: lvalue FULL_STOP ID{$$ = member_item($1, $3);}
	| 	lvalue LEFT_SQUARE_BRACKET expr RIGHT_SQUARE_BRACKET{
			$1 = emit_iftableitem($1);
			expr* tableitem;
			tableitem = newexpr(tableitem_e);
			tableitem -> sym = $1 -> sym;
			$$ = tableitem;
			$$ -> index = $3;
		}
	| 	call FULL_STOP ID{
			expr* temp_expr;
			temp_expr = newexpr(var_e);
			temp_expr -> sym = newtemp();
			emit(tablegetelem, $1, newexpr_conststring($3), temp_expr, -1, yylineno);
			$$ = temp_expr;	
		}
	| 	call LEFT_SQUARE_BRACKET expr RIGHT_SQUARE_BRACKET{
			expr* temp_expr;
			temp_expr = newexpr(var_e);
			temp_expr -> sym = newtemp();
			emit(tablegetelem, $1, $3, temp_expr, -1, yylineno);
			$$ = temp_expr;	
		}
	;
	
	
call: call LEFT_PARENTHESIS elist RIGHT_PARENTHESIS{
		$$ = make_call($1, $3);
	}
	| lvalue callsuffix{
		$1 = emit_iftableitem($1);
		if($2 -> method == 1){
			expr* t = $1;
			$1 = emit_iftableitem(member_item(t, $2 -> name));
			$2 -> elist -> next = t; //diff
		}
		$$ = make_call($1, $2 -> elist);
	}	
	| LEFT_PARENTHESIS funcdef RIGHT_PARENTHESIS LEFT_PARENTHESIS elist RIGHT_PARENTHESIS{
		expr* func = newexpr(programfunc_e);
		func -> sym = $2;
		$$ = make_call(func, $5);
	}
	;
	
	
callsuffix: normcall{
		$$ = $1;
	}
	| methodcall{
		$$ = $1;
	}
	;
	
	
normcall: LEFT_PARENTHESIS elist RIGHT_PARENTHESIS{
		call* norm_call;
		norm_call = malloc(sizeof(struct call));
		norm_call -> elist = $2;
		norm_call -> method = 0;
		norm_call -> name = NULL;
	}
	;
	

methodcall: FULL_STOP_FULL_STOP ID LEFT_PARENTHESIS elist RIGHT_PARENTHESIS{ //+++++
		call* method_call;
		method_call = malloc(sizeof(struct call)); 								//sizeof(struct call) || sizeof(call)??
		method_call -> elist = $4;
		method_call -> method = 1;
		method_call -> name = $2;
		$$ = method_call; 
	}
	;
	
	
elist:	expr expr_comma{
		if(!$2){
			$$ = (expr*)malloc(sizeof(expr));
			$$ -> sym = (struct symbol*)malloc(sizeof(struct symbol));
			$1 -> next = NULL;
			$$ = $1;
		}
		else{
			expr* temp_expr = $2;
			while(temp_expr -> next) temp_expr = temp_expr -> next;
			temp_expr -> next - $1;
			$$ = reverse($2); 														// make ftriaksw reverse
		}
	}
	|{$$ = NULL;}
	;
	

expr_comma:	COMMA expr expr_comma{
			expr* temp_expr = $3;
			if(temp){
				while(temp_expr -> next) temp_expr = temp_expr -> next;
				temp -> next = $2;
				$$ = $3;
			}
			else $$ = $2;
		}
	|{$$ = NULL;}
	;
	
	
objectdef: LEFT_SQUARE_BRACKET elist RIGHT_SQUARE_BRACKET{
		int c = 0;
		expr* t = newexpr(newtable_e);
		t -> sym = newtemp();
		emit(tablecreate, t, NULL, NULL, -1, yylineno);
		struct expr* list = $2;
		while(list -> next != NULL){
			emit(tablesetelem, newexpr_constint(c++), list, t, -1, yylineno); 		//elegxos emit!!!!
			list = list -> next;
		}
		$$ = t;
	}
	| LEFT_SQUARE_BRACKET indexed RIGHT_SQUARE_BRACKET{
		expr* t = newexpr(newtable_e);
		t -> sym = newtemp();
		emit(tablecreate, t, NULL, NULL, -1, yylineno);
		struct expr* ind = $2;
		while(int -> next != NULL){
			emit(tablesetelem, ind, ind->index, t, -1, yylineno);
            temp = temp->next;
		}
		$$ = t;
	}
	;

indexed: indexedelem indexedelem_comma
	;
	
indexedelem_comma: indexedelem_comma COMMA indexedelem
	;
	
indexedelem: LEFT_CURLY_BRACKET expr COLON expr RIGHT_CURLY_BRACKET 
	;
	
	
block: LEFT_CURLY_BRACKET {scope++;} stmts RIGHT_CURLY_BRACKET {hide(scope); scope--;}
	;					

funcname: ID{
		$$ = $1;
	}
	| /*empty*/ {
		sprintf(my_func_name, "_%d", temp);
		temp++;
		$$ = my_func_name;
	}
	;

funcdef: N funcprefix funcargs {
			push(loopcounterstack, loopcounter); 
			loopcounter = 0;
		} 
		funcbody {loopcounter = pop(loopcounterstack);} {
		//+++
		exitscopespace();
		$1.value -> funcVal = $3;
		int offset = pop(scopeoffsetstack);
		restorecurrscopeoffset(offset);
		$$ = $1;
		emit(funcend, $1, NULL, NULL); 
	}
	;
	
funcprefix: FUNCTION funcname{
		lookup_result = lookup_entry($2, USERFUNC, scope);
		if(lookup_result == 0){
			SymbolTableEntry* entry;
			entry = new_entry($2, USERFUNC, scope, yylineno, programfunc_s, currscopespace(), -1; //gt offset = 1???
			insert(entry);
			$$ = entry;
			$$ -> value.funcVal -> iaddress = nextQuadlabel();
			expr* prog_func = newexpr(programfunc_e);
			prog_func -> sym = temp;
			emit(funcstart, prog_func, NULL, NULL, -1, yylineno); //gt label -1?
			push(&scopeoffsetstack, currscopeoffset());
			enterscopespace();
			resetformalargoffset();
		}
		else //error
	};
	
funcargs: LEFT_PARENTHESIS {scope++;} idlist RIGHT_PARENTHESIS{scope++;}{
		enterscopespace();
		resetfunctionlocaloffset();
	};
	
funcbody: block{
		$$ = currscopeoffset(); //????? DIAFORETIKO
		exitscopespace();
	};	

idlist: ID {
		lookup_result = lookup_entry($1, FORMAL, scope);
		if(lookup_result == 3) insert(new_entry($1, FORMAL, scope, yylineno));
	} 
	| ID_comma
	;

ID_comma: ID_comma COMMA ID {
		lookup_result = lookup_entry($3, FORMAL, scope);
		if(lookup_result == 3) insert(new_entry($3, FORMAL, scope, yylineno));
	}
	;
	
ifprefix: IF LEFT_PARENTHESIS expr RIGHT_PARENTHESIS{
		emit(if_eq, $3, newexpr_constbool(1), NULL, nextQuad() + 2, yylineno);
		$$ = nextQuad();
		emit(jump,NULL,NULL,NULL,-1,yylineno);
	}
	;
	
elseprefix: ELSE{
		$$ = nextQuad();
		emit(jump, NULL, NULL, NULL, -1, yylineno);
	}
	;
	
ifstmt: ifprefix stmt{ //+++
		patchlabel($1, nextQuad());
	
	}
	| ifprefix stmt elseprefix stmt{
		patchlabel($1, $3 + 1);
        patchlabel($3,nextQuad());
	}
	;
	
loopstmt: {++loopcounter;} stmt {--loopcounter;}{
		$$ = $2;
	}
	;
	
whilestmt: whilestart whilecond stmt{
		emit(jump, NULL, NULL, NULL, $1, yylineno);
		patchlabel($2, nextQuad());
		backpatch($3 -> breaklist, nextQuad());
		backpatch($3 -> contlist, $1);
		$$ = $3;
	} 
	;
	
whilestart: WHILE{
		$$ = nextQuad();
	}
	;
	
whilecond: LEFT_PARENTHESIS expr RIGHT_PARENTHESIS{
		emit(if_eq, $2, newexpr_constbool(1), NULL, nextQuad() + 2, yylineno);
		$$ = nextQuad();
		emit(jump, NULL, NULL, NULL, -1, yylineno);
	}
	;
	
forprefix: FOR LEFT_PARENTHESIS elist SEMICOLON M expr SEMICOLON{
		$$[0] = $5;
		$$[1] = nextQuad();
		emit(if_eq, $6, newexpr_constbool(1), NULL, -1, yylineno);
	}
	;
	
forstmt: forprefix N elist RIGHT_PARENTHESIS N loopstmt N{
		patchlabel($1[q], $5 + 1);
		patchlabel($2, nextQuad());
		patchlabel($5, $1[0]);
		patchlabel($7, $2 + 1);
		backpatch($6 -> breaklist, nextQuad());
		backpatch($6 -> contlist, $2 + 1);
		$$ = $6;
	}
    ;
	
returnstmt: RETURN expr SEMICOLON{
		$$ = malloc(sizeof(struct expr));
        emit(ret, NULL, NULL, NULL, -1, yylineno);
        emit(jump, NULL, NULL, NULL,-1, yylineno);
    }
	| RETURN SEMICOLON{
		emit(ret, $2, NULL, NULL, -1, yylineno);
		emit(jump,NULL,NULL,NULL,-1,yylineno);
        $$ = $2;
	}
	;

%%

int yyerror (char* yaccProvidedMessage)
{
	fprintf(stderr, "%s: at line %d, before token: '%s'\n", yaccProvidedMessage, yylineno, yytext);
	fprintf(stderr,"INPUT INVALID.\n");
}


int main(int argc, char **argv) 
{
	int x, i;  
	if(argc > 1){
		if(!(yyin = fopen(argv[1], "r"))){
			x = fprintf(stderr,"Cannot open %s\n", argv[1]);
			return 1;
		}
	}
	else{
		yyin = stdin;
	}	
	for(i = 0; i < HASH_TABLE_SIZE; i++){
		SymbolTable[i] = malloc(sizeof(SymbolTableEntry));
	}
  	for(i = 0; i < MAX_SCOPE_SIZE; i++){
		ScopeList[i] = malloc(sizeof(SymbolTableEntry));
		ScopeList[i] = NULL;
	}
	insert(new_entry("print",LIBFUNC,0,0));
	insert(new_entry("input",LIBFUNC,0,0));
	insert(new_entry("objectmemberkeys",LIBFUNC,0,0));
	insert(new_entry("objecttotalmembers",LIBFUNC,0,0));
	insert(new_entry("objectcopy",LIBFUNC,0,0));
	insert(new_entry("totalarguments",LIBFUNC,0,0));
	insert(new_entry("argument",LIBFUNC,0,0));
	insert(new_entry("typeof",LIBFUNC,0,0));
	insert(new_entry("strtonum",LIBFUNC,0,0));
	insert(new_entry("sqrt",LIBFUNC,0,0));
	insert(new_entry("cos",LIBFUNC,0,0));
	insert(new_entry("sin",LIBFUNC,0,0));
	yyparse();
	print();
	return 0;
}
