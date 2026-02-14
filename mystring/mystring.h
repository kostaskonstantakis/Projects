/*Marios Konstantinos Konstantakis 
A.M:3219 
Assignment 2
mystring.h*/

#include<stdio.h>
#include<stddef.h>
#include<assert.h>

/*Returns the length of the string pcStr*/
size_t ms_length(const char pcStr[]);

/*Copies pcStr into newStr,ending it with a null terminator '\0'*/
char* ms_copy(char newStr[],const char pcStr[]);

/*Copies the num leftmost letters/characters of string pcStr to newStr*/
char* ms_ncopy(char newStr[],const char pcStr[],size_t num);

/*concatenates Str1 and Str2*/
char* ms_concat(const char Str1[],const char Str2[]);

/*Concatenates the num leftmost letters/characters of string Str2 to Str1*/
char* ms_nconcat(const char Str1[],const char Str2[],size_t num);

/*Checks if 2 strings are identical/'equal'/same by comparing letter by letter.
Continues while there's no difference,returns 0 if they're actually the same 
and returns 1 or -1 if the first string is alpharithmetically greater or less than the second string*/
int ms_compare(const char str1[],const char str2[]);

/*Checks if 2 strings are identical/'equal'/same by comparing letter by letter until the numth letter.
Continues while there's no difference,up until it reaches the numth letter,then it returns 0 if they're actually the same 
and returns 1 or -1 if the first string is alpharithmetically greater or less than the second string*/
int ms_ncompare(const char str1[],const char str2[],size_t num);

/*Searches  if string2 is a substring of string1.If it is ,then it returns a pointer to the element where it found string2,otherwise returns NULL*/
char* ms_search(const char string1[],const char string2[]);
