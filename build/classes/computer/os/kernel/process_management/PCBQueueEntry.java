package computer.os.kernel.process_management;

import computer.os.base.InnerMemoryManager;

/**
 * 进程控制块队列实体
 * @author Administrator
 */
public class PCBQueueEntry extends util.Queue.Entry
{

    public PCBQueueEntry(InnerMemoryManager innerMemoryManager)
    {
        super(innerMemoryManager);
    }
}
