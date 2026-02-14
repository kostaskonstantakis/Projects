/*
Marios Konstantinos Konstantakis, A.M: 3219, csd3219@csd.uoc.gr
*/
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <errno.h>
#include <linux/kernel.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/time.h>
#define __NR_dummy_sys 341 
#define __NR_set_deadlines 342

int main(int argc, char *argv[])
{
int deadline=0,computation_time=0;
int N=(int)argv[1];
int i=0;
pid_t cpid;
if(N>=11) printf("N only takes values in the range [2,10].\n");
else if(argc==1) N=2;
pid_t children[N];
		//fork children
		for(i=0;i<N;i++)
		{ 
			children[i]=fork();
			if (children[i]<0) 
			{
				perror("Fork failed.\n");
				exit(EXIT_FAILURE);
			}
			else if (children[i]==0) //child(ren) created
			{
				struct timeval current_time;
				deadline=gettimeofday(&current_time,NULL)+1000*100; //gettimeofday()+100 seconds.
				computation_time=gettimeofday(&current_time,NULL)+1000*100; //same as above
				syscall(__NR_set_deadlines, cpid, deadline, computation_time);
				int j=0; for(j=0;j<100;j++) {} //the process spins for some time
				exit(EXIT_SUCCESS);	
			}
			else //parent
			{
				wait(NULL); // Wait for child 
				exit(EXIT_SUCCESS);
			}
		}	     
return 0;
}
