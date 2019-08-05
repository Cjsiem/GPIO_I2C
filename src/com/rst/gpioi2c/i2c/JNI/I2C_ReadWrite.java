/*
 * I2C_ReadWrite.java
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
package com.rst.gpioi2c.i2c.JNI;

public class I2C_ReadWrite
{
  public native int cWrite(byte[] data, int address);
  public native int[] cRead(int amount, int address);
                             
  static
  {
     System.loadLibrary("myI2C_ReadWrite");
  }      
   public static void main (String[] args) {
    I2C_ReadWrite obj = new I2C_ReadWrite();
    obj.cRead(1,12);

	}
}
