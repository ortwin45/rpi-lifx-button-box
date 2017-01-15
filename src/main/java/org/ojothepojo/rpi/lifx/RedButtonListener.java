package org.ojothepojo.rpi.lifx;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.ojothepojo.lifx.LifxClient;
import org.ojothepojo.lifx.message.light.request.SetColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RedButtonListener implements GpioPinListenerDigital {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedButtonListener.class);

    private LampState lampState;
    private LifxClient lifxClient;

    public RedButtonListener(LampState lampState, LifxClient lifxClient) {
        this.lampState = lampState;
        this.lifxClient = lifxClient;
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        // display pin state on console
        LOGGER.debug(" --> GPIO PIN STATE CHANGE: " + event.getPin().getName() + " = " + event.getState());
        if (event.getState().isHigh()) {
            try {
                lifxClient.sendMessage(new SetColor("D0:73:D5:13:00:9B", lampState.getNextHue(), 65535, lampState.getBrightness(), 0, 200));
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
            synchronized (lampState) {
                lampState.notify();
            }
        }
    }
}
