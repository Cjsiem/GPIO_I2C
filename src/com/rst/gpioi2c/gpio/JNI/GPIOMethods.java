package com.rst.gpioi2c.gpio.JNI;

public class GPIOMethods
{
  public native int cExport(int pin);
  public native int cUnExport(int pin);   
  public native int cDirection(int pin, boolean dir); //dir is 0 for read and 1 for write
  public native int cGPIOWrite(int pin, boolean value); 
  public native int cGPIORead(int pin);
  public native int cReadDirection(int pin);
  public native int cPress(int pin);//Declaring the native function
  public native int cStopInterrupt();
  
                          
  static
  {  
     System.loadLibrary("myGPIOMethods");
  }          
  
  public static void main (String[] args) {
    GPIOMethods obj = new GPIOMethods();
    obj.cExport(17);
		System.out.println(obj.cReadDirection(17));
	}
  
}



