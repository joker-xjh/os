package computer.os.kernel.process_management;

import computer.hardware.cpu.CPU;
import computer.os.base.InnerMemoryManager;
import computer.os.base.OS;
import computer.os.kernel.file_management.OpenFileItem;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

/**
 *
 * @author Administrator
 */
public class ProcessManager extends computer.os.base.SystemProcess
{

    public final static int SIZE = PCBManager.SIZE;
    private int index;
    private PCBManager pcbManager;
    private InnerMemoryManager innerMemoryManager;
    private byte[] buffer;
    private byte[][] programs;
    private int[] programNames;
    private File out;
    private PrintWriter output;

    public ProcessManager(OS os)
    {
        super(os);
        this.innerMemoryManager = new InnerMemoryManager(os, SIZE);
        this.pcbManager = new PCBManager(os, innerMemoryManager);
        this.buffer = new byte[1 << 11];
        this.programs = new byte[6][];
        this.programNames = new int[this.programs.length];
        initProgram0();
        initProgram1();
        initProgram2();
        initProgram3();
        initProgram4();
        initProgram5();
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
        PCB pcb;
        int rr, lr, mcbIndx;
        push();
        mcbIndx = os.memoryManager.allocate(3);
        this.pcbManager.init(mcbIndx);
        rr = os.memoryManager.getStartAddress(mcbIndx);
        lr = os.memoryManager.getLength(mcbIndx);
        os.cpu.RR = rr;
        os.cpu.LR = lr;
        this.write(0, (byte) 'j');
        this.write(1, (byte) 0);
        this.write(2, (byte) 0);
        out = new File("out");
        try
        {
            output = new PrintWriter(out);
        } catch (FileNotFoundException ex)
        {
            //Logger.getLogger(ProcessEndInterruptHandler.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("打开输出文件异常！");
        }
        pop();
    }

    public void schedule()
    {
        save();
        load();
    }

    public void create(int index)
    {
        push();
        create(programs[index], programs[index].length, index);
        pop();
    }

    public void create(byte[][] fileNameOfProgram)
    {
        int size, name;
        push();
        if (os.fileManager.canExecute(fileNameOfProgram))
        {
            os.fileManager.open(fileNameOfProgram, (byte) OpenFileItem.OPEN_TYPE_READ);
            size = os.fileManager.read(fileNameOfProgram, buffer, buffer.length);
            os.fileManager.close(fileNameOfProgram);
            if (size >= 0)
            {
                name = 0xff & fileNameOfProgram[fileNameOfProgram.length - 1][3];
                name = (name << 8) | (0xff & fileNameOfProgram[fileNameOfProgram.length - 1][2]);
                name = (name << 8) | (0xff & fileNameOfProgram[fileNameOfProgram.length - 1][1]);
                name = (name << 8) | (0xff & fileNameOfProgram[fileNameOfProgram.length - 1][0]);
                this.create(buffer, size, name);
            }
        }
        pop();
    }

    private void create(byte[] program, int size, int name)
    {
        PCB pcb;
        int mcbIndex, dcbIndex, rr, lr;
        push();
        if (size >= 0)
        {
            pcb = this.pcbManager.getFreePCB();
            if (pcb != null)
            {
                mcbIndex = os.memoryManager.allocate((size + 2) / 3 * 3 + 3);
                dcbIndex = os.deviceManager.login(pcb.getPid());
                if (mcbIndex > 0 && dcbIndex >= 0)
                {
                    push();
                    rr = os.memoryManager.getStartAddress(mcbIndex);
                    lr = os.memoryManager.getLength(mcbIndex);
                    os.cpu.RR = rr;
                    os.cpu.LR = lr;
                    for (int j = 0; j < size; j++)
                    {
                        write(j, program[j]);
                    }
                    write((size + 2) / 3 * 3 + 0, (byte) 'e');
                    write((size + 2) / 3 * 3 + 1, (byte) 'n');
                    write((size + 2) / 3 * 3 + 2, (byte) 'd');
                    pop();
                    pcb.setStatus(PCB.STATUS_READY);
                    pcb.setPSW(CPU.PSW_IF_BIT);
                    pcb.setPC(0);
                    pcb.setAX(0);
                    pcb.setBX(0);
                    pcb.setCX(0);
                    pcb.setDX(0);
                    pcb.setName(name);
                    pcb.setMCBIndex(mcbIndex);
                    pcb.setDCBIndex(dcbIndex);
                    this.pcbManager.addLastReadyPCB(pcb.getPid());
                } else
                {
                    this.pcbManager.addFreePCB(pcb.getPid());
                }
            }
        }
        pop();
    }

