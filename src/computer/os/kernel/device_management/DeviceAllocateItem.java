package computer.os.kernel.device_management;

import computer.os.base.InnerMemoryManager;
import computer.os.base.InnerMemoryManager.InnerMemory;

/**
 * 设备分配项
 * @author Administrator
 */
public class DeviceAllocateItem
{

    public final static int SIZE = 7;
    private InnerMemory memory;

    public DeviceAllocateItem(InnerMemoryManager innerMemoryManager)
    {
        this.memory = innerMemoryManager.allocate(SIZE);
    }

    public void init()
    {
        init(0, 0, 0, 0, 0, 0, -1);
    }

    public void init(int pid, int claimA, int claimB, int claimC, int allocA, int allocB, int allocC)
    {
        this.setPid(pid);
        this.setClaim(0, claimA);
        this.setClaim(1, claimB);
        this.setClaim(2, claimC);
        this.setAllocation(0, allocA);
        this.setAllocation(1, allocB);
        this.setAllocation(2, allocC);
    }

    public int getClaim(int device)
    {
        return this.memory.read(device);
    }

    public int getAllocation(int device)
    {
        return this.memory.read(device + 3);
    }

    public int getPid()
    {
        return this.memory.read(6);
    }

    public int getNeed(int device)
    {
        return this.getClaim(device) - this.getAllocation(device);
    }

    public void setClaim(int device, int number)
    {
        this.memory.write(device, (byte) number);
    }

    public void setAllocation(int device, int number)
    {
        this.memory.write(device + 3, (byte) number);
    }

    public void setPid(int pid)
    {
        this.memory.write(6, (byte) pid);
    }
}
