package modules;

import com.google.inject.AbstractModule;

import actors.CameraActor;
import actors.MotionSensorActor;
import actors.SecuritySystemActor;
import play.libs.akka.AkkaGuiceSupport;

public class MyModule
    extends AbstractModule
    implements AkkaGuiceSupport {

    @Override
    protected void configure() {
        bindActor(SecuritySystemActor.class, "securitySystem");
        bindActor(CameraActor.class, "cameraActor");
        bindActor(MotionSensorActor.class, "motionSensorActor");
    }
}