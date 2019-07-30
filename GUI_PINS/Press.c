/*
 * Press.c
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
#include <string.h>
#include <errno.h>
#include <stdlib.h>
#include <wiringPi.h>
#include <limits.h>
#include <unistd.h>
#include <pthread.h>
#include <jni.h>
#include <time.h>
#include <errno.h>
#include <sys/time.h>



#define BUTTON_PIN 0

pthread_cond_t isr_cond = PTHREAD_COND_INITIALIZER;
pthread_mutex_t isr_mtx = PTHREAD_MUTEX_INITIALIZER;
unsigned int isr_count = 0;


void myInterrupt(void)
{
    pthread_mutex_lock(&isr_mtx);
    isr_count = 1;
    pthread_cond_signal(&isr_cond);
    pthread_mutex_unlock(&isr_mtx);

}
int pinArray[27] = {30,31, 8,9,7, 21,22,11, 10, 13, 12, 14, 26, 23, 15, 16, 27, 0, 1, 24, 28, 29, 3, 4, 5, 6, 25};

JNIEXPORT jint JNICALL Java_Press_cPress
(JNIEnv *env, jobject jobj, jint pin, jint d)
{
    isr_count = 0;
    if (wiringPiSetup () < 0) {
        fprintf (stderr, "Unable to setup wiringPi: %s\n", strerror (errno));
        return 3;
    }

    // set Pin 17/0 generate an interrupt on high-to-low transitions
    // and attach myInterrupt() to the interrupt
    if ( wiringPiISR (pinArray[pin], INT_EDGE_BOTH, &myInterrupt) < 0 ) {
        fprintf (stderr, "Unable to setup ISR: %s\n", strerror (errno));
        return 4;
    }

    pthread_mutex_lock(&isr_mtx);
    /*
       struct timeval tv;
       struct timespec ts;

       gettimeofday(&tv, NULL);
       ts.tv_sec = time(NULL) + d / 1000;
       ts.tv_nsec = tv.tv_usec * 1000 + 1000 * 1000 * (d % 1000);
       ts.tv_sec += ts.tv_nsec / (1000 * 1000 * 1000);
       ts.tv_nsec %= (1000 * 1000 * 1000);
     */
    pthread_cond_wait(&isr_cond, &isr_mtx);
    pthread_mutex_unlock(&isr_mtx);
    
    return isr_count;

    //return 0 when it times out
    //return 1 if switch hit off
    //short delay as input

}

JNIEXPORT jint JNICALL Java_Press_cStopInterrupt
  (JNIEnv *env, jobject jobj){
    pthread_mutex_lock(&isr_mtx);
    isr_count = 0;
    pthread_cond_signal(&isr_cond);
    pthread_mutex_unlock(&isr_mtx);
  }

