package GUI.desktop.window.process_manager;

/**
 * 进程信息
 * @author Administrator
 */
public class ProcessInformation
{

    private PCBItem[] table;
    private int count;

    public ProcessInformation()
    {
        super();
        this.table = new PCBItem[20];
        for (int i = 0; i < table.length; i++)
        {
            table[i] = new PCBItem();
        }
    }

    public void init()
    {
        count = 0;
    }

    public void add(int index, int name, int status, int mcbIndex, int psw, int pc)
    {
        this.add(index, name, status, mcbIndex, psw, pc, 0, 0, 0, 0);
    }

    public void add(int index, int name, int status, int mcbIndex, int psw, int pc, int ax, int bx, int cx, int dx)
    {
        this.table[count++].set(index, name, status, mcbIndex, psw, pc, ax, bx, cx, dx);
    }

    public void send()
    {
        util.out.printf("==============================\n");
        util.out.printf("|进程情况                    |\n");
        util.out.printf("|----------------------------|\n");
        util.out.printf("|标号 名称 状态 内存号 PSW PC|\n");
        util.out.printf("|----------------------------|\n");
        for (int i = 0; i < count; i++)
        {
            util.out.printf("|%4d %4d %4d %6d %3d %2d|\n", table[i].getIndex(), table[i].getName(),table[i].getStatus(), table[i].getMCBIndex(), table[i].getPSW(), table[i].getPC());
        }
        util.out.printf("==============================\n");

    }

    public int getCount()
    {
        return this.count;
    }
    
     public int getIndex(int index)
    {
        return this.table[index].getIndex();
    }

    /**
     * @return the name
     */
    public int getName(int index)
    {
        return this.table[index].getName();
    }

    /**
     * @return the status
     */
    public int getStatus(int index)
    {
        return this.table[index].getStatus();
    }

    /**
     * @return the MCBIndex
     */
    public int getMCBIndex(int index)
    {
        return this.table[index].getMCBIndex();
    }

    /**
     * @return the PSW
     */
    public int getPSW(int index)
    {
        return this.table[index].getPSW();
    }

    /**
     * @return the PC
     */
    public int getPC(int index)
    {
        return this.table[index].getPC();
    }

    /**
     * @return the AX
     */
    public int getAX(int index)
    {
        return this.table[index].getAX();
    }

    /**
     * @return the BX
     */
    public int getBX(int index)
    {
        return this.table[index].getBX();
    }

    /**
     * @return the CX
     */
    public int getCX(int index)
    {
        return this.table[index].getCX();
    }

    /**
     * @return the DX
     */
    public int getDX(int index)
    {
        return this.table[index].getDX();
    }
}

class PCBItem
{

    private int index;
    private int name;
    private int status;
    private int MCBIndex;
    private int PSW;
    private int PC;
    private int AX;
    private int BX;
    private int CX;
    private int DX;

    public PCBItem()
    {
        super();
    }

    public void set(int index, int name, int status, int mcbIndex, int psw, int pc, int ax, int bx, int cx, int dx)
    {
        this.setIndex(index);
        this.setName(name);
        this.setStatus(status);
        this.setMCBIndex(mcbIndex);
        this.setPSW(psw);
        this.setPC(pc);
        this.setAX(ax);
        this.setBX(bx);
        this.setCX(cx);
        this.setDX(dx);
    }

    /**
     * @return the index
     */
    public int getIndex()
    {
        return index;
    }

    /**
     * @return the name
     */
    public int getName()
    {
        return name;
    }

    /**
     * @return the status
     */
    public int getStatus()
    {
        return status;
    }

    /**
     * @return the MCBIndex
     */
    public int getMCBIndex()
    {
        return MCBIndex;
    }

    /**
     * @return the PSW
     */
    public int getPSW()
    {
        return PSW;
    }

    /**
     * @return the PC
     */
    public int getPC()
    {
        return PC;
    }

    /**
     * @return the AX
     */
    public int getAX()
    {
        return AX;
    }

    /**
     * @return the BX
     */
    public int getBX()
    {
        return BX;
    }

    /**
     * @return the CX
     */
    public int getCX()
    {
        return CX;
    }

    /**
     * @return the DX
     */
    public int getDX()
    {
        return DX;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index)
    {
        this.index = index;
    }

    /**
     * @param name the name to set
     */
    public void setName(int name)
    {
        this.name = name;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status)
    {
        this.status = status;
    }

    /**
     * @param MCBIndex the MCBIndex to set
     */
    public void setMCBIndex(int MCBIndex)
    {
        this.MCBIndex = MCBIndex;
    }

    /**
     * @param PSW the PSW to set
     */
    public void setPSW(int PSW)
    {
        this.PSW = PSW;
    }

    /**
     * @param PC the PC to set
     */
    public void setPC(int PC)
    {
        this.PC = PC;
    }

    /**
     * @param AX the AX to set
     */
    public void setAX(int AX)
    {
        this.AX = AX;
    }

    /**
     * @param BX the BX to set
     */
    public void setBX(int BX)
    {
        this.BX = BX;
    }

    /**
     * @param CX the CX to set
     */
    public void setCX(int CX)
    {
        this.CX = CX;
    }

    /**
     * @param DX the DX to set
     */
    public void setDX(int DX)
    {
        this.DX = DX;
    }
}