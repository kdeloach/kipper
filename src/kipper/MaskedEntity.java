package kipper;

import java.awt.geom.Rectangle2D;

public interface MaskedEntity extends Entity
{
    public Rectangle2D.Double[] getRectMask();
}
