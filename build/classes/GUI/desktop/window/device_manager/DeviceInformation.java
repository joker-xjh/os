package GUI.desktop.window.device_manager;

/**
 * 进程信息
 * @author Administrator
 */
public class DeviceInformation
{

    private DeviceAllocateTable deviceAllocateTable;
    private DeviceWaitQueue[] deviceWaitQueues;
    private int remainTime[][];

    public DeviceInformation()
    {
        this.deviceAllocateTable = new DeviceAllocateTable();
        this.deviceWaitQueues = new DeviceWaitQueue[3];
        for (int i = 0; i < this.deviceWaitQueues.length; i++)
        {
            this.deviceWaitQueues[i] = new DeviceWaitQueue(10);
        }
        remainTime = new int[3][2];
    }

    /**
     * @return the deviceAllocateTable
     */
    public DeviceAllocateTable getDeviceAllocateTable()
    {
        return deviceAllocateTable;
    }

    /**
     * @return the deviceWaitQueues
     */
    public DeviceWaitQueue[] getDeviceWaitQueues()
    {
        return deviceWaitQueues;
    }

    public class DeviceAllocateTable
    {

        private int[][] deviceUsers;

        public DeviceAllocateTable()
        {
            deviceUsers = new int[3][];
            deviceUsers[0] = new int[1];
            deviceUsers[1] = new int[2];
            deviceUsers[2] = new int[2];
        }

        public void init()
        {
            for (int i = 0; i < this.deviceUsers.length; i++)
            {
                for (int j = 0; j < this.deviceUsers[i].length; j++)
                {
                    this.deviceUsers[i][j] = -1;
                }
            }
        }

        public void set(int device, int number, int pid)
        {
            if (0 <= device && device < this.deviceUsers.length)
            {
                if (0 <= number && number < this.deviceUsers[device].length)
                {
                    this.deviceUsers[device][number] = pid;
                }
            }
        }

        public int get(int device, int number)
        {
            if (0 <= device && device < this.deviceUsers.length)
            {
                if (0 <= number && number < this.deviceUsers[device].length)
                {
                    return this.deviceUsers[device][number];
                }
            }
            return -1;
        }
    }

    public class DeviceWaitQueue
    {

        private int[] queue;
        private int length;

        public DeviceWaitQueue(int size)
        {
            this.length = 0;
            this.queue = new int[size];
        }

        public void init()
        {
            this.length = 0;
            for (int i = 0; i < this.queue.length; i++)
            {
                this.queue[i] = -1;
            }
        }

        public int getLength()
        {
            return this.length;
        }

        public void add(int pid)
        {
            if (0 <= this.length && this.length < this.queue.length)
            {
                this.queue[this.length++] = pid;
            }
        }

        public int get(int index)
        {
            if (0 <= index && index < this.length)
            {
                return this.queue[index];
            }
            return -1;
        }
    }

    public int getRemainTime(int device, int number)
    {
        if (0 <= device && device < this.remainTime.length)
        {
            if (0 <= number && number < this.remainTime[device].length)
            {
                return this.remainTime[device][number];
            }
        }
        return -1;
    }

    public void setRemainTime(int device, int number, int time)
    {
        if (0 <= device && device < this.remainTime.length)
        {
            if (0 <= number && number < this.remainTime[device].length)
            {
                this.remainTime[device][number] = time;
            }
        }
    }
}
