package computer.hardware.abstract_hardware;

/**
 * 抽象总线
 * @author Administrator
 */
public abstract class Bus
{

    private byte[] bus;

    /**
     * 创建位数为<width/>的总线
     * @param width 
     */
    public Bus(int width)
    {
        this.bus = new byte[(width + 7) >> 3];
    }

    /**
     * 获取表示总线的byte数组
     * @return 
     */
    public byte[] getBus()
    {
        return bus;
    }
}