    public void load()
    {
        PCB run;
        int rr, lr, pc, psw, ax, bx, cx, dx;
        push();
        run = this.pcbManager.getFirstReadyPCB();
        psw = run.getPSW();
        pc = run.getPC();
        ax = run.getAX();
        bx = run.getBX();
        cx = run.getCX();
        dx = run.getDX();
        rr = os.memoryManager.getStartAddress(run.getMCBIndex());
        lr = os.memoryManager.getLength(run.getMCBIndex());
        run.setStatus(PCB.STATUS_RUN);
        pcbManager.setRunPCB(run.getPid());
        pop();
        os.cpu.PSW = psw;
        os.cpu.RR = rr;
        os.cpu.LR = lr;
        os.cpu.PC = pc;
        os.cpu.AX = ax;
        os.cpu.BX = bx;
        os.cpu.CX = cx;
        os.cpu.DX = dx;
    }

    public void awake(int pid)
    {
        PCB pcb;
        push();
        if ((pcb = this.pcbManager.getWaitPCB(pid)) != null)
        {
            pcb.setStatus(PCB.STATUS_READY);
            pcbManager.addLastReadyPCB(pcb.getPid());
        }
        pop();
    }

    public void save()
    {
        PCB run;
        int psw = os.cpu.PSW;
        int pc = os.cpu.PC;
        int ax = os.cpu.AX;
        int bx = os.cpu.BX;
        int cx = os.cpu.CX;
        int dx = os.cpu.DX;
        push();
        if ((run = this.pcbManager.getRunPCB()) != null)
        {
            run.setPSW(psw);
            run.setPC(pc);
            run.setAX(ax);
            run.setBX(bx);
            run.setCX(cx);
            run.setDX(dx);
            run.setStatus(PCB.STATUS_READY);
            pcbManager.addLastReadyPCB(run.getPid());
        }
        pop();
    }

    public void block(int reason)
    {
        PCB run;
        int psw = os.cpu.PSW;
        int pc = os.cpu.PC;
        int ax = os.cpu.AX;
        int bx = os.cpu.BX;
        int cx = os.cpu.CX;
        int dx = os.cpu.DX;
        push();
        if ((run = this.pcbManager.getRunPCB()) != null)
        {
            run.setPSW(psw);
            run.setPC(pc);
            run.setAX(ax);
            run.setBX(bx);
            run.setCX(cx);
            run.setDX(dx);
            pcbManager.addWaitPCB(run.getPid(), reason);
        }
        pop();
    }

    public void destroy()
    {
        PCB pcb;
        push();
        if ((pcb = pcbManager.getRunPCB()) != null)
        {
            if (this.output != null)
            {
                this.output.println(this.pcbManager.getRunPCB().Name() + ":x=" + os.cpu.AX);
            }
            os.memoryManager.collect(pcb.getMCBIndex());
            os.deviceManager.logout(pcb.getPid(), pcb.getDCBIndex());
            pcb.setStatus(PCB.STATUS_END);
            pcbManager.addFreePCB(pcb.getPid());
        }
        pop();
    }

    public int getRunPid()
    {
        int pid;
        push();
        pid = this.pcbManager.getRunPCB().getPid();
        pop();
        return pid;
    }

    public int getRunDCBIndex()
    {
        int dcbIndex;
        push();
        dcbIndex = this.pcbManager.getRunPCB().getDCBIndex();
        pop();
        return dcbIndex;
    }

    public void close()
    {
        this.output.flush();
        this.output.close();
    }

    public void setMCBIndex(int index)
    {
        this.index = index;
    }

    public void sendInformation()
    {
        push();
        this.pcbManager.sendInformation();
        pop();
    }

    private void initProgram0()
    {
        int i = 0;
        this.programNames[i] = ((((int) 's') & 0xFF)) | ((((int) 'y') & 0xFF) << 8) | ((((int) 's') & 0xFF) << 16) | ((((int) '0') & 0xFF) << 24);
        this.programs[i] = new byte[9];
        this.programs[i][0] = (byte) 'x';
        this.programs[i][1] = (byte) '+';
        this.programs[i][2] = (byte) '+';
        this.programs[i][3] = (byte) '!';
        this.programs[i][4] = (byte) 'B';
        this.programs[i][5] = (byte) 8;
        this.programs[i][6] = (byte) 'j';
        this.programs[i][7] = (byte) 0;
        this.programs[i][8] = (byte) 0;
    }

    private void initProgram1()
    {
        int i = 1;
        this.programNames[i] = ((((int) 's') & 0xFF)) | ((((int) 'y') & 0xFF) << 8) | ((((int) 's') & 0xFF) << 16) | ((((int) '1') & 0xFF) << 24);
        this.programs[i] = new byte[9];
        this.programs[i][0] = (byte) 'x';
        this.programs[i][1] = (byte) '=';
        this.programs[i][2] = (byte) '8';
        this.programs[i][3] = (byte) '!';
        this.programs[i][4] = (byte) 'C';
        this.programs[i][5] = (byte) 7;
        this.programs[i][6] = (byte) 'j';
        this.programs[i][7] = (byte) 0;
        this.programs[i][8] = (byte) 0;
    }

