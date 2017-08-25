package computer.os.kernel.file_management;

/**
 *
 * @author Administrator
 */
public class FileTree
{

    private FAT fat;
    private HardDiskBuffer hdb;

    public FileTree(HardDiskBuffer hdb, FAT fat)
    {
        this.hdb = hdb;
        this.fat = fat;
    }

    /**
     * 在块号为startBlockNumber的块中找fileName
     * @param startBlockNumber
     * @param fileName
     * @param file 用于保存返回的文件
     * @return 成功返回true,否则返回false
     */
    private boolean find(byte startBlockNumber, byte[] fileName, File file)
    {
        boolean flag = false;
        if (startBlockNumber == -1 || startBlockNumber < 0 || startBlockNumber > 127)
        {
            file.setBlockNumber(-1);
            file.setOffset(0);
        } else
        {
            for (int i = 0; i < 64; i += 8)
            {
                flag = true;
                for (int j = 0; j < 4; j++)
                {
                    if (hdb.read(startBlockNumber, i + j) != fileName[j])
                    {
                        flag = false;
                        break;
                    }
                }
                if (flag)
                {
                    file.setBlockNumber(startBlockNumber);
                    file.setOffset(i);
                    break;
                }
            }
            if (!flag)
            {
                flag = find(fat.get(startBlockNumber), fileName, file);
            }
        }
        return flag;
    }

    /**
     * 绝对路径查找
     * @param path
     * @param len
     * @param file  用于保存返回的文件
     * @return 成功返回true,否则返回false
     */
    public boolean find(byte[][] path, int len, File file)
    {
        int blockNumber;
        file.setBlockNumber(2);
        file.setOffset(0);
        for (int i = 0; i < len; i++)
        {
            blockNumber = file.getStartBlockNumber();
            if (i >= path.length || path[i] == null || !file.isDirectory() || !find((byte) blockNumber, path[i], file))
            {
                file.setBlockNumber(-1);
                file.setOffset(0);
                return false;
            }
        }
        return true;
    }

    /**
     * 当前目录创建
     * @param blockNumber
     * @param fileName
     * @param attribute
     * @param file 用于返回创建的文件
     * @return 
     */
    private boolean create(byte blockNumber, byte[] fileName, byte attribute, File file)
    {
        int offset;
        byte newblockNumber = fat.allocate();
        if (3 <= newblockNumber && newblockNumber < 128)
        {
            while (blockNumber != -1)
            {
                for (offset = 0; offset < 64; offset += 8)
                {
                    if (hdb.read(blockNumber, offset) == (byte) ('$'))
                    {
                        hdb.write(blockNumber, offset + 0, fileName[0]);
                        hdb.write(blockNumber, offset + 1, fileName[1]);
                        hdb.write(blockNumber, offset + 2, fileName[2]);
                        hdb.write(blockNumber, offset + 3, fileName[3]);
                        hdb.write(blockNumber, offset + 4, attribute);
                        hdb.write(blockNumber, offset + 5, newblockNumber);
                        hdb.write(blockNumber, offset + 6, (byte) 0);
                        hdb.write(blockNumber, offset + 7, (byte) 0);
                        file.setBlockNumber(blockNumber);
                        file.setOffset(offset);
                        if (file.isDirectory())
                        {
                            for (int i = 0; i < 64; i += 8)
                            {
                                hdb.write(newblockNumber, i, (byte) ('$'));
                            }
                        }
                        return true;
                    }
                }
                if (fat.get(blockNumber) == -1)
                {
                    byte t = fat.allocate();
                    if (3 <= t && t < 128)
                    {
                        fat.set(blockNumber, t);
                        fat.set(t, (byte) (-1));
                        for (int i = 0; i < 64; i += 8)
                        {
                            hdb.write(t, i, (byte) ('$'));
                        }
                    } else
                    {
                        fat.collect(newblockNumber);
                        break;
                    }
                }
                blockNumber = fat.get(blockNumber);
            }
        }
        file.setBlockNumber(-1);
        file.setOffset(0);
        return false;
    }

    /**
     * 绝对路径创建
     * @param file
     * @param path
     * @param attribute
     * @return 
     */
    public boolean create(byte[][] path, byte attribute, File file)
    {
        byte blockNumber;
        System.out.println("开始创建！");
        if ((attribute & 1) == 0)
        {
            if (find(path, path.length - 1, file) && file.isDirectory())
            {
                blockNumber = file.getStartBlockNumber();
                if (!find(blockNumber, path[path.length - 1], file))
                {
                    System.out.println("创建成功！");
                    return create(blockNumber, path[path.length - 1], attribute, file);
                } else
                {
                    System.out.println("文件已存在！");
                }
            } else
            {
                System.out.println("找不到父目录！");
            }
        }
        System.out.println("创建失败！");
        file.setBlockNumber(-1);
        file.setOffset(-1);
        return false;
    }

    /**
     * 绝对路径删除文件(不能删除目录)
     * @param file
     * @param path
     * @return 
     */
    public boolean delete(byte[][] path, File file)
    {
        byte blockNumber;
        if (find(path, path.length - 1, file))
        {
            blockNumber = file.getStartBlockNumber();
            if (find(blockNumber, path[path.length - 1], file))
            {
                if (/*!file.isDirectory() &&*/ file.canWrite())
                {
                    delete(file.getStartBlockNumber());
                    hdb.write(blockNumber,file.getOffset(),(byte)'$');
                    return true;
                }
            }else
            {
                System.out.println("delete:找不到我。");
            }
        }else
        {
            System.out.println("delete:找不到父亲。");
        }
        return false;
    }
    
    /**
     * 删除文件主体
     * @param blockNumber 
     */
    private void delete(byte blockNumber)
    {
        if (blockNumber == -1)
        {
            return;
        }
        delete(fat.get(blockNumber));
        fat.set(blockNumber, (byte) 0);
    }

    public void format()
    {
        int i;
        for (i = 0; i < 128; i++)
        {
            fat.set((byte) i, (byte) 0);
        }
        fat.set((byte) 0, (byte) (1));
        fat.set((byte) 1, (byte) (-1));
        fat.set((byte) 2, (byte) (-1));
        for (i = 0; i < 64; i += 8)
        {
            hdb.write(2, i, (byte) ('$'));
        }
        hdb.write(2, 0, (byte) '.');
        hdb.write(2, 1, (byte) ' ');
        hdb.write(2, 2, (byte) ' ');
        hdb.write(2, 3, (byte) ' ');
        hdb.write(2, 4, (byte) 8);
        hdb.write(2, 5, (byte) 2);
        hdb.write(2, 6, (byte) 0);
        hdb.write(2, 7, (byte) 0);
//        hdb.write(2, 8 + 0, (byte) '.');
//        hdb.write(2, 8 + 1, (byte) '.');
//        hdb.write(2, 8 + 2, (byte) ' ');
//        hdb.write(2, 8 + 3, (byte) ' ');
//        hdb.write(2, 8 + 4, (byte) 8);
//        hdb.write(2, 8 + 5, (byte) 2);
//        hdb.write(2, 8 + 6, (byte) 0);
//        hdb.write(2, 8 + 7, (byte) 0);
    }
}
