/*
 * GUI_I2C.java
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


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class GUI_I2C extends JPanel {

        JPanel readPanel = new JPanel();
        JPanel writePanel = new JPanel();
        JPanel empty = new JPanel();
        JPanel clearPanel = new JPanel();

        JButton read = new JButton("Read");
        JLabel readAddressLabel = new JLabel("Input Address");
        JTextField readAddress = new JTextField();
        JLabel readAmountLabel = new JLabel("Input Amount");
        JTextField readAmount = new JTextField();
        
        JLabel answer = new JLabel("");

        JButton write = new JButton("Write");
        JLabel writeAddressLabel = new JLabel("Input Address");
        JTextField writeAddress = new JTextField();
        JLabel writeBytesLabel = new JLabel("Input Bytes (Seperate by Commas)");
        JTextField writeBytes = new JTextField(1);
        
        JButton clear = new JButton("Clear");

        public void addButtonActionListener(ActionListener al) {
            read.addActionListener(al);
			write.addActionListener(al);
			clear.addActionListener(al);
        }

        public GUI_I2C() {
            read.setActionCommand("Read");
            write.setActionCommand("Write");
            clear.setActionCommand("Clear");

            readPanel.setLayout(new GridLayout(1,5));
            readPanel.add(read);
            readPanel.add(readAddressLabel);
            readPanel.add(readAddress);
            readPanel.add(readAmountLabel);
            readPanel.add(readAmount);
            
            empty.add(answer);

            writePanel.setLayout(new GridLayout(1,5));
            writePanel.add(write);
            writePanel.add(writeAddressLabel);
            writePanel.add(writeAddress);
            writePanel.add(writeBytesLabel);
            writePanel.add(writeBytes);

			clearPanel.add(clear);

            this.setLayout(new GridLayout(0,1));
            this.add(readPanel);
            this.add(empty);
            this.add(writePanel);
            this.add(clearPanel);
      }

        public static void main (String[] args) {
            JFrame frame = new JFrame("I2C");
            frame.setSize(500,300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            GUI_I2C gui = new GUI_I2C();
            I2C_ReadWrite i2c = new I2C_ReadWrite();
			I2CController controller = new I2CController(i2c, gui);
			
            frame.getContentPane().add(gui);
            frame.setVisible(true);
        }
}

