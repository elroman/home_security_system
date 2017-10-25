package models;

public class Photo {

    private String systemDevice;
    private String size;
    private String directory;
    private String nameFile;
    private int quality;

    public Photo(
        final String systemDevice,
        final String pictureSize,
        final String pictureDir,
        final String date,
        final int quality
    ) {
        this.systemDevice = systemDevice;
        size = pictureSize;
        directory = pictureDir;
        this.quality = quality;
        nameFile = "pict_" + date.replace(" ", "_") + ".jpg";
    }

    public String getCommandForShot() {

        StringBuffer sb = new StringBuffer("fswebcam")
            .append(" -d ").append(systemDevice)
            .append(" -r ").append(size)
            .append(" ").append(directory)
            .append((directory.substring(directory.length() - 1).equals("/")) ? "" : "/")
            .append(nameFile)
            .append(" -S ").append(quality);

        return sb.toString();
    }

    public String getNameFile() {
        return nameFile;
    }
}
