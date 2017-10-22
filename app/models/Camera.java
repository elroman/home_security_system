package models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Camera {

    private String systemDevice;
    private String pictureSize;
    private String pictureDir;
    private int quality;

    public Camera(final String systemDevice, final String pictureSize, final String pictureDir, final int quality) {
        this.systemDevice = systemDevice;
        this.pictureSize = pictureSize;
        this.pictureDir = pictureDir;
        this.quality = quality;
    }

    public String getCommandForShot() {

        StringBuffer sb = new StringBuffer("fswebcam")
            .append(" -d ").append(systemDevice)
            .append(" -r ").append(pictureSize)
            .append(" ").append(pictureDir)
            .append((pictureDir.substring(pictureDir.length() - 1).equals("/")) ? "" : "/")
            .append("pict_").append(getStringFromDateTime(new Date())).append(".jpg")
            .append(" -S ").append(quality);

        return sb.toString();
    }

    public static String getStringFromDateTime(Date date) {
        if (date != null) {
            DateFormat dateTimeFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
            dateTimeFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
            return dateTimeFormatter.format(date);
        }
        return null;
    }
}
