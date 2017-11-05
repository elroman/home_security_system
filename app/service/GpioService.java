package service;

import com.pi4j.io.gpio.GpioController;

public interface GpioService {

    GpioController getGpio();
}
