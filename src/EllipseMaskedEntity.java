package kipper;

import java.awt.geom.Ellipse2D;

public interface EllipseMaskedEntity extends Entity
{
    public Ellipse2D.Double[] getEllipseMask();
}
