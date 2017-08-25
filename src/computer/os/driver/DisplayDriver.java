package computer.os.driver;

import computer.hardware.device_controller.DisplayController;
import computer.hardware.etc.Port;
import computer.os.base.OS;

/**
 * 显示器驱动
 * @author Administrator
 */
public class DisplayDriver extends Driver
{

    public DisplayDriver(OS os)
    {
        super(os, Port.Number.DISPLAY_CONTROLLER, DisplayController.REGISTER_STATUS, DisplayController.REGISTER_CONTROL_COMMAND, DisplayController.REGISTER_CONTROL_ARGUMENT, DisplayController.REGISTER_DATA);
    }

    public void refreshMemoryWindow()
    {
        push();
        int offset, command;
        offset = DisplayController.REGISTER_CONTROL_COMMAND;
        command = DisplayController.COMMAND_REFRESH_MEMORY_WINDOW;
        write(port + offset, (byte) command);
        pop();
    }

    public void refreshProcessWindow()
    {
        push();
        int offset, command;
        offset = DisplayController.REGISTER_CONTROL_COMMAND;
        command = DisplayController.COMMAND_REFRESH_PROCESS_WINDOW;
        write(port + offset, (byte) command);
        pop();
    }

    public void refreshDeviceWindow()
    {
        push();
        int offset, command;
        offset = DisplayController.REGISTER_CONTROL_COMMAND;
        command = DisplayController.COMMAND_REFRESH_DEVICE_WINDOW;
        write(port + offset, (byte) command);
        pop();
    }

    public void refreshFileWindow()
    {
        push();
        int offset, command;
        offset = DisplayController.REGISTER_CONTROL_COMMAND;
        command = DisplayController.COMMAND_REFRESH_FILE_WINDOW;
        write(port + offset, (byte) command);
        pop();
    }
}
