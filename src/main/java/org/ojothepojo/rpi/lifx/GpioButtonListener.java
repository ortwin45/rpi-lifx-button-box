package org.ojothepojo.rpi.lifx;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.ojothepojo.lifx.LifxClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GpioButtonListener implements GpioPinListenerDigital {
    private static final Logger LOGGER = LoggerFactory.getLogger(GpioButtonListener.class);

    private Object lock;
    private LifxClient lifxClient;

    public GpioButtonListener(Object lock, LifxClient lifxClient) {
        this.lock = lock;
        this.lifxClient = lifxClient;
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        // display pin state on console
        LOGGER.debug(" --> GPIO PIN STATE CHANGE: " + event.getPin().getName() + " = " + event.getState());
//        try {
//            lifxClient.sendMessage(new GetColor());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        synchronized (lock) {
            lock.notify();
        }
    }
}
