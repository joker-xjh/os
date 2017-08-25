package computer.os.driver;

import computer.hardware.abstract_hardware.DeviceController;
import computer.os.base.OS;
import computer.os.base.SystemProcess;

/**
 * 驱动程序基类
 * @author Administrator
 */
public abstract class Driver extends SystemProcess
{

    protected int port;
    protected int statusOffset;
    protected int commandOffset;
    protected int argumentOffset;
    protected int dataOffset;

    public Driver(OS os, int port, int statusOffset, int commandOffset, int argumentOffset, int dataOffset)
    {
        super(os);
        this.port = port;
        this.statusOffset = statusOffset;
        this.commandOffset = commandOffset;
        this.argumentOffset = argumentOffset;
        this.dataOffset = dataOffset;
    }

    public void open()
    {
        push();
        int offset, command, status;
        offset = this.statusOffset;
        status = this.read(port + offset);
        if ((status & DeviceController.STATUS_WORK) == 0)
        {
            offset = this.commandOffset;
            command = DeviceController.COMMAND_OPEN;
            this.write(port + offset, (byte) command);
            offset = this.statusOffset;
            do
            {
                sleep(1);
                status = this.read(port + offset);
            } while ((status & DeviceController.STATUS_WORK) == 0);
        }
        pop();
    }

    public void close()
    {
        push();
        int offset, command, status;
        offset = this.statusOffset;
        status = this.read(port + offset);
        if ((status & DeviceController.STATUS_WORK) != 0)
        {
            offset = this.commandOffset;
            command = DeviceController.COMMAND_CLOSE;
            this.write(port + offset, (byte) command);
            offset = this.statusOffset;
            do
            {
                sleep(1);
                status = this.read(port + offset);
            } while ((status & DeviceController.STATUS_WORK) != 0);
        }
        pop();
    }

    protected void sleep(int ms)
    {
        try
        {
            java.lang.Thread.sleep(ms);
        } catch (InterruptedException ex)
        {
            util.out.println("HardDiskDriver-sleep:睡眠中断");
        }
    }
}
