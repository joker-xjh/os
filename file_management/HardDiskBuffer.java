package computer.os.kernel.file_management;

import computer.hardware.cpu.CPU;
import computer.os.base.OS;

/**
 * 硬盘缓冲
 * @author Administrator
 */
public class HardDiskBuffer
{

    public final static int OCCUPY_MEMORY_SIZE = BufferBlock.OCCUPY_MEMORY_SIZE * 4;
    private CPU cpu;
    private BufferBlock[] blocks;

    public HardDiskBuffer(OS os, int startAddress)
    {
        this.cpu = os.cpu;
        this.blocks = new BufferBlock[4];
        for (int i = 0; i < blocks.length; i++)
        {
            this.blocks[i] = new BufferBlock(os, startAddress, i);
        }
    }

    public void init()
    {
        for (int i = 0; i < this.blocks.length; i++)
        {
            this.blocks[i].setNumber(-1);
        }
    }

    public byte read(int blockNumber, int offset)
    {
        int index = this.getIndexOfBlock(blockNumber);
        return this.blocks[index].read(offset);
    }

    public void write(int blockNumber, int offset, byte b)
    {
        int index = this.getIndexOfBlock(blockNumber);
        this.blocks[index].write(offset, b);
    }

    public void flush()
    {
        int block;
        for (int i = 0; i < this.blocks.length; i++)
        {
            block = this.blocks[i].getNumber();
            if (0 <= block && block < 128 && (this.blocks[i].getStatus() & BufferBlock.STATUS_HAVE_WRITE) != 0)
            {
                this.blocks[i].saveBlock();
            }
        }
    }

    private int getIndexOfBlock(int blockNumber)
    {
        int index = 0, pri = 4, number;
        for (int i = 0; i < this.blocks.length; i++)
        {
            number = this.blocks[i].getNumber();
            if (blockNumber == number)
            {
                index = i;
                break;
            }
            if (blockNumber == -1)
            {
                index = i;
                pri = -1;
            } else if (this.blocks[i].getStatus() < pri)
            {
                index = i;
                pri = this.blocks[i].getStatus();
            }
        }
        if (this.blocks[index].getNumber() != blockNumber)
        {
            this.swapBlock(index, blockNumber);
            this.blocks[index].setNumber(blockNumber);
        }
        return index;
    }

    private void swapBlock(int index, int newBlockNumber)
    {
        int block;
        if (0 <= index && index < this.blocks.length)
        {
            block = this.blocks[index].getNumber();
            if (0 <= block && block < 128 && (this.blocks[index].getStatus() & BufferBlock.STATUS_HAVE_WRITE) != 0)
            {
                this.blocks[index].saveBlock();
            }
            this.blocks[index].loadBlock(newBlockNumber);
        } else
        {
            util.out.println("HardDiskBufferManager-swapBlock:访问非法缓冲块！");
        }
    }
}
