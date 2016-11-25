package org.ojothepojo.rpi.lifx;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.ojothepojo.lifx.LifxClient;
import org.ojothepojo.lifx.message.light.request.SetColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Random;

public class RedButtonListener implements GpioPinListenerDigital {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedButtonListener.class);

    private Object lock;
    private LifxClient lifxClient;

    public RedButtonListener(Object lock, LifxClient lifxClient) {
        this.lock = lock;
        this.lifxClient = lifxClient;
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        // display pin state on console
        LOGGER.debug(" --> GPIO PIN STATE CHANGE: " + event.getPin().getName() + " = " + event.getState());

        try {
            lifxClient.sendMessage(new SetColor("D0:73:D5:13:00:9B", Util.getIpAddress(), randomHue(), 65000, 30000, 0, 200));
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
        }
        synchronized (lock) {
            lock.notify();
        }
    }

    private int randomHue() {
        Random randomHue = new Random();
        return randomHue.nextInt(65535);
    }
}
