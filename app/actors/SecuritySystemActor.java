package actors;

import static akka.pattern.Patterns.ask;
import static akka.pattern.Patterns.pipe;

import com.google.inject.Inject;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import actors.cmd.ActivationCmd;
import actors.cmd.ReadMotionSensorCmd;
import actors.cmd.SendMessageCmd;
import actors.cmd.TakePhotoCmd;
import actors.event.DetectedMoveEvt;
import actors.proto.GetStateReq;
import actors.proto.GetStateRes;
import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Cancellable;
import akka.event.LoggingReceive;
import akka.japi.pf.ReceiveBuilder;
import play.Logger;
import play.libs.F;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;
import scala.runtime.BoxedUnit;

public class SecuritySystemActor
    extends AbstractActor {

    @Inject
    @Named("cameraActor")
    ActorRef cameraActor;
    @Inject
    @Named("motionSensorActor")
    ActorRef motionSensorActor;

    @Inject
    @Named("mailActor")
    ActorRef mailActor;

    private Boolean active;
    private Cancellable sendMessageScheduler;

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
        //        mailActor.tell(new SendMessageCmd(), self());
        createScheduler();
    }

    private void detectedMove(DetectedMoveEvt cmd) {
        cameraActor.tell(new TakePhotoCmd(), self());
    }

    private void setActivation(ActivationCmd cmd) {
        active = cmd.isActive();
        motionSensorActor.tell(cmd, self());
    }

    private void getState(GetStateReq req) {
        pipe(F.Promise.pure(new GetStateRes(active)).wrapped(), getContext().dispatcher()).to(sender());
    }

    private void createScheduler() {
        if ((sendMessageScheduler == null) || sendMessageScheduler.isCancelled()) {
            Logger.debug("Sender message scheduler was activated !");

            sendMessageScheduler = getContext().system().scheduler().schedule(
                Duration.create(1, TimeUnit.MINUTES),
                Duration.Zero(),
                mailActor,
                new SendMessageCmd(),
                getContext().dispatcher(),
                self()
            );
        }
    }

    private void closeScheduler() {
        if ((sendMessageScheduler != null) && !sendMessageScheduler.isCancelled()) {
            Logger.debug("Sender message scheduler was deactivated !");
            sendMessageScheduler.cancel();
        }
    }

    @Override
    public void postStop() {
        closeScheduler();
    }

}
