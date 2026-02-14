#include <linux/kernel.h> 
#include <linux/syscalls.h> 
#include <asm/uaccess.h> 
#include <linux/d_params.h> 
#include <asm/current.h>

asmlinkage long sys_set_deadlines(int pid, unsigned long deadline, unsigned long computation_time) 
  struct d_params *d_arguments;
  struct task_struct *tsk;
  printk("\n Marios Konstantinos Konstantakis, A.M: 3219, set_deadlines.\n");
  if(computation_time<deadline)
  {
        if(pid<-1) return EINVAL;
        else
        {
          for_each_process(tsk)//search for the right pid
          {
             if(pid==-1||tsk->pid==pid )//current process or child process(es)
             {
                current->deadline = deadline;
                current->computation_time = computation_time;
                return (long) 0;
             }
          }
        }
 }
  else return EINVAL;
}
