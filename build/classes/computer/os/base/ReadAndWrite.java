/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.os.base;

/**
 *
 * @author Administrator
 */
public class ReadAndWrite
{
    public OS os;

    private ReadAndWrite()
    {
    }
    
    public ReadAndWrite(OS os)
    {
        this();
        this.os = os;
    }

    public byte read(int AR)
    {
        return this.os.read(AR);
    }

    public void write(int AR, byte b)
    {
        this.os.write(AR,b);
    }

    public int read2Byte(int AR)
    {
        int b;
        b = 0x000000ff & this.read(AR + 1);
        b = (b << 8);
//        util.out.println(AR+"b="+b);
        b |= (0x000000ff & this.read(AR));
//        util.out.println(AR+"b="+b);
        return b;
    }

    public void write2Byte(int AR, int b)
    {
        this.write(AR, (byte) (b & 0x000000ff));
        b = b >> 8;
        this.write(AR + 1, (byte) (b & 0x000000ff));
    }
}
