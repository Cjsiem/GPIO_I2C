/*
 * WriteJNI.c
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
#include <stdio.h>
#include <jni.h>

int I2C_Writing(int addr, unsigned char* buf, int bytes){
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

JNIEXPORT jint JNICALL Java_WriteJNI_cWrite
  (JNIEnv *env, jobject jobj, jbyteArray data, jint address){
	int n = (*env)->GetArrayLength(env, data);
    jboolean isCopy;
    unsigned char* buf = (*env)->GetByteArrayElements(env, data, &isCopy);
    return I2C_Writing(address, buf, n);
  }

