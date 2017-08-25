package computer.os.kernel.memory_management;

import computer.os.base.OS;
import GUI.desktop.window.memory_manager.MemoryInformation;

/**
 * 内存分配表
 * @author Administrator
 */
public class MemoryAllocateTable
{

    public final static int USE = MemoryAllocateItem.STATUS_USE;
    public final static int FREE = MemoryAllocateItem.STATUS_FREE;
    private int size;
    private MemoryAllocateItem[] table;
    private OS os;

    public MemoryAllocateTable(OS os, int startAddress)
    {
        this.os = os;
        this.size = 256;
        this.table = new MemoryAllocateItem[size / 9];
        for (int i = 0; i < table.length; i++)
        {
            table[i] = new MemoryAllocateItem(os, startAddress + i * MemoryAllocateItem.SIZE);
        }
    }

    public void init()
    {
        /***********|I|A|S|*Left**|*Right*|**Pre**|*Next**|**Use***|*/
        //未用结点头结点
        table[0].setNode(0, 0, (byte) 0, (byte) 0, (byte) 0, (byte) 5, (byte) MemoryAllocateItem.STATUS_FREE);
        //未分配内存头结点
        table[1].setNode(0, 0, (byte) 0, (byte) 0, (byte) 0, (byte) 4, (byte) MemoryAllocateItem.STATUS_USE);
        //内存分配表头结点
        table[2].setNode(0, 0, (byte) 0, (byte) 3, (byte) 0, (byte) 0, (byte) MemoryAllocateItem.STATUS_USE);
        //内存管理程序占用内存的结点
        table[3].setNode(0, size, (byte) 2, (byte) 4, (byte) 0, (byte) 0, (byte) MemoryAllocateItem.STATUS_USE);
        //第一块未用内存的结点
        table[4].setNode(size, 2048 - size, (byte) 3, (byte) 0, (byte) 1, (byte) 0, (byte) MemoryAllocateItem.STATUS_FREE);
        for (int i = 5; i < table.length - 1; i++)
        {
            table[i].setNext(i + 1);
            table[i].setUse(MemoryAllocateItem.STATUS_FREE);
        }
        table[table.length - 1].setNext(0);
    }

    public int getEmptyHead()
    {
        return 0;
    }

    public int getUnUseHead()
    {
        return 1;
    }

    public void removeFromUnUseQueue(int index)
    {
        int pre, nex;
        pre = table[index].getPre();
        nex = table[index].getNext();
        if (4 <= pre && pre < table.length)
        {
            table[pre].setNext(nex);
        }
        if (4 <= nex && nex < table.length)
        {
            table[nex].setPre(pre);
        }
    }

    public void insertToEmptyQueue(int index)
    {
        int head, nex;
        head = this.getEmptyHead();
        nex = table[head].getNext();
        table[head].setNext(index);
        table[index].setNext(nex);
        table[index].setNode(0, 0, (byte) 0, (byte) 0, (byte) 0, (byte) (nex & 0x000000ff), (byte) FREE);
    }

    public int getEmptyMCB()
    {
        int head, nex;
        head = this.getEmptyHead();
        nex = table[head].getNext();
        if (nex != 0)
        {
            table[head].setNext(table[nex].getNext());
        }
        return nex;
    }

    public MemoryAllocateItem get(int index)
    {
        if (0 <= index && index < table.length)
        {
            return table[index];
        }
        return null;
    }

    public int getStartAddress(int index)
    {
        if (get(index) != null)
        {
            return get(index).getStartAddress();
        }
        return 0;
    }

    public int getLength(int index)
    {
        if (get(index) != null)
        {
            return get(index).getLength();
        }
        return 0;
    }

    public void sendInformation()
    {
        MemoryInformation memoryInformation = os.cpu.memoryInformation;
        if (memoryInformation != null)
        {
            memoryInformation.init();
            for (int i = 2; i != 0; i = table[i].getRight())
            {
                memoryInformation.add(i, table[i].getStartAddress(), table[i].getLength(), table[i].getUse());
            }
        }
    }
}
