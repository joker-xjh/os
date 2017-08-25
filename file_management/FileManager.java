package computer.os.kernel.file_management;

import computer.os.base.OS;

/**
 * 文件管理程序
 * @author Administrator
 */
public class FileManager extends computer.os.base.SystemProcess
{

    public final static int SIZE = HardDiskBuffer.OCCUPY_MEMORY_SIZE + OpenFileTable.OCCUPY_MEMORY_SIZE;
    private FAT fat;
    private FileTree fileTree;
    private HardDiskBuffer hdb;
    private OpenFileTable table;
    private File file;
    private int index;

    public FileManager(OS os)
    {
        super(os);
        this.hdb = new HardDiskBuffer(os, 0);
        this.fat = new FAT(hdb);
        this.fileTree = new FileTree(hdb, fat);
        this.table = new OpenFileTable(os, HardDiskBuffer.OCCUPY_MEMORY_SIZE);
        this.file = new File(hdb);
    }

    @Override
    protected void push()
    {
        super.push();
        os.cpu.RR = os.memoryManager.getStartAddress(index);
        os.cpu.LR = os.memoryManager.getLength(index);
    }

    public void init()
    {
        push();
        hdb.init();
        table.init();
        pop();
    }

    public boolean create(byte[][] fileName, byte attribute)
    {
        push();
        System.out.println("开始创建！");
        boolean flag = this.fileTree.create(file, fileName, attribute);
        this.fat.sendInformation(os.cpu.fatInformation);
        this.os.displayManager.showFile();
        pop();
        return flag;
    }

    public boolean delete(byte[][] fileName)
    {
        push();
        boolean flag = this.fileTree.delete(file, fileName);
        this.fat.sendInformation(os.cpu.fatInformation);
        this.os.displayManager.showFile();
        pop();
        return flag;
    }

    public int open(byte[][] fileName, byte openType)
    {
        push();
        int filePoint = -1;
        this.fileTree.find(file, fileName, fileName.length);
        if (/*!file.isDirectory() &&*/((openType == OpenFileItem.OPEN_TYPE_READ && file.canRead())
                || (openType == OpenFileItem.OPEN_TYPE_WRITE && file.canWrite())))
        {
            if (table.find(file.getStartBlockNumber()) == -1)
            {
                filePoint = table.addItem(file, openType);
            }
        }
        pop();
        return filePoint;
    }

    public void close(byte[][] fileName)
    {
        int indx;
        push();
        this.fileTree.find(file, fileName, fileName.length);
        if (file.getStartBlockNumber() != -1)
        {
            indx = this.table.find(file.getStartBlockNumber());
            if (indx != -1)
            {
                OpenFileItem ofi = table.get(indx);
                if (ofi != null && ofi.getStartBlockNumber() != -1 && ofi.canWrite())
                {
                    file.setLength(ofi.getLength());
                    pop();
                    write1(indx, (byte) '#');
                    push();
                }
                this.table.removeItem(indx);
            }
        }
        pop();
    }

    public void format()
    {
        push();
        this.fileTree.format();
        this.fat.sendInformation(os.cpu.fatInformation);
        this.os.displayManager.showFile();
        pop();
    }

    public byte read1(int filePoint)
    {
        push();
        byte b = -2;
        int blockNumber, offset;
        OpenFileItem ofi;
        ofi = table.get(filePoint);
        if (ofi != null && ofi.canRead())
        {
            blockNumber = ofi.getNowBlockNumber();
            offset = ofi.getNowBlockOffset();
            b = hdb.read(blockNumber, offset);
            if (b == (byte) '#')
            {
                b = -1;
            } else
            {
                if (offset >= 64)
                {
                    ofi.setNowBlockNumber(fat.get((byte) blockNumber));
                    ofi.setNowBlockOffset((byte) 0);
                } else
                {
                    ofi.setNowBlockOffset((byte) (offset + 1));
                }
            }
        }
        pop();
        return b;
    }

