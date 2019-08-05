/*
* RguiController.java
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
package com.rst.gpioi2c.gpio;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.Runtime.*;

public class RguiController {


        public static int pinArray[] = {4, 5, 6, 12, 13, 16, 17, 22, 23, 24, 25, 26, 27};

        Rgui rgui;
        GPIOPin gpio;

        ActionListener al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String action = e.getActionCommand();
                if (action.equals("Read")) {
                    gpio.cancelInterrupt();
                    rgui.interruptLabel.setText("Off");
                    rgui.bWrite.setEnabled(false);
                    rgui.bRead.setEnabled(true);
                    rgui.interrupt.setEnabled(true);
                    gpio.direction(0);
                }
                else if(action.equals("Write")) {
                    gpio.cancelInterrupt();
                    rgui.interruptLabel.setText("Off");
                    rgui.bWrite.setEnabled(true);
                    rgui.bRead.setEnabled(true);
                    rgui.interrupt.setEnabled(false);
                    gpio.direction(1);
                }
                else if(action.equals("Interrupt")) {
                    if(rgui.interrupt.isSelected()) {
                        interruptThread iT = new interruptThread(rgui, gpio);
                        iT.start();
                        rgui.interruptLabel.setText("On");
                    }
                    else {
                        rgui.interruptLabel.setText("Off");
                        gpio.cancelInterrupt();
                    }
                }
                else if(action.equals("bRead")) {
                    rgui.output.setText(Integer.toString(gpio.readPin()));
                }
                else if(action.equals("bWrite")) {
                    gpio.writePin(Integer.parseInt(rgui.input.getText()));
                }
            }
        };

        public RguiController(Rgui rgui, GPIOPin gpio) {
            this.rgui = rgui;
            this.gpio = gpio;

            rgui.addButtonActionListener(al);
            int d = gpio.checkDirection();
            this.rgui.read.setSelected(d == 0);
            this.rgui.write.setSelected(d == 1);
            this.rgui.bWrite.setEnabled(d == 1);
        }

        private static class interruptThread extends Thread {

                Rgui rgui;
                GPIOPin gpio;

                public interruptThread(Rgui rgui, GPIOPin gpio) {
                    this.rgui = rgui;
                    this.gpio = gpio;
                }

                public void run() {
                        int a = gpio.pressInt();
                        if(a == 1) {
                            rgui.interruptLabel.setText("Triggered");
                            this.rgui.interrupt.setSelected(false);
                        }
                        else if(a == 0) {
                            rgui.interruptLabel.setText("cancelled");
                        }
                        else {
                            rgui.interruptLabel.setText("Off");
                            System.out.println("error");
                        
                    }
                }
        };




}

