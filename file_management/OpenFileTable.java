package computer.os.kernel.file_management;

import computer.os.base.OS;



/**
 * 文件打开表
 * @author Administrator
 */
public class OpenFileTable
{

    public final static int SIZE = 16;
    public final static int OCCUPY_MEMORY_SIZE = SIZE * OpenFileItem.OPEN_FILE_TABLE_ITEM_SIZE;
    private OpenFileItem[] table;

    public OpenFileTable(OS os, int address)
    {
        this.table = new OpenFileItem[SIZE];
        for (int i = 0; i < this.table.length; i++)
        {
            this.table[i] = new OpenFileItem(os, address, i);
        }
    }

    public void init()
    {
        for (int i = 0; i < this.table.length; i++)
        {
            this.table[i].setStartBlockNumber((byte) (-1));
        }
    }

    public OpenFileItem get(int index)
    {
        if (0 <= index && index < table.length)
        {
            return table[index];
        }
        return null;
    }

    public int addItem(File file, byte openType)
    {
        for (int i = 0; i < table.length; i++)
        {
            if (table[i].getStartBlockNumber() == -1)
            {
                table[i].setStartBlockNumber(file.getStartBlockNumber());
                table[i].setLength(file.getLength());
                table[i].setAttribute(file.getAttribute());
                table[i].setNowBlockNumber(file.getStartBlockNumber());
                table[i].setNowBlockOffset((byte) 0);
                table[i].setOpenType(openType);
                return i;
            }
        }
        return -1;
    }

    public void removeItem(int index)
    {
        table[index].setStartBlockNumber((byte) (-1));
    }

    public int find(int startBlockNumber)
    {
        for (int i = 0; i < table.length; i++)
        {
            if (table[i].getStartBlockNumber() == startBlockNumber)
            {
                return i;
            }
        }
        return -1;
    }
}
