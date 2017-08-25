package computer.hardware.abstract_hardware;

/**
 * 设备控制器接口
 * @author Administrator
 */
public interface IODeviceInterface extends DeviceInterface
{

    /**
     * 中断被接受
     */
    public void INTA();
}
