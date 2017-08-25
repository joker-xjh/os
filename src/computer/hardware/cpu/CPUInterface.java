package computer.hardware.cpu;

/**
 * 中央处理器外部接口
 * @author Administrator
 */
public interface CPUInterface
{

    /**
     * 时钟信号输入端
     */
    public void CLK();

    /**
     * 复位信号输入端
     */
    public void RESET();

    /***
     * 设置CPU开始自动执行指令
     */
    public void START();

    /***
     * 设置CPU暂停自动执行指令
     */
    public void STOP();

    /**
     * 设置CPU每秒中执行的指令数
     * @param ips 
     */
    public void setIPS(int ips);

    /**
     * 判断CPU是否自动运行
     */
    public boolean isRun();

    /**
     * 获取PSW
     */
    public int getPSW();

    /**
     * 获取IR
     */
    public int getIR();

    /**
     * 获取AX
     */
    public int getAX();

    /**
     * 获取运行速度
     * @return 
     */
    public int getIPS();

    public void init();
}
