package computer.os.kernel.process_management;

import computer.os.base.InnerMemoryManager;
import computer.os.base.InnerMemoryManager.InnerMemory;

/**
 * 进程控制块
 * @author Administrator
 */
public class PCB
{

    public final static int SIZE = 20;
    public final static int STATUS_END = 0;
    public final static int STATUS_RUN = 1;
    public final static int STATUS_READY = 2;
    public final static int STATUS_WAIT = 3;
    public final static int STATUS_WAIT_DEVICE_A = 4;
    public final static int STATUS_WAIT_DEVICE_B = 5;
    public final static int STATUS_WAIT_DEVICE_C = 6;
    public final static int STATUS_WAIT_IO = 7;
    public final static int[] STATUS_WAIT_DEVICE = new int[]
    {
        STATUS_WAIT_DEVICE_A, STATUS_WAIT_DEVICE_B, STATUS_WAIT_DEVICE_C, STATUS_WAIT_IO
    };
    private InnerMemory memory;

    public PCB(InnerMemoryManager memoryManager, int pid)
    {
        this.memory = memoryManager.allocate(SIZE);
    }

    public void init()
    {
        init(STATUS_END, 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public void init(int status, int psw, int pc, int ax, int bx, int cx, int dx, int name, int mcbIndex, int dcbIndex)
    {
        setStatus(status);
        setPSW(psw);
        setPC(pc);
        setAX(ax);
        setBX(bx);
        setCX(cx);
        setDX(dx);
        setName(name);
        setMCBIndex(mcbIndex);
        setDCBIndex(dcbIndex);
    }

    public void set(int status, int psw, int pc, int ax, int bx, int cx, int dx, int name, int mcbIndex, int dcbIndex)
    {
        setStatus(status);
        setPSW(psw);
        setPC(pc);
        setAX(ax);
        setBX(bx);
        setCX(cx);
        setDX(dx);
        setName(name);
        setMCBIndex(mcbIndex);
        setDCBIndex(dcbIndex);
    }

    public void setPid(int pid)
    {
        this.memory.write(0, (byte) (0x000000ff & pid));
    }

    public void setStatus(int status)
    {
        this.memory.write(1, (byte) (0x000000ff & status));
    }

    public void setPSW(int psw)
    {
        this.memory.write2Byte(2, psw);
    }

    public void setPC(int pc)
    {
        this.memory.write2Byte(4, pc);
    }

    public void setAX(int ax)
    {
        this.memory.write2Byte(6, ax);
    }

    public void setBX(int bx)
    {
        this.memory.write2Byte(8, bx);
    }

    public void setCX(int cx)
    {
        this.memory.write2Byte(10, cx);
    }

    public void setDX(int dx)
    {
        this.memory.write2Byte(12, dx);
    }

    public void setName(int name)
    {
        this.memory.write2Byte(14, 0xffff & name);
        this.memory.write2Byte(16, (0xffff0000 & name) >> 16);
    }

    public void setMCBIndex(int mcbIndex)
    {
        this.memory.write(18, (byte) (0x000000ff & mcbIndex));
    }

    public void setDCBIndex(int dcbIndex)
    {
        this.memory.write(19, (byte) (0x000000ff & dcbIndex));
    }

    public int getPid()
    {
        return this.memory.read(0);
    }

    public int getStatus()
    {
        return 0x000000ff & this.memory.read(1);
    }

    public int getPSW()
    {
        return this.memory.read2Byte(2);
    }

    public int getPC()
    {
        return this.memory.read2Byte(4);
    }

    public int getAX()
    {
        return this.memory.read2Byte(6);
    }

    public int getBX()
    {
        return this.memory.read2Byte(8);
    }

    public int getCX()
    {
        return this.memory.read2Byte(10);
    }

    public int getDX()
    {
        return this.memory.read2Byte(12);
    }

    public int getName()
    {
        return this.memory.read2Byte(14) | (this.memory.read2Byte(16) << 16);
    }

    public String Name()
    {
        return "" + (char) (this.memory.read(14)) + "" + (char) (this.memory.read(15))
                + "" + (char) (this.memory.read(16)) + "." + (char) (this.memory.read(17));
    }

    public int getMCBIndex()
    {
        return 0x000000ff & this.memory.read(18);
    }

    public int getDCBIndex()
    {
        return 0x000000ff & this.memory.read(19);
    }
}
