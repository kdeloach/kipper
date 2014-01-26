package kipper;

import kipper.ships.*;
import kipper.weapons.*;
import kipper.upgrades.*;

public interface Upgradable
{
    public Upgrade upgradeAt(int index);
    public void addUpgrade(Upgrade a);
    public void removeUpgrade(int index);
}
