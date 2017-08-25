package computer.os.kernel.file_management;

import computer.os.base.InnerMemoryManager;
import computer.os.base.OS;
import java.util.Iterator;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 文件管理程序
 * @author Administrator
 */
public class FileManager extends computer.os.base.SystemProcess
{

    public final static int SIZE = HardDiskBuffer.SIZE + OpenFileTable.SIZE;
    private InnerMemoryManager innerMemoryManager;
    private FAT fat;
    private FileTree fileTree;
    private HardDiskBuffer hdb;
    private OpenFileTable table;
    private File file;
    private int index;
    private FileText fileText;

    public FileManager(OS os)
    {
        super(os);
        this.innerMemoryManager = new InnerMemoryManager(os, SIZE);
        this.hdb = new HardDiskBuffer(os, innerMemoryManager);
        this.fat = new FAT(hdb);
        this.fileTree = new FileTree(hdb, fat);
        this.table = new OpenFileTable(innerMemoryManager);
        this.file = new File(hdb);
        this.fileText = new FileText();
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
        boolean flag = this.fileTree.create(fileName, attribute, file);
        if (flag)
        {
            os.cpu.fileInformation.error = "文件创建成功。";
        } else
        {
            os.cpu.fileInformation.error = "文件路径不存在或者文件已存在，文件创建失败。";
        }
//        this.fat.sendInformation(os.cpu.fatInformation);
//        this.os.displayManager.showFile();
        pop();
        return flag;
    }

    public boolean delete(byte[][] fileName)
    {
        push();
        boolean flag = this.fileTree.delete(fileName, file);
        if (flag)
        {
            os.cpu.fileInformation.error = "文件删除成功。";
        } else
        {
            os.cpu.fileInformation.error = "文件不存在，删除失败。";
        }
//        this.fat.sendInformation(os.cpu.fatInformation);
//        this.os.displayManager.showFile();
        pop();
        return flag;
    }

    public boolean type(byte[][] fileName)
    {
        push();
        int n = -64;
        int t;
        int length;
        byte[] b = null;
        boolean flage = this.fileTree.find(fileName, fileName.length, file);
        int blocknumber;
        if (flage)
        {
            this.fileText.setLength(this.file.getLength());
            b = new byte[file.getLength() + 65];
            blocknumber = file.getStartBlockNumber();
            while (true)
            {
                n = n + 64;
                for (int i = 0; i < 64; i++)
                {
                    b[n + i] = this.hdb.read(blocknumber, i);
                    t = n + i;
                }
                if (this.fat.get((byte) blocknumber) != -1)
                {
                    blocknumber = this.fat.get((byte) blocknumber);
                } else
                {
                    break;
                }
            }

            os.cpu.fileInformation.file.init();
            os.cpu.fileInformation.error = "文件显示成功。";
            for (int i = 0; i < file.getLength(); i++)
            {
                os.cpu.fileInformation.file.add(b[i]);
            }
        } else
        {
            os.cpu.fileInformation.error = "文件路径不存在，显示失败。";
        }
        //os.cpu.fileInformation.file.add((byte) '#');
        pop();
        return flage;
    }

    public boolean copy(byte[][] oldFileName, byte[][] newFileName)
    {
        push();
        int length;
        int oldblocknumber;
        int nowblocknumber;
        byte attribute;
        int number;
        boolean flage = this.fileTree.find(oldFileName, oldFileName.length, file);
        if (flage)
        {
            oldblocknumber = file.getStartBlockNumber();
            attribute = file.getAttribute();
            length = this.file.getLength();
            flage = false;
            flage = this.create(newFileName, attribute);
            if (flage)
            {
                byte b;
                flage = this.fileTree.find(newFileName, newFileName.length, file);
                file.setLength(length);
                nowblocknumber = file.getStartBlockNumber();
                while (true)
                {
                    for (int i = 0; i < 64; i++)
                    {
                        b = this.hdb.read(oldblocknumber, i);
                        this.hdb.write(nowblocknumber, i, b);
                    }
                    if (this.fat.get((byte) oldblocknumber) != -1)
                    {
                        oldblocknumber = this.fat.get((byte) oldblocknumber);
                        number = nowblocknumber;
                        nowblocknumber = this.fat.allocate();
                        this.fat.set((byte) number, (byte) nowblocknumber);
                    } else
                    {
                        this.fat.set((byte) nowblocknumber, (byte) -1);
                        break;
                    }
                }
                os.cpu.fileInformation.error = "文件复制成功。";
            } else
            {
                os.cpu.fileInformation.error = "目标文件已存在，文件复制失败。";
            }
        } else
        {
            os.cpu.fileInformation.error = "源文件不存在，文件复制失败。";
        }
        pop();
        return flage;
    }

