package org.ojothepojo.rpi.lifx;

import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.ojothepojo.lifx.LifxClient;
import org.ojothepojo.lifx.message.light.request.SetColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * This is the button that handles the brightness / power of the lamp.
 */
public class YellowButtonListener implements GpioPinListenerDigital {
    private static final Logger LOGGER = LoggerFactory.getLogger(YellowButtonListener.class);

    private LampState lampState;
    private LifxClient lifxClient;

    YellowButtonListener(LampState lampState, LifxClient lifxClient) {
        this.lampState = lampState;
        this.lifxClient = lifxClient;
    }

    @Override
    public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
        // display pin state on console
        LOGGER.debug(" --> GPIO PIN STATE CHANGE: " + event.getPin().getName() + " = " + event.getState());

        if (event.getState().isHigh()) {
            lampState.setYellowButtonHigh(true);
            try {
                lifxClient.sendMessage(new SetColor("D0:73:D5:13:00:9B", lampState.getHue(), 0, lampState.getNextBrightness(), 0, 200));
            } catch (IOException e) {
                LOGGER.error(e.getMessage());
            }
            synchronized (lampState) {
                lampState.notify();
            }
        } else {
            lampState.setYellowButtonHigh(false);
        }
    }
}
