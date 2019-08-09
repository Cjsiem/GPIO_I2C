/*
 * I2C_ReadWrite.c
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

#include <stdio.h>
#include <errno.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include <linux/i2c-dev.h>
#include <sys/ioctl.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdio.h>
#include <jni.h>

int I2C_Writing(int addr, unsigned char* buf, int bytes) {
    int file;
    char *filename = (char*)"/dev/i2c-1";
    if ((file = open(filename, O_WRONLY)) < 0) {
        printf("Failed to open the bus.");
        return -1;
    }
    if (ioctl(file,I2C_SLAVE,addr) < 0) {
        printf("Failed to acquire bus access and/or talk to slave.\n");
        return -2;
    }
    int a = write(file, buf, bytes);
    if(a != bytes) {
        printf("Failed to write to the i2c bus.\n");
        printf("\n\n");
        return -3;
    }
    return a;
}

void I2C_Reading(int addr, char *buf, int bytes) {
    int file;
    char *filename = (char*)"/dev/i2c-1";
    if ((file = open(filename, O_RDONLY)) < 0) {
        printf("Failed to open the bus.");
        buf[0] = -1;
        buf[1] = 1;
    }
    if (ioctl(file,I2C_SLAVE,addr) < 0) {
        printf("Failed to acquire bus access and/or talk to slave.\n");
        buf[0] = -1;
        buf[1] = 2;
    }
    if (read(file,buf,bytes) != bytes) {
        printf("Failed to read from the i2c bus.\n");
        printf("\n\n");
        buf[0] = -1;
        buf[1] = 3;
    }
}

JNIEXPORT jint JNICALL Java_com_rst_gpioi2c_i2c_JNI_I2C_1ReadWrite_cWrite
(JNIEnv *env, jobject jobj, jbyteArray data, jint address) {
    int n = (*env)->GetArrayLength(env, data);
    jboolean isCopy;
    unsigned char* buf = (*env)->GetByteArrayElements(env, data, &isCopy);
    return I2C_Writing(address, buf, n);
}

JNIEXPORT jintArray JNICALL Java_com_rst_gpioi2c_i2c_JNI_I2C_1ReadWrite_cRead
(JNIEnv *env, jobject jobj, jint size, jint address) {
    char* buf;
    char temp[size];
    buf = temp;
    I2C_Reading(address, buf, size);
    jintArray result;
    result = (*env)->NewIntArray(env, size);
    int i;
    jint fill[size];
    for (i = 0; i < size; i++) {
        fill[i] = temp[i];
    }
    (*env)->SetIntArrayRegion(env, result, 0, size, fill);
    return result;
}