    public boolean mkdir(byte[][] fileName, byte attribute)
    {
        push();
        boolean flage = this.create(fileName, attribute);
        os.cpu.fileInformation.error = "kk。";
        // this.fat.sendInformation(os.cpu.fatInformation);
        //this.os.displayManager.showFile();
        if (flage)
        {
            os.cpu.fileInformation.error = "目录创建成功。";
        } else
        {
            os.cpu.fileInformation.error = "目录路径不存在或者目录已存在，目录创建失败。";
        }
        pop();
        return flage;
    }

    public boolean rmdir(byte[][] fileName)
    {
        push();
        int number = 0;
        int blocknumber;
        boolean flage = this.fileTree.find(fileName, fileName.length, file);
        if (flage)
        {
            blocknumber = file.getStartBlockNumber();
            for (int i = 0; i < 8; i++)
            {
                if (this.hdb.read(blocknumber, i * 8) == '$')
                {
                    number++;
                }
            }
            if (number < 8)
            {
                os.cpu.fileInformation.error = "目录非空，不能删除。";
            } else
            {
                this.fileTree.delete(fileName, file);
                os.cpu.fileInformation.error = "空目录删除成功。";
            }
        } else
        {
            os.cpu.fileInformation.error = "目录不存在，目录删除失败。";
        }
        //boolean flage = this.fileTree.delete(fileName, file);
        //this.fat.sendInformation(os.cpu.fatInformation);
        //this.os.displayManager.showFile();
        pop();
        return flage;
    }

    public boolean chdir(byte[][] oldfileName, byte[][] newfileName)
    {
        push();
        boolean flage = this.move(oldfileName, newfileName);
        //this.fat.sendInformation(os.cpu.fatInformation);
        //this.os.displayManager.showFile();
        pop();
        return flage;
    }

    public boolean deldir(byte[][] fileName)
    {
        push();
        int nul = 0;
        File file1 = new File(this.hdb);
        byte b[][] = new byte[10][10];
        byte[][] fileNameNew = new byte[fileName.length + 1][4];
        int blocknumber;
        boolean flage = this.fileTree.find(fileName, fileName.length, file1);
        if (flage)
        {
            blocknumber = file1.getStartBlockNumber();
            for (int i = 0; i < 8; i++)
            {
                for (int j = 0; j < 8; j++)
                {
                    if (blocknumber != 2 || i != 0)
                    {
                        b[i][j] = this.hdb.read(blocknumber, i * 8 + j);
                    } else
                    {
                        b[0][0] = '$';
                    }
                }
            }

            for (int i = 0; i < fileName.length; i++)
            {
                for (int j = 0; j < 4; j++)
                {
                    fileNameNew[i][j] = fileName[i][j];
                }
            }

            for (int i = 0; i < 8; i++)
            {
                if (b[i][0] == '$')
                {
                    nul++;
                } else
                {
                    break;
                }
            }
            if (nul < 8)
            {
                for (int i = 0; i < 8; i++)
                {
                    if (b[i][0] != '$')
                    {
                        fileNameNew[fileName.length][0] = b[i][0];
                        fileNameNew[fileName.length][1] = b[i][1];
                        fileNameNew[fileName.length][2] = b[i][2];
                        fileNameNew[fileName.length][3] = b[i][3];
                        if ((b[i][4] & (1 << 2)) != 0)
                        {
                            flage = this.delete(fileNameNew);
                        } else
                        {
                            flage = this.deldir(fileNameNew);
                        }
                    }
                }
            } 
            else
                this.rmdir(fileName);
            os.cpu.fileInformation.error = "目录删除成功。";
        } else
        {
            os.cpu.fileInformation.error = "目录不存在，目录删除失败。";
        }
       // flage = this.rmdir(fileName);
       // if (flage)
        //{
       //     os.cpu.fileInformation.error = "目录删除成功。";
      //  }
        //this.fat.sendInformation(os.cpu.fatInformation);
        //this.os.displayManager.showFile();
        pop();
        return flage;
    }

