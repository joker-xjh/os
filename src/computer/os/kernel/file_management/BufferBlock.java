package computer.os.kernel.file_management;

import computer.os.base.InnerMemoryManager;
import computer.os.base.InnerMemoryManager.InnerMemory;
import computer.os.base.OS;

/**
 * 缓冲块
 * @author Administrator
 */
public class BufferBlock extends computer.os.base.ReadAndWrite
{

    public final static int STATUS_CLEAR = 0;
    public final static int STATUS_HAVE_READ = 1;
    public final static int STATUS_HAVE_WRITE = 2;
    public final static int SIZE = 66;
    private InnerMemory memory;

    public BufferBlock(OS os, InnerMemoryManager innerMemoryManager)
    {
        super(os);
        this.memory = innerMemoryManager.allocate(SIZE);
    }

    public int getNumber()
    {
        return memory.read(64);
    }

    public int getStatus()
    {
        return memory.read(65);
    }

    public void setNumber(int number)
    {
        memory.write(64, (byte) (0x000000ff & number));
    }

    public void setStatus(int status)
    {
        memory.write(65, (byte) (0x000000ff & status));
    }

    @Override
    public byte read(int offset)
    {
        int status;
        status = this.getStatus();
        status |= BufferBlock.STATUS_HAVE_READ;
        this.setStatus(status);
        return memory.read(offset);
    }

    @Override
    public void write(int offset, byte b)
    {
        int status;
        status = this.getStatus();
        status |= BufferBlock.STATUS_HAVE_WRITE;
        this.setStatus(status);
        memory.write(offset, b);
    }

    public void loadBlock(int blockNumber)
    {
        int count = 0, status;
        this.setNumber(blockNumber);
        this.setStatus(BufferBlock.STATUS_CLEAR);
        os.hardDiskDriver.open();
        memory.stream().init();
        os.hardDiskDriver.readBlock(blockNumber, memory.stream(), 64);
    }

    public void saveBlock()
    {
        int count = 0, status;
        this.setStatus(BufferBlock.STATUS_CLEAR);
        memory.stream().init();
        os.hardDiskDriver.writeBlock(this.getNumber(), memory.stream(), 64);
    }
}
