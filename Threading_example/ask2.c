#include <pthread.h>
#include <semaphore.h>
#include <time.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

sem_t sem;

void *employee( void *ptr){
printf("The employee is dealing with the client.\n");
sleep(1);
sem_post(&sem); //semaphore unlocked (Up)!
}

void *customer( void *ptr){
sem_wait(&sem); //semaphore locked (Down)!
printf("The client has been served.\n");
}

void main(int argc, char * argv[] ){
int n=0;
printf("Please, enter the number of threads you want: ");
scanf("%d",&n);
pthread_t customers[n];
pthread_t employee_thread, customer_thread;
int rc, rc2;
int i=0;
/*Semaphore: 0 and 1 --> (locked/unlocked)*/
sem_init(&sem, 0, 0 ); /*Initialize semaphore with intraprocess scope*/
//rc = pthread_create(&chef_thread, NULL, chef, NULL);
//rc2 = pthread_create(&customer_thread, NULL, customer, NULL);
for(i=0;i<n;i++)
{
	printf("Client #%d has entered the store.\n",i+1);
	rc = pthread_create(&employee_thread, NULL, employee, NULL);
	rc2 = pthread_create(&customers[i], NULL, customer, NULL);
	pthread_join(employee_thread,NULL);//Wait for the thread to finish.
	pthread_join(customers[i], NULL);
	printf("\n");
}
//pthread_join(chef_thread,NULL);//Wait for the thread to finish.
//pthread_join(customer_thread,NULL);

}
