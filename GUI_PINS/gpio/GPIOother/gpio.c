/* blink.c
 *
 * Raspberry Pi GPIO example using sysfs interface.
 * Guillermo A. Amaral B. <g@maral.me>
 *
 * This program blinks GPIO 4 (P1-07) while reading GPIO 24 (P1_18).
 */

#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#define VALUE_MAX 30

#define IN   0
#define OUT  1

#define LOW  0
#define HIGH 1

#define PIN  17 /* P1-18 */
#define POUT 17  /* P1-07 */

static int GPIOExport(int pin);
static int GPIOUnexport(int pin);
static int GPIODirection(int pin, int dir);
//static int GPIORead(int pin);
static int GPIOWrite(int pin, int value);

int
main(int argc, char *argv[])
{


    /*
     * Enable GPIO pins
     */
    if (-1 == GPIOExport(POUT))
	return(1);

    /*
     * Set GPIO directions
     */
    if (-1 == GPIODirection(POUT, OUT)){
	return(2);
}
	/*
	 * Write GPIO value
	 */
	if (-1 == GPIOWrite(POUT,0)){
	    return(3);
}
	/*
	 * Read GPIO value
	 *
	printf("I'm reading %d in GPIO %d\n", GPIORead(PIN), PIN);

	usleep(500 * 1000);
    
    
*/
    
    if (-1 == GPIOUnexport(POUT)){
	return(4);
}
    return(0);
}

int
GPIOExport(int pin)
{

    char buffer[BUFFER_MAX];
    ssize_t bytes_written;
    int fd;

    fd = open("/sys/class/gpio/export", O_WRONLY);
    if (-1 == fd) {
	fprintf(stderr, "Failed to open export for writing!\n");
	return(-1);
    }

    bytes_written = snprintf(buffer, BUFFER_MAX, "%d", pin);
    write(fd, buffer, bytes_written);
    close(fd);
    return(0);
}

int
GPIOUnexport(int pin)
{
    char buffer[BUFFER_MAX];
    ssize_t bytes_written;
    int fd;

    fd = open("/sys/class/gpio/unexport", O_WRONLY);
    if (-1 == fd) {
	fprintf(stderr, "Failed to open unexport for writing!\n");
	return(-1);
    }

    bytes_written = snprintf(buffer, BUFFER_MAX, "%d", pin);
    write(fd, buffer, bytes_written);
    close(fd);
    return(0);
}

int
GPIODirection(int pin, int dir)
{
    static const char s_directions_str[]  = "in\0out";

#define DIRECTION_MAX 35
    char path[DIRECTION_MAX];
    int fd;

    snprintf(path, DIRECTION_MAX, "/sys/class/gpio/gpio%d/direction", pin);
    fd = open(path, O_WRONLY);
    if (-1 == fd) {
	fprintf(stderr, "Failed to open gpio direction for writing!\n");
	return(-1);
    }

    if (-1 == write(fd, &s_directions_str[IN == dir ? 0 : 3], IN == dir ? 2 : 3)) {
	fprintf(stderr, "Failed to set direction!\n");
	return(-1);
    }

    close(fd);
    return(0);
}
/*
int
GPIORead(int pin)
{

    char path[VALUE_MAX];
    char value_str[3];
    int fd;

    snprintf(path, VALUE_MAX, "/sys/class/gpio/gpio%d/value", pin);
    fd = open(path, O_RDONLY);
    if (-1 == fd) {
	fprintf(stderr, "Failed to open gpio value for reading!\n");
	return(-1);
    }

    if (-1 == read(fd, value_str, 3)) {
	fprintf(stderr, "Failed to read value!\n");
	return(-1);
    }

    close(fd);

    return(atoi(value_str));
}
*/
int
GPIOWrite(int pin, int value)
{
    static const char s_values_str[] = "01";

    char path[VALUE_MAX];
    int fd;

    snprintf(path, VALUE_MAX, "/sys/class/gpio/gpio%d/value", pin);
    fd = open(path, O_WRONLY);
    if (-1 == fd) {
	fprintf(stderr, "Failed to open gpio value for writing!\n");
	return(-1);
    }

    if (1 != write(fd, &s_values_str[LOW == value ? 0 : 1], 1)) {
	fprintf(stderr, "Failed to write value!\n");
	return(-1);
    }

    close(fd);
    return(0);
}

