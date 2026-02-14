#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>
#include <unistd.h>
#include "alpha.h"


extern unsigned int programVarOffset;
programVarOffset=0;
extern unsigned int functionLocalOffset;
functionLocalOffset = 0;
extern unsigned int formalArgOffset;
formalArgOffset=0;
extern unsigned int scopeSpaceCounter;
scopeSpaceCounter = 1;

extern quad* quads; 
quads=NULL; //(quad*) 0;

extern unsigned int total;
total= 0;

extern unsigned int currQuad; 
currQuad= 0;

type* create_type(type* t,symbol s,stmt statement,call_t callType,expr expression,expr_t expressionType, char *string,int integer,double realNumber,quad q,const_t constType,iopcode op){
	assert(t!=NULL);
	t->s=s;
	t->statement=statement;
	t->callType=callType;
	t->expression=expression;
	t->expressionType=expressionType;
	t->string=string;
	t->integer=integer;
	t->realNumber=realNumber;
	t->q=q;
	t->constType=constType;
	t->op=op;
	return t;
}


// expand the quad structure size to insert a newly found quad
extern void expand(){
	assert(total == currQuad);
	quad* p = (quad*)malloc(NEW_SIZE);
	if(quads){
		memcpy(p, quads, CURR_SIZE);
		free(quads);
	}
	quads = p; //giati?
	total += EXPAND_SIZE;
}


void emit(iopcode op, expr* arg1, expr* arg2, expr* result, unsigned label, unsigned line){
	if(currQuad == total) expand();
	quad* p = quads + currQuad++;
	p -> arg1 = arg1;
	p -> arg2 = arg2;
	p -> result = result;
	p -> label = label;
	p -> line = line;
}

scopespace_t currscopespace(void){
	if(scopeSpaceCounter == 1) return programVar;
	else{
		if(scopeSpaceCounter % 2 == 0) return formalarg;
		else return functionlocal;
	}
}

unsigned currscopeoffset(void){
	switch(currscopespace()){
		case programvar:
			return programVarOffset;
		case functionlocal:
			return functionLocalOffset;
		case formalarg:
			return formalArgOffset;
		default: 
			assert(0);
	}
}

void incurrscopeoffset(void){
	switch(currscopespace()){
		case programvar:
			++programVarOffset;
			break;
		case functionlocal:
			++functionLocalOffset;
			break;
		case formalarg:
			++formalArgOffset;
			break;
		default:
			assert(0);
	}
}

void enterscopespace(void){
	++scopeSpaceCounter;
}

void exitscopespace(void){
	assert(scopeSpaceCounter > 1);
	--scopeSpaceCounter;
}

void resetformalargsoffset(void){
	formalArgOffset = 0;
}

void resetfunctionlocaloffset(void){
	functionLocalOffset = 0;
}

void restorecurrscopeoffset(unsigned n){
	switch(currscopespace()){
		case programvar:
			programVarOffset = n;
			break;
		case functionlocal:
			functionLocalOffset = n;
			break;
		case formalarg:
			formalArgOffset = n;
			break;
		default:
			assert(0);
	}
}

unsigned nextquadlabel(void){
	return currQuad;
}


expr* lvalue_expr(SymbolTableEntry* sym){
	assert(sym);
	expr* e = (expr*)malloc(sizeof(expr));
	memset(e, 0, sizeof(expr));
	e -> next = (expr*) 0;
	e -> sym =sym; //(SymbolTableEntry*) GIATI, IDIOS TYPOS EINAI!
	switch(sym -> type){
		case var_s:
			e -> type = var_e;
			break;
		case programfunc_s:
			e -> type = programfunc_e;
			break;
		case libraryfunc_s:
			e -> type = libraryfunc_e;
			break;
		default:
			assert(0);
	}
	return e;
}

expr* member_item(expr* lv, char* name){
	lv = emit_iftableitem(lv);
	expr* ti = newexpr(tableitem_e);
	ti -> sym = lv -> sym;
	ti -> index = newexpr_conststring(name);
	return ti;
}

expr* newexpr(expr_t t){
	expr* e = (expr*)malloc(sizeof(expr));
	memset(e, 0, sizeof(expr));
	e -> type = t;
	return e;
}

