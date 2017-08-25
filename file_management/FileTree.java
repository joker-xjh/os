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
     * 绝对路径查找
     * @param file
     * @param path
     * @param len
     * @return 
     */
    public boolean find(File file, byte[][] path, int len)
    {
        int blockNumber;
        blockNumber = 2;
        System.out.println("FileTree::find::len="+len);
        if (len == 0)
        {
            file.setBlockNumber(2);
            file.setOffset(0);
        }
        for (int i = 0; i < len; i++)
        {
            if ((i < len - 1 && !file.isDirectory()) || !find(file, (byte) blockNumber, path[i]))
            {
                file.setBlockNumber(-1);
                file.setOffset(-1);
                return false;
            }
            blockNumber = file.getStartBlockNumber();
        }
        return true;
    }

    /**
     * 相对路径查找
     * @param file
     * @param curDirectory
     * @param relativePath
     * @param offset
     * @param pathLength
     * @return 
     */
    public boolean find(File file, File curDirectory, byte[][] relativePath, int offset, int pathLength)
    {
        file.setBlockNumber(curDirectory.getBlockNumber());
        file.setOffset(curDirectory.getOffset());
        for (int i = offset; i < pathLength && i < relativePath.length; i++)
        {
            System.out.println("::" + i);
            if (!file.isDirectory() && !find(file, file.getStartBlockNumber(), relativePath[i]))
            {
                file.setBlockNumber(-1);
                file.setOffset(-1);
                return false;
            }
        }
        return true;
    }

    /**
     * 当前目录查找
     * @param file
     * @param blockNumber
     * @param fileName
     * @return 
     */
    private boolean find(File file, byte blockNumber, byte[] fileName)
    {
        int offset;
        while (1<blockNumber && blockNumber <128)
        {
            for (offset = 0; offset < 64; offset += 8)
            {
                System.out.println("___________________________________________");
                System.out.print((char)hdb.read(blockNumber, offset + 0));
                System.out.print((char)hdb.read(blockNumber, offset + 1));
                System.out.print((char)hdb.read(blockNumber, offset + 2));
                System.out.println((char)hdb.read(blockNumber, offset + 3));
                System.out.println("___________________________________________");
                if (hdb.read(blockNumber, offset + 0) == fileName[0])
                {
                    if (hdb.read(blockNumber, offset + 1) == fileName[1])
                    {
                        if (hdb.read(blockNumber, offset + 2) == fileName[2])
                        {
                            if (hdb.read(blockNumber, offset + 3) == fileName[3])
                            {
                                file.setBlockNumber(blockNumber);
                                file.setOffset(offset);
                                return true;
                            }
                        }
                    }
                }
            }
            blockNumber = fat.get(blockNumber);
        }
        file.setBlockNumber(-1);
        file.setOffset(-1);
        return false;
    }

    /**
     * 绝对路径创建
     * @param file
     * @param path
     * @param attribute
     * @return 
     */
    public boolean create(File file, byte[][] path, byte attribute)
    {
        byte blockNumber;
        System.out.println("开始创建！");
        if ((attribute & 1) == 0)
        {
            if (find(file, path, path.length - 1) && file.isDirectory())
            {
                blockNumber = file.getStartBlockNumber();
                if (!find(file, blockNumber, path[path.length - 1]))
                {
                    System.out.println("创建成功！");
                    return create(file, blockNumber, path[path.length - 1], attribute);
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
     * 当前目录创建
     * @param file
     * @param blockNumber
     * @param fileName
     * @param attribute
     * @return 
     */
    private boolean create(File file, byte blockNumber, byte[] fileName, byte attribute)
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
                        if ((attribute & 8) == 0)
                        {
                            hdb.write(blockNumber, offset + 3, fileName[3]);
                        } else
                        {
                            hdb.write(blockNumber, offset + 3, (byte) ' ');
                        }
                        hdb.write(blockNumber, offset + 4, attribute);
                        hdb.write(blockNumber, offset + 5, newblockNumber);
                        hdb.write(blockNumber, offset + 6, (byte) 64);
                        hdb.write(blockNumber, offset + 7, (byte) 0);
                        file.setBlockNumber(blockNumber);
                        file.setOffset(offset);
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
                        blockNumber = t;
                        for (int i = 0; i < 64; i += 8)
                        {
                            hdb.write(blockNumber, i, (byte) ('$'));
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
        file.setOffset(-1);
        return false;
    }

    /**
     * 绝对路径删除文件
     * @param file
     * @param path
     * @return 
     */
    public boolean delete(File file, byte[][] path)
    {
        byte blockNumber;
        if (find(file, path, path.length - 1))
        {
            blockNumber = file.getStartBlockNumber();
            if (find(file, blockNumber, path[path.length - 1]))
            {
                if (!file.isDirectory() && file.canWrite())
                {
                    return delete(file, blockNumber);
                }
            }
        }
        return false;
    }

    /**
     * 从目录文件中删除文件目录
     * @param file
     * @param blockNumber
     * @return 
     */
    private boolean delete(File file, byte blockNumber)
    {
        int block, offset;
        block = file.getBlockNumber();
        offset = file.getOffset();
        delete(file.getStartBlockNumber());
        while (true)
        {
            if (fat.get((byte) block) == -1 && offset == 56)
            {
                hdb.write(block, offset, (byte) ('$'));
                break;
            } else if (hdb.read(block + (offset + 8) / 64, (offset + 8) % 64) != (byte) ('$'))
            {
                for (int i = 0; i < 8; i++)
                {
                    hdb.write(block, offset + i, hdb.read(block + (offset + 8) / 64, (offset + 8) % 64));
                }
                block = block + (offset + 8) / 64;
                offset = (offset + 8) % 64;
            } else
            {
                if (offset > 0 || block == blockNumber)
                {
                    hdb.write(block, offset, (byte) ('$'));
                } else
                {
                    for (int i = blockNumber; i != -1; i = fat.get((byte) i))
                    {
                        if (fat.get((byte) i) == block)
                        {
                            fat.set((byte) i, (byte) 0);
                        }
                    }
                }
                break;
            }
        }
        return true;
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
        hdb.write(2, 8+0, (byte) '.');
        hdb.write(2, 8+1, (byte) '.');
        hdb.write(2, 8+2, (byte) ' ');
        hdb.write(2, 8+3, (byte) ' ');
        hdb.write(2, 8+4, (byte) 8);
        hdb.write(2, 8+5, (byte) 2);
        hdb.write(2, 8+6, (byte) 0);
        hdb.write(2, 8+7, (byte) 0);
    }
}
