

/* struct to hold all the information about the tokens. 
Each node is a newly identified token from the input file.
numline = number of line where lies the given token
numtoken = token number
content = ponter to token content
type = pointer to token type
alpha_type = 
*/

//for phase4-5
//#define AVM_ENDING_PC codeSize

// from phase2lib.h

#define HASH_TABLE_SIZE 1009
#define MAX_SCOPE_SIZE 100


// from quad.h

#define EXPAND_SIZE 1024
#define CURR_SIZE (total*sizeof(quad))
#define NEW_SIZE (EXPAND_SIZE*sizeof(quad) + CURR_SIZE)

typedef struct alpha_token_t Token;

struct alpha_token_t {
	unsigned int numline;
	unsigned int numToken;
	char *content;
	char *type;
	char *alpha_type;
  	struct alpha_token_t *next;
};

Token* head;

void addToken(unsigned int line_number, unsigned int token_number, char* realcontent, char* realtype, char* real_alpha_token);

void printTokens();

// end of quad.h





// from myfunctions.h 

char* firstmerge(const char* s1, const char* s2);


int find_new_lineno(char* string);

char* block_comment_lines(int first_line, int line);

int string_proccessing(char* text, int line, int token);

// end of myfunctions.h



// from mystack.h

struct SymtableStackNode{
	int line;
	struct SymtableStackNode* next;
};

struct SymtableStackNode* root ;

struct SymtableStackNode* newNode(int l);

int isEmptyphase2(struct SymtableStackNode* r);

void pushphase2(struct SymtableStackNode** root, int l);

void popphase2(struct SymtableStackNode** r);

void peekphase2(struct SymtableStackNode* r);

// end of mystack.h


// from phase2lib.h


enum Type{GLOBAL, LOCL, FORMAL, USERFUNC, LIBFUNC};

typedef struct Variable{
        const char* name;
        unsigned int scope;
        unsigned int line;
}Variable;

typedef struct Arguments{
        char* name;
        struct Arguments* next;
}Arguments;

typedef struct Function{
        const char* name;
        Arguments *args;
        unsigned int scope;
        unsigned int line;
}Function;

typedef struct SymbolTableEntry{
        unsigned int isActive;
        union{
                Variable* varVal;
                Function* funcVal;
        }value;
        enum Type type;
        struct SymbolTableEntry* next;
        struct SymbolTableEntry* next_in_scope;
}SymbolTableEntry;

int get_hash(const char* token);

SymbolTableEntry *new_entry(const char *name, enum Type type, unsigned int scope, unsigned int line);

void insert(SymbolTableEntry *entry);

void print();

char* lookup_selected_scope(const char* name, unsigned int scope);

int is_accessible(const char* name, unsigned int c_scope); //unsigned int  d_scope

int is_lib_symbol(const char* name);

int is_function(const char* name, unsigned int scope);

void hide(unsigned int scope);

int lookup_entry(const char* name, enum Type type, unsigned int current_scope);

// end of phase2lib.h



// p3stack.h
// Contains the stack used in Phase 3 to produce the intermediate code. 


typedef struct StackNode{
	unsigned int offset;
	struct StackNode* next;
}StackNode;

int isEmpty(StackNode* r);

void push(StackNode** root, int offset);

unsigned int pop_and_top(StackNode** root);


// end of p3stack.h








//from quad.h



unsigned int programVarOffset;
unsigned int functionLocalOffset;
unsigned int formalArgOffset;
unsigned int scopeSpaceCounter;
int tempcounter;


//========enum ====================



typedef enum expr_t{
	var_e,
	tableitem_e,
	programfunc_e,
	libraryfunc_e,
	arithexpr_e,
	boolexpr_e,
	assignexpr_e,
	newtable_e,
	constint_e,
	constreal_e,
	constbool_e,
	conststring_e,
	nil_e
}expr_t;

typedef enum scopespace_t{
	programvar,functionlocal,formalarg
}scopespace_t;

typedef enum symbol_t{
	var_s, programfunc_s, libraryfunc_s
}symbol_t;

typedef enum iopcode{
	assign, add, sub, mul, division, mod, 											//div is already a library function 
	uminus, and, or, not, if_eq, if_noteq,
	if_lesseq, if_greatereq, if_less, if_greater, call, param,
	ret, getretval, funcstart, funcend, tablecreate, tablegetelem,
	tablesetelem
}iopcode;

