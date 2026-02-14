/*Marios Konstantinos Konstantakis, A.M: 3219.
csd3219@csd.uoc.gr*/
#include <stdio.h>
#include <sys/wait.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/stat.h> 
#include <sys/types.h>
#include <fcntl.h> 

void main(int argc, char *argv[], char *envp[]) 
{
int pipefd[2]; //used for pipes
pid_t cpid;
char buf;
char path[1024]; //used for getlogin() and getcwd()
int i=0;//used for chdir()'s return value
char *c=NULL;
FILE *fp=NULL; //file pointer
int file_descriptor=0; //used for redirection
printf("\033[1;31m"); //sets printf()'s output to be  red. 
printf("\n[cs345sh][%s][%s]~$. \n", getlogin() , getcwd(path,sizeof(path)));
cpid = fork();
if(cpid == -1) 
{
	perror("Fork failed!\n");
	exit(EXIT_FAILURE);
}
 else if(cpid != 0)  // fork off child process from parent
 {	close(pipefd[0]); /* Close unused read end */
	write(pipefd[1], argv[1], strlen(argv[1]));
	close(pipefd[1]); 
   	wait(NULL); // wait for child to exit 
   	exit(EXIT_SUCCESS);
 }
  else // Child code
  { 
	if(strcmp(argv[1],"cd")==0)
	{ 
		i=chdir(argv[2]);
		printf("\n%s@cs345sh%s$. \n", getlogin() , getcwd(path,sizeof(path)));
		 printf("\033[0m"); //back to normal coloring
	}
      	else if(strcmp(argv[1],"exit")==0) 
	{
		printf("\033[0m"); //back to normal coloring
		exit(0);
	}
	else if(strcmp(argv[1],"setenv")==0)
	{
		 i=setenv(argv[2],argv[3],1);//set environmental variable to given value
		 c=getenv(argv[2]);//get value
                 printf("\n%s=%s\n",argv[2],c);
		 printf("\033[0m"); //back to normal coloring
	}	  
	else if(strcmp(argv[1],"unsetenv")==0)
	{       
		 i=unsetenv(argv[2]);
		 c=getenv(argv[2]);//get value
                 printf("\n%s=%s\n",argv[2],c);
		 printf("\033[0m"); //back to normal coloring
        }
	else if(strcmp(argv[1],"env")==0)//print environmental variables
	{
		c=getenv("HOME");//get HOME's  value
		printf("\nHOME=%s\n",c);
		c=getenv("PATH");
                printf("\nPATH=%s\n",c);
		 printf("\033[0m"); //back to normal coloring
	}
	else if(strcmp(argv[1],"chmod")==0)
        {   i=chmod(argv[3],(int)argv[2]);
              if(i<0)
		{
			 perror("chmod() failed!\n");
			 printf("\033[0m"); //back to normal coloring
        		 exit(EXIT_FAILURE);

		}
              
        }
	else if(strcmp(argv[1],"fchmod")==0)
        {   
		int fd=open(argv[3],(int)argv[2]); //O_RDWR, now it opens every text  file
	      	if(fd>0) fchmod(fd,(int)argv[2]); //S_IRUSR|S_IWUSR|S_IRGRP|S_IWGRP|S_IROTH
           	else
                {
                   perror("fchmod() failed!\n");
			printf("\033[0m"); //back to normal coloring
                   exit(EXIT_FAILURE);
                }

        }
	 else if(strcmp(argv[1],"mkdir")==0)
        {   i=mkdir(argv[2],ACCESSPERMS); //grants access to everyone. This could have been done with S_IRWXU | S_IRWXG | S_IRWXO   //(int)argv[3]);
              if(i<0)
                {
                         perror("mkdir() failed!\n");
			 printf("\033[0m"); //back to normal coloring
                         exit(EXIT_FAILURE);

                }

        } //redirection 
	else if(strcmp(argv[2],"<")==0) 
	{ //standard input redirection
		    fp = fopen(argv[3], "r");
                    file_descriptor = fileno(fp);
                    dup2(file_descriptor, STDIN_FILENO);
                    fclose(fp);
	}
	else if(strcmp(argv[2],">")==0||strcmp(argv[3],">")==0) 
        { //standard output redirection
                    fp = fopen(argv[4], "w+");
                    file_descriptor = fileno(fp);
                    dup2(file_descriptor, STDOUT_FILENO);
                    fclose(fp);
        }
	else if(strcmp(argv[2],">>")==0||strcmp(argv[3],">>")==0)
        { //standard output append
                    fp = fopen(argv[4], "a");
                    file_descriptor = fileno(fp);
                    dup2(file_descriptor, STDOUT_FILENO);
                    fclose(fp);
        }
	else //pipes
	{
		//execve(argv[1], argv, 0);
		if (pipe(pipefd) == -1) {
		perror("Pipe failed!\n");
		exit(EXIT_FAILURE);
		} 

		close(pipefd[1]); /* Close unused write end */
		while (read(pipefd[0], &buf, 1) > 0)
		write(STDOUT_FILENO, &buf, 1);
		write(STDOUT_FILENO, "\n", 1);
		close(pipefd[0]);
		exit(EXIT_SUCCESS);

	}


	//execve(argv[1], argv, 0); // execute command
	printf("\033[0m"); //back to normal coloring
	exit(EXIT_SUCCESS);
  }

 }
 

