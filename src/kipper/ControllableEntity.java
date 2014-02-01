package kipper;

public interface ControllableEntity extends Entity
{
    public boolean isUnderControl();
    public void gainControl();
    public void releaseControl();
    public void handleInput();
}
