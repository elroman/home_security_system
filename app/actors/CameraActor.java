package actors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import actors.cmd.TakePhotoCmd;
import akka.actor.AbstractActor;
import akka.event.LoggingReceive;
import akka.japi.pf.ReceiveBuilder;
import models.Camera;
import play.Logger;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class CameraActor
    extends AbstractActor {

    private final String PATH_TO_FOLDER = context().system().settings().config().getString("config.pathToFolder");
    private final String DEVICE = context().system().settings().config().getString("config.camera_device");

    private Camera camera;

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return LoggingReceive.create(ReceiveBuilder
                                         .match(TakePhotoCmd.class, this::takePhotoCmd)
                                         .build(), getContext());
    }

    @Override
    public void preStart() throws Exception {
        camera = new Camera(DEVICE, "640x480", PATH_TO_FOLDER, 30);
    }

    private void takePhotoCmd(TakePhotoCmd cmd) {
        final String command = camera.getCommandForShot();
        Logger.debug("CameraActor: takePhotoCmd() exec command: " + command);

        executeCommand(camera.getCommandForShot());
    }

    private void executeCommand(String cmd) {
        Runtime rt = Runtime.getRuntime();
        try {
            final Process process = rt.exec(cmd);
            BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    break;
                }
                System.out.println(line);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
