/*
Marios Konstantinos Konstantakis, A.M: 3219, csd3219@csd.uoc.gr
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
//#include <linux/d_params.h>
#define __NR_dummy_sys 341 //these aren't actually needed here...
#define __NR_set_deadlines 342

int main(void)
{
int deadline,computation_time;
//struct d_params *d_args=malloc(sizeof(struct d_params));;
printf("Trap to kernel level.\n");
syscall(__NR_dummy_sys, 42); /* you should check return value for errors */
printf("Back to user level. Now give me input!\n");
scanf("%d %d %d %d", &deadline, &computation_time); //read user input
if(syscall(__NR_set_deadlines, -1, deadline, computation_time)!=0)//call set_deadlines()
{
     perror("Error!");
     exit(errno);           
}
if(syscall(__NR_set_deadlines, getpid(), deadline, computation_time)!=0)//call set_deadlines()
{
     perror("Error!");
     exit(errno);
}
//wrong parameters from here onwards...

if(syscall(__NR_set_deadlines, -3, deadline, computation_time)!=0)//call set_deadlines()
{
     perror("Error! Wrong parameters.\n");
     exit(errno);
}
//messed up  args here
if(syscall(__NR_set_deadlines, getpid(), computation_time, deadline)!=0)//call set_deadlines()
{
     perror("Error! Messed up arguments.\n");
     exit(errno);
}


printf("Finally, back to user level.\n");
return 0;

}
