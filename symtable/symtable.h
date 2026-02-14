/*Marios Konstantinos Konstantakis A.M:3219
Here are all the necessary definitions for this exercise to work properly*/
#define HASH_MULTIPLIER 65599
#define BUCKETS 509

typedef struct binding{ 
  char *key;
  void *value;
  struct binding *next;
}binding;

typedef struct SymTable{
  unsigned int numberofbindings;		/*counter*/
  struct binding *head;				/*pointer to binding*/
  struct binding *table[BUCKETS]; /*buckets for the hash implementation*/
}SymTable_T; 

/*SymTable_new returns a new empty SymTable_T with no bindings*/
SymTable_T SymTable_new(void);

/*SymTable_free must deallocate all the memory used by oSymTable.
if oSymTable=NULL then the function does nothing*/
void SymTable_free(SymTable_T oSymTable);

/*SymTable_getLength returns the number of bindings in oSymTable.
It's a checked runtime error if oSymTable=NULL.*/
unsigned int SymTable_getLength(SymTable_T oSymTable);

/*SymTable_put must (if there's no binding with a key=pcKey in oSymTable) add a new binding in oSymTable ,comprising of pcKey and pvValue,
then return 1 as a true value or otherwise must return 0 as a false value,without changing oSymTble at all
It's a checked runtime error if oSymTable or pcKey are NULL.*/
int SymTable_put(SymTable_T oSymTable, const char *pcKey,const void *pvValue);

/*Return 1 if there's a binding with a key=pcKey in oSymTable and remove it ,else don't modify oSymTable and return 0
It's a checked runtime error if either oSymTable or pcKey are NULL.*/
int SymTable_remove(SymTable_T oSymTable, const char *pcKey);

/*Return 1 if oSymTable contains a binding with a key=pcKey or 0 elsewise 
It's a checked runtime error if  either oSymTable or pcKey are NULL.*/
int SymTable_contains(SymTable_T oSymTable, const char *pcKey);

/*Return the value of oSymTable's binding ,which has a key=pcKey or NULL in the case of no such binding
It's a checked runtime error if  either oSymTable or pcKey are NULL.*/
void* SymTable_get(SymTable_T oSymTable, const char *pcKey);

/*Maps *pfApply in each and every binding in
oSymTable ,havin' pvExtra as an extra parameter
It's a checked runtime error either oSymTable or pfApply are NULL. */
void SymTable_map(SymTable_T oSymTable,void (*pfApply)(const char *pcKey, void *pvValue,void *pvExtra),const void *pvExtra);
