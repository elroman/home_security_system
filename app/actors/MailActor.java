package actors;

import com.google.common.collect.Lists;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import actors.cmd.SendMessageCmd;
import akka.actor.AbstractActor;
import akka.event.LoggingReceive;
import akka.japi.pf.ReceiveBuilder;
import play.Logger;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;
import service.DateUtil;

public class MailActor
    extends AbstractActor {

    private final String PATH_TO_FOLDER = context().system().settings().config().getString("config.pathToFolder");

    private HashMap<File, Boolean> mailBox = new HashMap<>();

    @Override
    public PartialFunction<Object, BoxedUnit> receive() {
        return LoggingReceive.create(ReceiveBuilder
                                         .match(SendMessageCmd.class, this::sendMessageCmd)
                                         .build(), getContext());
    }

    private void sendMessageCmd(SendMessageCmd cmd) {
        Logger.debug("MailActor sendMessageCmd");

        final Date photoTime = new Date();
        final String pref = PATH_TO_FOLDER.substring(PATH_TO_FOLDER.length() - 1).equals("/") ? "" : "/";
        final File theDir = new File(PATH_TO_FOLDER + pref + DateUtil.getStringFromDate(photoTime));

        addFilesToMailBox(theDir);
        sendFilesFromMileBox();
    }

    private void addFilesToMailBox(final File folder) {
        FilenameFilter jpgFilter = new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".jpg");
            }
        };

        Arrays.asList(folder.listFiles(jpgFilter)).forEach(fileEntry -> mailBox.putIfAbsent(fileEntry, false));
    }

    private void sendFilesFromMileBox() {
        final List<File> fileList = mailBox.entrySet()
            .stream()
            .filter(elem -> !elem.getValue())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        fileList.forEach(elem -> System.out.println("sending...." + elem));
    }
}

