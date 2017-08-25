package GUI.desktop.window.memory_manager;

/**
 * 
 * @author Administrator
 */
public class MemoryInformation
{

    private int count;
    private DMemoryAllocateItem[] table;
    
    public MemoryInformation()
    {
        this.count = 0;
        this.table = new DMemoryAllocateItem[1000];
        for (int i = 0; i < table.length; i++)
        {
            table[i] = new DMemoryAllocateItem();
        }
    }

    public void init()
    {
        count = 0;
    }

    public void add(int index, int startAddress, int length, int use)
    {
        table[count++].setNode(index, startAddress, length, use);
    }

    public int getIndex(int index)
    {
        return table[index].getIndex();
    }

    public int getStartAddress(int index)
    {
        return table[index].getStartAddress();
    }

    public int getLength(int index)
    {
        return table[index].getLength();
    }

    public int getUse(int index)
    {
        return table[index].getUse();
    }

    public void send()
    {
            util.out.printf("============================\n");
            util.out.printf("|内存使用情况              |\n");
            util.out.printf("|--------------------------|\n");
            util.out.printf("|%2s%6s%4s%4s|\n", "标号", "起始地址", "长度", "状态");
            util.out.printf("|--------------------------|\n");
            int count = getCount();
            for (int i = 0; i < count; i++)
            {
                util.out.printf("|%4d%10d%6d%6d|\n", getIndex(i), getStartAddress(i), getLength(i), getUse(i));
            }
            util.out.printf("============================\n");
    }
    
    public int getCount()
    {
        return this.count;
    }
}

class DMemoryAllocateItem
{

    private int index;
    private int startAddress;
    private int length;
    private int use;

    public DMemoryAllocateItem()
    {
    }

    public void setNode(int index, int address, int length, int use)
    {
        this.setIndex(index);
        this.setStartAddress(address);
        this.setLength(length);
        this.setUse(use);
    }

    /**
     * @return the index
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * @return the startAddress
     */
    public int getStartAddress()
    {
        return startAddress;
    }

    /**
     * @return the length
     */
    public int getLength()
    {
        return length;
    }

    /**
     * @return the useFlag
     */
    public int getUse()
    {
        return use;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index)
    {
        this.index = index;
    }

    /**
     * @param startAddress the startAddress to set
     */
    public void setStartAddress(int startAddress)
    {
        this.startAddress = startAddress;
    }

    /**
     * @param length the length to set
     */
    public void setLength(int length)
    {
        this.length = length;
    }

    /**
     * @param useFlag the useFlag to set
     */
    public void setUse(int useFlag)
    {
        this.use = useFlag;
    }
}