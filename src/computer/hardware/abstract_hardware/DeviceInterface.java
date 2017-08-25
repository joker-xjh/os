package computer.hardware.abstract_hardware;

/**
 * 设备控制器接口
 * @author Administrator
 */
public interface DeviceInterface
{
    
    /**
     * 读设备控制器寄存器
     */
    public void read();

    /**
     * 写设备控制器寄存器
     */
    public void write();
}
