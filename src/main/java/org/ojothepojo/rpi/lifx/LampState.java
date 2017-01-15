package org.ojothepojo.rpi.lifx;

import java.util.Random;

class LampState {
    private static final int[] brightnessStates = {0, 6553, 32767, 65535};

    private static int currentBrightnessIndex = 0;

    private static final Random random = new Random();

    private static int hue = 0;

    static int getBrightness() {
        return brightnessStates[currentBrightnessIndex];
    }

    static int getNextBrightness() {
        if (currentBrightnessIndex >= brightnessStates.length - 1) {
            currentBrightnessIndex = 0;
        } else {
            currentBrightnessIndex++;
        }
        return getBrightness();
    }

    static int getHue() {
        return hue;
    }

    static int getNextHue() {
        hue = random.nextInt(65535);
        return getHue();
    }

}
