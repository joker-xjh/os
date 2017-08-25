package computer.hardware.device_controller;

import computer.hardware.Power;
import computer.hardware.abstract_hardware.DeviceController;
import computer.hardware.bus.AddressBus;
import computer.hardware.bus.DataBus;
import computer.hardware.bus.InterruptBus;

/**
 * 控制台控制器
 * @author Administrator
 */
public class ConsoleController extends DeviceController
{

    public ConsoleController(Power power, AddressBus addressBus, DataBus dataBus, InterruptBus interruptBus, int registerSize)
    {
        super(power, addressBus, dataBus, interruptBus, registerSize);
        this.register[HardDiskController.REGISTER_STATUS] = 0;
    }

    public ConsoleController(Power power, AddressBus addressBus, DataBus dataBus, InterruptBus outerInterruptBus)
    {
        this(power, addressBus, dataBus, outerInterruptBus, 90);
    }

    @Override
    public void run()
    {
        int command;
        while (this.power.isOpen())
        {
            command = this.register[HardDiskController.REGISTER_CONTROL_COMMAND];
            this.register[HardDiskController.REGISTER_CONTROL_COMMAND] = HardDiskController.COMMAND_NULL;
            switch (command)
            {
                case HardDiskController.COMMAND_OPEN:
                    if ((register[REGISTER_STATUS] & STATUS_WORK) == 0)
                    {
                        this.register[HardDiskController.REGISTER_STATUS] |= HardDiskController.STATUS_WORK;
                    }
                    break;
                case HardDiskController.COMMAND_CLOSE:
                    if ((register[REGISTER_STATUS] & STATUS_WORK) != 0)
                    {
                        this.register[HardDiskController.REGISTER_STATUS] &= ~(HardDiskController.STATUS_WORK);
                    }
                    break;
                case HardDiskController.COMMAND_READ:
                    this.register[HardDiskController.REGISTER_STATUS] &= ~(HardDiskController.STATUS_READY);
                    this.register[HardDiskController.REGISTER_STATUS] |= HardDiskController.STATUS_READY;
//                    this.INTR();
                    break;
                case HardDiskController.COMMAND_WRITE:
                    this.register[HardDiskController.REGISTER_STATUS] |= HardDiskController.STATUS_BUSY;
                    this.register[HardDiskController.REGISTER_STATUS] &= ~(HardDiskController.STATUS_BUSY);
//                    this.INTR();
                    break;
                case HardDiskController.COMMAND_END_INTERRUPT:
                    this.INTA();
                    break;
            }
            try
            {
                java.lang.Thread.sleep(1000);
            } catch (InterruptedException ex)
            {
            }
        }
    }
}
