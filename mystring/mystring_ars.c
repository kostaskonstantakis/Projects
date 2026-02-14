/*Marios Konstantinos Konstantakis 
A.M:3219 
Assignment 2
mystring_ars.c*/

#include<stdio.h>
#include<stddef.h>
#include"mystring.h"
#include<assert.h>

/*Returns the length of the string pcStr*/
size_t ms_length(const char str[])
{
 size_t uiLength= 0U; /*initialize the counter*/
 assert(str != NULL);/*/check if pcStr is NULL*/
 while (str[uiLength] != '\0')/*/continue 'till you reach the null terminator*/
 uiLength++;/*/length increases as we pass letter by letter in each element of the character array */
 return uiLength;/*/return the found length of string*/
}

/*Copies pcStr into newStr,ending it with a null terminator '\0'*/
char* ms_copy(char newStr[],const char str[])
{
	size_t length=ms_length(str); /*string's length*/
   size_t i;/*/iterator /counter declaration*/
   assert(str!=NULL);/*/check if pcStr is NULL*/
   for(i=0U;i<length;i++)/*/OU is the default price for (unsigned) size_t ''integers/numbers'' */
   {
     newStr[i]=str[i];	/*/copy every element of pcStr to newStr from start until the end*/
   }
   newStr[length]='\0';/*/stick null terminator at string's end*/
   return newStr;/*/now return it*/
}

/*Copies the n leftmost letters/characters of string pcStr to newStr*/
char* ms_ncopy(char newStr[],const char str[],size_t n)
{
	size_t i=0U;/*/initialization n declaration of variable*/
   assert(str!=NULL);/*check if pcStr is NULL*/
   while(i<n && str[i]!='\0')/*while we still haven't reached the nth letter and current element isn't NULL continue on this loop*/
   {
     newStr[i]=str[i];/*copy old string's value to the respective new  string  element*/
     i++;/*augment the counter*/
   }
   return newStr;/*return string*/
}

/*concatenates Str1 and Str2*/
char* ms_concat(const char str1[],const char str2[])
{
  size_t L1=ms_length(str1);/*length of first string*/
  size_t L2=ms_length(str2);/*length of second string*/
  int L=(int)L1+L2;/*length of new string*/
  char str[L];/*new string*/
  size_t i;/*iterator*/
  size_t j;/*counter for inner loop*/
  assert(str1!=NULL);/* check if str1 is NULL*/
  assert(str2!=NULL);/*check if str2 is NULL*/
  for(i=0U;i<L1;i++)/*fill in str's leftmost elems w/ str1's elements*/
   	str[i]=str1[i];
     for(i=L1-1;i<L;i++)/* now continue w/ str2's elems.str1's elems end @ L1-1 . so we start indexin'  from there till we reach L-1 */
       for(j=0U;j<L2;j++) str[i]=str2[j];
  str[L]='\0';/*''glue'' NULL terminator in the end of string */
  return str;/*return it */
  
}

/*Concatenates the n leftmost letters/characters of string pcStr to newStr*/
char* ms_nconcat(const char str1[],const char str2[],size_t n)
{
	
  size_t L1=ms_length(str1);/*length of first string*/
  size_t L2=ms_length(str2);/*length of second string*/
  int L=(int)L1+L2;/*length of new string*/
  char str[L];/*new string*/
  size_t i;/*iterator*/
  size_t j;/*counter for inner loop*/
  size_t z=0U;
  assert(str1!=NULL);/* check if str1 is NULL*/
  assert(str2!=NULL);/*check if str2 is NULL*/
  while(z<n)/*concatenate 'em as long as we haven't reached nth letter yet*/
  {
   for(i=0U;i<L1;i++)/*fill in str's leftmost elems w/ str1's elements*/
   {
   	str[i]=str1[i];
   }
     for(i=L1-1;i<L;i++)/* now continue w/ str2's elems.str1's elems end @ L1-1 . so we start indexin'  from there till we reach L-1 */
     {
      
       for(j=0U;j<L2;j++) str[i]=str2[j];
   
     }
     z++;
  }
  str[L]='\0';/*''glue'' NULL terminator in the end of string */
  return str;/*return it */
	
}

