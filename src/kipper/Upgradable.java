package kipper;

import kipper.ships.*;
import kipper.weapons.*;
import kipper.upgrades.*;

public interface Upgradable
{
    public Ability upgradeAt(int index);
    public void addUpgrade(Ability a);
    public void removeUpgrade(int index);
}
