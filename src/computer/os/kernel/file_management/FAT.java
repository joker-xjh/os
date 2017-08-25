package computer.os.kernel.file_management;

import GUI.desktop.window.fat_manager.FATInformation;

/**
 * 文件分配表
 * @author Administrator
 */
public class FAT
{

    private HardDiskBuffer hdbm;

    public FAT(HardDiskBuffer hdbm)
    {
        this.hdbm = hdbm;
    }

    public byte get(byte index)
    {
        if (0 <= index && index < 64)
        {
            return hdbm.read(0, index);
        } else if (64 <= index && index < 128)
        {
            return hdbm.read(1, index - 64);
        }
        return -2;
    }

    public void set(byte index, byte b)
    {
        if (0 <= index && index < 64)
        {
            hdbm.write(0, index, b);
        } else if (64 <= index && index < 128)
        {
            hdbm.write(1, index - 64, b);
        }
    }

    public byte allocate()
    {
        byte index = -1;
        for (int i = 3; i < 128; i++)
        {
            if (get((byte) i) == 0)
            {
                index = (byte) i;
                set(index, (byte) (-1));
                break;
            }
        }
        return index;
    }

    public void collect(byte index)
    {
        if (3 <= index && index < 128)
        {
            set(index, (byte) 0);
        }
    }

    public void sendInformation(FATInformation fatInformation)
    {
        if (fatInformation != null)
        {
            fatInformation.init();
            for (int i = 0; i < 128; i++)
            {
                fatInformation.set(i, this.get((byte) i));
            }
        }
    }
}
