package actors;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.RaspiPin;

import java.time.Duration;

import actors.cmd.ActivationCmd;
import actors.cmd.TakePhotoCmd;
import akka.actor.AbstractActor;
import akka.event.LoggingReceive;
import akka.japi.pf.ReceiveBuilder;
import play.Logger;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class MotionSensorActor
    extends AbstractActor {

    private Boolean active;
    private final GpioController gpio;
    private final GpioPinDigitalInput input;

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return LoggingReceive.create(ReceiveBuilder
                                         .match(ActivationCmd.class, this::setActivation)
                                         .build(), getContext());
    }

    public MotionSensorActor() {
        gpio = GpioFactory.getInstance();
        input = gpio.provisionDigitalInputPin(RaspiPin.GPIO_11, "MyInput");
    }

    private void setActivation(ActivationCmd cmd) {
        Logger.debug("MotionSensorActor: setActivation:  " + cmd);

        active = cmd.isActive();
        readMotionSensor();
    }

    private void readMotionSensor() {

        while (active) {
            if (input.getState().isHigh()) {
                Logger.debug("== Move detected!!!!!");
                sender().tell(new TakePhotoCmd(), self());
            }
            try {
                Thread.sleep(Duration.ofSeconds(1).toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
