package org.ojothepojo.rpi.lifx;


import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import org.ojothepojo.lifx.LifxClient;
import org.ojothepojo.lifx.message.device.request.GetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RasPiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(RasPiClient.class);

    private final GpioController gpio = GpioFactory.getInstance();
    private LifxClient lifxClient;
    private GpioPinDigitalInput redButton;
    private GpioPinDigitalInput yellowButton;

    private final LampState lock = new LampState();

    public static void main(String[] args) {
        LOGGER.info("Starting...");
        RasPiClient rasPiClient = new RasPiClient();

        rasPiClient.startLifxClient();
        rasPiClient.initialize();
        rasPiClient.doWait();
        rasPiClient.shutdown();
    }

    private void startLifxClient() {
        try {
            lifxClient = new LifxClient();
            lifxClient.startListenerThread();
            lifxClient.sendMessage(new GetService());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doWait() {
        boolean done = false;
        while (!done) {
            synchronized (lock) {
                try {
                    lock.wait();
                    LOGGER.debug("I'm done waiting");
                } catch (InterruptedException e) {
                    LOGGER.debug("Who woke me up?");
                }
            }
        }
    }

    private void initialize() {
        LOGGER.debug("Starting init...");
        redButton = gpio.provisionDigitalInputPin(
                RaspiPin.GPIO_02,             // PIN NUMBER
                "RedButton",           // PIN FRIENDLY NAME (optional)
                PinPullResistance.PULL_DOWN); // PIN RESISTANCE (optional)
        yellowButton = gpio.provisionDigitalInputPin(
                RaspiPin.GPIO_03,             // PIN NUMBER
                "YellowButton",        // PIN FRIENDLY NAME (optional)
                PinPullResistance.PULL_DOWN); // PIN RESISTANCE (optional)

        redButton.setShutdownOptions(true);
        redButton.setDebounce(100);
        redButton.addListener(new RedButtonListener(lock, lifxClient));

        yellowButton.setShutdownOptions(true);
        yellowButton.setDebounce(100);
        yellowButton.addListener(new YellowButtonListener(lock, lifxClient));

        LOGGER.debug("Init done...");
    }

    private void shutdown() {
        LOGGER.info("Shutting down...");
        gpio.shutdown();
    }
}