    public boolean move(byte[][] oldfileName, byte[][] nowfileName)
    {
        push();
        /* boolean flage = false;
        int length, blockNumber, offset;
        byte startBlockNumber, attribute;
        push();
        if (this.fileTree.find(oldfileName, oldfileName.length, file))
        {
        blockNumber = file.getBlockNumber();
        offset = file.getOffset();
        startBlockNumber = file.getStartBlockNumber();
        attribute = file.getAttribute();
        length = file.getLength();
        if (!this.fileTree.find(nowfileName, nowfileName.length, file))
        {
        if (this.fileTree.create(nowfileName, attribute, file))
        {
        fat.collect(file.getStartBlockNumber());
        file.setAttribute(attribute);
        file.setStartBlockNumber(startBlockNumber);
        file.setLength(length);
        for (int i = 0; i < 4 && i < nowfileName[nowfileName.length-1].length; i++)
        {
        hdb.write(file.getBlockNumber(), file.getOffset() + i, nowfileName[nowfileName.length-1][i]);
        }
        hdb.write(blockNumber, offset, (byte) '$');
        flage = true;
        }
        }
        }*/
        int length;
        int startblocknumber;
        byte attribute;
        int n = 0;
        int s[] = new int[129];
        boolean flage = this.fileTree.find(oldfileName, oldfileName.length, file);
        if (flage)
        {
            length = this.file.getLength();
            attribute = file.getAttribute();
            startblocknumber = this.file.getStartBlockNumber();
            if (!this.fileTree.find(nowfileName, nowfileName.length, file))
            {
                s[n++] = startblocknumber;
                while (this.fat.get((byte) startblocknumber) != -1)
                {
                    startblocknumber = this.fat.get((byte) startblocknumber);
                    s[n++] = startblocknumber;
                }
                this.delete(oldfileName);
                flage = this.fileTree.create(nowfileName, attribute, file);
                this.fat.collect(this.file.getStartBlockNumber());
                this.file.setStartBlockNumber((byte) s[0]);
                this.file.setLength(length);
                for (int i = 1; i < n; i++)
                {
                    this.fat.set((byte) s[i - 1], (byte) s[i]);
                }
                this.fat.set((byte) s[n - 1], (byte) -1);
                os.cpu.fileInformation.error = "文件移动成功。";
            } else
            {
                os.cpu.fileInformation.error = "目标文件已存在，文件移动失败。";
            }
        } else
        {
            os.cpu.fileInformation.error = "源文件不存在，文件移动失败。";
        }
        pop();
        return flage;
    }

    public boolean change(byte[][] fileName, byte attribute)
    {
        push();
        boolean flage = this.fileTree.find(fileName, fileName.length, file);
        if (flage)
        {
            file.setAttribute(attribute);
            this.hdb.flush();
            os.cpu.fileInformation.error = "文件存在，属性修改成功。";
        } else
        {
            os.cpu.fileInformation.error = "文件不存在，属性修改失败。";
        }
        pop();
        return flage;
    }

