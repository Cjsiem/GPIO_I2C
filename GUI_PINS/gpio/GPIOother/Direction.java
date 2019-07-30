/*
 * Direction.java
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


public class Direction
{
  native int cDirection(int pin, int direction);//Declaring the native function
                             
  static
  {
     System.loadLibrary("myDirection");//Linking the native library
  }                                      //which we will be creating.
 
  public static void main(String args[]) 
  {
	 if(args.length == 0){
		 System.out.println("needs and argument");
		 System.exit(0);
	 }
	 Direction obj = new Direction();
     System.out.println(obj.cDirection(Integer.parseInt(args[0]), (Integer.parseInt(args[1]))));
  }
}

