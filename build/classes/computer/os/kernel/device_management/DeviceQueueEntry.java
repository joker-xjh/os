package computer.os.kernel.device_management;

import computer.os.base.InnerMemoryManager;

/**
 * 设备队列元素
 * @author Administrator
 */
public class DeviceQueueEntry extends util.Queue.Entry
{

    public DeviceQueueEntry(InnerMemoryManager innerMemoryManager)
    {
        super(innerMemoryManager);
    }
}
