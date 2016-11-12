package org.ojothepojo.rpi.lifx;


import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
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
    private GpioPinDigitalInput myButton;
    private GpioPinDigitalOutput myLed;

    private final Object lock = new Object();

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
            Thread.sleep(500);
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
        myButton = gpio.provisionDigitalInputPin(
                RaspiPin.GPIO_02,             // PIN NUMBER
                "MyButton",                   // PIN FRIENDLY NAME (optional)
                PinPullResistance.PULL_DOWN); // PIN RESISTANCE (optional)

        myLed = gpio.provisionDigitalOutputPin(
                RaspiPin.GPIO_04,   // PIN NUMBER
                "MyLED",           // PIN FRIENDLY NAME (optional)
                PinState.LOW);      // PIN STARTUP STATE (optional)

        myButton.setShutdownOptions(true);
        myLed.setShutdownOptions(true, PinState.LOW);

        myButton.setDebounce(100);
        myButton.addListener(new GpioButtonListener(myLed, lock, lifxClient));
        LOGGER.debug("Init done...");
    }

    private void shutdown() {
        LOGGER.info("Shutting down...");
        gpio.shutdown();
    }
}
