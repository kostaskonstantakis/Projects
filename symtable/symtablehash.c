/*Marios Konstantinos Konstantakis A.M:3219
HY255 3rd assignment*/
#include <stdio.h>
#include <string.h>
#include <assert.h>
#include <stdlib.h>
#include "symtable.h"

/* Return a hash code for pcKey. */
static unsigned int SymTable_hash(const char *pcKey)
{
size_t ui;
unsigned int uiHash = 0U;
for (ui = 0U; pcKey[ui] != '\0'; ui++)
uiHash = uiHash * HASH_MULTIPLIER + pcKey[ui];
return uiHash;
}

SymTable_T SymTable_new(void)
{
	SymTable_T oSymTable=malloc(sizeof(SymTable_T));
   size_t counter;
  for(counter=0U;counter<BUCKETS;counter++)
    oSymTable.table[counter]=NULL;
  oSymTable.numberofbindings=0U;
  oSymTable.head=NULL;
  return oSymTable;
}

void SymTable_free(SymTable_T oSymTable)
{
  size_t j=0U;
  assert(oSymTable!=NULL);
  while(j<BUCKETS&&oSymTable.table[j]!=NULL)
  { 
      free(oSymTable.table[j].key);
      oSymTable.table[j]=NULL;
      j++;
  }
  free(oSymTable);
}

unsigned int SymTable_getLength(SymTable_T oSymTable)
{
  unsigned int integer=0U;
  assert(oSymTable!=NULL);
  integer=oSymTable.numberofbindings;/*the length of this Symbol Table is the number of its bindings*/
  return integer;
}

int SymTable_put(SymTable_T oSymTable, const char *pcKey,const void *pvValue)
{
	
}

int SymTable_remove(SymTable_T oSymTable, const char *pcKey)
{
	
}

int SymTable_contains(SymTable_T oSymTable, const char *pcKey)
{
	
}

void* SymTable_get(SymTable_T oSymTable, const char *pcKey)
{
	
}

void SymTable_map(SymTable_T oSymTable,void (*pfApply)(const char *pcKey, void *pvValue,void *pvExtra),const void *pvExtra)
{
	
}

