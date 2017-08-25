package computer.os.interrupt_handler;

import computer.os.base.OS;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 进程结束中断处理程序
 * @author Administrator
 */
public class IODeviceInterruptHandler extends InterruptHandler
{

    private int device;
    private int number;

    public IODeviceInterruptHandler(OS os, int device, int number)
    {
        super(os);
        this.device = device;
        this.number = number;
    }

    @Override
    public void run()
    {
        int pid;
        push();
        pid = os.deviceManager.getDeviceUser(device, number);
        if (pid != -1)
        {
            os.deviceDriver[device][number].close();
            os.deviceManager.collect(pid, device);
            os.processManager.awake(pid);
            pid = os.deviceManager.allocateNext(device, number);
            if (pid != -1)
            {
                os.deviceDriver[device][number].use(-1);
                os.cpu.deviceInformation.setRemainTime(device, number, -1);
                os.processManager.awake(pid);
            }
        }
        os.deviceManager.sendInformation();
        os.processManager.sendInformation();
        os.displayManager.showProcess();
        os.displayManager.showDevice();
        try
        {
            java.lang.Thread.sleep(1000);
        } catch (InterruptedException ex)
        {
            //Logger.getLogger(IODeviceInterruptHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        pop();
    }
}
