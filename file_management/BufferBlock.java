package computer.os.kernel.file_management;

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
    public final static int OCCUPY_MEMORY_SIZE = 66;
    private int index;
    private int startAddress;
    
    public BufferBlock(OS os, int startAddress, int index)
    {
        super(os);
        this.startAddress = startAddress;
        this.index = index;
    }

    public int getNumber()
    {
        return 0x000000ff & super.read(startAddress + index * OCCUPY_MEMORY_SIZE);
    }

    public int getStatus()
    {
        return 0x000000ff & super.read(startAddress + index * OCCUPY_MEMORY_SIZE + 1);
    }

    public void setNumber(int number)
    {
        super.write(startAddress + index * OCCUPY_MEMORY_SIZE, (byte) (0x000000ff & number));
    }

    public void setStatus(int status)
    {
        super.write(startAddress + index * OCCUPY_MEMORY_SIZE + 1, (byte) (0x000000ff & status));
    }

    @Override
    public byte read(int offset)
    {
        int status;
        status = this.getStatus();
        status |= BufferBlock.STATUS_HAVE_READ;
        this.setStatus(status);
        return super.read(startAddress + index * OCCUPY_MEMORY_SIZE + 2 + offset);
    }

    @Override
    public void write(int offset, byte b)
    {
        int status;
        status = this.getStatus();
        status |= BufferBlock.STATUS_HAVE_WRITE;
        this.setStatus(status);
        super.write(startAddress + index * OCCUPY_MEMORY_SIZE + 2 + offset, b);
    }

    public void loadBlock(int blockNumber)
    {
        int count = 0, status;
        this.setNumber(blockNumber);
        this.setStatus(BufferBlock.STATUS_CLEAR);
        os.hardDiskDriver.open();
        os.hardDiskDriver.readBlock(blockNumber, startAddress + index * OCCUPY_MEMORY_SIZE + 2, 64);
    }

    public void saveBlock()
    {
        int count = 0, status;
        this.setStatus(BufferBlock.STATUS_CLEAR);
        os.hardDiskDriver.writeBlock(this.getNumber(), startAddress + index * OCCUPY_MEMORY_SIZE + 2, 64);
    }
}
