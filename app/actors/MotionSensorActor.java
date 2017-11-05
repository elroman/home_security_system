package actors;

import com.google.inject.Inject;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.RaspiPin;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import actors.cmd.ActivationCmd;
import actors.cmd.ReadMotionSensorCmd;
import actors.event.DetectedMoveEvt;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.event.LoggingReceive;
import akka.japi.pf.ReceiveBuilder;
import play.Logger;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;
import scala.runtime.BoxedUnit;
import service.DateUtil;

public class MotionSensorActor
    extends AbstractActor {

    private GpioController gpio;
    private GpioPinDigitalInput input;
    private Cancellable trackerScheduler;

    @Inject
    @Named("securitySystem")
    ActorRef securitySystem;

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return LoggingReceive.create(ReceiveBuilder
                                         .match(ActivationCmd.class, this::setActivation)
                                         .match(ReadMotionSensorCmd.class, this::readMotionSensor)
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

    //    private void setActivation(ActivationCmd cmd) {
    //        Logger.debug("MotionSensorActor: setActivation:  " + cmd);
    //
    //        active = cmd.isActive();
    //
    //        if (active) {
    //            self().tell(new ReadMotionSensorCmd(), ActorRef.noSender());
    //        }
    //    }

    private void readMotionSensor(ReadMotionSensorCmd cmd) {
        if (input.getState().isHigh()) {
            Logger.debug("Move detected:", DateUtil.getStringFromDateTime(new Date()));
            securitySystem.tell(new DetectedMoveEvt(), self());
        }
    }

    private void setActivation(ActivationCmd cmd) {
        if (cmd.isActive()) {
            Logger.debug("Motion sensor was activated !");
            if (trackerScheduler.isCancelled()) {
                trackerScheduler = getContext().system().scheduler().schedule(
                    Duration.Zero(),
                    Duration.create(1, TimeUnit.SECONDS),
                    self(),
                    new ReadMotionSensorCmd(),
                    getContext().dispatcher(),
                    null
                );
            } else {
                Logger.debug("Motion sensor was deactivated!");
                trackerScheduler.cancel();
            }
        }
    }
/*
    private void startReadMotionSensor(ReadMotionSensorCmd cmd) {

        for (int i = 0; i < 10; i++) {
            if (input.getState().isHigh()) {
                Logger.debug("== Move detected!!!!!");
                sender().tell(new DetectedMoveEvt(), self());
            }
        }
    }*/
}
