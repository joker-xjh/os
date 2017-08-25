package computer.os.kernel.device_management;

import GUI.desktop.window.device_manager.DeviceInformation;
import computer.os.base.InnerMemoryManager;
import computer.os.base.OS;
import computer.os.kernel.device_management.DeviceManager.Status;
import java.util.HashMap;
import util.Queue.Queue;

/**
 * 设备分配算法
 * @author Administrator
 */
public class DeviceAlloter
{

    public final static int DEVICE_NUMBER = 3;
    public final static int SIZE = DeviceQueueEntry.SIZE * (DeviceAllocateTable.TABEL_SIZE + 1)
            + DeviceAllocateTable.SIZE;
    private Queue queue;
    private HashMap<Integer, Integer> pidToIndex;
    private DeviceQueueEntry[] entrys;
    private DeviceAllocateTable table;
    private OS os;
    private int[] available;
    private int[][] deviceUser;
    private int[][] deviceSattus;

    public DeviceAlloter(OS os, InnerMemoryManager innerMemoryManager)
    {
        this.os = os;
        this.table = new DeviceAllocateTable(innerMemoryManager);
        this.available = new int[DEVICE_NUMBER];
        this.deviceUser = new int[DEVICE_NUMBER][2];
        this.deviceSattus = new int[DEVICE_NUMBER][2];
        this.entrys = new DeviceQueueEntry[DeviceAllocateTable.TABEL_SIZE + 1];
        for (int i = 0; i < entrys.length; i++)
        {
            entrys[i] = new DeviceQueueEntry(innerMemoryManager);
        }
        this.queue = new Queue(entrys.length - 1, entrys);
        this.pidToIndex = new HashMap<Integer, Integer>();
    }

    public void init()
    {
        this.pidToIndex.clear();
        this.table.init();
        this.queue.clear();
        for (int i = 0; i < DeviceAllocateTable.TABEL_SIZE; i++)
        {
            entrys[i].init(i);
        }
        for (int i = 0; i < this.deviceUser.length; i++)
        {
            for (int j = 0; j < this.deviceUser[i].length; j++)
            {
                this.deviceUser[i][j] = -1;
                this.deviceSattus[i][j] = 0;
            }
        }
    }

    public int login(int pid)
    {
        int index = -1;
        if (!this.pidToIndex.containsKey(pid))
        {
            if (!table.getFreeItemQueue().isEmpty())
            {
                index = table.getFreeItemQueue().removeFirst();
                table.setPid(index, pid);
                for (int i = 0; i < DEVICE_NUMBER; i++)
                {
                    table.setClaim(index, i, 0);
                    table.setAllocation(index, i, 0);
                }
                table.getUsedItemQueue().addLast(index);
                this.pidToIndex.put(pid, index);
            }
        }
        return index;
    }

    public void logout(int pid, int index)
    {
        if (pidToIndex.containsKey(pid) && pidToIndex.get(pid) == index)
        {
            table.quickRemove1(index);
            table.quickRemove2(index);
            table.setPid(index, -1);
            for (int i = 0; i < DEVICE_NUMBER; i++)
            {
                table.setClaimSum(i, table.getClaimSum(i) - table.getClaim(index, i));
                table.setAvailable(i, table.getAvailable(i) + table.getAllocation(index, i));
                table.setClaim(index, i, 0);
                table.setAllocation(index, i, 0);
                for (int j = 0; j < this.deviceUser[i].length; j++)
                {
                    if (this.deviceUser[i][j] == pid)
                    {
                        this.deviceUser[i][j] = -1;
                    }
                }
            }
            table.getFreeItemQueue().addLast(index);
            pidToIndex.remove(pid);
        }
    }

    public Status allocate(int pid, int device)
    {
        int index;
        if (pidToIndex.containsKey(pid))
        {
            index = pidToIndex.get(pid);
            if (table.getAvailable(device) > 0)
            {
                table.setClaimSum(device, table.getClaimSum(device) + 1);
                table.setAvailable(device, table.getAvailable(device) - 1);
                table.setClaim(index, device, table.getClaim(index, device) + 1);
                table.setAllocation(index, device, table.getAllocation(index, device) + 1);
                for (int i = 0; i < this.deviceUser[device].length; i++)
                {
                    if (this.deviceUser[device][i] == -1)
                    {
                        this.deviceUser[device][i] = pid;
                        break;
                    }
                }
                return Status.SUCCESS;
            } else
            {
                table.quickRemove2(index);
                table.getWaitQueue(device).addLast(index);
                return Status.FAILURE;
            }
        }
        return Status.ERROR;
    }

