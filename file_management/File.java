package computer.os.kernel.file_management;

/**
 *
 * @author Administrator
 */
public class File
{

    private int blockNumber;
    private int offset;
    private HardDiskBuffer hdb;

    public File(HardDiskBuffer hdb)
    {
        this.hdb = hdb;
    }

    public boolean canExecute()
    {
        return hdb.read(getBlockNumber(), getOffset() + 3) == (byte) ('e');
    }

    public boolean canRead()
    {
        return true;
    }

    public boolean canWrite()
    {
        return (getAttribute() & 1) == 0;
    }

    public boolean isSystemFile()
    {
        return (getAttribute() & (1 << 1)) != 0;
    }

    public boolean isCommonFile()
    {
        return (getAttribute() & (1 << 2)) != 0;
    }

    public boolean isDirectory()
    {
        return (getAttribute() & (1 << 3)) != 0;
    }

    public void getFileName(byte[] bt)
    {
        for (int i = 0; i < 4 && i < bt.length; i++)
        {
            bt[i] = hdb.read(getBlockNumber(), getOffset() + i);
        }
    }

    public byte getAttribute()
    {
        return hdb.read(getBlockNumber(), getOffset() + 4);
    }

    public byte getStartBlockNumber()
    {
        return hdb.read(getBlockNumber(), getOffset() + 5);
    }

    public int getLength()
    {
        return util.NumberTool.getIntFrom2Byte(hdb.read(getBlockNumber(), getOffset() + 6), hdb.read(getBlockNumber(), getOffset() + 7));
    }

    public void setLength(int length)
    {
        hdb.write(getBlockNumber(), getOffset() + 6, util.NumberTool.getLowByteFromInt(length));
        hdb.write(getBlockNumber(), getOffset() + 7, util.NumberTool.getHigByteFromInt(length));
    }

    public void setAttribute(byte attribute)
    {
        hdb.write(getBlockNumber(), getOffset() + 4, attribute);
    }

    public void setExecutable(boolean executable)
    {
        if (executable)
        {
            hdb.write(getBlockNumber(), getOffset() + 3, (byte) ('e'));
        } else
        {
            hdb.write(getBlockNumber(), getOffset() + 3, (byte) ('c'));
        }
    }

    public void setReadable(boolean readable)
    {
    }

    public void setWritable(boolean writable)
    {
        if (writable)
        {
            setAttribute((byte) (getAttribute() | 1));
        } else
        {
            setAttribute((byte) (getAttribute() & (~1)));
        }
    }

    public void setSystemFile(boolean flag)
    {
        if (flag)
        {
            setAttribute((byte) (getAttribute() | 2));
        } else
        {
            setAttribute((byte) (getAttribute() & (~2)));
        }
    }

    public void setCommonFile(boolean flag)
    {
        if (flag)
        {
            setAttribute((byte) (getAttribute() | 4));
        } else
        {
            setAttribute((byte) (getAttribute() & (~4)));
        }
    }

    public void setDirectory(boolean flag)
    {
        if (flag)
        {
            setAttribute((byte) (getAttribute() | 8));
        } else
        {
            setAttribute((byte) (getAttribute() & (~8)));
        }
    }

    /**
     * @param blockNumber the blockNumber to set
     */
    public void setBlockNumber(int blockNumber)
    {
        this.blockNumber = blockNumber;
    }

    /**
     * @param offset the offset to set
     */
    public void setOffset(int offset)
    {
        this.offset = offset;
    }

    /**
     * @return the blockNumber
     */
    public int getBlockNumber()
    {
        return blockNumber;
    }

    /**
     * @return the offset
     */
    public int getOffset()
    {
        return offset;
    }
}
