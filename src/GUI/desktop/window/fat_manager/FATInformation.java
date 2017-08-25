package GUI.desktop.window.fat_manager;

/**
 * 进程信息
 * @author Administrator
 */
public class FATInformation
{

    private byte[] fat;

    public FATInformation()
    {
        this.fat = new byte[128];
    }

    public void init()
    {
    }

    public void set(int index, byte b)
    {
        fat[index] = b;
    }

    public byte get(int index)
    {
        return fat[index];
    }

    public void send()
    {
        util.out.printf("文件分配表\n");
        util.out.printf("|");
        for (int k = 0; k < 8; k++)
        {
            util.out.printf("----");
        }
        util.out.printf("|\n");
        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 8; j++)
            {
                util.out.printf("|");
                for (int k = 0; k < 8; k++)
                {
                    util.out.printf("%3d ", (int) get((byte) ((i * 8 + j) * 8 + k)));
                }
                util.out.printf("|\n");
            }
            util.out.printf("|");
            for (int k = 0; k < 8; k++)
            {
                util.out.printf("----");
            }
            util.out.printf("|\n");
        }
    }
}