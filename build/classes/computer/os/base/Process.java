package computer.os.base;

/**
 *
 * @author Administrator
 */
public abstract class Process
{
    protected OS os;

    protected Process(OS os)
    {
        this.os = os;
    }

    public abstract void run();

    protected void push()
    {
        os.push(os.cpu.RR);
        os.push(os.cpu.LR);
        os.push(os.cpu.AR);
        os.push(os.cpu.DR);
        os.push(os.cpu.PC);
        os.push(os.cpu.IR);
        os.push(os.cpu.AX);
        os.push(os.cpu.BX);
        os.push(os.cpu.CX);
        os.push(os.cpu.DX);
    }

    protected void pop()
    {
        os.cpu.DX=os.pop();
        os.cpu.CX=os.pop();
        os.cpu.BX=os.pop();
        os.cpu.AX=os.pop();
        os.cpu.IR=os.pop();
        os.cpu.PC=os.pop();
        os.cpu.DR=os.pop();
        os.cpu.AR=os.pop();
        os.cpu.LR=os.pop();
        os.cpu.RR=os.pop();
    }
}
