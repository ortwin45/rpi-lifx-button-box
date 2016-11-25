package org.ojothepojo.rpi.lifx;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.ojothepojo.lifx.LifxClient;
import org.ojothepojo.lifx.message.device.request.SetPower;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class YellowButtonListener implements GpioPinListenerDigital {
    private static final Logger LOGGER = LoggerFactory.getLogger(YellowButtonListener.class);

    private Object lock;
    private LifxClient lifxClient;
    private boolean powerLevel = true;

    public YellowButtonListener(Object lock, LifxClient lifxClient) {
        this.lock = lock;
        this.lifxClient = lifxClient;
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        // display pin state on console
        LOGGER.debug(" --> GPIO PIN STATE CHANGE: " + event.getPin().getName() + " = " + event.getState());

        if (event.getState().isHigh()) {
            try {
                powerLevel = !powerLevel;
                lifxClient.sendMessage(new SetPower("D0:73:D5:13:00:9B", Util.getIpAddress(), powerLevel));
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
            synchronized (lock) {
                lock.notify();
            }
        }
    }
}