/*Checks if 2 strings are identical/'equal'/same by comparing letter by letter.
Continues while there's no difference,returns 0 if they're actually the same 
and returns 1 or -1 if the first string is alpharithetically greater or less than the second string*/
int ms_compare(const char str1[],const char str2[])
{
  size_t i;
  size_t length1=ms_length(str1);
  size_t length2=ms_length(str2);
  size_t max=0U;/*initialize the maximum length*/
  assert(str1!=NULL&&str2!=NULL);/*check whether both str1 or str2 are NULL*/
  if(length1==length2) return 0;/*if equal ret 0*/
  else if(length1>length2)/*else */
    max=length1;/*obviously length1 is max here*/
  else if (length1<length2)/*otherwise*/
    max=length2;/*length of str2 is max now*/
  for(i=0U;i<max;i++)/*now check both strings letter by letter up 'till the maximum letter,defined just above */
  {
    if(str1[i]>str2[i])/*if 1st string is alpharithmetically greater than 2nd ret 1*/
      return 1;    
    else if(str1[i]<str2[i])    /*otherwise ret -1*/  
      return -1;
  }
 
}

/*Checks if 2 strings are identical/'equal'/same by comparing letter by letter until the numth letter.
Continues while there's no difference,up until it reaches the numth letter,then it returns 0 if they're actually the same 
and returns 1 or -1 if the first string is alpharithetically greater or less than the second string*/
int ms_ncompare(const char str1[],const char str2[],size_t num)
{
  size_t i,z;
  i=z=0U;
  size_t length1=ms_length(str1);
  size_t length2=ms_length(str2);
  size_t max=0U;/*initialize the maximum length*/
  assert(str1!=NULL&&str2!=NULL);/*check whether both str1 or str2 are NULL*/
  if(length1==length2) return 0;/*if equal ret 0*/
  else if(length1>length2)/*else */
    max=length1;/*obviously length1 is max here*/
  else if (length1<length2)/*otherwise*/
    max=length2;/*length of str2 is max now*/
  while(z<num && i<max)/*now check both strings letter by letter up 'till the maximum letter,defined just above */
  {
    if(str1[i]>str2[i])/*if 1st string is alpharithmetically greater than 2nd ret 1*/
      return 1;    
    else if(str1[i]<str2[i])    /*otherwise ret -1*/  
      return -1;
      z++;/*augment the while loop counters n go back to the condition */
      i++;
  }
  
}

/*Searches  if string2 is a substring of string1.If it is ,then it returns a pointer to the element where it found the resemblance,otherwise returns NULL*/
char* ms_search(const char string1[],const char string2[]) 
{

  int x=0;
  x=ms_compare(string1,string2);	
  if(x==0) return (char *)&string1[0];/*they are identical so string2 is a substring of string1 and vice versa,so return a pointer to the base of string1*/ 	
  size_t length1=ms_length(string1);
  size_t length2=ms_length(string2);
  size_t pointer,resemblance;
  pointer=resemblance=0U;
  size_t i,j;
  i=j=0U;
  assert(string1!=NULL&&string2!=NULL);
  while(string1[i]!='\0')/*while we haven't reached the end of string1*/
  {
    while(string2[j]!='\0')/*check letter by letter if every element/character/letter od string2  is the same as string1's*/
    {
      if(string1[i]==string2[j])/*if every element/character/letter od string2 is the same as string1's*/
      {
	    i++;/*continue;'point' to the next element*/
	    j++;/*same as above*/
	    resemblance=1;/*flag for resemblance*/
      }
      else/*if they differ*/
       {
	      i++;/*continue searching in string1*/
	      j=0U;/*for this particular character*/
	      resemblance=0U;/*zero this out-''*/
         break;
       }
      
         if(resemblance==1&&j==length2)/*if they're identical n we reached the end of string2*/
	      pointer=i;      /*pointer is the ith element*/
		  j++;
    }
	i++;
  }

  if(pointer!=0)
    return (char *)&string1[pointer-length2+1];
  else
    return NULL;
  
	
}
