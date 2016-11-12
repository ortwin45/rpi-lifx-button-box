package org.ojothepojo.rpi.lifx;


import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;

public class RasPiClient {
    private final GpioController gpio = GpioFactory.getInstance();
    GpioPinDigitalInput myButton;
    GpioPinDigitalOutput myLed;

    public static void main(String[] args) {
        RasPiClient rasPiClient = new RasPiClient();

        rasPiClient.initialize();
        rasPiClient.work();
        rasPiClient.shutdown();
    }

    private void work() {
        try {
            while (true) {
                Thread.sleep(500);
                myLed.toggle();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void initialize() {
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

        myButton.addListener(new GpioButtonListener());
    }

    private void shutdown() {
        gpio.shutdown();
    }
}
