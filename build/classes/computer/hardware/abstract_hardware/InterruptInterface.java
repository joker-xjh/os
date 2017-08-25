package computer.hardware.abstract_hardware;

/**
 * 中断接口
 * @author Administrator
 */
public interface InterruptInterface
{

    /**
     * 中断请求
     */
    public void INTR(int interruptNumber);
}