    public Status collect(int pid, int device)
    {
        int index;
        if (pidToIndex.containsKey(pid))
        {
            index = pidToIndex.get(pid);
            if (table.getAllocation(index, device) > 0)
            {
                table.setClaimSum(device, table.getClaimSum(device) - 1);
                table.setAvailable(device, table.getAvailable(device) + 1);
                table.setClaim(index, device, table.getClaim(index, device) - 1);
                table.setAllocation(index, device, table.getAllocation(index, device) - 1);
                for (int i = 0; i < this.deviceUser[device].length; i++)
                {
                    if (this.deviceUser[device][i] == pid)
                    {
                        this.deviceUser[device][i] = -1;
                    }
                }
                return Status.SUCCESS;
            } else
            {
                table.quickRemove2(index);
                table.getWaitQueue(device).addLast(index);
                return Status.FAILURE;
            }
        }
        return Status.ERROR;
    }

    public int getDeviceNumber(int pid, int device)
    {
        int index, number = -1;
        if (pidToIndex.containsKey(pid))
        {
            index = pidToIndex.get(pid);
            if (table.getAllocation(index, device) > 0)
            {
                for (int i = 0; i < this.deviceUser[device].length; i++)
                {
                    if (this.deviceUser[device][i] == pid)
                    {
                        number = i;
                        break;
                    }
                }
            }
        }
        return number;
    }

    public int getDeviceUser(int device, int number)
    {
        if (0 <= device && device < this.deviceUser.length)
        {
            if (deviceUser[device] != null && 0 <= number && number < deviceUser[device].length)
            {
                return this.deviceUser[device][number];
            }
        }
        return -1;
    }

    public int allocateNext(int device, int number)
    {
        int pid = -1, dcbIndex;
        if (!table.getWaitQueue(device).isEmpty())
        {
            dcbIndex = table.getWaitQueue(device).removeFirst();
            pid = table.getPid(dcbIndex);
            table.setClaimSum(device, table.getClaimSum(device) + 1);
            table.setAvailable(device, table.getAvailable(device) - 1);
            table.setClaim(dcbIndex, device, table.getClaim(dcbIndex, device) + 1);
            table.setAllocation(dcbIndex, device, table.getAllocation(dcbIndex, device) + 1);
            this.deviceUser[device][number] = pid;
        }
        return pid;
    }

    private boolean safe()
    {
        boolean flag1, flag2;
        util.Queue.Queue.Iterator it = table.getUsedItemQueue().iterator();
        for (queue.clear(), it.init(); it != null && it.hasNext();)
        {
            queue.addLast(it.next());
        }
        for (int i = 0; i < available.length; i++)
        {
            available[i] = table.getAvailable(i);
        }
        flag1 = true;
        while (flag1 && (!queue.isEmpty()))
        {
            flag1 = false;
            for ((it = queue.iterator()).init(); it.hasNext();)
            {
                int i = it.next();
                flag2 = true;
                for (int j = 0; j < available.length; j++)
                {
                    if (available[j] < table.getNeed(i, j))
                    {
                        flag2 = false;
                        break;
                    }
                }
                if (flag2)
                {
                    it.removeAndPrevious();
                    flag1 = true;
                    for (int j = 0; j < available.length; j++)
                    {
                        available[j] += table.getAllocation(i, j);
                    }
                }
            }
        }
        return flag1;
    }

