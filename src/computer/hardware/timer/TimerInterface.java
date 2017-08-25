package computer.hardware.timer;

/**
 *
 * @author Administrator
 */
public interface TimerInterface
{

    /**
     * 发送时钟信号
     */
    public void CLK();

    /**
     * 复位
     */
    public void RESET();

    /***
     * 开始自动运转
     */
    public void START();

    /***
     * 暂停自动运转
     */
    public void STOP();

    /**
     * 设置时钟的脉冲间隔
     * @param sleeepTime 
     */
    public void setSleeepTime(int sleeepTime);

    /**
     * 获取时钟的时间
     * @return 
     */
    public int getTime();

    /**
     * 判断时钟是否开启自动
     */
    public boolean isRun();

    public int getSleeepTime();
}
