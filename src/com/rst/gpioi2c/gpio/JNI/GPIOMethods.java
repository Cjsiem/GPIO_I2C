package com.rst.gpioi2c.gpio.JNI;

public class GPIOMethods
{
  public native int cExport(int pin);
  public native int cUnExport(int pin);   
  public native int cDirection(int pin, int direction);
  public native int cGPIOWrite(int pin, int value);
  public native int cGPIORead(int pin);
  public native int cReadDirection(int pin);
  
                          
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



