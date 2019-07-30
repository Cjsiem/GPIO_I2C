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


public class GPIOPin {

        GPIOMethods gpio = new GPIOMethods();
        int pin;
        Press press = new Press();
        int stop = 0;

        public GPIOPin(int temp) {
            pin = temp;
            gpio.cExport(pin);
        }

        public void cancelInterrupt() {
            press.cStopInterrupt();
        }
        public void direction(int dir) {
            gpio.cDirection(pin, dir);
        }

        public int readPin() {
            return gpio.cGPIORead(pin);
        }

        public void writePin(int value) {
            gpio.cGPIOWrite(pin, value);
        }

        public int checkDirection() {
            return gpio.cReadDirection(pin);
        }

        public int pressInt() {
            return press.cPress(pin);
        }
}

