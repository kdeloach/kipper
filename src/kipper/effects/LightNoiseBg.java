package kipper.effects;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import kipper.*;

// Credit: http://www.java-gaming.org/topics/fastest-perlinnoise-improved-version-bicubic-amp-bilinear-grad-amp-value-noise/23771/view.html
public class LightNoiseBg
{
    BufferedImage img;
    OuterSpacePanel osp;

    public LightNoiseBg(OuterSpacePanel osp)
    {
        this.osp = osp;
        // XXX: No idea why I should need this offset but otherwise the BufferedImage comes up short.
        img = new BufferedImage(OuterSpacePanel.WIDTH + 10, OuterSpacePanel.HEIGHT + 10, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int c = FastNoise.noise(x / 128f, y / 128f, 7);
                double brightness = 0.25;
                int r = 0;
                int g = (int)(c * brightness);
                int b = (int)(0xFF * brightness);
                img.setRGB(x, y, new Color(r << 16 | g << 8 | b).getRGB());
            }
        }
    }

    public void update()
    {
    }

    public void draw(Graphics g)
    {
        g.drawImage(img, 0, 0, osp);
    }
}