    public void sendInformation()
    {
        DeviceInformation deviceInformation = os.cpu.deviceInformation;
        if (deviceInformation != null)
        {
            deviceInformation.getDeviceAllocateTable().init();
            for (int i = 0; i < this.deviceUser.length; i++)
            {
                for (int j = 0; j < this.deviceUser[i].length; j++)
                {
                    deviceInformation.getDeviceAllocateTable().set(i, j, this.deviceUser[i][j]);
                }
            }
            for (int i = 0; i < deviceInformation.getDeviceWaitQueues().length; i++)
            {
                deviceInformation.getDeviceWaitQueues()[i].init();
                util.Queue.Queue.Iterator it = table.getWaitQueue(i).iterator();
                it.init();
                while (it.hasNext())
                {
                    deviceInformation.getDeviceWaitQueues()[i].add(table.getPid(it.next()));
                }
            }
        }
    }
}
//
//
//public Status claim(int index, int device)
//    {
//        int oldNumber;
//        DeviceAllocateItem dai;
//        dai = table.get(index);
//        if (dai != null)//&& dai.getNeed(device) <= 0)
//        {
//            if (table.getClaim()[device] < table.getResource()[device])
//            {
//                oldNumber = dai.getClaim(device);
//                dai.setClaim(device, oldNumber + 1);
//                table.getClaim()[device]++;
//                if (isSafe())
//                {
//                    return Status.SUCCESS;
//                } else
//                {
//                    dai.setClaim(device, oldNumber);
//                    table.getClaim()[device]--;
//                    return Status.FAILURE;
//                }
//            } else
//            {
//                return Status.FAILURE;
//            }
//        }
//        return Status.ERROR;
//    }
//
//    public Status allocate(int index, int device)
//    {
//        int oldNumber;
//        DeviceAllocateItem dai;
//        dai = table.get(index);
//        if (dai != null && dai.getNeed(device) > 0)
//        {
//            if (table.getAvailable()[device] > 0)
//            {
//                oldNumber = dai.getAllocation(device);
//                dai.setAllocation(device, oldNumber + 1);
//                table.getAvailable()[device]--;
//                if (isSafe())
//                {
//                    for (int i = 0; i < table.getDeviceUser()[device].length; i++)
//                    {
//                        if (table.getDeviceUser()[device][i] == -1)
//                        {
//                            table.getDeviceUser()[device][i] = index;
//                            dai.setDeviceNumber(device, i);
//                            break;
//                        }
//                    }
//                    return Status.SUCCESS;
//                } else
//                {
//                    dai.setAllocation(device, oldNumber);
//                    table.getAvailable()[device]++;
//                    this.table.getWaitQueues()[device].addLast(index);
//                    return Status.FAILURE;
//                }
//            } else
//            {
//                this.table.getWaitQueues()[device].addLast(index);
//                return Status.FAILURE;
//            }
//        }
//        return Status.ERROR;
//    }
//
//    public int collect(int index, int device)
//    {
//        int oldNumber, newIndex;
//        util.Queue.Queue.Iterator it;
//        DeviceAllocateItem dai;
//        dai = table.get(index);
//        if (dai != null && (oldNumber = dai.getAllocation(device)) > 0)
//        {
//            for (int i = 0; i < table.getDeviceUser()[device].length; i++)
//            {
//                if (table.getDeviceUser()[device][i] == index)
//                {
//                    table.getDeviceUser()[device][i] = -1;
//                    dai.setDeviceNumber(device, -1);
//                    break;
//                }
//            }
//            dai.setAllocation(device, oldNumber - 1);
//            table.getAvailable()[device]++;
//            (it = this.table.getWaitQueues()[device].iterator()).init();
//            while (it.hasNext())
//            {
//                newIndex = it.next();
//                if (this.allocate(newIndex, device) == Status.SUCCESS)
//                {
//                    it.removeAndPrevious();
//                    it.init();
//                    return newIndex;
//                }
//            }
//        }
//        return -1;
//    }
//
//    private boolean isSafe()
//    {
//        boolean flag;
//        int va, vb, vc;
//        DeviceAllocateItem dai;
//        util.Queue.Queue.Iterator it;
//        queue.clear();
//        (it = table.getUsedItemQueue().iterator()).init();
//        while (it.hasNext())
//        {
//            queue.addLast(it.next());
//        }
//        while (!queue.isEmpty())
//        {
//            (it = queue.iterator()).init();
//            flag = false;
//            while (it.hasNext())
//            {
//                dai = table.get(it.next());
//                if (va >= dai.getNeedA() && vb >= dai.getNeedB() && vc >= dai.getNeedC())
//                {
//                    va += dai.getA();
//                    vb += dai.getB();
//                    vc += dai.getC();
//                    it.removeAndPrevious();
//                    flag = true;
//                }
//            }
//            if (!flag)
//            {
//                queue.clear();
//                it.init();
//                return false;
//            }
//        }
//        it.init();
//        return true;
//    }