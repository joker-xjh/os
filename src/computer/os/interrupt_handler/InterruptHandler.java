package computer.os.interrupt_handler;

import computer.os.base.OS;

/**
 * 中断处理程序基类
 * @author Administrator
 */
public abstract class InterruptHandler extends computer.os.base.Process
{
    public InterruptHandler(OS os)
    {
        super(os);
    }
}
