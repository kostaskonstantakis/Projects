/*Marios Konstantinos Konstantakis A.M:3219
HY255 3rd assignment*/
#include <stdio.h>
#include <string.h>
#include <assert.h>
#include <stdlib.h>
#include "symtable.h"

SymTable_T SymTable_new(void)
{
  SymTable_T oSymTable;
  oSymTable=malloc(sizeof(SymTable_T));
  oSymTable.numberofbindings=0U;
  oSymTable.head=NULL;
  size_t counter;
  for(counter=0U;counter<BUCKETS;counter++)
    oSymTable.table[counter]=NULL; //make all the array cells null
  return oSymTable;
}

void SymTable_free(SymTable_T oSymTable)
{
  binding *temporary1,*temporary2;
  assert(oSymTable!=NULL);
  temporary1=oSymTable.head;
  while(temporary1!=NULL)
  {
    temporary2=temporary1->next;
    free(temporary1->key);
    free(temporary1->value);
    temporary1=temporary2;
  }
  free(oSymTable);
}

unsigned int SymTable_getLength(SymTable_T oSymTable)
{
  unsigned int length;
  assert(oSymTable!=NULL);
  length = oSymTable.numberofbindings;
  return length;
	
}

/*SymTable_put must (if there's no binding with a key=pcKey in oSymTable) add a new binding in oSymTable ,comprising of pcKey and pvValue,
then return 1 as a true value or otherwise must return 0 as a false value,without changing oSymTble at all
It's a checked runtime error if oSymTable or pcKey are NULL.*/
int SymTable_put(SymTable_T oSymTable, const char *pcKey,const void *pvValue)
{
	assert(oSymTable!=NULL && pcKey!=NULL);
/*	binding *tmp=oSymTable.head;
	tmp->key=pcKey;
	tmp->value=pvValue; */
	if(contains(oSymTable,tmp->pcKey)) return 0; //there's a binding with such fields already in symtable,go back to the caller
	else //there isn't such a thing,so create it yourself now!
	{
		binding *tmp;
		binding *tmp2;
		tmp2=malloc(sizeof(binding *))
		tmp=malloc(sizeof(binding *));
		tmp->key=pcKey;
		tmp->value=pvValue;
		tmp2=oSymTable.head;
		while(tmp2!=NULL)
		{
			if(tmp2->next==NULL) //if we reached the last node,insert the new one here
			{
				tmp2->next=tmp;
				tmp2->key=tmp->key;
				tmp2->value=tmp->value;
				return 1;
			}
			tmp2=tmp2->next;//point to the next element of the list
			
		}
		
		
		
	}
}

/*Return 1 if there's a binding with a key=pcKey in oSymTable and remove it ,else don't modify oSymTable and return 0
It's a checked runtime error if either oSymTable or pcKey are NULL.*/
int SymTable_remove(SymTable_T oSymTable, const char *pcKey)
{
	assert(oSymTable!=NULL&&pcKey!=NULL);
	if(!contains(oSymTable,pcKey)) return 0;
	else 
	{
		binding *tmp;
		tmp=malloc(sizeof(binding *));
		tmp=oSymTable.head;
		while(tmp!=NULL)//search the symtable for pcKey
		{
			if(tmp->key==pcKey)//if found,delete that particular node-it'll be surely found here somewhere though :)
			{
				tmp->key=NULL;
				tmp->value=NULL;
				tmp->next=NULL;
				free(tmp);
				return 1;
			}
			tmp=tmp->next;
		}
		
	}
	
}

int SymTable_contains(SymTable_T oSymTable, const char *pcKey)
{
	assert(oSymTable!=NULL&&pckey!=NULL);
	binding *tmp=oSymTable.head;
	while(tmp!=NULL)
	{
		if(strcmp(tmp->key,pcKey)) return 1;//found it,success
		tmp=tmp->next;//go to the next one
		
		
	}
	 return 0;//it doesn't contain that binding,failure 
	
	
	
}

void* SymTable_get(SymTable_T oSymTable, const char *pcKey)
{
	assert(oSymTable!=NULL&&pckey!=NULL);
	binding *tmp=oSymTable.head;
//	if(contains(oSymTable,pcKey)) return *tmp->;
		while(tmp!=NULL)//while we haven't hit a null node yet
	  {
		if(tmp->key==pcKey) return tmp->value; //check the key values and return if we found the one we wanted 
		
		tmp=tmp->next; //otherwise go to the next element
		
		
	  }
	  return NULL;//if the command flow reaches this point,then it means there's no such key in symtable,so give 'em back a null pointer
	
}


void pfApply(const char *pcKey, void *pvValue, void *pvExtra)
{
	pvValue += pvExtra;
}

/*Maps *pfApply in each and every binding in
oSymTable ,havin' pvExtra as an extra parameter
It's a checked runtime error either oSymTable or pfApply are NULL. */
void SymTable_map(SymTable_T oSymTable,void (*pfApply)(const char *pcKey, void *pvValue,void *pvExtra),const void *pvExtra)
{
	
	assert(oSymTable!=NULL&&pfApply!=NULL);
	binding *temporary;
	temporary=malloc(sizeof(binding *));
	temporary=oSymTable.head;
	while(temporary!=NULL)
	{
		pfApply(temporary->key,temporary->value,pvExtra);
		temporary=temporary->next;
	}
	
}







