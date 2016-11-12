package org.ojothepojo.rpi.lifx;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.ojothepojo.lifx.LifxClient;
import org.ojothepojo.lifx.message.light.request.GetColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GpioButtonListener implements GpioPinListenerDigital {
    private static final Logger LOGGER = LoggerFactory.getLogger(GpioButtonListener.class);

    private GpioPinDigitalOutput led;
    private Object lock;
    private LifxClient lifxClient;

    public GpioButtonListener(GpioPinDigitalOutput led, Object lock, LifxClient lifxClient) {
        this.led = led;
        this.lock = lock;
        this.lifxClient = lifxClient;
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        // display pin state on console
        LOGGER.debug(" --> GPIO PIN STATE CHANGE: " + event.getPin() + " = " + event.getState());
        led.toggle();
        try {
            lifxClient.sendMessage(new GetColor());
        } catch (IOException e) {
            e.printStackTrace();
        }
        synchronized (lock) {
            lock.notify();
        }
    }
}
