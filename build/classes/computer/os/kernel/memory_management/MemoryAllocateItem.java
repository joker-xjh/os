package computer.os.kernel.memory_management;

import computer.os.base.OS;

/**
 *
 * @author Administrator
 */
public class MemoryAllocateItem extends computer.os.base.ReadAndWrite
{

    public final static int STATUS_USE = 1;
    public final static int STATUS_FREE = 0;
    public final static int MEMORY_ALLOCATE_ITEM_SIZE = 9;
    public final static int SIZE = MEMORY_ALLOCATE_ITEM_SIZE;
    private final static int SATAR_ADDRESS = 0;
    private final static int LENGTH = 2;
    private final static int LEFT = 4;
    private final static int RIGHT = 5;
    private final static int PRE = 6;
    private final static int NEXT = 7;
    private final static int USE = 8;
    private int startAddress;

    public MemoryAllocateItem(OS os, int startAddress)
    {
        super(os);
        this.startAddress = startAddress;
    }

    public void setNode(int address, int length, byte left, byte rigth, byte pre, byte next, byte use)
    {
        this.setStartAddress(address);
        this.setLength(length);
        this.setLeft(left);
        this.setRight(rigth);
        this.setPre(pre);
        this.setNext(next);
        this.setUse(use);
    }

    public void setStartAddress(int address)
    {
        this.write2Byte(startAddress + 0, address);
    }

    public void setLength(int length)
    {
        this.write2Byte(startAddress + 2, length);
    }

    public void setLeft(int left)
    {
        this.write(startAddress + 4, (byte) (0x000000ff & left));
    }

    public void setRight(int right)
    {
        this.write(startAddress + 5, (byte) (0x000000ff & right));
    }

    public void setPre(int pre)
    {
        this.write(startAddress + 6, (byte) (0x000000ff & pre));
    }

    public void setNext(int next)
    {
        this.write(startAddress + 7, (byte) (0x000000ff & next));
    }

    public void setUse(int use)
    {
        this.write(startAddress + 8, (byte) (0x000000ff & use));
    }

    public int getStartAddress()
    {
        return this.read2Byte(startAddress + 0);
    }

    public int getLength()
    {
        return this.read2Byte(startAddress + 2);
    }

    public int getLeft()
    {
        return 0x000000ff & this.read(startAddress + 4);
    }

    public int getRight()
    {
        return 0x000000ff & this.read(startAddress + 5);
    }

    public int getPre()
    {
        return 0x000000ff & this.read(startAddress + 6);
    }

    public int getNext()
    {
        return 0x000000ff & this.read(startAddress + 7);
    }

    public int getUse()
    {
        return 0x000000ff & this.read(startAddress + 8);
    }
}
