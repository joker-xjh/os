package computer.hardware.bus;

import computer.hardware.abstract_hardware.IODeviceInterface;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

/**
 * 中断总线
 * @author Administrator
 */
public class InterruptBus
{

    private int interVector;
    private ArrayList<IODeviceInterface> devices;
    private HashMap<IODeviceInterface, Integer> devicesToInterruptNumberMap;
    private Semaphore mutex;

    public InterruptBus()
    {
        mutex = new Semaphore(1);
        devices = new ArrayList<IODeviceInterface>();
        devicesToInterruptNumberMap = new HashMap<IODeviceInterface, Integer>();
    }

    private int getInterruptNumber(IODeviceInterface device)
    {
        if (devicesToInterruptNumberMap.containsKey(device))
        {
            return devicesToInterruptNumberMap.get(device);
        } else
        {
            return -1;
        }
    }

    public void connectDevice(IODeviceInterface device)
    {
        if (device != null)
        {
            devicesToInterruptNumberMap.put(device, devices.size());
            devices.add(device);
        }
    }

    public void INTR(IODeviceInterface device)
    {
        int interruptNumber = getInterruptNumber(device);
        if (interruptNumber >= 0)
        {
            try
            {
                mutex.acquire();
                interVector |= 1 << (interruptNumber);
                mutex.release();
            } catch (InterruptedException ex)
            {
                util.err.printf("InterruptBus-INTR:中断异常！");
            }
        } else
        {
            util.err.println("InterruptBus-INTR:设备没有对应中断号！");
        }
    }

    public void INTX(IODeviceInterface device)
    {
        int interruptNumber = getInterruptNumber(device);
        if (interruptNumber >= 0)
        {
            try
            {
                mutex.acquire();
                interVector &= ~(1 << (interruptNumber));
                mutex.release();
            } catch (InterruptedException ex)
            {
                util.err.printf("InterruptBus-INTR:中断异常！");
            }
        } else
        {
            util.err.println("InterruptBus-INTR:设备没有对应中断号！");
        }
    }

    public void INTA(int interruptNumber)
    {
        if (0 <= interruptNumber && interruptNumber < devices.size())
        {
            devices.get(interruptNumber).INTA();
        } else
        {
            util.err.println("InterruptBus-INTA:中断号没有对应设备！");
        }
    }

    /**
     * @return the interVector
     */
    public int getInterVector()
    {
        return interVector;
    }
}