typedef enum const_t{
nil_t, true_t, false_t, number_t, string_t
}const_t;


//===================structs====


typedef struct quad{
	iopcode op;
	struct expr* result;
	struct expr* arg1;
	struct expr* arg2;
	unsigned label;
	unsigned line;
}quad;



typedef struct incompleteList{
        int quad;
		quad q;
		struct incompleteList* next;
}incompleteList;



typedef struct expr{
	expr_t type;
	union{
		int intVal;
		char* stringVal;
		double doubleVal;
	}val;
	SymbolTableEntry* sym;
	struct expr* index;
	double numConst;
	int intConst;
	char* strConst;
	unsigned char boolConst;
	struct expr* next;
	incompleteList *truelist;
  	incompleteList *falselist;
  	incompleteList *breaklist;
  	incompleteList *contlist;
  	incompleteList *retlist;
	unsigned offset; 						//offset in scope space	
	unsigned scope; 						//scope value
	unsigned label;
	unsigned line; 							//source line of declaration
	//+++
}expr;


typedef struct call{
  expr *elist;
  unsigned char* method;
  char* name;
}call_t;


typedef struct stmt{
	int breakList, contList;
}stmt;



typedef struct symbol
{
symbol_t type; 				//Error here, BUT WHY?! Maybe it should be a string, after all.
char* name; 				//dynamic string
scopespace_t space; 		//originating scope space
unsigned offset; 			//offset in scope space
unsigned scope; 			//scope value
unsigned line; 				//source line of declaration
}symbol;

typedef struct type{
	symbol s;
	stmt statement;
	call_t callType;
	expr expression;
	expr_t expressionType;
	char *string;
	int integer;
	double realNumber;
	quad q;
	const_t constType;
	iopcode op;
}type;


quad* quads;

type* create_type(type* t,symbol s,stmt statement,call_t callType,
					expr expression,expr_t expressionType,char *string,
					int integer,double realNumber,quad q,const_t constType,iopcode op);

//quad Quads[total];

unsigned int total;

unsigned int currQuad;

void expand();

void emit(iopcode op, expr* arg1, expr* arg2, expr* result, unsigned label, unsigned line);

scopespace_t currscopespace(void);

unsigned currscopeoffset(void);

void incurrscopeoffset(void);

void enterscopespace(void);

void exitscopespace(void);

void resetformalargsoffset(void);

void resetfunctionlocaloffset(void);

void restorecurrscopeoffset(unsigned n);

unsigned nextquadlabel(void);

expr* member_item(expr* lv, char* name);

expr* newexpr(expr_t t);

expr* emit_iftableitem(expr* e);

expr* newexpr_conststring(char* s);

expr* make_call(expr* lv, expr* reversed_elist);

expr *newexpr_constint(int i);

void comperror(char* format, const char* cont);

void check_arith(expr* e, const char* context);

unsigned int istempname(char* s);

unsigned int istempexpr(expr* e);

void patchlabel(unsigned quadNo, unsigned label);

expr* newexpr_constbool(unsigned int b);

unsigned nextQuad(void);

void make_stmt(stmt* s);

int newlist(int i);

void patchlist (int list, int label);

incompleteList* merge(incompleteList* list1, incompleteList* list2);

incompleteList* makelist(unsigned quad);

void backpatch(incompleteList* list, quad q);

char* newtempname();

void resettemp();

symbol newtemp();


//phase4-5

