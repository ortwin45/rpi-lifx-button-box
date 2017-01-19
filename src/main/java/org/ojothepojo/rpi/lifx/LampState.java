package org.ojothepojo.rpi.lifx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

class LampState {
    private static final Logger LOGGER = LoggerFactory.getLogger(LampState.class);

    private static final int[] brightnessStates = {0, 6553, 32767, 65535};

    private int currentBrightnessIndex = 0;

    private static final Random random = new Random();

    private int hue = 0;

    private boolean isRedButtonHigh = false;

    private boolean isYellowButtonHigh = false;

    private boolean shutdownActivated = false;

    private long previousClick = 0;

    int getBrightness() {
        return brightnessStates[currentBrightnessIndex];
    }

    int getNextBrightness() {
        if (currentBrightnessIndex >= brightnessStates.length - 1) {
            currentBrightnessIndex = 0;
        } else {
            currentBrightnessIndex++;
        }
        return getBrightness();
    }

    int getHue() {
        return hue;
    }

    int getNextHue() {
        hue = random.nextInt(65535);
        return getHue();
    }

    boolean isShutdownActivated() {
        return shutdownActivated;
    }

    void setRedButtonHigh(boolean state) {
        isRedButtonHigh = state;
        checkShutdownSequence();
    }

    void setYellowButtonHigh(boolean state) {
        isYellowButtonHigh = state;
        checkShutdownSequence();
    }

    private void checkShutdownSequence() {
        long timeBetweenClicks = System.currentTimeMillis() - previousClick;
        if (isRedButtonHigh && isYellowButtonHigh & timeBetweenClicks < 1000) {
            shutdownActivated = true;
        }
        previousClick = System.currentTimeMillis();
    }
}
