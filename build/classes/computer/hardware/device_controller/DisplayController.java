package computer.hardware.device_controller;

import computer.hardware.Power;
import computer.hardware.abstract_hardware.DeviceController;
import computer.hardware.bus.AddressBus;
import computer.hardware.bus.DataBus;
import computer.hardware.bus.InterruptBus;
import computer.hardware.device.Display;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

/**
 * 显示器控制器
 * @author Administrator
 */
public class DisplayController extends DeviceController
{

    private Display display;

    public DisplayController(Power power, AddressBus addressBus, DataBus dataBus, InterruptBus interruptBus)
    {
        this(power, addressBus, dataBus, interruptBus, 4);
    }

    public DisplayController(Power power, AddressBus addressBus, DataBus dataBus, InterruptBus interruptBus, int registerSize)
    {
        super(power, addressBus, dataBus, interruptBus, registerSize);
    }

    /**
     * @param display the display to set
     */
    public void setDisplay(Display display)
    {
        this.display = display;
        this.display.addActionListenerForCommandLine(new java.awt.event.ActionListener()
        {

            @Override
            public void actionPerformed(ActionEvent e)
            {
                DisplayController.this.INTR();
            }
        });
        this.display.addWindowListener(new java.awt.event.WindowAdapter()
        {

            @Override
            public void windowClosing(WindowEvent e)
            {
                DisplayController.this.INTR();
            }
        });
    }

    @Override
    public void write()
    {
        int command;
        super.write();
        command = this.register[HardDiskController.REGISTER_CONTROL_COMMAND];
        this.register[HardDiskController.REGISTER_CONTROL_COMMAND] = HardDiskController.COMMAND_NULL;
        executiveCommand(command);
    }

    private void executiveCommand(int command)
    {
        switch (command)
        {
            case DisplayController.COMMAND_REFRESH_FILE_WINDOW:
                if (this.display != null)
                {
                    this.display.refreshFileWindow();
                }
                break;
            case DisplayController.COMMAND_REFRESH_MEMORY_WINDOW:
                if (this.display != null)
                {
                    this.display.refreshMemoryWindow();
                }
                break;
            case DisplayController.COMMAND_REFRESH_DEVICE_WINDOW:
                if (this.display != null)
                {
                    this.display.refreshDeviceWindow();
                }
                break;
            case DisplayController.COMMAND_REFRESH_PROCESS_WINDOW:
                if (this.display != null)
                {
                    this.display.refreshProcessWindow();
                }
                break;
        }
    }

    @Override
    protected boolean INTR()
    {
        return super.INTR();
    }

    @Override
    public void run()
    {
        int command;
        while (this.power.isOpen())
        {
            command = this.register[HardDiskController.REGISTER_CONTROL_COMMAND];
            this.register[HardDiskController.REGISTER_CONTROL_COMMAND] = HardDiskController.COMMAND_NULL;
            executiveCommand(command);
            try
            {
                java.lang.Thread.sleep(1000);
            } catch (InterruptedException ex)
            {
            }
        }
    }
    public final static int COMMAND_REFRESH_FILE_WINDOW = 6;
    public final static int COMMAND_REFRESH_MEMORY_WINDOW = 7;
    public final static int COMMAND_REFRESH_DEVICE_WINDOW = 8;
    public final static int COMMAND_REFRESH_PROCESS_WINDOW = 9;
}
