package computer.os.base;

/**
 *
 * @author Administrator
 */
public class SystemProcess extends computer.os.base.Process
{

    public SystemProcess(OS os)
    {
        super(os);
    }

    @Override
    public void run()
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public byte read(int AR)
    {
        return os.read(AR);
    }

    public void write(int AR, byte b)
    {
        os.write(AR, b);
    }

    public int read2Byte(int AR)
    {
        return os.read2Byte(AR);
    }

    public void write2Byte(int AR, int b)
    {
        os.write2Byte(AR, b);
    }
}
