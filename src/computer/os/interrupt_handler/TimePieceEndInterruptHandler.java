package computer.os.interrupt_handler;

import computer.hardware.cpu.CPU;
import computer.os.base.OS;

/**
 * 时间片结束中断处理
 * @author Administrator
 */
public class TimePieceEndInterruptHandler extends InterruptHandler
{

    public TimePieceEndInterruptHandler(OS os)
    {
        super(os);
    }

    @Override
    public void run()
    {
        push();
        os.displayManager.showProcess();
        pop();
        os.processManager.schedule();
        os.processManager.sendInformation();
        os.cpu.time = 5;
        os.cpu.PSW &= ~CPU.PSW_INNER_INTERRUPT_BIT;
        os.cpu.PSW |= CPU.PSW_IF_BIT;
    }
}
