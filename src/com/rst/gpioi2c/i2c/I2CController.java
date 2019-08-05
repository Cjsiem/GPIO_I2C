/*
* I2CController.java
*
* Copyright 2019  <pi@raspberrypi>
*
* This program is free software;
you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation;
either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY;
without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program;
if not, write to the Free Software
* Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
* MA 02110-1301, USA.
*
*
*/
package com.rst.gpioi2c.i2c;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.Runtime.*;
import java.util.*;
import com.rst.gpioi2c.i2c.JNI.*;
import com.rst.gpioi2c.i2c.*;

public class I2CController {

        I2C_ReadWrite i2c;
        GUI_I2C gui;
        byte[] writeArray;

        public void writeAction(String str) {
            String temp = "";
            ArrayList<Integer> list = new ArrayList<>();
            for(int i = 0; i < (str.length()); i++) {
                if((str.charAt(i) == ',')) {
                    list.add(Integer.parseInt(temp));
                    temp = "";
                }
                else if(i == (str.length() - 1)) {
                    temp += str.charAt(i);
                    list.add(Integer.parseInt(temp));
                    temp = "";
                }
                else {
                    temp += str.charAt(i);
                }
            }
            writeArray = new byte[list.size()];
            for(int i = 0; i < list.size(); i++) {
                int a = (list.get(i));
                writeArray[i] = (byte) a;
            }
        }

        public String intArraytoString(int[] intArray) {
            String str = "";
            for(int i = 0; i < intArray.length; i++) {
                str += Integer.toString(intArray[i]);
                str += " ";
            }
            return str;
        }

        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String action = e.getActionCommand();
                if (action.equals("Read")) {
                    if((gui.readAmount.getText().equals("")) || (gui.readAddress.getText().equals(""))) {
                        System.out.println("error");
                    }
                    else {
                        gui.answer.setText(intArraytoString(i2c.cRead(Integer.parseInt(gui.readAmount.getText()), Integer.parseInt(gui.readAddress.getText()))));
                        gui.readAmount.setText("");
                    }
                }
                else if(action.equals("Write")) {
                    if((gui.writeBytes.getText().equals("")) || (gui.writeAddress.getText().equals(""))) {
                        System.out.println("error");
                    }
                    else {
                        writeAction(gui.writeBytes.getText());
                        i2c.cWrite(writeArray, Integer.parseInt(gui.writeAddress.getText()));
                        gui.writeBytes.setText("");
                    }
                }
                else if(action.equals("Clear")){
					gui.writeAddress.setText("");
					gui.readAddress.setText("");
					gui.readAmount.setText("");
					gui.writeBytes.setText("");
					gui.answer.setText("");
				}
            }
        };

        public I2CController(I2C_ReadWrite i2c, GUI_I2C gui) {
            this.i2c = i2c;
            this.gui = gui;
            gui.addButtonActionListener(al);
        }



}