    private boolean write1(int filePoint, byte b)
    {
        push();
        boolean flag = false;
        int blockNumber, offset, length;
        OpenFileItem ofi;
        ofi = table.get(filePoint);
        if (ofi != null && ofi.canWrite())
        {
            blockNumber = ofi.getNowBlockNumber();
            offset = ofi.getNowBlockOffset();
            length = ofi.getLength();
            hdb.write(blockNumber, offset, b);
            if (offset >= 63)
            {
                byte t = fat.allocate();
                if (t != -1)
                {
                    fat.set((byte) blockNumber, t);
                    fat.set(t, (byte) -1);
                    ofi.setNowBlockNumber(fat.get((byte) blockNumber));
                    ofi.setNowBlockOffset((byte) 0);
                    ofi.setLength(length + 1);
                    flag = true;
                }
            } else
            {
                ofi.setNowBlockOffset((byte) (offset + 1));
                ofi.setLength(length + 1);
                flag = true;
            }
        }
        pop();
        return flag;
    }

    public int read(byte[][] fileName, byte[] bt, int length)
    {
        push();
        int len = -1, cnt = 0;
        int filePoint;
        OpenFileItem ofi;
        if (fileTree.find(file, fileName, fileName.length))
        {
            filePoint = table.find(file.getStartBlockNumber());
            ofi = table.get(filePoint);
            if (filePoint != -1 && ofi != null && ofi.canRead())
            {
                len = 0;
                for (int i = 0; i < length && i < bt.length; i++)
                {
                    pop();
                    bt[i] = read1(filePoint);
                    System.out.println(cnt++ + "::" + bt[i]);
                    push();
                    len++;
                    if (bt[i] == -1)
                    {
                        break;
                    }
                }
            }
        }
        pop();
        return len;
    }

    public boolean write(byte[][] fileName, byte[] bt, int length)
    {
        push();
        boolean flag = false;
        int filePoint;
        OpenFileItem ofi;
        if (fileTree.find(file, fileName, fileName.length))
        {
            filePoint = table.find(file.getStartBlockNumber());
            ofi = table.get(filePoint);
            if (filePoint != -1 && ofi != null && ofi.canWrite())
            {
                for (int i = 0; i < length && i < bt.length; i++)
                {
                    pop();
                    flag = write1(filePoint, bt[i]);
                    push();
                    if (!flag)
                    {
                        break;
                    }
                }
                flag = true;
            }
        }
        pop();
        return flag;
    }

    public byte getFileAttribute(byte[][] fileName)
    {
        byte attribute = -1;
        push();
        if (this.fileTree.find(file, fileName, fileName.length))
        {
            attribute = file.getAttribute();
        }
        pop();
        return attribute;
    }

    public int getFileLength(byte[][] fileName)
    {
        int length = -1;
        push();
        if (this.fileTree.find(file, fileName, fileName.length))
        {
            length = file.getLength();
        }
        pop();
        return length;
    }

    public boolean canExecute(byte[][] fileName)
    {
        boolean flag = false;
        push();
        if (this.fileTree.find(file, fileName, fileName.length))
        {
            flag = file.canExecute();
        }
        pop();
        return flag;
    }

    public void flush()
    {
        push();
        this.hdb.flush();
        pop();
    }

    public void setMCBIndex(int index)
    {
        this.index = index;
    }

    public void sendInformation()
    {
        push();
        this.fat.sendInformation(os.cpu.fatInformation);
        this.os.displayManager.showFile();
        pop();
    }

    public boolean type(byte[][] fileName)
    {
        push();
        boolean flag = this.fileTree.find(file, fileName, fileName.length);
        pop();
        if (flag)
        {
            if (this.open(fileName, (byte) OpenFileItem.OPEN_TYPE_READ) >= 0)
            {
                System.out.println("打开成功！");
//                this.read(fileName, this.os.cpu.displayManager.getFileInformation().getBody(), Integer.MAX_VALUE);
                System.out.println("已读取！");
                this.close(fileName);
                System.out.println("关闭成功！");
            }
        }
        return flag;
    }

    public void check(int blockNumber)
    {
        push();
        for (int i = 0; i < 64; i++)
        {
            System.out.printf("%4d",(0xff&hdb.read(blockNumber,i)));
            if(i%8==7)
            {
                System.out.println("");
            }
        }
        pop();
    }
}
