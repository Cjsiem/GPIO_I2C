//#include <glib.h>
//#include <glib/gprintf.h>
#include <errno.h>
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <linux/i2c-dev.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>



void I2C_Reading(int addr, char *buf, int bytes){
	int file;
	char *filename = (char*)"/dev/i2c-1";
	if ((file = open(filename, O_RDONLY)) < 0) {
		printf("Failed to open the bus.");
		exit(1);
}
	if (ioctl(file,I2C_SLAVE,addr) < 0) {
		printf("Failed to acquire bus access and/or talk to slave.\n");
		exit(1);
}
	if (read(file,buf,bytes) != bytes) {
		printf("Failed to read from the i2c bus.\n");
		printf("\n\n");
    }
}

int I2C_Writing(int addr, char* buf, int bytes){
	int file;
	char *filename = (char*)"/dev/i2c-1";
	if ((file = open(filename, O_WRONLY)) < 0) {
		printf("Failed to open the bus.");
		exit(1);
}
	if (ioctl(file,I2C_SLAVE,addr) < 0) {
		printf("Failed to acquire bus access and/or talk to slave.\n");
		exit(1);
}
	int a = write(file, buf, bytes);
	if(a != bytes){
		printf("Failed to write to the i2c bus.\n");
		printf("\n\n");
	}
	return a;
}

int main(void){
	
int addr = 0x10;
char buf[3] = {0};

I2C_Reading(addr, buf, 1); //return buf

printf("%d", buf[0]);
printf("\n");
buf[0] = 1;
buf[1] = 12;
buf[2] = 2;
I2C_Writing(addr,buf, 3);
buf[0] = 1;
buf[1] = 12;
buf[2] = 0;
int b = I2C_Writing(addr,buf, 3); //just addr and buf (return error code)
printf("%d", b);
}
