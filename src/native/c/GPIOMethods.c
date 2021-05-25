/*
 * GPIOMethods.c
 * 
 * Copyright 2019  <pi@raspberrypi>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 * 
 */
#include <jni.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <wiringPi.h>
#include <pthread.h>
#include <errno.h>
#include <string.h>

#define BUFFER_MAX 3
#define VALUE_MAX 30
#define DIRECTION_MAX 35

#define IN   0
#define OUT  1

#define LOW  0
#define HIGH 1

#define BUTTON_PIN 0

pthread_cond_t isr_cond = PTHREAD_COND_INITIALIZER;
pthread_mutex_t isr_mtx = PTHREAD_MUTEX_INITIALIZER;
unsigned int isr_count = 0;
bool first_call = true;

JNIEXPORT jint JNICALL Java_com_rst_gpioi2c_gpio_JNI_GPIOMethods_cExport
(JNIEnv *env, jclass jobj, jint pin){

	char buffer[BUFFER_MAX];
	ssize_t bytes_written;
	int fd;

	char path[VALUE_MAX];
	int fda;
	snprintf(path, VALUE_MAX, "/sys/class/gpio/gpio%d", pin);
	fda = access(path, F_OK);
	if (-1 != fda) {
		close(fda);
		return (0);
	}
	close(fda);
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

JNIEXPORT jint JNICALL Java_com_rst_gpioi2c_gpio_JNI_GPIOMethods_cUnExport
(JNIEnv *env, jclass jobj, jint pin){
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

JNIEXPORT jint JNICALL Java_com_rst_gpioi2c_gpio_JNI_GPIOMethods_cDirection
(JNIEnv *env, jclass jobj, jint pin, jboolean dir){

	static const char s_directions_str[]  = "in\0out";
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
		return(-2);
	}
	close(fd);
	return(0);
}

JNIEXPORT jint JNICALL Java_com_rst_gpioi2c_gpio_JNI_GPIOMethods_cGPIOWrite
(JNIEnv *env, jclass jobj, jint pin, jboolean value){
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
		return(-2);
	}

	close(fd);
	return(0);
}

JNIEXPORT jint JNICALL Java_com_rst_gpioi2c_gpio_JNI_GPIOMethods_cGPIORead
(JNIEnv *env, jclass jobj, jint pin){


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
		return(-2);
	}

	close(fd);

	return(atoi(value_str));
}

JNIEXPORT jint JNICALL Java_com_rst_gpioi2c_gpio_JNI_GPIOMethods_cReadDirection
(JNIEnv *env, jobject job, jint pin){
	char path[DIRECTION_MAX];
	char value_str[3];
	int fd;

	snprintf(path, DIRECTION_MAX, "/sys/class/gpio/gpio%d/direction", pin);
	fd = open(path, O_RDONLY);
	if (-1 == fd) {
		fprintf(stderr, "Failed to open gpio value for reading!\n");
		return(-1);
	}

	if (-1 == read(fd, value_str, 3)) {
		fprintf(stderr, "Failed to read value!\n");
		return(-2);
	}

	close(fd);
	if(value_str[0] == 'i'){
		return 0;
	}
	else{
		return 1;
	}
}

void myInterrupt(void)
{
	fprintf(stderr, "starting myinterrupt");
	pthread_mutex_lock(&isr_mtx);
	isr_count = 1;
	pthread_cond_signal(&isr_cond);
	pthread_mutex_unlock(&isr_mtx);

	fprintf(stderr, "myinterrupt complete");
}
int pinArray[28] = {30,31, 8,9,7, 21,22,11, 10, 13, 12, 14, 26, 23, 15, 16, 27, 0, 1, 24, 28, 29, 3, 4, 5, 6, 25, 2};

	JNIEXPORT jint JNICALL Java_com_rst_gpioi2c_gpio_JNI_GPIOMethods_cPress
(JNIEnv *env, jobject jobj, jint pin, jint d)
{
	isr_count = 0;
	if (first_call){
		fprintf (stderr, "first call, wiringpisetup");
		if (wiringPiSetup () < 0) {
			fprintf (stderr, "Unable to setup wiringPi: %s\n", strerror (errno));
			return -1;
		}

		// set Pin 17/0 generate an interrupt on high-to-low transitions
		// and attach myInterrupt() to the interrupt

		if ( wiringPiISR (pinArray[pin], INT_EDGE_BOTH, &myInterrupt) < 0 ) {
			fprintf (stderr, "Unable to setup ISR: %s\n", strerror (errno));
			return -2;
		}

	}
	first_call = false;

	pthread_mutex_lock(&isr_mtx);
	pthread_cond_wait(&isr_cond, &isr_mtx);
	pthread_mutex_unlock(&isr_mtx);
	/*
	   pthread_mutex_destroy(&isr_mtx);
	   pthread_cond_destroy(&isr_cond);
	   */
	return isr_count;

}

JNIEXPORT jint JNICALL Java_com_rst_gpioi2c_gpio_JNI_GPIOMethods_cStopInterrupt
(JNIEnv *env, jobject jobj){
	pthread_mutex_lock(&isr_mtx);
	isr_count = 0;
	pthread_cond_signal(&isr_cond);
	pthread_mutex_unlock(&isr_mtx);
}


