package actors;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.RaspiPin;

import java.util.concurrent.TimeUnit;

import actors.cmd.ActivationCmd;
import actors.cmd.ReadMotionSensorCmd;
import actors.cmd.Ticktack;
import actors.event.DetectedMoveEvt;
import akka.actor.AbstractActor;
import akka.actor.Cancellable;
import akka.event.LoggingReceive;
import akka.japi.pf.ReceiveBuilder;
import play.Logger;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;
import scala.runtime.BoxedUnit;

public class MotionSensorActor
    extends AbstractActor {

    private Boolean active;
    private GpioController gpio;
    private GpioPinDigitalInput input;
    private Cancellable trackerScheduler;

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return LoggingReceive.create(ReceiveBuilder
                                         .match(ActivationCmd.class, this::setActivation)
                                         .match(ReadMotionSensorCmd.class, this::startReadMotionSensor)
                                         .build(), getContext());
    }

    @Override
    public void postStop() {
        trackerScheduler.cancel();
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
        Logger.debug(" == setActivation: ", cmd.isActive());
        if (cmd.isActive()) {
            if (trackerScheduler.isCancelled()) {
                trackerScheduler = getContext().system().scheduler().schedule(
                    Duration.Zero(),
                    Duration.create(500, TimeUnit.MILLISECONDS),
                    self(),
                    new ReadMotionSensorCmd(),
                    getContext().dispatcher(),
                    null
                );
            } else {
                trackerScheduler.cancel();
            }
        }
    }

    private void startReadMotionSensor(ReadMotionSensorCmd cmd) {
        if (input.getState().isHigh()) {
            Logger.debug("== Move detected!!!!!");
            sender().tell(new DetectedMoveEvt(), self());
        }

    }

}
