package computer.os.kernel.memory_management;

import computer.os.base.OS;

/**
 * 内存管理程序
 * @author Administrator
 */
public class MemoryManager extends computer.os.base.SystemProcess
{

    private MemoryAllocateTable memoryAllocateTable;
    private MemoryAllocateAlgorithm memoryAllocateAlgorithm;

    public MemoryManager(OS os)
    {
        super(os);
        memoryAllocateTable = new MemoryAllocateTable(os, 0);
        memoryAllocateAlgorithm = new FirstFitAlgorithm(memoryAllocateTable);
    }

    @Override
    protected void push()
    {
        super.push();
        os.cpu.RR = 0;
        os.cpu.LR = 256;
    }

    public void init()
    {
        push();
        memoryAllocateTable.init();
        pop();
    }

    public int allocate(int length)
    {
        int index;
        push();
        index = memoryAllocateAlgorithm.allocate(length);
        memoryAllocateTable.sendInformation();
        this.os.displayManager.showMemory();
        pop();
        return index;
    }

    public void collect(int index)
    {
        push();
        memoryAllocateAlgorithm.collect(index);
        memoryAllocateTable.sendInformation();
        this.os.displayManager.showMemory();
        pop();
    }

    public int getStartAddress(int index)
    {
        int address;
        push();
        address = memoryAllocateTable.getStartAddress(index);
        pop();
        return address;
    }

    public int getLength(int index)
    {
        int length;
        push();
        length = memoryAllocateTable.getLength(index);
        pop();
        return length;
    }

    public void sendInformation()
    {
        push();
        this.memoryAllocateTable.sendInformation();
        pop();
    }
}
