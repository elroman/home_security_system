package actors;

import com.google.inject.Inject;

import javax.inject.Named;

import actors.cmd.ActivationCmd;
import actors.cmd.TakePhotoCmd;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.LoggingReceive;
import akka.japi.pf.ReceiveBuilder;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class SecuritySystemActor
    extends AbstractActor {

    @Inject
    @Named("cameraActor")
    ActorRef cameraActor;
    @Inject
    @Named("motionSensorActor")
    ActorRef motionSensorActor;

    private Boolean active;

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return LoggingReceive.create(ReceiveBuilder
                                         .match(ActivationCmd.class, this::setActivation)
                                         .match(TakePhotoCmd.class, this::takePhotoCmd)
                                         .build(), getContext());
    }

    @Override
    public void preStart() throws Exception {
        active = false;
    }

    private void takePhotoCmd(TakePhotoCmd cmd) {
        cameraActor.tell(cmd, self());
    }

    private void setActivation(ActivationCmd cmd) {
        active = cmd.isActive();
        motionSensorActor.tell(cmd, self());
    }
}
