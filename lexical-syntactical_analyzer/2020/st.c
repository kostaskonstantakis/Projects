#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <assert.h>
#include "alpha.h"


#define MAX_SCOPE_SIZE 100
#define HASH_TABLE_SIZE 1009

SymbolTableEntry *ScopeList[MAX_SCOPE_SIZE];
SymbolTableEntry *SymbolTable[HASH_TABLE_SIZE];

char *enumtostr[5] = {"global", "local", "formal", "user", "library"};

int get_hash(const char* token){
	int i, sum = 0;
	for(i = 0; i < sizeof(token); i++) sum = sum + token[i];
	return sum % HASH_TABLE_SIZE;
}

//allocates memory and creates a new entry 
SymbolTableEntry *new_entry(const char *name, enum Type type, unsigned int scope, unsigned int line){ 
	SymbolTableEntry *new_entry = malloc(sizeof(SymbolTableEntry));
	new_entry -> isActive = 1;
	new_entry -> type = type;
	new_entry -> next = NULL;
	new_entry -> next_in_scope = NULL;
	if(type == GLOBAL || type == LOCL || type == FORMAL){
		new_entry -> value.varVal = malloc(sizeof(Variable));
		new_entry -> value.varVal -> name = strdup(name);
		new_entry -> value.varVal -> scope = scope;
		new_entry -> value.varVal -> line = line;
		return new_entry;
	}
	else if(type == USERFUNC || type == LIBFUNC){
		new_entry -> value.funcVal = malloc(sizeof(Function));
		new_entry -> value.funcVal -> name = strdup(name);
		new_entry -> value.funcVal -> args = NULL;
		new_entry -> value.funcVal -> scope = scope;
		new_entry -> value.funcVal -> line = line;
		return new_entry;
	}
	fprintf(stderr, "Failed to allocate new entry(returned NULL)!\n");
	return NULL;
}


void insert(SymbolTableEntry *entry){ //inserts new entry in symbol table and scopelist
	int hash, s;
	SymbolTableEntry *temp_sym, *temp_scope;
	const char *temp_name;
	enum Type temp_type;

	assert(entry != NULL);
	if(entry -> type == GLOBAL || entry -> type == LOCL || entry -> type == FORMAL){ 
	//checking if it's function or variable, to keep what's useful
		temp_name = strdup(entry -> value.varVal -> name);
		s = entry -> value.varVal -> scope;
	}
	else if(entry -> type == USERFUNC || entry -> type == LIBFUNC){
		temp_name = strdup(entry -> value.funcVal -> name);
		s = entry -> value.funcVal -> scope;
	}
	else{
		fprintf(stderr, "Error at entry type, failed to insert!\n");
		return;
	}
	temp_type = entry -> type; 
	//keeping all the entry elements
	temp_scope = ScopeList[s];
	hash = get_hash(temp_name);
	temp_sym = SymbolTable[hash];
	if(SymbolTable[hash] == NULL){ //insertion into the symbol table
		SymbolTable[hash] = entry;
		SymbolTable[hash] -> next = NULL;
	}
	else{
		while(temp_sym -> next != NULL) temp_sym = temp_sym -> next;
		temp_sym -> next = entry;
	}
	if(ScopeList[s] == NULL){ //insertion into the scope list
		ScopeList[s] = entry;
		ScopeList[s] -> next_in_scope = NULL;
	}
	else{
		while(temp_scope -> next_in_scope != NULL) temp_scope= temp_scope -> next_in_scope;
		temp_scope -> next_in_scope = entry;
	}
}

