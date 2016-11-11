package org.ojothepojo.rpi.lifx;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;

public class Initializer {
    public static void main(String[] args) {
        Initializer initializer = new Initializer();
        initializer.initialize();
    }

    public void initialize() {
        final GpioController gpio = GpioFactory.getInstance();

    }
}
