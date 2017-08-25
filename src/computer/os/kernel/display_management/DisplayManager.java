package computer.os.kernel.display_management;

import computer.os.base.OS;

/**
 * 显示管理
 * @author Administrator
 */
public class DisplayManager extends computer.os.base.SystemProcess
{

    public DisplayManager(OS os)
    {
        super(os);
    }

    public void showMemory()
    {
        push();
        os.displayDriver.refreshMemoryWindow();
        pop();
    }

    public void showProcess()
    {
        push();
        os.displayDriver.refreshProcessWindow();
        pop();
    }

    public void showFile()
    {
        push();
        os.displayDriver.refreshFileWindow();
        pop();
    }

    public void showDevice()
    {
        push();
        os.displayDriver.refreshDeviceWindow();
        pop();
    }
}
