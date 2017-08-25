package computer.os.interrupt_handler;

import computer.hardware.cpu.CPU;
import computer.os.base.OS;

/**
 * 进程结束中断处理程序
 * @author Administrator
 */
public class ProcessEndInterruptHandler extends InterruptHandler
{
    
    public ProcessEndInterruptHandler(OS os)
    {
        super(os);
        
    }

    @Override
    public void run()
    {
        push();
        os.displayManager.showProcess();
        pop();
        os.processManager.destroy();
        os.processManager.load();
        os.processManager.sendInformation();
        os.cpu.time = 5;
        os.cpu.PSW &= ~CPU.PSW_INNER_INTERRUPT_BIT;
        os.cpu.PSW |= CPU.PSW_IF_BIT;
    }
}
