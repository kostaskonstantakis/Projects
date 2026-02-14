A.M:3219 Μάριος Κωνσταντίνος Κωνσταντάκης 

2)

   0x8048aac <readString>:      push   %ebp
   0x8048aad <readString+1>:    mov    %esp,%ebp
   0x8048aaf <readString+3>:    sub    $0x34,%esp
   0x8048ab2 <readString+6>:    movl   $0x0,-0x4(%ebp)
   0x8048ab9 <readString+13>:   mov    0x80d4b44,%eax
   0x8048abe <readString+18>:   push   %eax
   0x8048abf <readString+19>:   call   0x8050850 <getc>
   0x8048ac4 <readString+24>:   add    $0x4,%esp
   0x8048ac7 <readString+27>:   mov    %eax,-0x8(%ebp)
   0x8048aca <readString+30>:   cmpl   $0xffffffff,-0x8(%ebp)
   0x8048ace <readString+34>:   je     0x8048ae8 <readString+60>
   0x8048ad0 <readString+36>:   cmpl   $0xa,-0x8(%ebp)
   0x8048ad4 <readString+40>:   je     0x8048ae8 <readString+60>
   0x8048ad6 <readString+42>:   mov    -0x4(%ebp),%eax
   0x8048ad9 <readString+45>:   lea    0x1(%eax),%edx
   0x8048adc <readString+48>:   mov    %edx,-0x4(%ebp)
   0x8048adf <readString+51>:   mov    -0x8(%ebp),%edx
   0x8048ae2 <readString+54>:   mov    %dl,-0x34(%ebp,%eax,1)
   0x8048ae6 <readString+58>:   jmp    0x8048ab9 <readString+13>
   0x8048ae8 <readString+60>:   lea    -0x34(%ebp),%edx
   0x8048aeb <readString+63>:   mov    -0x4(%ebp),%eax
   0x8048aee <readString+66>:   add    %edx,%eax
   0x8048af0 <readString+68>:   movb   $0x0,(%eax)

   
   (gdb) print &grade
$1 = 0x80d46e8 <grade> "3"


(gdb) print grade
$2 = 51 '3'


(gdb) print &Name
$3 = (char (*)[44]) 0x80d65e0 <Name>

(gdb) break *readString+73
Note: breakpoint 1 also set at pc 0x8048af5.
Breakpoint 2 at 0x8048af5: file hello.c, line 24.
(gdb) run
Undefined command: "▒".  Try "help".



 print $esp
No registers.

(gdb) print $ebp
No registers.


(gdb) x/??b $esp
No registers.

