package computer.hardware;

/**
 * 电源
 * @author Administrator
 */
public class Power
{

    private boolean open;

    Power(boolean open)
    {
        this.open = open;
    }

    /**
     * @return the open
     */
    public boolean isOpen()
    {
        return open;
    }

    /**
     * @param open the open to set
     */
    public void setOpen(boolean open)
    {
        this.open = open;
    }
}
