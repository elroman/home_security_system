package actors;

import actors.cmd.ActivationCmd;
import akka.actor.AbstractActor;
import akka.event.LoggingReceive;
import akka.japi.pf.ReceiveBuilder;
import play.Logger;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class MotionSensorActor
    extends AbstractActor {

    private Boolean active;

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return LoggingReceive.create(ReceiveBuilder
                                         .match(ActivationCmd.class, this::setActivation)
                                         .build(), getContext());
    }

    private void setActivation(ActivationCmd cmd) {
        Logger.debug("MotionSensorActor: setActivation:  " + cmd);

        active = cmd.isActive();
        readMotionSensor();
    }

    private void readMotionSensor() {

    }
}
