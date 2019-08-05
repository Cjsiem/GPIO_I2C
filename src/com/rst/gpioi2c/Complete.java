/*
 * I2C_AND_GPIO.java
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
package com.rst.gpioi2c;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import com.rst.gpioi2c.gpio.*;
import com.rst.gpioi2c.i2c.*;
import com.rst.gpioi2c.gpio.JNI.GPIOMethods;
import com.rst.gpioi2c.i2c.JNI.I2C_ReadWrite;

public class Complete {
	
	
	
	public static void main (String[] args) {
            JFrame frame = new JFrame("I2C and GPIO");
            frame.setSize(1000, 1000);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            GUI_I2C gui = new GUI_I2C();
            I2C_ReadWrite i2c = new I2C_ReadWrite();
			I2CController controller = new I2CController(i2c, gui);
			
           
            ShutDownTask shutDownTask = new ShutDownTask();
            Runtime.getRuntime().addShutdownHook(shutDownTask);

            
            for(int i = 0; i < RguiController.pinArray.length; i++) {
                Rgui rgui = new Rgui(RguiController.pinArray[i]);
                GPIOPin gpio = new GPIOPin(rgui.pin);
                RguiController rgc = new RguiController(rgui, gpio);
                gui.add(rgui);

            }
            

           frame.getContentPane().add(gui);
            frame.setVisible(true);
        }

        private static class ShutDownTask extends Thread {

                @Override
                public void run() {

                    GPIOMethods gpioM = new GPIOMethods();
                    for(int i = 0; i < RguiController.pinArray.length; i++) {
                        gpioM.cUnExport(RguiController.pinArray[i]);
                    }
                }
        }
        }

