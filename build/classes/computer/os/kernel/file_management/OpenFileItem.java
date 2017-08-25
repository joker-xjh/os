package computer.os.kernel.file_management;

import computer.os.base.InnerMemoryManager;
import computer.os.base.InnerMemoryManager.InnerMemory;

/**
 * 文件打开表项
 * @author Administrator
 */
public class OpenFileItem
{

    public final static int SIZE = 8;
    public final static int OPEN_TYPE_READ = 0;
    public final static int OPEN_TYPE_WRITE = 1;
    private InnerMemory memory;

    public OpenFileItem(InnerMemoryManager innerMemoryManager)
    {
        memory = innerMemoryManager.allocate(SIZE);
    }

    public boolean canRead()
    {
        return (getOpenType() == OPEN_TYPE_READ);
    }

    public boolean canWrite()
    {
        return (getOpenType() == OPEN_TYPE_WRITE);
    }

    public byte getStartBlockNumber()
    {
        return memory.read(0);
    }

    public int getLength()
    {
        return memory.read2Byte(1);
    }

    public byte getAttribute()
    {
        return memory.read(3);
    }

    public byte getOpenType()
    {
        return memory.read(4);
    }

    public byte getNowBlockNumber()
    {
        return memory.read(5);
    }

    public byte getNowBlockOffset()
    {
        return memory.read(6);
    }

    public void setStartBlockNumber(byte b)
    {
        memory.write(0, b);
    }

    public void setLength(int length)
    {
        memory.write2Byte(1, length);
    }

    public void setAttribute(byte b)
    {
        memory.write(3, b);
    }

    public void setOpenType(byte b)
    {
        memory.write(4, b);
    }

    public void setNowBlockNumber(byte b)
    {
        memory.write(5, b);
    }

    public void setNowBlockOffset(byte b)
    {
        memory.write(6, b);
    }
}
