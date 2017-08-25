package computer.os.driver;

import computer.hardware.abstract_hardware.DeviceController;
import computer.hardware.device_controller.DeviceAController;
import computer.os.base.OS;

/**
 * 硬盘驱动程序
 * @author Administrator
 */
public class IODeviceDriver extends Driver
{
    
    public IODeviceDriver(OS os, int port)
    {
        super(os, port, DeviceAController.REGISTER_STATUS, DeviceAController.REGISTER_CONTROL_COMMAND, DeviceAController.REGISTER_CONTROL_ARGUMENT, DeviceAController.REGISTER_DATA);
    }
    
    public void use(int time)
    {
        push();
        int offset;
        offset = this.dataOffset;
        this.write(port + offset, (byte) time);
        pop();
    }
    
    public void update()
    {
        push();
        int offset, command;
        offset = this.commandOffset;
        command = DeviceController.COMMAND_READ;
        this.write(port + offset, (byte) command);
        pop();
    }
    
    public int getRemainTime()
    {
        int remainTime;
        push();
        remainTime = this.read(port + this.dataOffset);
        pop();
        return remainTime;
    }
}
