/*
 * GPIOPin.java
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
package com.rst.gpioi2c.gpio;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.rst.gpioi2c.gpio.JNI.*;

public class GPIOPin {

    GPIOMethods gpio = new GPIOMethods();
    int pin;

    public GPIOPin(int temp) {
        pin = temp;
        if (gpio.cExport(pin) != 0) {
            JFrame frame = new JFrame("Export Error");
            JOptionPane.showMessageDialog(frame, "Failed to open export for writing!\n");
        }
        ;
    }

    public void cancelInterrupt() {
        gpio.cStopInterrupt();
    }

    public void direction(boolean dir) {
        int temp = gpio.cDirection(pin, dir); //dir is 0 for read and 1 for write
        if (temp == -1) {
            JFrame frame = new JFrame("Direction Error");
            JOptionPane.showMessageDialog(frame, "Failed to open gpio direction for writing!\n");
        } else if (temp == -2) {
            JFrame frame = new JFrame("Direction Error");
            JOptionPane.showMessageDialog(frame, "Failed to set direction!\n");
        }
    }


    public int readPin() {
        int temp = gpio.cGPIORead(pin);
        if (temp == -1) {
            JFrame frame = new JFrame("Read Error");
            JOptionPane.showMessageDialog(frame, "Failed to open gpio value for reading!\n");
        } else if (temp == -2) {
            JFrame frame = new JFrame("Read Error");
            JOptionPane.showMessageDialog(frame, "Failed to Read value!\n");
        }
        return temp;
    }

    public void writePin(boolean value) {
        int temp = gpio.cGPIOWrite(pin, value);
        if (temp == -1) {
            JFrame frame = new JFrame("Write Error");
            JOptionPane.showMessageDialog(frame, "Failed to open gpio value for writing!\n");
        } else if (temp == -2) {
            JFrame frame = new JFrame("Write Error");
            JOptionPane.showMessageDialog(frame, "Failed to write value!\n");
        }
    }

    public int checkDirection() {
        int temp = gpio.cReadDirection(pin);
        if (temp == -1) {
            JFrame frame = new JFrame("Read Direction Error");
            JOptionPane.showMessageDialog(frame, "Failed to open gpio direction for reading!\n");
        } else if (temp == -2) {
            JFrame frame = new JFrame("Read Direction Error");
            JOptionPane.showMessageDialog(frame, "Failed to set reading!\n");
        }
        return temp;
    }

    public int pressInt() {
        int temp = gpio.cPress(pin);
        if (temp == -1) {
            JFrame frame = new JFrame("Press Error");
            JOptionPane.showMessageDialog(frame, "Unable to setup wiringPi\n");
        } else if (temp == -2) {
            JFrame frame = new JFrame("Press Error");
            JOptionPane.showMessageDialog(frame, "Unable to setup ISR");
        }
        return temp;
    }
}

