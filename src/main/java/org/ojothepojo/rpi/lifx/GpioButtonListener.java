package org.ojothepojo.rpi.lifx;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GpioButtonListener implements GpioPinListenerDigital {
    private static final Logger LOGGER = LoggerFactory.getLogger(GpioButtonListener.class);

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        // display pin state on console
        System.out.println(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
        LOGGER.debug(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());

    }
}