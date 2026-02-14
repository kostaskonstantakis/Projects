typedef struct d_params {
unsigned long deadline;
unsigned long computation_time;
unsigned long elapsed_runtime;
unsigned long remaining_time;
unsigned long slack_time;
long double possibility; //possibility of execution for each process
}d_params;
