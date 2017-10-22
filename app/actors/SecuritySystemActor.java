package actors;

import static akka.pattern.Patterns.pipe;

import com.google.inject.Inject;

import java.time.Duration;

import javax.inject.Named;

import actors.cmd.ActivationCmd;
import actors.cmd.TakePhotoCmd;
import actors.event.DetectedMoveEvt;
import actors.proto.GetStateReq;
import actors.proto.GetStateRes;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.event.LoggingReceive;
import akka.japi.pf.ReceiveBuilder;
import play.libs.F;
import scala.PartialFunction;
import scala.concurrent.Future;
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
                                         .match(DetectedMoveEvt.class, this::detectedMove)
                                         .match(GetStateReq.class, this::getState)
                                         .build(), getContext());
    }

    @Override
    public void preStart() throws Exception {
        active = false;
    }

    private void takePhotoCmd(TakePhotoCmd cmd) {
        cameraActor.tell(cmd, self());
    }

    private void detectedMove(DetectedMoveEvt cmd) {
        for (int i = 0; i < 5; i++) {
            cameraActor.tell(cmd, self());
            try {
                Thread.sleep(Duration.ofSeconds(1).toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void setActivation(ActivationCmd cmd) {
        active = cmd.isActive();
        motionSensorActor.tell(cmd, self());
    }

    private void getState(GetStateReq req) {
        pipe(F.Promise.pure(new GetStateRes(active)).wrapped(), getContext().dispatcher()).to(sender());
    }
}
