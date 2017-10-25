package actors;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.RaspiPin;

import java.time.Duration;

import actors.cmd.ActivationCmd;
import actors.event.DetectedMoveEvt;
import akka.actor.AbstractActor;
import akka.event.LoggingReceive;
import akka.japi.pf.ReceiveBuilder;
import play.Logger;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class MotionSensorActor
    extends AbstractActor {

    private Boolean active;
    private GpioController gpio;
    private GpioPinDigitalInput input;

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return LoggingReceive.create(ReceiveBuilder
                                         .match(ActivationCmd.class, this::setActivation)
                                         .build(), getContext());
    }

    public MotionSensorActor() {
        try {
            gpio = GpioFactory.getInstance();
            input = gpio.provisionDigitalInputPin(RaspiPin.GPIO_11, "MyInput");
        } catch (UnsatisfiedLinkError ex) {
            Logger.debug(" == UnsatisfiedLinkError == ");
        }
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
                sender().tell(new DetectedMoveEvt(), self());
            }
            try {
                Thread.sleep(Duration.ofSeconds(20).toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