    private void initProgram2()
    {
        int i = 2;
        this.programNames[i] = ((((int) 's') & 0xFF)) | ((((int) 'y') & 0xFF) << 8) | ((((int) 's') & 0xFF) << 16) | ((((int) '2') & 0xFF) << 24);
        this.programs[i] = new byte[27];
        this.programs[i][0] = (byte) 'x';
        this.programs[i][1] = (byte) '=';
        this.programs[i][2] = (byte) '8';
        this.programs[i][3] = (byte) 'x';
        this.programs[i][4] = (byte) '+';
        this.programs[i][5] = (byte) '+';
        this.programs[i][6] = (byte) 'x';
        this.programs[i][7] = (byte) '-';
        this.programs[i][8] = (byte) '-';
        this.programs[i][9] = (byte) 'x';
        this.programs[i][10] = (byte) '+';
        this.programs[i][11] = (byte) '+';
        this.programs[i][12] = (byte) 'x';
        this.programs[i][13] = (byte) '+';
        this.programs[i][14] = (byte) '+';
        this.programs[i][15] = (byte) 'x';
        this.programs[i][16] = (byte) '-';
        this.programs[i][17] = (byte) '-';
        this.programs[i][18] = (byte) 'x';
        this.programs[i][19] = (byte) '+';
        this.programs[i][20] = (byte) '+';
        this.programs[i][21] = (byte) '!';
        this.programs[i][22] = (byte) 'A';
        this.programs[i][23] = (byte) 2;
        this.programs[i][24] = (byte) 'e';
        this.programs[i][25] = (byte) 'n';
        this.programs[i][26] = (byte) 'd';
    }

    private void initProgram3()
    {
        int i = 3;
        this.programNames[i] = ((((int) 's') & 0xFF)) | ((((int) 'y') & 0xFF) << 8) | ((((int) 's') & 0xFF) << 16) | ((((int) '3') & 0xFF) << 24);
        this.programs[i] = new byte[18];
        this.programs[i][0] = (byte) 'x';
        this.programs[i][1] = (byte) '=';
        this.programs[i][2] = (byte) '8';
        this.programs[i][3] = (byte) 'x';
        this.programs[i][4] = (byte) '+';
        this.programs[i][5] = (byte) '+';
        this.programs[i][6] = (byte) 'x';
        this.programs[i][7] = (byte) '-';
        this.programs[i][8] = (byte) '-';
        this.programs[i][9] = (byte) 'x';
        this.programs[i][10] = (byte) '-';
        this.programs[i][11] = (byte) '-';
        this.programs[i][12] = (byte) 'e';
        this.programs[i][13] = (byte) 'n';
        this.programs[i][14] = (byte) 'd';
    }

    private void initProgram4()
    {
        int i = 4;
        this.programNames[i] = ((((int) 's') & 0xFF)) | ((((int) 'y') & 0xFF) << 8) | ((((int) 's') & 0xFF) << 16) | ((((int) '4') & 0xFF) << 24);
        this.programs[i] = new byte[100];
        this.programs[i][0] = (byte) 'x';
        this.programs[i][1] = (byte) '=';
        this.programs[i][2] = (byte) '8';
        this.programs[i][3] = (byte) 'x';
        this.programs[i][4] = (byte) '+';
        this.programs[i][5] = (byte) '+';
        this.programs[i][6] = (byte) 'x';
        this.programs[i][7] = (byte) '-';
        this.programs[i][8] = (byte) '-';
        this.programs[i][9] = (byte) 'x';
        this.programs[i][10] = (byte) '-';
        this.programs[i][11] = (byte) '-';
        this.programs[i][12] = (byte) 'e';
        this.programs[i][13] = (byte) 'n';
        this.programs[i][14] = (byte) 'd';
    }

    private void initProgram5()
    {
        int i = 5;
        this.programNames[i] = ((((int) 's') & 0xFF)) | ((((int) 'y') & 0xFF) << 8) | ((((int) 's') & 0xFF) << 16) | ((((int) '5') & 0xFF) << 24);
        this.programs[i] = new byte[50];
        this.programs[i][0] = (byte) 'x';
        this.programs[i][1] = (byte) '=';
        this.programs[i][2] = (byte) '8';
        this.programs[i][3] = (byte) 'x';
        this.programs[i][4] = (byte) '+';
        this.programs[i][5] = (byte) '+';
        this.programs[i][6] = (byte) 'x';
        this.programs[i][7] = (byte) '-';
        this.programs[i][8] = (byte) '-';
        this.programs[i][9] = (byte) 'x';
        this.programs[i][10] = (byte) '-';
        this.programs[i][11] = (byte) '-';
        this.programs[i][12] = (byte) 'e';
        this.programs[i][13] = (byte) 'n';
        this.programs[i][14] = (byte) 'd';
    }
}
