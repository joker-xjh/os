package computer.hardware.abstract_hardware;

import computer.hardware.Power;

/**
 * 硬件接口
 * @author Administrator
 */
public abstract class  HardWare
{
    protected Power power;
    public HardWare(Power power)
    {
        this.power=power;
    }
    protected boolean isOpen()
    {
        return power.isOpen();
    }
}