void print(){
	
        int i = 0;
        SymbolTableEntry *SList;
        for(i = 0; i < MAX_SCOPE_SIZE; i++){
        	SList = ScopeList[i];
        	printf("------------------------------SCOPE %d------------------------------\n", i);
        	while(SList != NULL){
			if(SList -> type == GLOBAL || SList -> type == LOCL || SList -> type == FORMAL){
		                printf("isActive: %d | type: %s | name: %s | scope: %d | line:%d. \n", SList -> isActive, enumtostr[SList -> type], SList -> value.varVal -> name, SList -> value.varVal -> scope, SList -> value.varVal -> line);
		        }
		        else if(SList -> type == USERFUNC || SList -> type == LIBFUNC){
		                printf("isActive: %d | type: %s | name: %s | scope: %u | line:%d. \n", SList -> isActive, enumtostr[SList -> type], SList -> value.funcVal -> name, SList -> value.funcVal -> scope, SList -> value.funcVal -> line);
		        }
		        SList = SList -> next_in_scope;
        	}
        }

}


//searching for a symbol in a specific scope to see if it is already 
char* lookup_selected_scope(const char* name, unsigned int scope){
	SymbolTableEntry *selected_list = ScopeList[scope];
	while(selected_list != NULL){
			if(selected_list -> type == GLOBAL || selected_list -> type == LOCL || selected_list -> type == FORMAL){
				if(!strcmp(selected_list -> value.varVal -> name, name)){
					if(selected_list -> isActive == 1){
						printf("%s successfully found in scope:%d.\n", name, scope);
						return enumtostr[selected_list -> type];
					}
				}
			}
			else if(selected_list -> type == USERFUNC || selected_list -> type == LIBFUNC){
				if(!strcmp(selected_list -> value.funcVal -> name, name) && selected_list -> isActive == 1){
					if(selected_list -> isActive == 1){
						printf("%s successfully found in scope:%d.\n", name, scope);
						return enumtostr[selected_list -> type];
					}
				}
			}
		selected_list = selected_list -> next_in_scope;
	}
	fprintf(stderr, "%s not found in scope:%d(return NULL)!\n", name, scope);
	return NULL;
}

int search_name(const char* name, unsigned int scope){
	SymbolTableEntry *t;
	int s = scope;
	while(s > -1){
		t = ScopeList[s];
		while(t != NULL){
			if(t -> type == LOCL || t -> type == GLOBAL || t -> type == FORMAL){
				if(t -> isActive == 1){
					if(!strcmp(t -> value.varVal -> name, name)){
						return 1;
					}
				}
			}
			t = t -> next_in_scope;
		}
		s--;
	}
	return 0;
}

int is_accessible(const char* name, unsigned int c_scope){
	SymbolTableEntry *s_list;
	int flag1 = 0, flag2 = 0, temp_scope = c_scope;
	const char *t_name;
	temp_scope--;
	while(temp_scope != 0){
		s_list = ScopeList[temp_scope];
		while(s_list != NULL){
			if((s_list -> type == LOCL || s_list -> type == FORMAL)&& s_list -> isActive == 1 && (!strcmp(s_list -> value.varVal -> name, name))){
				flag1++;
			}
			if(flag1 != 0){
				if(s_list -> type == USERFUNC || s_list -> type == LIBFUNC){
					if(s_list -> isActive == 1) return 0;
				}
			}
			s_list = s_list -> next_in_scope;
		}
		temp_scope--;
	}
	return 1;
	
}


int is_lib_symbol(const char* name){
	SymbolTableEntry *temp = ScopeList[0];
	while(temp != NULL){
		if(temp -> type == LIBFUNC){
			if(!strcmp(temp -> value.funcVal -> name, name)){
				fprintf(stderr, "%s is a library symbol, SHADOWING\n", name);
				return 1;
			}
		}
		temp = temp -> next_in_scope;
	}
	printf("%s is not library symbol.\n", name);
	return 0;
}

int is_function(const char* name, unsigned int scope){
	SymbolTableEntry *temp = ScopeList[scope];
	while(temp != NULL){
		if((temp -> type == LIBFUNC || temp -> type == USERFUNC)){
			return 1;
		}
		temp = temp -> next;
	}
	return 0;
}

