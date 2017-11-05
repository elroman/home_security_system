package modules;

import com.google.inject.AbstractModule;

import actors.CameraActor;
import actors.MailActor;
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
        bindActor(MailActor.class, "mailActor");
        //        bind(GpioService.class).annotatedWith(Names.named("gpioService")).to(GpioServiceImpl.class);

    }
}