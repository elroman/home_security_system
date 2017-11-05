package actors;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Date;

import actors.cmd.TakePhotoCmd;
import akka.actor.AbstractActor;
import akka.event.LoggingReceive;
import akka.japi.pf.ReceiveBuilder;
import models.Photo;
import play.Logger;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;
import service.DateUtil;

public class CameraActor
    extends AbstractActor {

    private final String PATH_TO_FOLDER = context().system().settings().config().getString("config.pathToFolder");
    private final String DEVICE = context().system().settings().config().getString("config.camera_device");
    private final String PICTURE_SIZE = "640x480";
    private final Integer QUALITY = 30;

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return LoggingReceive.create(ReceiveBuilder
                                         .match(TakePhotoCmd.class, this::takePhotoCmd)
                                         .build(), getContext());
    }

/*    private void makeSeriaPhotos(TakePhotoCmd cmd) {
        for (int i = 0; i < 5; i++) {
            takePhotoCmd();
            try {
                Thread.sleep(Duration.ofSeconds(3).toMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }*/

    private void takePhotoCmd(TakePhotoCmd cmd) {
        final Date photoTime = new Date();
        final String fullPath = geFullPhotoPath(photoTime);

        Photo photo = new Photo(DEVICE, PICTURE_SIZE, fullPath, DateUtil.getStringFromDateTime(photoTime), QUALITY);
        String command = photo.getCommandForShot();

        //        Logger.debug("CameraActor: takePhotoCmd() exec command: " + command);
        Logger.debug("CameraActor: photo:" + photo.getNameFile());

        executeCommand(command);
    }

    private String geFullPhotoPath(final Date photoTime) {
        final String pref = PATH_TO_FOLDER.substring(PATH_TO_FOLDER.length() - 1).equals("/") ? "" : "/";
        final File theDir = new File(PATH_TO_FOLDER + pref + DateUtil.getStringFromDate(photoTime));

        if (theDir.exists()) {
            return theDir.toString();
        }

        try {
            theDir.mkdir();
            return theDir.toString();
        } catch (SecurityException se) {
            Logger.error("Problem created new folder! ", se);
            return PATH_TO_FOLDER;
        }
    }

    private void executeCommand(String cmd) {
        Runtime rt = Runtime.getRuntime();
        try {
            final Process process = rt.exec(cmd);
            BufferedReader r = new BufferedReader(new InputStreamReader(process.getInputStream()));
            Thread.sleep(Duration.ofSeconds(4).toMillis());
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