//Hiding all the symbols in a given scope
void hide(unsigned int scope){
	SymbolTableEntry *temp = ScopeList[scope];
	while(temp){
		temp -> isActive == 0;
		temp = temp -> next_in_scope;
	}
}

int lookup_entry(const char* name, enum Type type, unsigned int current_scope){
	int myscope = current_scope;
	SymbolTableEntry* current_sl = ScopeList[myscope];
	int is_lib, flag = 0, access;
	unsigned char* temp_name;
	unsigned int s;
	char* inscope;
	if(type == LOCL){
		if(is_lib_symbol(name)) return 0;
		inscope = lookup_selected_scope(name, myscope);
		if(inscope != NULL){ 
		//If it's not null, then there's a symbol in this scope, so we refer to that instead. 
			printf("%s :  %s successfully found in current scope(%d).\n", enumtostr[type], name, myscope);
			return 1;
		}
		else if(inscope == NULL){ 
			is_lib = is_lib_symbol(name);
		/* If inscope==NULL mans that the symbol we're looking for isn't in this scope, 
		if there's no collision with a lib symbol, then into the symbol table it goes*/
			if(is_lib == 0){
				if(myscope == 0){
					fprintf(stderr, "UNDECLARED symbol:%s must be declared in global scope.\n", name);
					return 2;
				}
				else{ // Otherwise, we declare that as LOCL in current scope
				
					fprintf(stderr, "UNDECLARED symbol:%s must be declared in current scope.\n", name);
					return 3;
				}
			}
		}
	}
	else if(type == GLOBAL){
		if(is_lib_symbol(name)) return 0;
		if(myscope == 0){
			inscope = lookup_selected_scope(name, myscope);
			if(inscope == NULL){
				fprintf(stderr, "UNDECLARED symbol:%s and type:%s must be declared at scope 0.\n", name , enumtostr[type]);
				return 2;
			}
			else{
				if(!strcmp(inscope, "global")){ 
				// global variable reference(::), checking if it's valid, looking for it in global scope.
					printf("Global variable %s successfully found in global scope.\n", name);
					return 1;
				}
				else{
					fprintf(stderr, "Referrence at UNDECLARED global variable:%s \n", name);
					return 0;
				}
			}
		}
		else{
			int flag22 = search_name(name, myscope);
			if(flag22 == 0){
				return 3;
			}
			else{
				access = is_accessible(name, myscope);
				if(access == 1){
					printf("The symbol with name:%s and type:%s is accessible.\n", name, enumtostr[type]);
					return 1;
				}
				else{
					inscope = lookup_selected_scope(name, 0);
					if(inscope != NULL){
						if(!strcmp(inscope, "global")){
							printf("The symbol: %s is in global scope, so it is accessible.\n", name);
				                                return 1;
						}
						else{
							fprintf(stderr, "OUT OF SCOPE symbol:%s\n", name);
							return 0;
						}
					}
					else{
						fprintf(stderr, "OUT OF SCOPE symbol:%s\n", name);
						return 0;
					}
				}
			}
		}
	}
	else if(type == USERFUNC){
		inscope = lookup_selected_scope(name , myscope);
		is_lib = is_lib_symbol(name);
		if(inscope == NULL && is_lib == 0){
			fprintf(stderr, "UNDECLARED symbol with name:%s and type:%s \n", name, enumtostr[type], myscope);
			return 3;
		}
		else{
			fprintf(stderr, "REDACLARATION symbol with name:%s\n", name);
			return 0;
		}
	}
	else if(type == FORMAL){
		inscope = lookup_selected_scope(name, myscope);
		is_lib = is_lib_symbol(name);
		if(inscope == NULL && is_lib == 0){
                        printf("UNDECLARED symbol with name:%s can be declared as formal in scope:%d.\n", name, myscope + 1);
                        return 3;
                }
                else{
                        fprintf(stderr, "REDECLARATION of symbol with name:%s\n", name);
                        return 0;
                }
	}
}

