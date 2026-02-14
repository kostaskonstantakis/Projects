#include <linux/kernel.h>
#include <linux/syscalls.h>
#include <asm/uaccess.h>

asmlinkage long sys_dummy_sys(int arg0)
{
printk("Called system call dummy_sys with argument: %d\n", arg0);
return ((long)arg0 * 2);
}
