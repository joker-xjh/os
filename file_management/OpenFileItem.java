package computer.os.kernel.file_management;

import computer.os.base.OS;



/**
 * 文件打开表项
 * @author Administrator
 */
public class OpenFileItem extends computer.os.base.ReadAndWrite
{

    public final static int OPEN_FILE_TABLE_ITEM_SIZE = 8;
    public final static int OFTI_SIZE = OPEN_FILE_TABLE_ITEM_SIZE;
    public final static int OPEN_TYPE_READ = 0;
    public final static int OPEN_TYPE_WRITE = 1;
    private int index;
    private int address;

    public OpenFileItem(OS os, int address, int index)
    {
        super(os);
        this.address = address;
        this.index = index;
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
        return super.read(address + index * OFTI_SIZE);
    }

    public int getLength()
    {
        return super.read2Byte(address + index * OFTI_SIZE + 1);
    }

    public byte getAttribute()
    {
        return super.read(address + index * OFTI_SIZE + 3);
    }

    public byte getOpenType()
    {
        return super.read(address + index * OFTI_SIZE + 4);
    }

    public byte getNowBlockNumber()
    {
        return super.read(address + index * OFTI_SIZE + 5);
    }

    public byte getNowBlockOffset()
    {
        return super.read(address + index * OFTI_SIZE + 6);
    }

    public void setStartBlockNumber(byte b)
    {
        super.write(address + index * OFTI_SIZE, b);
    }

    public void setLength(int length)
    {
        super.write2Byte(address + index * OFTI_SIZE + 1, length);
    }

    public void setAttribute(byte b)
    {
        super.write(address + index * OFTI_SIZE + 3, b);
    }

    public void setOpenType(byte b)
    {
        super.write(address + index * OFTI_SIZE + 4, b);
    }

    public void setNowBlockNumber(byte b)
    {
        super.write(address + index * OFTI_SIZE + 5, b);
    }

    public void setNowBlockOffset(byte b)
    {
        super.write(address + index * OFTI_SIZE + 6, b);
    }
}
