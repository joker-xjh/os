package computer.os.kernel.process_management;

import GUI.desktop.window.process_manager.ProcessInformation;
import computer.hardware.cpu.CPU;
import computer.os.base.InnerMemoryManager;
import computer.os.base.OS;
import util.Queue.Queue;
import util.Queue.Queue.Iterator;

/**
 * 进程控制块表
 * @author Administrator
 */
public class PCBTable
{

    public final static int PCB_TABEL_SIZE = 10;
    public final static int SIZE = PCB_TABEL_SIZE * (PCB.SIZE + PCBQueueEntry.SIZE) + 2 * PCBQueueEntry.SIZE;
    private OS os;
    private PCB[] table;
    private PCBQueueEntry[] entry;
    private int run;
    private Queue readyQueue;
    private Queue waitQueue;
    private Queue freeQueue;

    public PCBTable(OS os, InnerMemoryManager innerMemoryManager)
    {
        this.os = os;
        this.table = new PCB[PCB_TABEL_SIZE];
        this.entry = new PCBQueueEntry[PCB_TABEL_SIZE + 2];
        for (int i = 0; i < PCB_TABEL_SIZE; i++)
        {
            this.table[i] = new PCB(innerMemoryManager, i);
            this.entry[i] = new PCBQueueEntry(innerMemoryManager);
        }
        this.entry[entry.length - 2] = new PCBQueueEntry(innerMemoryManager);
        this.entry[entry.length - 1] = new PCBQueueEntry(innerMemoryManager);
        this.readyQueue = new Queue(0, entry);
        this.waitQueue = new Queue(entry.length - 2, entry);
        this.freeQueue = new Queue(entry.length - 1, entry);
    }

    public void init(int mcbIndexOfFreeProcess)
    {
        this.run = 0;
        this.readyQueue.clear();
        this.waitQueue.clear();
        this.freeQueue.clear();
        for (int i = 1; i < this.table.length; i++)
        {
            this.table[i].setPid(i);
            this.table[i].setStatus(PCB.STATUS_END);
            this.entry[i].init(i);
            this.freeQueue.addLast(i);
        }
        this.table[0].setPid(0);
        this.table[0].setStatus(PCB.STATUS_READY);
        this.table[0].setPSW(CPU.PSW_IF_BIT);
        this.table[0].setPC(0);
        this.table[0].setAX(0);
        this.table[0].setBX(0);
        this.table[0].setCX(0);
        this.table[0].setDX(0);
        this.table[0].setName((((int) 'f')) | (((int) 'r') << 8) | (((int) 'e') << 16) | (((int) 'e') << 24));
        this.table[0].setMCBIndex(mcbIndexOfFreeProcess);
    }

    public PCB get(int pid)
    {
        if (0 <= pid && pid < table.length)
        {
            return this.table[pid];
        }
        return null;
    }

    public void setRun(int pid)
    {
        if (0 <= pid && pid < table.length)
        {
            this.run = pid;
            table[pid].setStatus(PCB.STATUS_RUN);
        } else
        {
            this.run = 0;
            table[0].setStatus(PCB.STATUS_RUN);
        }
    }

    public int getRun()
    {
        return this.run;
    }

    public Queue getReadyQueue()
    {
        return this.readyQueue;
    }

    public Queue getWaitQueue()
    {
        return this.waitQueue;
    }

    public Queue getFreeQueue()
    {
        return this.freeQueue;
    }

    public PCB quickRemovePCB(int pid)
    {
        int prev, next;
        PCB pcb;
        if ((pcb = get(pid)) != null)
        {
            prev = entry[pid].getPrevious();
            next = entry[pid].getNext();
            entry[prev].setNext(next);
            entry[next].setPrevious(prev);
            entry[pid].setPrevious(pid);
            entry[pid].setNext(pid);
            table[pid].setStatus(PCB.STATUS_END);
        }
        return pcb;
    }

    public PCB removeWaitPCB(int pid)
    {
        PCB pcb;
        if ((pcb = get(pid)) != null)
        {
            if (PCB.STATUS_WAIT_DEVICE[0] <= pcb.getStatus() && pcb.getStatus() <= PCB.STATUS_WAIT_DEVICE[PCB.STATUS_WAIT_DEVICE.length - 1])
            {
                pcb = this.quickRemovePCB(pid);
            } else
            {
                pcb = null;
            }
        }
        return pcb;
    }

    void sendInformation()
    {
        int i;
        PCB pcb;
        Iterator it;
        ProcessInformation processInformation = os.cpu.processInformation;
        if (processInformation != null)
        {
            processInformation.init();
            pcb = get(getRun());
            if (pcb != null)
            {
                processInformation.add(pcb.getPid(), pcb.getName(), pcb.getStatus(), pcb.getMCBIndex(), pcb.getPSW(), pcb.getPC());
            }
            (it = this.readyQueue.iterator()).init();
            while (it.hasNext())
            {
                pcb = get(it.next());
                if (pcb != null)
                {
                    processInformation.add(pcb.getPid(), pcb.getName(), pcb.getStatus(), pcb.getMCBIndex(), pcb.getPSW(), pcb.getPC());

                }
            }
            pcb = get(it.next());
            processInformation.add(pcb.getPid(), pcb.getName(), pcb.getStatus(), pcb.getMCBIndex(), pcb.getPSW(), pcb.getPC());
            (it = this.waitQueue.iterator()).init();
            while (it.hasNext())
            {
                pcb = get(it.next());
                processInformation.add(pcb.getPid(), pcb.getName(), pcb.getStatus(), pcb.getMCBIndex(), pcb.getPSW(), pcb.getPC());
            }
            os.displayManager.showProcess();
        }
    }
}
