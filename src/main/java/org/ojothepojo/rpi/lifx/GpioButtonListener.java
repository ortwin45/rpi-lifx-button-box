package org.ojothepojo.rpi.lifx;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GpioButtonListener implements GpioPinListenerDigital {
    private static final Logger LOGGER = LoggerFactory.getLogger(GpioButtonListener.class);

    private GpioPinDigitalOutput led;

    public GpioButtonListener(GpioPinDigitalOutput led) {
        this.led = led;
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        // display pin state on console
        LOGGER.debug(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
        led.toggle();
        wakeup();
    }

    synchronized void wakeup() {
        led.notify();
    }
}