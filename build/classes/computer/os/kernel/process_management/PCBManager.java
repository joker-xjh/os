package computer.os.kernel.process_management;

import computer.os.base.InnerMemoryManager;
import computer.os.base.OS;

/**
 * 进程控制快管理器
 * @author Administrator
 */
public class PCBManager
{

    public final static int SIZE = PCBTable.SIZE;
    private PCBTable pcbTable;

    public PCBManager(OS os, InnerMemoryManager innerMemoryManager)
    {
        this.pcbTable = new PCBTable(os, innerMemoryManager);
    }

    public void init(int mcbIndexOfFreeProcess)
    {
        this.pcbTable.init(mcbIndexOfFreeProcess);
    }

    public PCB getRunPCB()
    {
        return pcbTable.get(pcbTable.getRun());
    }

    public PCB getFirstReadyPCB()
    {
        PCB readyPCB = null;
        readyPCB = pcbTable.get(pcbTable.getReadyQueue().getFirst());
        if (!pcbTable.getReadyQueue().isEmpty())
        {
            pcbTable.getReadyQueue().removeFirst();
        }
        return readyPCB;
    }

    public PCB getFreePCB()
    {
        PCB freePCB = null;
        if (!pcbTable.getFreeQueue().isEmpty())
        {
            freePCB = pcbTable.get(pcbTable.getFreeQueue().getFirst());
            pcbTable.getFreeQueue().removeFirst();
        }
        return freePCB;
    }

    public PCB getWaitPCB(int pid)
    {
        return this.pcbTable.removeWaitPCB(pid);
    }

    public void setRunPCB(int pid)
    {
        PCB pcb;
        if ((pcb = pcbTable.get(pid)) != null)
        {
            pcb.setStatus(PCB.STATUS_RUN);
            this.pcbTable.setRun(pid);
        }
    }

    public void addLastReadyPCB(int pid)
    {
        PCB pcb;
        if ((pcb = pcbTable.get(pid)) != null)
        {
            pcb.setStatus(PCB.STATUS_READY);
            this.pcbTable.getReadyQueue().addLast(pid);
        }
    }

    public void addFreePCB(int pid)
    {
        PCB pcb;
        if ((pcb = pcbTable.get(pid)) != null)
        {
            pcb.setStatus(PCB.STATUS_END);
            this.pcbTable.getFreeQueue().addLast(pid);
        }
    }

    public void addWaitPCB(int pid, int waitReason)
    {
        PCB pcb;
        if ((pcb = pcbTable.get(pid)) != null)
        {
            pcb.setStatus(waitReason);
            this.pcbTable.getWaitQueue().addLast(pid);
        }
    }

    public void sendInformation()
    {
        this.pcbTable.sendInformation();
    }
}
