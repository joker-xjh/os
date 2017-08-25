package computer.hardware.device;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * 模拟磁盘
 * @author Administrator
 */
public class HardDisk
{

    //辅助变量
    private boolean openFlag;
    private String diskFileName;
    private int blockNumber;
    private int blockLength;
    private RandomAccessFile disk;

    /**
     * 构造磁盘文件为"disk"、有128块、块长为64字节的模拟磁盘。
     */
    public HardDisk()
    {
        this("disk", 128, 64);
    }

    /**
     * 构造磁盘文件为"diskFileName"，有blockNumber块，块长为blockLength字节的模拟磁盘。
     * @param diskFileName
     * @param blockNumber
     * @param blockLength
     */
    public HardDisk(String diskFileName, int blockNumber, int blockLength)
    {
        this.openFlag = false;
        this.diskFileName = diskFileName;
        this.blockNumber = blockNumber;
        this.blockLength = blockLength;
    }

    /**
     * 打开磁盘文件，磁盘文件已打开返回true，否则返回false。
     * @return
     */
    public void open()
    {
        FileInputStream fis;
        File file = new File(this.diskFileName);
        if (!this.openFlag)
        {
            if (!file.exists() || file.getTotalSpace() < this.blockNumber * this.blockLength)
            {
                this.create();
            }
            try
            {
                disk = new RandomAccessFile(file, "rw");
            } catch (FileNotFoundException ex)
            {
                this.create();
            }
            this.openFlag = true;
        }
        util.out.println("HardDisk-open:硬盘打开成功");
    }

    /**
     * 保存磁盘文件，关闭磁盘。
     */
    public void close()
    {
        FileOutputStream fos;
        File file = new File(this.diskFileName);
        if (this.openFlag)
        {
            try
            {
                this.disk.close();
                util.out.println("HardDisk-close:硬盘关闭成功");
            } catch (IOException ex)
            {
                util.out.println("HardDisk-close:关闭文件失败！");
            }
            this.openFlag=false;
        }
    }

    public void read(byte[] bt, int offset, int blockNum)
    {
        if (this.openFlag)
        {
            try
            {
                this.disk.seek((blockNum << 6));
                for (int i = 0; offset + i < bt.length & i < this.blockLength; i++)
                {
                    bt[offset + i] = (byte) this.disk.read();
                }
                util.out.println("HardDisk-read:读成功！");
            } catch (IOException ex)
            {
                util.out.println("HardDisk-read:读取文件失败！");
            }
        } else
        {
            util.out.println("HardDisk-read:硬盘未开启！");
        }
    }

    public void write(byte[] bt, int offset, int blockNum)
    {
        if (this.openFlag)
        {
            try
            {
                this.disk.seek((blockNum << 6));
                for (int i = 0; offset + i < bt.length & i < this.blockLength; i++)
                {
                    this.disk.write(bt[offset + i]);
                }
                util.out.println("HardDisk-write:写成功！");
            } catch (IOException ex)
            {
                util.out.println("HardDisk-write:写入文件失败！");
            }
        } else
        {
            util.out.println("HardDisk-write:硬盘未开启！");
        }
    }

    private void create()
    {
        FileOutputStream fos;
        File file = new File(this.diskFileName);
        try
        {
            fos = new FileOutputStream(file);
            for (int i = 0; i < this.blockNumber; i++)
            {
                for (int j = 0; j < this.blockLength; j++)
                {
                    fos.write(0);
                }
            }
            fos.flush();
            fos.close();
        } catch (FileNotFoundException ex)
        {
            this.openFlag = true;
        } catch (IOException ex)
        {
            this.openFlag = true;
        }
    }
}
