package org.ojothepojo.rpi.lifx;


import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RasPiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(RasPiClient.class);

    private final GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalInput myButton;
    private GpioPinDigitalOutput myLed;

    public static void main(String[] args) {
        LOGGER.info("Starting...");
        RasPiClient rasPiClient = new RasPiClient();

        rasPiClient.initialize();
        //rasPiClient.work();
        rasPiClient.doWait();
        rasPiClient.shutdown();
    }

    private void doWait() {
        boolean done = false;
        while (!done) {
            synchronized (this) {
                try {
                    this.wait();
                    LOGGER.debug("I'm done waiting");
                } catch (InterruptedException e) {
                    LOGGER.debug("Who woke me up?");
                }
            }
        }
    }

    private void work() {
        try {
            while (true) {
                Thread.sleep(500);
                myLed.toggle();
                LOGGER.debug("Toggling..." + myLed.getState());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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

        myButton.addListener(new GpioButtonListener(myLed));
        LOGGER.debug("Init done...");
    }

    private void shutdown() {
        LOGGER.info("Shutting down...");
        gpio.shutdown();
    }
}
