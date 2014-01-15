package kipper;

import java.awt.Image;
import java.awt.Polygon;

public interface MaskedEntity extends Entity
{
    public Image getImage();
    public Polygon getMask();
}
