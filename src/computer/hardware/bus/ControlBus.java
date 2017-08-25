package computer.hardware.bus;

import computer.hardware.abstract_hardware.DeviceInterface;
import java.util.ArrayList;

/**
 * 控制总线
 * @author Administrator
 */
public class ControlBus
{

    private ArrayList<DeviceInterface> deviceInterface;

    public ControlBus()
    {
        this.deviceInterface = new ArrayList<DeviceInterface>();
    }

    public void read()
    {
        DeviceInterface device;
        for (int i = 0; i < this.deviceInterface.size(); i++)
        {
            device = this.deviceInterface.get(i);
            if (device != null)
            {
                device.read();
            }
        }
    }

    public void write()
    {
        DeviceInterface device;
        for (int i = 0; i < this.deviceInterface.size(); i++)
        {
            device = this.deviceInterface.get(i);
            if (device != null)
            {
                device.write();
            }
        }
    }

    public void connectHardWare(DeviceInterface deviceInterface)
    {
        this.deviceInterface.add(deviceInterface);
    }
}
