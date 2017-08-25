package computer.os.kernel.device_management;

import computer.os.base.InnerMemoryManager;
import computer.os.base.OS;

/**
 * 设备管理程序
 * @author Administrator
 */
public class DeviceManager extends computer.os.base.SystemProcess
{

    public final static int SIZE = DeviceAlloter.SIZE;

    public enum Status
    {

        SUCCESS, FAILURE, ERROR
    };
    private int mcbIndex;
    private DeviceAlloter deviceAllocater;
    private InnerMemoryManager innerMemoryManager;

    public DeviceManager(OS os)
    {
        super(os);
        this.innerMemoryManager = new InnerMemoryManager(os, DeviceAlloter.SIZE);
        this.deviceAllocater = new DeviceAlloter(os, innerMemoryManager);
    }

    @Override
    protected void push()
    {
        super.push();
        os.cpu.RR = os.memoryManager.getStartAddress(mcbIndex);
        os.cpu.LR = os.memoryManager.getLength(mcbIndex);
    }

    public void init()
    {
        push();
        this.deviceAllocater.init();
        pop();
    }

    public int login(int pid)
    {
        int dcbIndex;
        push();
        dcbIndex = this.deviceAllocater.login(pid);
        pop();
        return dcbIndex;
    }

    public void logout(int pid, int index)
    {
        push();
        this.deviceAllocater.logout(pid, index);
        pop();
    }

    public Status allocate(int pid, int device)
    {
        Status status;
        push();
        status = deviceAllocater.allocate(pid, device);
        pop();
        return status;
    }

    public Status collect(int pid, int device)
    {
        Status status;
        push();
        status = deviceAllocater.collect(pid, device);
        pop();
        return status;
    }

    public int getDeviceNumber(int pid, int device)
    {
        int deviceNumber;
        push();
        deviceNumber = deviceAllocater.getDeviceNumber(pid, device);
        pop();
        return deviceNumber;
    }

    public int getDeviceUser(int device, int number)
    {
        int pid;
        push();
        pid = this.deviceAllocater.getDeviceUser(device, number);
        pop();
        return pid;
    }

    public int allocateNext(int device, int number)
    {
        int pid;
        push();
        pid = this.deviceAllocater.allocateNext(device, number);
        pop();
        return pid;
    }

    public void sendInformation()
    {
        push();
        this.deviceAllocater.sendInformation();
        pop();
    }

    public void setMCBIndex(int mcbIndex)
    {
        this.mcbIndex = mcbIndex;
    }
}
