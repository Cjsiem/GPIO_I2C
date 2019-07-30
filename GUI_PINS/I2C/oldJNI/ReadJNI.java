/*
 * ReadJNI.java
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


public class ReadJNI
{
  native int[] cRead(int amount, int address);//Declaring the native function
                             
  static
  {
     System.loadLibrary("myRead");//Linking the native library
  }                                      //which we will be creating.
 
  public static void main(String args[]) 
  {
	 int address = 16; //this will normally come in from outside
	 int amount = 2;
	 int data[] = new int[2]; //also will normally come from outside
     ReadJNI obj = new ReadJNI();
     data = obj.cRead(amount, address);//Calling the native function
     System.out.println(data[0]);
     System.out.println(data[1]);
  }
}