expr* emit_iftableitem(expr* e){
	if(e -> type != tableitem_e) return e;
	else{
		expr* result = newexpr(var_e);
		result -> sym = newtemp();
		emit(tablegetelem, e, e -> index, result,e->label,e->line); 
		return result;
	}
}

expr* newexpr_conststring(char* s){
	expr* e = newexpr(conststring_e);
	e -> strConst = strdup(s);
	return e;
}

expr* make_call(expr* lv, expr* reversed_elist){
	expr* func = emit_iftableitem(lv);
	while(reversed_elist){
		emit(param, reversed_elist, NULL, NULL,reversed_elist->label,reversed_elist->line);
		reversed_elist = reversed_elist -> next;
	}
	emit(call, func, NULL, NULL,func->label,func->line);
	expr* result = newexpr(var_e);
	result -> sym = newtemp();
	emit(getretval, NULL, NULL, result,result->label,result->line);
	return result;
}

expr *newexpr_constint(int i){
  expr *e = newexpr(constint_e);
  e->intConst = i;
  return e;
}

// function to throw comp error 
void comperror(char* format, const char* cont){
	fprintf(stderr,"%s%s\n", format, cont);
}

// function to check that an expression is numeric - throws error otherwise
void check_arith(expr* e, const char* context){
	if(e -> type == constbool_e || e -> type == conststring_e || e -> type == nil_e || e -> type == newtable_e || e -> type == programfunc_e || e -> type == libraryfunc_e || e -> type == boolexpr_e){
		comperror("Illegal expr used in:", context);
	}	
}


extern int tempcounter;
char* newtempname() { return "_t" + tempcounter; }
void resettemp() { tempcounter = 0; }



symbol newtemp() {
	char* name = newtempname();
	symbol sym = lookup(name, currscope());
	if (sym==NULL /* == nil*/)
		return newsymbol(name);
	else
		return sym;
}



unsigned int istempname(char* string){
	return *string == '-';
}

unsigned int istempexpr(expr* e){
	return e -> sym && istempname(e -> strConst); //sym->name
}

void patchlabel(unsigned quadNo, unsigned label){
	assert(quadNo < currQuad && !quads[quadNo].label);
	quads[quadNo].label = label;
}

expr* newexpr_constbool(unsigned int b){
	expr* e = newexpr(constbool_e);
	e -> boolConst = b; 
	return e;
}

unsigned nextQuad(void){
	return currQuad;
}


void make_stmt (stmt* s){
	s->breakList = s->contList = 0;
}

int newlist (int i){
	quads[i].label = 0; 
	return i;
}

/*int mergelist (int l1, int l2) {
	if(!l1) return l2;
	else
	if(!l2) return l1;
	else{
		int i = l1;
		while (quads[i].label) i = quads[i].label;
		quads[i].label = l2;
		return l1;
	}
}*/

void patchlist (int list, int label) {
	while (list){
		int next = quads[list].label;
		quads[list].label = label;
		list = next;
	}
}

// merge apo tis 2 listes

incompleteList* merge(incompleteList* list1, incompleteList* list2){
	incompleteList* l1;
	incompleteList* l2;
	incompleteList* newList; 
	incompleteList* new_quad;
	l1 = list1;
	l2 = list2;
	newList = NULL;
	while(l1 -> next != NULL){ // l1
		new_quad = malloc(sizeof(incompleteList));
		new_quad -> quad = l1 -> quad;
		new_quad -> next = NULL;
		if(newList == NULL) newList = new_quad;
		else{
			new_quad -> next = newList;
			newList = new_quad;
		}
		l1 = l1 -> next;
	}
	while(l2 -> next != NULL){
		new_quad = malloc(sizeof(incompleteList));
		new_quad -> quad = l1 -> quad;
		new_quad -> next = NULL;
		if(newList == NULL) newList = new_quad;
		else{
			new_quad -> next = newList;
			newList = new_quad;
		}
		l2 = l2 -> next;
	}
	return newList;
}

// incompleteList ylopoiisi

incompleteList* makelist(unsigned quad){
	incompleteList l;
	l=(incompleteList)malloc(sizeof(incompleteList));
	l->quad=quad;
	return l;

}



void backpatch(incompleteList* list, quad q)
{
	list->next->q=q;	// protimotero me next apoti list->q=q;
}


