/*
* read-gui.java
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

public class Rgui extends JPanel {

        public int pin;
        JLabel label = new JLabel();
        
        ButtonGroup group = new ButtonGroup();
        JRadioButtonMenuItem read = new JRadioButtonMenuItem("Read");
        JRadioButtonMenuItem write = new JRadioButtonMenuItem("Write");

        

        JButton bRead = new JButton("Read");
        JLabel output = new JLabel();

        ButtonGroup group2 = new ButtonGroup();
        JRadioButtonMenuItem on = new JRadioButtonMenuItem("On");
        JRadioButtonMenuItem off = new JRadioButtonMenuItem("Off");

        JToggleButton interrupt = new JToggleButton("Interrupt");
        JLabel interruptLabel = new JLabel("Off");
        
        
        public void addButtonActionListener(ActionListener al) {
            read.addActionListener(al);
            write.addActionListener(al);
            interrupt.addActionListener(al);
            bRead.addActionListener(al);
            on.addActionListener(al);
            off.addActionListener(al);
        }

        public Rgui(int temp) {
            this.pin = temp;
            this.label.setText("Pin " + Integer.toString(this.pin));
            read.setActionCommand("Read");
            write.setActionCommand("Write");
            interrupt.setActionCommand("Interrupt");
            bRead.setActionCommand("bRead");
            on.setActionCommand("On");
            off.setActionCommand("Off");
            
            group.add(read);
            group.add(write);


            group2.add(on);
            group2.add(off);
            
            this.setLayout(new GridLayout(1,7));
            this.add(label);
            this.add(write);
            this.add(read);
            this.add(bRead);
            this.add(output);
            this.add(on);
            this.add(off);
            this.add(interrupt);
            this.add(interruptLabel);

        }

}

