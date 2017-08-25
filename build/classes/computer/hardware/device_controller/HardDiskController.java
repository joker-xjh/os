package computer.hardware.device_controller;

import computer.hardware.device.HardDisk;
import computer.hardware.bus.InterruptBus;
import computer.hardware.Power;
import computer.hardware.bus.DataBus;
import computer.hardware.bus.AddressBus;
import computer.hardware.abstract_hardware.DeviceController;

/**
 * 硬盘控制器
 * @author Administrator
 */
public class HardDiskController extends DeviceController
{

    private HardDisk disk;

    public HardDiskController(Power power, AddressBus addressBus, DataBus dataBus, InterruptBus interruptBus)
    {
        this(power, addressBus, dataBus, interruptBus, 67);
    }

    public HardDiskController(Power power, AddressBus addressBus, DataBus dataBus, InterruptBus interruptBus, int registerSize)
    {
        super(power, addressBus, dataBus, interruptBus, registerSize);
        this.register[HardDiskController.REGISTER_STATUS] = 0;
    }

    public void setHardDisk(HardDisk disk)
    {
        this.disk = disk;
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
                        this.disk.open();
                        this.register[HardDiskController.REGISTER_STATUS] |= HardDiskController.STATUS_WORK;
                    }
                    break;
                case HardDiskController.COMMAND_CLOSE:
                    if ((register[REGISTER_STATUS] & STATUS_WORK) != 0)
                    {
                        this.disk.close();
                        this.register[HardDiskController.REGISTER_STATUS] &= ~(HardDiskController.STATUS_WORK);
                    }
                    break;
                case HardDiskController.COMMAND_READ:
                    this.register[HardDiskController.REGISTER_STATUS] &= ~(HardDiskController.STATUS_READY);
                    this.disk.read(register, HardDiskController.REGISTER_DATA, register[HardDiskController.REGISTER_CONTROL_ARGUMENT]);
                    this.register[HardDiskController.REGISTER_STATUS] |= HardDiskController.STATUS_READY;
//                    this.INTR();
                    break;
                case HardDiskController.COMMAND_WRITE:
                    this.register[HardDiskController.REGISTER_STATUS] |= HardDiskController.STATUS_BUSY;
                    this.disk.write(register, HardDiskController.REGISTER_DATA, register[HardDiskController.REGISTER_CONTROL_ARGUMENT]);
                    this.register[HardDiskController.REGISTER_STATUS] &= ~(HardDiskController.STATUS_BUSY);
//                    this.INTR();
                    break;
                case HardDiskController.COMMAND_END_INTERRUPT:
                    this.INTA();
                    break;
            }
            try
            {
                java.lang.Thread.sleep(1);
            } catch (InterruptedException ex)
            {
            }
        }
        this.disk.close();
    }
}