    public int open(byte[][] fileName, byte openType)
    {
        push();
        int filePoint = -1;
        if (fileTree.find(fileName, fileName.length, file) && !file.isDirectory() && ((openType == OpenFileItem.OPEN_TYPE_READ && file.canRead())
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
        this.fileTree.find(fileName, fileName.length, file);
        if (file.getStartBlockNumber() != -1)
        {
            indx = this.table.find(file.getStartBlockNumber());
            if (indx != -1)
            {
                OpenFileItem ofi = table.get(indx);
                if (ofi != null && ofi.getStartBlockNumber() != -1 && ofi.canWrite())
                {
                    file.setLength(ofi.getLength());
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
        hdb.flush();
        os.hardDiskDriver.close();
        os.hardDiskDriver.open();
        os.cpu.fileInformation.error = "格式化成功。";
        pop();
    }

    public void write(byte[][] fileName)
    {
        push();
        int length = 0;
        byte n, t, disknumber;
        if (this.fileTree.find(fileName, fileName.length, file))
        {
            Iterator it = os.cpu.fileInformation.file.iterator();
            if (it != null)
            {
                while (it.hasNext())
                {
                    length++;
                    it.next();
                }
                byte[] b = new byte[length];
                it = os.cpu.fileInformation.file.iterator();
                int m = 0;
                while (it.hasNext())
                {
                    b[m++] = (Byte) it.next();
                }

                file.setLength(length);
                n = file.getStartBlockNumber();
                while (this.fat.get(n) != -1)
                {
                    t = this.fat.get(n);
                    this.fat.collect(n);
                    n = t;
                }
                this.fat.collect(n);
                for (int i = 0; i < (length + 63) / 64; i++)
                {
                    disknumber = this.fat.allocate();
                    for (int j = 0; j < 64; j++)
                    {
                        if (i * 64 + j < length)
                        {
                            this.hdb.write(disknumber, j, b[i * 64 + j]);
                        }
                    }
                    if (i == 0)
                    {
                        file.setStartBlockNumber(disknumber);
                    } else
                    {
                        this.fat.set(n, disknumber);
                    }
                    n = disknumber;
                    this.fat.set(disknumber, (byte) -1);
                }
                os.cpu.fileInformation.error = "文件写入成功。";
            }
        } else
        {
            os.cpu.fileInformation.error = "文件不存在，文件写入失败。";
        }
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
            if (offset >= 64)
            {
                ofi.setNowBlockNumber(fat.get((byte) blockNumber));
                ofi.setNowBlockOffset((byte) 0);
                blockNumber = ofi.getNowBlockNumber();
                offset = ofi.getNowBlockOffset();
            }
            b = hdb.read(blockNumber, offset);
            if (b == (byte) '#')
            {
                b = -1;
            } else
            {
                ofi.setNowBlockOffset((byte) (offset + 1));
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
        OpenFileItem ofi = table.get(filePoint);
        if (ofi != null && ofi.canWrite())
        {
            blockNumber = ofi.getNowBlockNumber();
            offset = ofi.getNowBlockOffset();
            length = ofi.getLength();
            if (offset >= 64)
            {
                byte t;
                if ((t = fat.get((byte) blockNumber)) == -1)
                {
                    if ((t = fat.allocate()) != -1)
                    {
                        fat.set((byte) blockNumber, t);
                        fat.set(t, (byte) -1);
                    }
                }
                if (t != -1)
                {
                    hdb.write(t, 0, b);
                    ofi.setNowBlockNumber(t);
                    ofi.setNowBlockOffset((byte) 1);
                    ofi.setLength(length + 1);
                    flag = true;
                }
            } else
            {
                hdb.write(blockNumber, offset, b);
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
        if (fileTree.find(fileName, fileName.length, file))
        {
            filePoint = table.find(file.getStartBlockNumber());
            ofi = table.get(filePoint);
            if (filePoint != -1 && ofi != null && ofi.canRead())
            {
                len = 0;
                for (int i = 0; i < length && i < bt.length && i < ofi.getLength(); i++)
                {
                    bt[i] = read1(filePoint);
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
        if (fileTree.find(fileName, fileName.length, file))
        {
            filePoint = table.find(file.getStartBlockNumber());
            ofi = table.get(filePoint);
            if (filePoint != -1 && ofi != null && ofi.canWrite())
            {
                for (int i = 0; i < length && i < bt.length; i++)
                {
                    if (!(flag = write1(filePoint, bt[i])))
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
        if (this.fileTree.find(fileName, fileName.length, file))
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
        if (this.fileTree.find(fileName, fileName.length, file))
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
        if (this.fileTree.find(fileName, fileName.length, file))
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
        if (this.fat.get((byte) 0) == 1 && this.fat.get((byte) 1) == -1)
        {
            sendTree();
        }
        //this.os.displayManager.showFile();
        pop();
    }

    public void check(int blockNumber)
    {
        push();
        for (int i = 0; i < 64; i++)
        {
            System.out.printf("%4d", (0xff & hdb.read(blockNumber, i)));
            if (i % 8 == 7)
            {
                System.out.println("");
            }
        }
        pop();
    }

    private DefaultMutableTreeNode addNode(DefaultMutableTreeNode root, int number)
    {
        byte b[][] = new byte[10][10];
        File file1 = new File(this.hdb);
        DefaultMutableTreeNode root1;
        for (int i = 0; i < 8; i++)
        {
            file1.setBlockNumber(number);
            file1.setOffset(i * 8);
            for (int j = 0; j < 8; j++)
            {
                if (number != 2 || i != 0)
                {
                    b[i][j] = this.hdb.read(number, i * 8 + j);
                } else
                {
                    b[0][0] = '$';
                }
            }
            if ((char) b[i][0] != '$')
            {
                if (file1.isCommonFile())
                {
                    root1 = new DefaultMutableTreeNode("" + (char) b[i][0] + (char) b[i][1] + (char) b[i][2] + "." + (char) b[i][3]);
                    root.add(root1);
                } else if (file1.isDirectory())
                {
                    root1 = new DefaultMutableTreeNode("" + (char) b[i][0] + (char) b[i][1] + (char) b[i][2] + (char) b[i][3]);
                    root1 = addNode(root1, file1.getStartBlockNumber());
                    root.add(root1);
                }
            }
        }
        return root;
    }

    public void sendTree()
    {
        push();
        File file1 = new File(this.hdb);
        file1.setBlockNumber(2);
        file1.setOffset(0);
        if (file1.getStartBlockNumber() == 2 && file1.isDirectory())
        {
            DefaultMutableTreeNode root = new DefaultMutableTreeNode("\\");
            root = this.addNode(root, 2);
            os.cpu.fileInformation.rootNode = root;
        }
        //os.displayManager.showFile();
        pop();
    }
}
