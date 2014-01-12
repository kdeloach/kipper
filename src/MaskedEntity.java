package kipper;

import java.awt.Polygon;

public interface MaskedEntity extends Entity
{
    public Polygon getMask();
}
