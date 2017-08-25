package computer.os.kernel.device_management;

import computer.os.base.InnerMemoryManager;
import computer.os.base.InnerMemoryManager.InnerMemory;
import util.Queue.Queue;

/**
 * 设备分配表
 * @author Administrator
 */
public class DeviceAllocateTable
{

    public final static int TABEL_SIZE = 20;
    public final static int SIZE = 9 + 5 * DeviceQueueEntry.SIZE
            + TABEL_SIZE * (DeviceAllocateItem.SIZE + DeviceQueueEntry.SIZE * 2);
    private InnerMemory resource;
    private InnerMemory available;
    private InnerMemory claimSum;
    private DeviceAllocateItem[] table;
    private Queue usedItemQueue;
    private Queue freeItemQueue;
    private Queue[] waitQueues;
    private DeviceQueueEntry[] entrys1;
    private DeviceQueueEntry[] entrys2;

    public DeviceAllocateTable(InnerMemoryManager innerMemoryManager)
    {
        resource = innerMemoryManager.allocate(3);
        claimSum = innerMemoryManager.allocate(3);
        available = innerMemoryManager.allocate(3);
        table = new DeviceAllocateItem[TABEL_SIZE];
        entrys1 = new DeviceQueueEntry[TABEL_SIZE + 2];
        entrys2 = new DeviceQueueEntry[TABEL_SIZE + 3];
        for (int i = 0; i < TABEL_SIZE; i++)
        {
            table[i] = new DeviceAllocateItem(innerMemoryManager);
            entrys1[i] = new DeviceQueueEntry(innerMemoryManager);
            entrys2[i] = new DeviceQueueEntry(innerMemoryManager);
        }
        entrys1[entrys1.length - 2] = new DeviceQueueEntry(innerMemoryManager);
        entrys1[entrys1.length - 1] = new DeviceQueueEntry(innerMemoryManager);
        usedItemQueue = new Queue(entrys1.length - 2, entrys1);
        freeItemQueue = new Queue(entrys1.length - 1, entrys1);
        entrys2[entrys2.length - 3] = new DeviceQueueEntry(innerMemoryManager);
        entrys2[entrys2.length - 2] = new DeviceQueueEntry(innerMemoryManager);
        entrys2[entrys2.length - 1] = new DeviceQueueEntry(innerMemoryManager);
        waitQueues = new Queue[3];
        waitQueues[0] = new Queue(entrys2.length - 3, entrys2);
        waitQueues[1] = new Queue(entrys2.length - 2, entrys2);
        waitQueues[2] = new Queue(entrys2.length - 1, entrys2);
    }

    public void init()
    {
        this.getUsedItemQueue().clear();
        this.freeItemQueue.clear();
        for (int i = 0; i < waitQueues.length; i++)
        {
            this.waitQueues[i].clear();
        }
        for (int i = 0; i < TABEL_SIZE; i++)
        {
            entrys1[i].init(i);
            entrys2[i].init(i);
            this.freeItemQueue.addFirst(i);
        }
        this.setResource(0, 1);
        this.setResource(1, 2);
        this.setResource(2, 2);
        this.setAvailable(0, 1);
        this.setAvailable(1, 2);
        this.setAvailable(2, 2);
        this.setClaimSum(0, 0);
        this.setClaimSum(1, 0);
        this.setClaimSum(2, 0);
        for (int i = 0; i < table.length; i++)
        {
            for (int j = 0; j < 3; j++)
            {
                this.setClaim(i, j, 0);
                this.setAllocation(i, j, 0);
            }
        }
    }

    public void quickRemove1(int index)
    {
        int prev, next;
        if (0 <= index && index < TABEL_SIZE)
        {
            prev = entrys1[index].getPrevious();
            next = entrys1[index].getNext();
            entrys1[prev].setNext(next);
            entrys1[next].setPrevious(prev);
            entrys1[index].setPrevious(index);
            entrys1[index].setNext(index);
        }
    }
    
    public void quickRemove2(int index)
    {
        int prev, next;
        if (0 <= index && index < TABEL_SIZE)
        {
            prev = entrys2[index].getPrevious();
            next = entrys2[index].getNext();
            entrys2[prev].setNext(next);
            entrys2[next].setPrevious(prev);
            entrys2[index].setPrevious(index);
            entrys2[index].setNext(index);
        }
    }

    private void setResource(int i, int number)
    {
        this.resource.write(i, (byte) number);
    }

    public void setAvailable(int i, int number)
    {
        this.available.write(i, (byte) number);
    }

    public void setClaimSum(int i, int number)
    {
        this.claimSum.write(i, (byte) number);
    }

    public void setPid(int i, int pid)
    {
        this.table[i].setPid(pid);
    }

    public void setClaim(int i, int j, int number)
    {
        this.table[i].setClaim(j, number);
    }

    public void setAllocation(int i, int j, int number)
    {
        this.table[i].setAllocation(j, number);
    }

    public int getResource(int i)
    {
        return this.resource.read(i);
    }

    public int getAvailable(int i)
    {
        return this.available.read(i);
    }

    public int getClaimSum(int i)
    {
        return this.claimSum.read(i);
    }

    public int getPid(int i)
    {
        return this.table[i].getPid();
    }

    public int getClaim(int i, int j)
    {
        return this.table[i].getClaim(j);
    }

    public int getAllocation(int i, int j)
    {
        return this.table[i].getAllocation(j);
    }

    public int getNeed(int i, int j)
    {
        return this.table[i].getNeed(j);
    }

    /**
     * @return the usedItemQueue
     */
    public Queue getUsedItemQueue()
    {
        return usedItemQueue;
    }

    public Queue getFreeItemQueue()
    {
        return freeItemQueue;
    }

    public Queue getWaitQueue(int i)
    {
        return this.waitQueues[i];
    }
}
