package service;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.impl.GpioControllerImpl;

public class GpioServiceImpl
    implements GpioService {

    @Override
    public GpioController getGpio() {
        GpioController gpio;
        try {
            gpio = GpioFactory.getInstance();
        } catch (UnsatisfiedLinkError ex) {
            gpio = new GpioControllerImpl();
        }
        return gpio;
    }
}