/* typedef struct vmarg { 
struct vmarg type; 
unsigned val;
}vmarg;

unsigned consts_newstring(char* s); unsigned consts_newnumber(double n); 
unsigned libfuncs_newused(char* s); 
void make_operand(expr* e, vmarg* arg) {
	switch(e->type) {
	//All those below use a variable for storage
	case var_e:
	case tableitem_e:
	case arithexpr_e:
	case boolexpr_e:
	case newtable_e: {
		arg->val=e->sym->offset;
		switch(e->sym->space) {
			case programvar: arg->type-global_a; break;
			case fnctionlocal: arg->type-local_a; break;
			case formalarg: arg->type-formal_a; break;
			default: assert(0);
		}
}
//Constants 
case constbool_e: { arg->val=e->boolConst; arg->type=bool_s; break;
}
case conststring_e: { arg->val=consts_newstring(e->strConst); arg->type=string_a; break;
}
case constnum_e: { arg->val=consts_newnumber(e->numConst); arg->type=number_a; break;
}
case nil_e: arg->type=nil_a; break; 
//Functions 

case programfunc_e: { arg->type=userfunc_a; arg->val=e->sym->taddress; break;
}
case libraryfunc_e: { arg->type=libfunc_a; arg->val=libfncs_newused(e->sym->name); break;
}
default: assert(0);
}
}


struct instruction { 
vmopcode opcode; 
vmarg result; 
vmarg arg1; 
vmarg arg2; 
unsigned srcLine;
};

typedef struct incomplete_jump { 
unsigned instrNo; //the jump instruction number 
unsigned iaddress; //i-code jump-target address 
struct incomplete_jump* next; //a trivial linked list
}incomplete_jump;

incomplete_jump* ij_head=(incomplete_jump*)0; 
unsigned ij_total=0; 

void add_incomplete_jump(unsigned instrNo, unsigned iaddress); 
struct avm_table;
 
typedef struct avm_memcell { 
avm_memcell_t type; 
union { 
double numVal; 
char* strVal; 
unsigned char boolVal; 
avm_table* tableVal; 
unsigned funcVal; 
char* libfuncVal;
}data; 
}avm_memcell;

typedef enum avm_memcall_t 
{ number_m=0, string_m=1, bool_m=2, table_m=3, userfunc_m=4, libfunc_m=5, nil_m=6, undef_m=7
}avm_memcell_t;

avm_memcell* avm_translate_operand(vmarg* arg, avm_memcell* reg) {
	switch(arg->type) {
		case global_a: return &stack[AVM_STACKSIZE-1-arg->val];
		case local_a: return &stack[topsp-arg->val];
		case forlam_a: return &stack[topsp+AVM_STACKENV_SIZE+1+arg->val];
		case retval_a: return &retval;

		case number_a: {
			reg->type=number_m;
			reg->data.strVal=consts_getnumber(arg->val);
			return reg;
		}

		case string_a: {
			reg->type=string_a;
			reg->data.strVal=consts_ getstring(arg->val);
			return reg;
		}

		case bool_a: {
			reg->type=bool_m;
			reg->data.boolVal=arg->val;
			return reg;
		}
	
		case nil_a: reg->type=nil_m; return reg;

		case userfunc_a: {
			reg->type=libfunc_m;
			reg->data.libfuncVal=libfunc_getused(arg->val);
			return reg;
		}
	}
}

typedef void (*generator_func_t)(quad*);
 
generator_func_t generators[] = { generate_ADD, generate_SUB, generate_MUL, 
generate_DIV, generate_MOD, generate_NEWTABLE, 
generate_TABLEGETELEM, generate_TABLESETELEM, 
generate_ASSIGN, generate_NOP, 
generate_JUMP, generate_IF_EQ, 
generate_IF_NOTEQ, generate_IF_GREATER, generate_IF_GREATEREQ, 
generate_IF_LESS, generate_IF_LESSEQ, generate_NOT, generate_OR, 
generate_PARAM, generate_CALL, 
generate_GETRETVAL, generate_FUNCSTART, generate_FUNCEND, generate_RETURN
};

void generate(void){ 
unsigned i=0; 
	for(i=0;i<total;++i) 
		(*generators[quads[i].op])(quads+i); 

}

execute_func_t executeFuncs[]={
execute_assign,
execute_add,
execute_sub,
execute_mul,
execute_div,
execute_mod,
execute_uminus,
execute_and,
execute_or,
execute_not,
execute_jeq,
execute_jne,
execute_jle,
execute_jge,
execute_jlt,
execute_jgt,
execute_call,
execute_pusharg,
execute_funcenter,
execute_funcexit,
execute_newtable,
execute_tablegetelem,
execute_ablesetelem,
execute_nop
};

unsigned int executionFisished=0; //not char, better use int
unsigned pc=0;
unsigned currLine=0;
unsigned codeSize=0;
instruction* code=(instruction*)0;

void execute_cycle() {


if(executionFinished) return;
else if(pc==AVM_ENDING_PC) {
	executionFinished=1;
	return;
	}

else {

	assert(pc<AVM_EDNING_PC);
	instruction* instr=code+pc;
	assert(instr->opcode>=0&&instr->opcode<=AVM_MAX_INSTRUCTIONS);
	if(instr->srcLine) currLine=instr->srcLine;
	unsigned oldPC=pc;
	(*executeFuncs[instr->opcode])(instr);
	if(pc==oldPC) ++pc;

     }

}


void avm_callsaveenvironment() {

avm_push_envvalue(totalActuals);
avm_push_envvalue(pc+1);
avm_push_envvalue(top+totalActuals+2);
avm_push_envvalue(topsp);

}

void execute_funcexit(instruction* unused) {

unsigned oldTop=top;
top=avm_get_envvalue(topsp+AVM_SAVEDTOP_OFFSET);
pc=avm_get_envvalue(topsp+AVM_SAVEDPC_OFFSET);
topsp=avm_get_envvalue(topsp+AVM_SAVEDTOPSP_OFFSET);

while(ldTop++<=top) //intentionally ignoring first
avm_memcallclear(&stack[oldTop]);

}

typedef void (*library_func_t)(void);
library_func_t avm_getlibraryfunc(char* id); //typical hashing

void avm_calllibfunc(char* id)
{

	library_func_t f=avm_getlibraryfunc(id);
	if(!f) {
		avm_error("Unsupported lib func '%s' called!\n,id); 
		//or use printf here
		executionFinished=1;
	}
	else {
		//exit and enter functons are called manually!
		topsp=top;
		totalActuals=0;
		(*f)();
		if(@executionFinished) execute_funcexit((instruction*)0);
		//return sequence

	}


} 

unsigned avm_totalactuals() {
return avm_get_envvalue(topsp+AVM_NUMACTUALS_OFFSET);
}


avm_memcell* avm_getactual(unsigned i) {

assert(i<avm_totalactuals());
return &stack[topsp +AVM_STACKENV_SIZE+1+i];

}


//Implementation of the library function print
void libfunc_print(void)
{

	unsigned n=avm_totalactuals();
	unsigned i=0;
	for(i=0;i<n;++i) {
		char* s=avm_tostring(avm_getactual(i));
		puts(s);
		free(s);
	}


}


//With the following every lib function is anually
//added in the VM lib function resolution map

void avm_registerlibfunc(char* id, library_func_t addr);

void libfunc_typeof() {

unsigned n=avm_totalactuals();
if(n!=1) avm_error("One argument (not %d) expected in 'typeof'!\n",n);
else {

//that's howa lib function returns a result
//It only has to set the retval register!
avm_memcell(&reatval);
retval.type=string_m;
retval.data.strVal=strdup(typeStrings[avm_getactual(0)->type]);
}

}

void libfunc_totalarguments() {

/get topsp of previous record

unsigned p_topsp=avm_get_envvalue(topsp+AVM_SAVEDTOPSP_OFFSET);
avm_memcellclear(&retval);

if(!p_topsp) {
	avm_error("'totalarguments' called outside a function!");
	retval.type=nil_m;
}
else {
//extract the number of actual arguments for the previous activation record
retval.type=number_m;
retval.data.numVal=avm_get_envvalue(p_topsp+AVM_NUMACTUALS_OFFSET);

}

}

#define execute_add execute arithmetic
#define execute_sub execute_arithmetic
#define execute_mul execute_arithmetic
#define execute_div execute_arithmetic
#define execute_mod execute_arithmetic

typedef double (*arithmetic_func_t)(double x, double y);

double add_impl(double x, double y) { return x+y;}
double sub_impl(double x, double y) { return x-y;}
double mul_impl(double x, double y) { return x*y;}
double div_impl(double x, double y) { return x/y;}
double mod_impl(double x, double y) { return ((unsigned)x)%((unsigned)y);}

//dispatcher just for arithmetics fucntions
arithmetic_func_t arithmeticFuncs[]= {
add_impl,
sub_impl,
mul_umpl,
div_impl,
mod_impl
};



void execute_arithmetic(instruction* instr)
{

	avm_memcell* lv=avm_translate_operand(&instr->result,(avm_memcell*)0);
	avm_memcell* rv1=avm_translate_operand(&instr->arg1,&ax);
	avm_memcell* rv2=avm_translate_operand()&instr->arg2,&bx);

	assert(lv&&(&stack[0]<=lv&&&stack[top]>lv||lv==&retval));
	assert(rv1&&rv2);

	if(rv1->type!=number_m||rv2->type!=number_m) {

		avm_error("Not a number in arithmetic!");
		executionFinished=1;
	}
	else {

		arithmetic_func_t op=arithmeticFuncs[instr->opcode-add_v];
		avm_memcellclear(lv);
		lv->type=number_m;
		lv->data.numVal=(*op)(rv1->data.numVal,rv2->data.numVal);
	}

} 


typedef unsigned char (*tobool_func_t)(avm_memcell*);

unsigned char number_tobool(avm_memcell* m) {return m->data.numVal!=0;}
unsigned char string_tobool(avm_memcell* m) {return m->data.strVal[0]!=0;}
unsigned char bool_tobool(avm_memcell* m) {return m->data.boolVal;}
unsigned char table_tobool(avm_memcell* m) {return 1;}
unsigned char userfunc_tobool(avm_memcell* m) {return 1;}
unsigned char libfunc_tobool(avm_memcell* m) {return 1;}
unsigned char nil_tobool(avm_memcell* m) {return 0;}
unsigned char undef_tobool(avm_memcell* m) {assert(0); return 0;}

tobool_func_t toboolFuncs[]={

number_tobool,
string_tobool,
bool_tobool,
table_tobool,
userfunc_tobool,
libfunc_tobool,
nil_tobool,
undef_tobool
};

unsigned char avm_tobool(avm_memcell* m) {

assert(m->type>=0 && m->type<undef_m);
return (*toboolFuncs[m->type])(m);

}

char* typeStrings[]={

"number",
"string",
"bool",
"table",
"userfunc",
"libfunc",
"nil",
"undef"

};

void execute_jeq(instruction* instr)
{

assert(instr->result.type==label_a);

avm_memcell* rv1=avm_translate_operand(&instr->arg1,&ax);
avm_memcell* rv2=avm_translate_operand(&instr->arg2,&bx);

unsigned char result=0;

if(rv1->type==undef_m || rv2->type==undef_m) 
			avm_error("'undef' involved in equality!");

else if(rv1->type!=rv2->type)
  			avm_error("%s==%s is illegal!",typeStrings[rv1->type],typeStrings[rv2->type]);

//else {} //equality check with dispatching
//TODO-Symplhrwse to implementaion. 
//Arkei na kanw dispatching ws pros rv1->type. 

if(!executionFinished&&result) pc=instr->result.val;


}

void execute_newtable(instruction* instr) {

avm_memcell* lv=avm_translate_operand(&instr->result,(avm_memcell*)0);
assert(lv&&(&stack[0]<=lv&&&stack[top]>lv||lv==&retval));
avm_memcellclear(lv);
lv->type=table_m;
lv->data.tableVal=avm_tablenew();
avm_tableincrefcounter(lv->data.tableVal);

}

avm_memcell* avm_tablegetelem(avm_table* table, avm_memcell* index);


void avm_tablesetelem(avm_table* table, avm_memcell* index, avm_memcell* content);

void execute_tablegetelem(instruction* instr) {

avm_memcell* lv=avm_translate_operand(&instr->result,(avm_memcell*)0);
avm_memcell* t=avm_translate_operand(&instr->arg1,(avm_memcell*)0);
avm_memcell* i=avm_translate_operand(&instr->arg2,(avm_memcell*)0);


assert(lv && (&stack[0]<=lv && &stack[top]>lv || lv==&retval));
assert(t && (&stack[0]<t && &stack[top]>t
assert(i);

avm_memcellclear(lv);
lv->type=nil_m;


if(t->type!=table_m) avm_error("Illegal use of type %s as table!",typeStrings[t->type]);

else {


	avm_memcell* content=avm_tablegetelem(t->data.tableVal,i);
	if(content) avm_assign(lv,content);
	else {
		char* ts=avm_tostring(t);
		char* is=avm_tostring(i);
		avm_warning("%s[%s] not found!",ts,is);
		free(ts);
		free(is);


	}

   }


}

void execute_tablesetelem(instruction* instr) {

avm_memcell* t=avm_translate_operand(&instr->result,(avm_memcell*)0);
avm_memcell* i==avm_translate_operand(&instr->arg1,&ax);
avm_memcell* c==avm_translate_operand(&instr->arg2,&bx);

assert(t&& &stack[0]<=t && &stack[top]>t);
assert(i&&c);

if(t->type!=table_m) avm_error("Illegal use of type %s as table!",typeStrings[t->type);
else avm_tablesetelem(t->data.tableVal, i ,c);
}


//Maybe, I should change all the occurences of avm_error() to printf()

*/
