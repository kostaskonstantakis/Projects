all:
	gcc -o test test.c
	gcc -o demo4 demo4.c
	cd linux-2.6.38.1
	make ARCH=i386 bzImage
	qemu-system-i386 -hda hy345-linux.img -append "root=/dev/hda" -kernel linux-2.6.38.1/arch/x86/boot/bzImage -curses
	./test
	dmesg | tail
	./demo4
	dmesg | tail

clean:
	rm -rf bzImage
	rm -rf test
	rm -rf demo4
