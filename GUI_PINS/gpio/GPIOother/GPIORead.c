/*
 * GPIOREAD.c
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
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#define IN   0
#define OUT  1

#define LOW  0
#define HIGH 1

int
GPIORead(int pin)
{
#define VALUE_MAX 30
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

JNIEXPORT jint JNICALL Java_GPIORead_cGPIORead
  (JNIEnv *env, jobject jobj, jint pin)
{
	return GPIORead(pin);
}

