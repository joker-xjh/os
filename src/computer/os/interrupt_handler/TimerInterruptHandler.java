package computer.os.interrupt_handler;

import computer.os.base.OS;

/**
 * 时钟驱动程序
 * @author Administrator
 */
public class TimerInterruptHandler extends InterruptHandler
{

    public TimerInterruptHandler(OS os)
    {
        super(os);
    }

    @Override
    public void run()
    {
        push();
        os.displayManager.showProcess();
        os.cpu.time--;
        for (int i = 0; i < os.deviceDriver.length; i++)
        {
            for (int j = 0; j < os.deviceDriver[i].length; j++)
            {
                os.deviceDriver[i][j].update();
                os.cpu.deviceInformation.setRemainTime(i, j, os.deviceDriver[i][j].getRemainTime());
            }
        }
        os.displayManager.showDevice();
        try
        {
            java.lang.Thread.sleep(1);
        } catch (InterruptedException ex)
        {
            //Logger.getLogger(IODeviceInterruptHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        if ((os.cpu.time) <= 0)
        {
            if ((os.cpu.time) < 0)
            {
                os.cpu.time = 5;
            }
            os.cpu.INT(1);
        }
        pop();
    }
}
