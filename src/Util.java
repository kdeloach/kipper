import java.awt.*;
import java.io.File;

public class Util
{
    final public static Util instance = new Util();

    public Image loadImage(String filename)
    {
        String basePath = getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        return Toolkit.getDefaultToolkit().createImage(new File(basePath, filename).getPath());
    }
}